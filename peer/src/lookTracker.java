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

public class lookTracker extends PeerConfig implements Sender{

    String message = "look [";
    public void sendMessage(ArrayList<JTextField> texts){
        String answer = "";
        try{
            texts.get(0).setText("");
            Socket socket = new Socket(super.trackerIp,super.trackerPort);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            pw.println(message.substring(0,message.length() - 1) + "]");
            System.out.println("<"+message.substring(0,message.length() - 1) + "]");
            PeerConfig.writeInLogs("<"+message.substring(0,message.length() - 1) + "]");
            answer = br.readLine();// Ca c'est pour suivre en temps réel sur le terminal.
            System.out.println("> "+answer);
            PeerConfig.writeInLogs("> "+answer);
            addFIlesAndKeys(answer);
            pw.println("END");
            pw.close();
            br.close();
            socket.close();
            message = "look [";
        }catch(Exception e){
            System.out.println("Error in look"); // Faudra peut être donner des erreurs plus explicites.
            PeerConfig.writeInLogs("Error in look");
        }
    }

    public void addValues(ArrayList<JTextField> texts){
        if(!texts.get(0).getText().equals("")){
            this.message = this.message +texts.get(0).getText() +" ";
            texts.get(0).setText("");
            System.out.println(this.message);
        }
    }

    public void flush(ArrayList<JTextField> texts){
        for(JTextField text : texts) {
            text.setText("");
        }
        this.message = "look [";
    }

    private void addFIlesAndKeys(String answer){
        if(answer == ""){
            return;
        }
        answer = answer.substring("look [".length(),answer.length());
        answer = answer.substring(0,answer.length()-1);
        String[] splitting = answer.split(" ");
        int index =0;
        FileManager fm = FileManager.getInstance();
        while(index < splitting.length){
            fm.updateFileMatch(splitting[index+3],splitting[index]);
            index = index + 4;
        }
    }
}