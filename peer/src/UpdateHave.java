import java.io.*;
import java.net.*;
import java.util.*;
import java.util.TimerTask;

class UpdateHave extends TimerTask{
    public void run() {
        try{
            // TODO : need to iterate on peerBasePort
            Socket socket = new Socket(PeerConfig.trackerIp,PeerConfig.trackerPort);
            DatFileParser getMessage= new DatFileParser();
            int inPor = PeerConfig.inPort;
            if(inPor==0){
                ServerSocket s = new ServerSocket(0);
                inPor = s.getLocalPort();
                s.close();
            }
            // message depends on the file currently being downloaded
            String message = getMessage.getFilesKeyFrom(PeerConfig.seedFile);
            if(message !="") {
                message = "have" + message;
                message = message.substring(0, message.length() - 1);
            }else{
                message = "have";
            }
            message = message.substring(0,message.length() - 1) + "]";
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            pw.println(message);
            System.out.println("<"+message);
            PeerConfig.writeInLogs("<"+message);
            String str = br.readLine();// Ca c'est pour suivre en temps réel sur le terminal.
            System.out.println(">"+str);
            PeerConfig.writeInLogs(">"+str);
            pw.println("END");
            pw.close();
            br.close();
            socket.close();
        }catch(Exception e){
            System.out.println("Socket connecting error on have");
            PeerConfig.writeInLogs("Socket connecting error on have");
        }
    }
}