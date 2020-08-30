import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * */

public class ReceiveFromPeer extends PeerConfig implements Runnable{

  String connect;
  int port;

  public ReceiveFromPeer(String connect, int port){
    this.connect = connect;
    this.port = port;
  }

  public void receivePeer() throws Exception{

    while(true){
      //Socket connectionSocket = new Socket(connect, peerBasePort); // Attention ici inPort peut être set à 0 ce qui signifie attribution automatique de port
      ServerSocket welcomeSocket = new ServerSocket(port);
      Socket connectionSocket = welcomeSocket.accept();
      //welcomeSocket.setSoTimeout(1000*1000);

      BufferedReader br = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream())),true);


      while(true){ // exception when not connected to a peer

        String message ="";

        message =br.readLine();
        if(message == null){
          break;
        }
        System.out.println("<" + message);
        PeerConfig.writeInLogs(message);
        String answer = "";
        boolean interestedSent = false;

        // gets the data structure
        FileManager fm = FileManager.getInstance();

        // Analyzing the message
        String delims = "[ \\[\\]]";
        String[] tokens = message.split(delims);
        /*
        for (int i = 0; i<tokens.length; i++) {
          System.out.println("is" + tokens[i]);
        }
        */
        switch(tokens[0]){
          /*
              This part is for analyzing a request from another peer
          */

          case "interested":
            //answer with have
            answer = "have " + tokens[1] + " " + fm.getBuffermapToString(tokens[1]);
            pw.println(answer);
            System.out.println("Sending > " + answer);
            interestedSent = true;
            break;

          case "getpieces":
            ArrayList<Integer> indexes = new ArrayList<>();
            for (int i = 3; i < tokens.length; i++) {
              indexes.add(Integer.parseInt(String.valueOf(tokens[i])));
            }
            //System.out.println(indexes);
            //String buffermap = fm.getBuffermapToString(tokens[1]);
            answer = "data " + tokens[1] + " [";
            if(fm.filePieces.containsKey(tokens[1])){
              String[] TableofPieces = fm.filePieces.get(tokens[1]);
              for(int ind : indexes){
                if(ind>=TableofPieces.length || TableofPieces[ind].equals("")){
                  answer = "nok";
                  System.out.println("Sending2 > " + answer);
                  pw.println(answer);
                  break;
                }
                answer = answer +indexes + ":" + TableofPieces[ind] +" ";
              }
              answer =answer.substring(0,answer.length()-1) + "]";
              System.out.println("Sending > " + answer);
              pw.println(answer);
              break;
            }
            String fileToSend = new String(Files.readAllBytes(Paths.get(fm.getPath(tokens[1])))); // problem of path
            String path = fm.getPath(tokens[1]);
            File f =null;
            try {
              f = new File(path);
            }catch (Exception e){
              System.out.println("Error while opening file to send.");
              PeerConfig.writeInLogs("Error while opening file to send.");
            }
            long lenFile = f.length();
            double totalStringSize = fileToSend.length();
            int nb_pieces = (int)Math.ceil(totalStringSize/pieceSize);
            //System.out.println("Le fichier a envoyer :"+fileToSend);
            //System.out.println("Le path :"+fm.getPath(tokens[1]));
            // splits the file
            String[] filePieces = new String[nb_pieces];
            int globalIndex = 0;
            String tmp = "";
            int j = 0;
            while (j<fileToSend.length() && j < totalStringSize) {
              if(j % pieceSize == 0 && j > 0){
                filePieces[globalIndex] = tmp;
                globalIndex++;
                tmp = "";
                //System.out.println("File cutting");
              }
              tmp += fileToSend.charAt(j);
              j+=1;
            }
            String piece = new String(tmp);
            filePieces[globalIndex] = piece;
            globalIndex++;
            // creates the answer
            for (int i = 0; i < indexes.size(); i++) {
              answer += indexes.get(i) + ":"+ filePieces[indexes.get(i) - 1];
              if(i != indexes.size() - 1)
                answer += " ";
            }
            answer += "]";
            System.out.println("Sending > " + answer);
            pw.println(answer);
            pw.println("END");
            break;

          case "have":
            /*
              Two cases :
              - answer to an interested
              - request
            */
            if(!interestedSent){ // checks
              answer = "have " + tokens[1] + " " + fm.getBuffermapToString(tokens[1]);
              System.out.println("Sending > " + answer);
              pw.println(answer);
            }
            interestedSent = false;

            //need to deal with the answer and update the buffermap of this peer in the data structure of this file
            break;

          case "data":
            // TODO
            String[] pieces = new String[tokens.length - 2];
            for (int i = 2; i < tokens.length; i++) {
              pieces[i - 2] = tokens[i];
            }
            fm.updatePieces(tokens[1], pieces);
            break;

          case "nok":
            System.out.println("Error in transmission");
            break;

          default:
            answer = "nok";
            System.out.println("Sending > " + answer);
            pw.println(answer);
        }
          
        
      }
      br.close();
      pw.close();
      connectionSocket.close();
      welcomeSocket.close();
    }
  }


  public void run(){
    try{
      receivePeer();
    } catch (Exception e){
      System.out.println("Listening Interrupted.");
      PeerConfig.writeInLogs("Listening Interrupted.");
      e.printStackTrace();
    }
    return;
  }
}
