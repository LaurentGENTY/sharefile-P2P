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
import java.net.InetAddress;
/**
 * */

public class InterestedPeer extends PeerConfig implements Sender{

    String message = "interested ";

    public void sendMessage(ArrayList<JTextField> texts){ 
      /*
          TODO : Rule Interested to send to each other peer on the network
      */
        String address = texts.get(0).getText();
        String port = texts.get(1).getText();
        String message = address.replaceAll("\\s+","") + ":" + port.replaceAll("\\s+","");
        String key = texts.get(2).getText();
        SendToPeer(message,key);
        flush(texts);
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
        this.message = "interested ";
    }

    public void sendFromLook(String str){
        str = str.replaceAll("\\[","");
        str = str.replaceAll("\\]","");
        String[] strParsed = str.split(" ");
        //C'est interessant à partir de 3
        int index = 1;
        String key = strParsed[1];
        while(index < strParsed.length-1){
            index = index + 1;
            try{
                InetAddress ia = InetAddress.getLocalHost();
                if(!strParsed[index].equals(ia.getHostAddress()+":"+PeerConfig.inPort) && !strParsed[index].equals("127.0.0.1"+":"+PeerConfig.inPort)){
                    SendToPeer(strParsed[index],key);
                }
            }catch(Exception e){
                //System.out.println("Error while getting own IP address");
                //PeerConfig.writeInLogs("Error while getting own IP address");
            }
        }
        if(index == 1){
            System.out.println("No peer have key "+ key + ". Interested not send");
            PeerConfig.writeInLogs("No peer have key "+ key + ". Interested not send");
        }
    }

    public void SendToPeer(String toSend,String key){
        String[] infos = toSend.split(":");
        String answer ="";
        try{
            Socket socket = new Socket(infos[0],Integer.parseInt(infos[1]));
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            pw.println("interested "+key);
            System.out.println("< interested "+key);
            PeerConfig.writeInLogs("< interested "+key);
            answer = br.readLine();// Ca c'est pour suivre en temps réel sur le terminal.
            System.out.println(">"+answer);
            PeerConfig.writeInLogs(">"+answer);
            //pw.println("END");
            pw.close();
            br.close();
            socket.close();
        }catch(Exception e){
            System.out.println("Error in interested");
            PeerConfig.writeInLogs("Error in interested");
            return;
        }
        GetPiecesPeer GetPPeer = new GetPiecesPeer();
        GetPPeer.sendFromInt(answer,infos[0],infos[1]);

    }

}