import java.io.*;
import java.net.*;
import java.security.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * */

public class PeerConfig{
	  static int port = 11000; // pas une super idée le port http pour nos connexions

	  static int inPort = 15000; // A voir, un pour les communications, l'autre pour les transferts de fichier
	  static int outPort = 10000;
	  static String trackerIp = "127.0.0.1";
	  static int trackerPort = 10000;

	  static final String folderName = "../seed";
	  static final String logFile = "../log.txt";
	  static int pieceSize = 1024;
	  static int maxNbPair = 5;
	  static int maxPieceSize = 8;
	  static int period = 10;
	  static boolean okAnnounce = false;
	  static String seedFile = "../seed.dat";
	  static String leechFile = "../leech.dat";

	  public static void getElementFromConfig(){
		  try {
			  File file = new File("../config.ini");    //creates a new file instance
			  FileReader fr = new FileReader(file);   //reads the file
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
				  if (stri != "") {
					  String[] arra = stri.split("=");
					  switch (arra[0]) {
						  case "tracker-address" :
						  	trackerIp=arra[1];
						  	break;
						  case "tracker-port" :
						  	trackerPort= Integer.parseInt(arra[1]);
						  	break;
						  case "open-port":
						  	inPort = Integer.parseInt(arra[1]);
						  	break;
						  case "max-pair":
						  	maxNbPair = Integer.parseInt(arra[1]);
						  	break;
						  case "max-piece-size" :
						  	maxPieceSize = Integer.parseInt(arra[1]);
						  	break;
						  case "update-period" :
						  	period = Integer.parseInt(arra[1]);
						  	break;
						  default :
						  	System.out.println("Can't interpret the config :"+it);
						  	PeerConfig.writeInLogs("Can't interpret the config"+it);
					  }

				  }
			  }
		  }
		  catch(IOException e)
		  {
			  e.printStackTrace();
		  }
	  }

	public static void getElementFromCommandLine(String[] args){
		if(args.length>0){
			port = Integer.parseInt(args[0]);
		}
		if(args.length>1){
			trackerIp = args[1];
		}
		if(args.length>2){
			trackerPort = Integer.parseInt(args[2]);
		}
	}
	  /*** PRIVATE METHODS ***/

	File[] fileList(String folder){
	    File fold = new File(folder);
	    File[] list = fold.listFiles();
	    return list;
	}

	String parseFileList(File[] fileL) throws Exception{
	    String message = "[";
	    for (final File fileEntry : fileL) {
	        message = message + fileEntry.getName();
	        message = message + " " + fileEntry.length();
	        message = message + " " + pieceSize;
	        message = message + " " + FileManager.getFileChecksumMD5(fileEntry) + " ";
	    }
	    message = message.substring(0,message.length() - 1) + "]";
	    return message;
	}

	static public void writeInLogs(String message){
		if(!message.equals("")){
			try{
				FileWriter fr = new FileWriter(logFile,true);
				fr.write(message+"\n");
				fr.close();
			}catch(Exception e){
				System.out.println("Unable to write datas in "+logFile);
				PeerConfig.writeInLogs("Unable to write datas in "+logFile);
			}
		}
	}

	String parseFileKeyList(File[] fileL) throws Exception{
		String message = "announce listen " + inPort + " seed [";
		for (final File fileEntry : fileL) {
			message = message + " " + FileManager.getFileChecksumMD5(fileEntry) + " ";
		}
		message = message.substring(0,message.length() - 1) + "]";
		return message;
	}
}
