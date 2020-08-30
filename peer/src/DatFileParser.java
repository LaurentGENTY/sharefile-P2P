import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatFileParser {

    public String getFilesFrom(String filename){
        String message = "";
        try{
            File f = new File(filename);
            if(f.exists() && !f.isDirectory()) {
                FileReader fr = new FileReader(f);   //reads the file
                BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
                List<String> sb = new ArrayList<String>();    //constructs a string buffer with no characters
                String line;
                while ((line = br.readLine()) != null) {
                    sb.add(line);      //appends line to string buffer
                }
                fr.close();    //closes the stream and release the resources
                ListIterator<String> it = sb.listIterator();
                while (it.hasNext()) {
                    String stri = it.next();
                    File currentFile = new File(stri);
                    if(f.exists() && !f.isDirectory()) {
                        message = message + currentFile.getName();
                        message = message + " " + currentFile.length();
                        message = message + " " + PeerConfig.pieceSize;
                        message = message + " " + FileManager.getFileChecksumMD5(currentFile) + " ";
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error while reading "+filename);
            PeerConfig.writeInLogs("Error while reading "+filename);
        }
        return message;
    }

    public String getLeechKeys(){
        FileManager fm = FileManager.getInstance();
        String message = "";
        for(String key : fm.filePieces.keySet()){
            message = message + key +" ";
        }
        return message;
    }

    public String getFilesKeyFrom(String filename){
        String message = "";
        try{
            File f = new File(filename);
            if(f.exists() && !f.isDirectory()) {
                FileReader fr = new FileReader(f);   //reads the file
                BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
                List<String> sb = new ArrayList<String>();    //constructs a string buffer with no characters
                String line;
                while ((line = br.readLine()) != null) {
                    sb.add(line);      //appends line to string buffer
                }
                fr.close();    //closes the stream and release the resources
                ListIterator<String> it = sb.listIterator();
                while (it.hasNext()) {
                    String stri = it.next();
                    File currentFile = new File(stri);
                    if(f.exists() && !f.isDirectory()) {
                        message = message + FileManager.getFileChecksumMD5(currentFile) + " ";
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error while reading "+filename);
            PeerConfig.writeInLogs("Error while reading "+filename);
        }
        return message;
    }

    public void addFileTo(String filename, String toAdd){
        if(!lookForAFile(filename,toAdd) && !toAdd.equals("")){
            try{
                FileWriter fr = new FileWriter(filename,true);
                fr.write(toAdd+"\n");
                fr.close();
            }catch(Exception e){
                System.out.println("Unable to write datas in "+filename);
                PeerConfig.writeInLogs("Unable to write datas in "+filename);
            }
        }
    }

    public void addToFileMatch(String filename){
        FileManager fm = FileManager.getInstance();
        try{
            File f = new File(filename);
            if(f.exists() && !f.isDirectory()) {
                FileReader fr = new FileReader(f);   //reads the file
                BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
                List<String> sb = new ArrayList<String>();    //constructs a string buffer with no characters
                String line;
                while ((line = br.readLine()) != null) {
                    sb.add(line);      //appends line to string buffer
                }
                fr.close();    //closes the stream and release the resources
                ListIterator<String> it = sb.listIterator();
                while (it.hasNext()) {
                    String stri = it.next();
                    File currentFile = new File(stri);
                    if(f.exists() && !f.isDirectory() && !fm.fileMatch.containsKey(FileManager.getFileChecksumMD5(currentFile))) {
                        fm.updateFileMatch(FileManager.getFileChecksumMD5(currentFile),currentFile.getPath());
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error while reading "+filename);
            PeerConfig.writeInLogs("Error while reading "+filename);
        }
    }
    /*
    public void removeFileTo(String filename){

    }*/

    private boolean lookForAFile(String filename, String search){
        try{
            File f = new File(filename);
            if(f.exists() && !f.isDirectory()) {
                FileReader fr = new FileReader(f);   //reads the file
                BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
                List<String> sb = new ArrayList<String>();    //constructs a string buffer with no characters
                String line;
                while ((line = br.readLine()) != null) {
                    sb.add(line);      //appends line to string buffer
                }
                fr.close();    //closes the stream and release the resources
                ListIterator<String> it = sb.listIterator();
                while (it.hasNext()) {
                    String stri = it.next();
                    if(filename == search){
                        return true;
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error when looking for "+filename);
            PeerConfig.writeInLogs("Error when looking for "+filename);
        }
        return false;
    }

    void removeFileTo(String filename, String toRemove){
        try{
            String fileToAnalyse = new String(Files.readAllBytes(Paths.get(filename)));
            fileToAnalyse = fileToAnalyse.replaceAll(toRemove+"\n","");
            PrintWriter writer = new PrintWriter(new File(filename));
            writer.append(fileToAnalyse);
            writer.flush();
        }catch(Exception e){
            System.out.println("Error when looking for "+filename);
            PeerConfig.writeInLogs("Error when looking for "+filename);
        }
    }
}
