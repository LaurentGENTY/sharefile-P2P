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

/**
 * */

public class GetFileTracker extends PeerConfig implements Sender{

    String message = "getfile ";
    public void sendMessage(ArrayList<JTextField> texts){
        String answer ="";
        try{
            Socket socket = new Socket(super.trackerIp,super.trackerPort);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            pw.println(message + texts.get(0).getText()+ " ");
            System.out.println("<" + message + texts.get(0).getText());
            PeerConfig.writeInLogs("<" + message + texts.get(0).getText());
            answer = br.readLine();// Ca c'est pour suivre en temps réel sur le terminal.
            
            /*// gets the data structure
            FileManager fm = FileManager.getInstance();
            
            // handles the answer and stores it 
            String delims = "[ \\[\\]]";
            String[] tokens = message.split(delims);
            String[] peersData = new String[tokens.length - 2];
            for(int i = 2; i < tokens.length; i++){
                peersData[i - 2] = tokens[i];
            }

            fm.peerUpdate(tokens[1], peersData);*/

            System.out.println(">"+answer);
            PeerConfig.writeInLogs(">"+answer);
            pw.println("END");
            texts.get(0).setText("");
            pw.close();
            br.close();
            socket.close();
        }catch(Exception e){
            System.out.println("Error in getfile"); // Faudra peut être donner des erreurs plus explicites.
            PeerConfig.writeInLogs("Error in look");
        }
        if(answer != "" && answer != "nok"){
            InterestedPeer IntPe = new InterestedPeer();
            IntPe.sendFromLook(answer);
        }
    }

    public void addValues(ArrayList<JTextField> texts){

    }

    public void flush(ArrayList<JTextField> texts){
        for(JTextField text : texts) {
            text.setText("");
        }
    }
}