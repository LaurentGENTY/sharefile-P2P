import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;


/**
 * */

public class AddFileSender extends PeerConfig implements Sender{

    DatFileParser parser;

    public AddFileSender(){
        this.parser = new DatFileParser();
    }

    public void sendMessage(ArrayList<JTextField> texts){ // on devrait pas passer le dossier en paramètre ?
        try{
            Socket socket = new Socket(super.trackerIp,super.trackerPort);
            String message = "";
            int inPor = super.inPort;
            if(inPor==0){
                ServerSocket s = new ServerSocket(0);
                inPor = s.getLocalPort();
                s.close();
            }
            File f = new File(texts.get(0).getText());
            File[] files = {f};
            message = parseFileList(files);
            message = "announce listen " + inPor + " seed " + message;
            message = message.substring(0, message.length() - 1) + "]";
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            pw.println(message);
            System.out.println("< "+message);
            PeerConfig.writeInLogs("< "+message);
            String str = br.readLine();
            System.out.println(">"+str);
            PeerConfig.writeInLogs(">"+str);
            pw.println("END");
            pw.close();
            br.close();
            socket.close();
        }catch(Exception e){
            System.out.println("Socket connecting error");
            PeerConfig.writeInLogs("Socket connecting error");
        }
    }

    public void addValues(ArrayList<JTextField> texts){
        if(texts.get(0).getText()!=""){
            this.parser.addFileTo(super.seedFile,texts.get(0).getText());
            this.parser.addToFileMatch(super.seedFile);
        }
        texts.get(0).setText("");
    }

    public void flush(ArrayList<JTextField> texts){
        for(JTextField text : texts) {
            text.setText("");
        }
    }

}
