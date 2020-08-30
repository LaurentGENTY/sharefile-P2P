import java.io.*;
import java.net.*;
/**
 * */

public class SendToPeer extends PeerConfig implements Runnable{

  String connect;
  String cmd;

  public SendToPeer(String connect, String cmd){
    this.connect = connect;
    this.cmd = cmd;
  }

  public void announcePeer() throws Exception{
    
    //ServerSocket welcomeSocket = new ServerSocket(peerBasePort);
    //Socket socket = welcomeSocket.accept();
    
    Socket socket = new Socket(connect, inPort);

    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
    //System.out.println("Sending " + cmd);        
    // Tells another peer what is given in cmd
    pw.println(cmd);
    //System.out.println("Command sent");
    String answer = br.readLine();
    System.out.println(answer);
    PeerConfig.writeInLogs(answer);

    pw.close();
    //br.close();
    socket.close();

    return ;
  }

  public void run(){
    try{
      System.out.println("Thread started");
      PeerConfig.writeInLogs("Thread started");
      announcePeer();
    } catch (Exception e){
      System.out.println("Sending Interrupted.");
      PeerConfig.writeInLogs("Sending Interrupted");
      e.printStackTrace();
    }
    return;
  }


}
