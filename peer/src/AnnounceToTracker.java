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

public class AnnounceToTracker extends PeerConfig implements Sender{


    public void sendMessage(ArrayList<JTextField> texts){ // on devrait pas passer le dossier en paramètre ?
      /*
          TODO : Rules announce, look, getfile
      */
        try{
            System.out.println(super.trackerPort);
            Socket socket = new Socket(super.trackerIp,super.trackerPort);
            /*File[] fileL = super.fileList("../seed"); // This fonctionne pas normalement en ce moment.
            String message = super.parseFileList(fileL);*/
            DatFileParser getMessage = new DatFileParser();
            getMessage.addToFileMatch(super.seedFile);
            String message = getMessage.getFilesFrom(super.seedFile);
            int inPor = super.inPort;
            if(inPor==0){
                ServerSocket s = new ServerSocket(0);
                inPor = s.getLocalPort();
                super.inPort = inPor;
                s.close();
            }
            if(message !="") {
                message = "announce listen " + inPor + " seed [" + message;
                message = message.substring(0, message.length() - 1) + "]";
            }else{
                message = "announce listen " + inPor + " seed []";
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            pw.println(message);
            System.out.println("< "+message);
            PeerConfig.writeInLogs("< "+message);
            String str = br.readLine();// Ca c'est pour suivre en temps réel sur le terminal.
            if(str.equals("ok")){
                PeerConfig.okAnnounce = true;
                (new Thread(new ReceiveFromPeer("127.0.0.1", inPort))).start();
            }
            System.out.println(">"+str);
            PeerConfig.writeInLogs(">"+str);
            pw.println("END");
            pw.close();
            br.close();
            socket.close();
            if(PeerConfig.okAnnounce){
                Timer timer = new Timer();
                timer.schedule(new UpdateAnnounce(),0,PeerConfig.period*1000);
            }
        }catch(Exception e){
            System.out.println("Socket connecting error");
            PeerConfig.writeInLogs("Socket connecting error");
        }

    }
    public void addValues(ArrayList<JTextField> texts){

    }

    public void flush(ArrayList<JTextField> texts){

    }


}
