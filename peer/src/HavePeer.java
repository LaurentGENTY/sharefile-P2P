import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
/**
 * */

public class HavePeer extends PeerConfig implements Sender{

    String message = "have ";

    public void sendMessage(ArrayList<JTextField> texts){ 
      /*
          TODO : Rule Interested to send to each other peer on the network
      */
        texts.get(0).setText("");
        try{
            // TODO : iterate on peerBasePort for each peer
            ServerSocket welcomeSocket = new ServerSocket(super.inPort);
            Socket socket = welcomeSocket.accept();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);

            // Tells another peer what is given in cmd
            pw.println(message);
            String answer = br.readLine();
            System.out.println(answer + "\n");
            PeerConfig.writeInLogs(answer + "\n");

            pw.close();
            br.close();
            socket.close();
        }catch(Exception e){
            System.out.println("Error in Have Rule"); // Faudra peut être donner des erreurs plus explicites.
            PeerConfig.writeInLogs("Error in Have Rule");
        }
        message = "have ";
    }
    public void addValues(ArrayList<JTextField> texts){
        if(!texts.get(0).getText().equals("")){
            this.message = this.message +texts.get(0).getText() +" "; // need to add the buffermap
            //this.message += Buffermap.getBuffermap(texts.get(0).getText()).toString()
            texts.get(0).setText("");
            System.out.println(this.message);
            PeerConfig.writeInLogs(this.message);
        }
    }

    public void flush(ArrayList<JTextField> texts){
        for(JTextField text : texts) {
            text.setText("");
        }
        this.message = "have ";
    }


}