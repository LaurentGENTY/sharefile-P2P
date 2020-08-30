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

public class GetPiecesSender extends PeerConfig implements Sender{

    String message = "getpieces ";
    String key = "";
    String indexes ="";
    public void sendMessage(ArrayList<JTextField> texts){
        try{
            Socket socket = new Socket(super.trackerIp,super.trackerPort);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            pw.println(message + key + " ["+indexes.substring(0,message.length() - 1) + "]");
            System.out.println("<"+message.substring(0,message.length() - 1) + "]");
            PeerConfig.writeInLogs("<"+message.substring(0,message.length() - 1) + "]");
            String str = br.readLine();// Ca c'est pour suivre en temps réel sur le terminal.
            System.out.println(">"+str);
            PeerConfig.writeInLogs(">"+str);
            //pw.println("END");
            texts.get(0).setText("");
            pw.close();
            br.close();
            socket.close();
        }catch(Exception e){
            System.out.println("Error in look"); // Faudra peut être donner des erreurs plus explicites.
            PeerConfig.writeInLogs("Error in look");
        }
    }

    public void addValues(ArrayList<JTextField> texts){
        this.message = this.indexes + texts.get(1).getText() +" ";
        texts.get(1).setText("");
    }

    public void flush(ArrayList<JTextField> texts){
        for(JTextField text : texts) {
            text.setText("");
        }
        this.message = "getpieces ";
        this.key = "";
        this.indexes ="";
    }
}