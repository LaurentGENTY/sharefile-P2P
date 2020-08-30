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
import java.util.Arrays;
import java.util.Base64;
/**
 * */

public class GetPiecesPeer extends PeerConfig implements Sender{

    String index = "[";

    public void sendMessage(ArrayList<JTextField> texts){

        String address = texts.get(0).getText();
        String port = texts.get(1).getText();
        String key = texts.get(2).getText();
        String message = key + " " + this.index;
        if(index == "[") {
            System.out.println("No pieces asked");
            PeerConfig.writeInLogs("No pieces asked");
        }else {
            sendToPeer(message, address, port);
        }
        flush(texts);
    }

    public void addValues(ArrayList<JTextField> texts){
        if(!texts.get(0).getText().equals("")){
            this.index = this.index + texts.get(0).getText() + " ";
            texts.get(0).setText("");
            System.out.println(this.index);
            PeerConfig.writeInLogs(this.index);
        }
    }

    public void flush(ArrayList<JTextField> texts){
        for(JTextField text : texts) {
            text.setText("");
        }
        this.index = "[";
    }

    public void sendFromInt(String mess,String add, String port){
        // Parser mess
        String[] messList = mess.split(" ");
        FileManager fm = FileManager.getInstance();
        fm.updateFilePieces(messList[1],messList[2]);
        DatFileParser pars = new DatFileParser();
        pars.addFileTo(PeerConfig.leechFile,fm.fileMatch.get(messList[1]));
        byte[] str = Base64.getDecoder().decode(messList[2]);
        int len = str.length*8;
        boolean[] b = new boolean[len];
        Arrays.fill(b,false);
        fm.buffermapUpdate(messList[1],b);
        String message = messList[1] + " " + transformBuffermap(messList[2]);
        sendToPeer(message,add,port);
    }

    private void sendToPeer(String message,String add, String port){
        String answer ="";
        try{
            Socket socket = new Socket(add,Integer.parseInt(port));
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            pw.println("getpieces "+ message.substring(0,message.length() - 1) +"]");
            System.out.println("< getpieces "+ message.substring(0,message.length() - 1) +"]");
            PeerConfig.writeInLogs("< getpieces "+ message.substring(0,message.length() - 1) +"]");
            String answering ="";
            while(!(answering = br.readLine()).equals("END")){
                // Ca c'est pour suivre en temps réel sur le terminal.
                answer += answering+"\n";
            }
            answer=answer.substring(0,answer.length()-1);
            System.out.println(">"+answer);
            PeerConfig.writeInLogs(">"+answer);
            //pw.println("END");
            pw.close();
            br.close();
            socket.close();
        }catch(Exception e){
            System.out.println("Error in GetPieces");
            PeerConfig.writeInLogs("Error in GetPieces");
            return;
        }
        FileManager fm = FileManager.getInstance();
        fm.storePieces(answer);
    }

    String transformBuffermap(String buffer){
        int index = 0;
        int piece = 1;
        String message = "";
        String submessage ="";
        byte[] buff = Base64.getDecoder().decode(buffer.getBytes());
        while(index < buff.length) {
            byte bit = buff[index];
            byte mask = 1;
            piece = 8*(index+1);
            submessage ="";
            for (int j = 0; j < 8; j++)
            {
                int value = bit & mask;
                if(value!=0){ // il veut pas convertir en un booléen
                    submessage =  " " + piece + submessage ;
                }
                piece -= 1;
                mask <<= 1;
            }
            message = message + submessage;
            index += 1;
        }
        if(message.length() >0){
            message = "[" + message.substring(1,message.length());
        }
        return message+" ";
    }

}