import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Arrays; 
import java.util.concurrent.BlockingQueue;
import java.security.MessageDigest;
import java.util.concurrent.locks.ReentrantLock;
/**
 * */

public class FileManager extends PeerConfig implements Runnable{

	// Data structure
	Map<String, boolean[]> fileManager; // stores the hash and the buffermap for each file of this peer
	Map<String, String[]> peerManager;	// stores the hash of a file on the network and all the peers who have it
	Map<String, String> fileMatch; // stores the hash of a file and its path
	Map<String, String[]> filePieces; // stores the pieces for a given file

	// lock for concurrent accesses
	ReentrantLock lock;

	// Instance for singleton behavior
	private static final FileManager fileManagerInstance = new FileManager();

	private FileManager(){
		fileManager = new HashMap<String, boolean[]>();
		lock = new ReentrantLock();
		peerManager = new HashMap<String, String[]>();
		fileMatch = new HashMap<String, String>();
		filePieces = new HashMap<String, String[]>();
		try{
			buffermapInit();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Function to call when you want to call a method on the data base afterwards
	 * 
	 * @param 
	 * @return instance of the database
	 * */
	public static FileManager getInstance(){
		return fileManagerInstance;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// client methods


	/**
	 * Function for getting the md5sum of a file 
	 * 
	 * @param file file you want to convert
	 * @return String hash of the file
	 * */
	public static String getFileChecksumMD5(File file) throws Exception
	{
		//Use MD5 algorithm
		MessageDigest md5Digest = MessageDigest.getInstance("MD5");

	    //Get file input stream for reading the file content
	    FileInputStream fis = new FileInputStream(file);
	     
	    //Create byte array to read data in chunks
	    byte[] byteArray = new byte[1024];
	    int bytesCount = 0; 
	      
	    //Read file data and update in message digest
	    while ((bytesCount = fis.read(byteArray)) != -1) {
	        md5Digest.update(byteArray, 0, bytesCount);
	    };
	     
	    //close the stream; We don't need it now.
	    fis.close();
	     
	    //Get the hash's bytes
	    byte[] bytes = md5Digest.digest();
	     
	    //This bytes[] has bytes in decimal format;
	    //Convert it to hexadecimal format
	    StringBuilder sb = new StringBuilder();
	    for(int i=0; i< bytes.length ;i++)
	    {
	        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	     
	    //return complete hash
	   return sb.toString();
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Function for updating the path of a file
	 * 
	 * @param hash hash of the file you want to update
	 * @param filename new path of the file
	 * @return void
	 * */
	public void updateFileMatch(String hash, String filename){
		fileMatch.put(hash,filename);
	}

	/**
	 * Function for initialization of the pieces in the filePieces map 
	 * 
	 * @param key hash of the file you want to initalize
	 * @param buffermap buffermap of the file
	 * @return void
	 * */
	void updateFilePieces(String key,String buffermap){

        if(filePieces.containsKey(key)){
            return;
        }
        byte str[] = null;
		try{
        	str = Base64.getDecoder().decode(buffermap);
		}catch(Exception e){
			System.out.println("Invalid arg " +buffermap);
		}

        int len = str.length*8;
        String[] arr = new String[len];
        Arrays.fill(arr,"");


        filePieces.put(key,arr);

    }

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Methods to handle the file manager

	private void buffermapInit() throws Exception{
		// gets all the files in the seed/ folder and adds them in the file manager
		File[] filelist = fileList(folderName); 
	    for (File f : filelist) {
	    	String hash = getFileChecksumMD5(f);
	    	long bmlength = f.length()/pieceSize + 1;
	    	boolean[] buffermap = new boolean[(int) bmlength];
	    	Arrays.fill(buffermap, Boolean.TRUE);
	    	fileManager.put(hash, buffermap);
	    	fileMatch.put(hash,f.getPath());
    	}
    	return;
	}

	/**
	 * Function for updating the buffermap of a file
	 * 
	 * @param hash hash of the file which data you want to update
	 * @param buffermap new buffermap of the file
	 * @return void
	 * */
	public void buffermapUpdate(String hash, boolean[] buffermap){
		lock.lock();
	    fileManager.put(hash, buffermap);
	    lock.unlock();
		return;
	}

	/**
	 * Function for initialization of the pieces in the filePieces map 
	 * 
	 * @param hash hash of the file which buffermap you want to get
	 * @return boolean[] Corresponding buffermap (empty if not existing)
	 * */
	public boolean[] getBuffermap(String hash){
		lock.lock();
		boolean[] ret = fileManager.get(hash);
		lock.unlock();
		return ret;
	}

	/**
	 * Function for converting a buffermap into a string
	 * 
	 * @param hash hash of the file which buffermap you want to get in string form
	 * @return String buffermap in string form
	 * */
	public String getBuffermapToString(String hash){
		lock.lock();
		String res = "";
		String message = "";
		int index = 0;
		byte b=0;
		byte one=1;
		boolean first = true;
		if(!fileManager.containsKey(hash)){
			if(!fileMatch.containsKey(hash)){
				System.out.println("The file asked doesn't exist");
				PeerConfig.writeInLogs("The file asked doesn't exist");
				return "nok";
			}
			try{
				System.out.println("Je suis ici : " + hash);
				File f = new File(fileMatch.get(hash));
				int len = (int) Math.ceil((double)f.length() / PeerConfig.pieceSize);
				System.out.println("Je suis ici3 : " + (int) Math.ceil((double)f.length() / PeerConfig.pieceSize));
				byte[] table = new byte[(int)Math.ceil((double)len/8)];
				System.out.println("Hey :"+(int)Math.ceil((double)len/8));
				Arrays.fill(table,(byte)~0);
				if( len%8 != 0){
					byte oct = ~0;
					oct= (byte)(oct <<(8-(len%8)));
					table[table.length-1] = oct;
				}
				message = Base64.getEncoder().encodeToString(table);
				System.out.println("Je suis ici2 : " + message);
				/*if(len%8!=0){
					int rest = (int)(len % 8);
					int lastBytes = (byte) ((byte)~0 << (rest));
					message = message + lastBytes;
				}*/
				return message;
			}catch(Exception e){
				System.out.println("Error while reading file with key :"+hash);
				PeerConfig.writeInLogs("Error while reading file with key :"+hash);
			}
		}
		boolean[] value = fileManager.get(hash);
		int len = (int) Math.ceil((double)value.length / PeerConfig.pieceSize);
		byte[] table = new byte[(int)Math.ceil((double)len/8)];
		int ind =0;
		for(boolean bit: value){
			if(index%8 == 0 && !first) {
				table[ind] = b;
				ind = ind +1;
				res =  res + b;
				b = 0;
			}
			first = false;
			b = (byte) (b << 1);
			if(bit){
				b =(byte) (b + one);
			}
			index = (index + 1);
			/*if(entry.getKey().equals(hash)){
				for(boolean bit: entry.getValue()){
					if(bit == true){res += "1";}
					if(bit == false){res += "0";}
				}
			}*/
			if(index != 0){
				table[ind] = (byte) (b << (8-index));
			}
			res = Base64.getEncoder().encodeToString(table);
		}
		lock.unlock();
		return res;
	}

	/**
	 * Function for the reverse conversion (string to buffermap in boolean array form)
	 * @param buffer buffermap in string form
	 * @return boolean[] buffermap in array form
	 * */
	public boolean[] getStringToBuffermap(String buffer){
		int index = 0;
		int piece = 0;
		int size = buffer.length() * 8;
		byte[] buff = buffer.getBytes();
		boolean[] buffermap = new boolean[size];
		while(index < buff.length) {
			byte bit = buff[index];
			byte mask = 0x01;
			for (int j = 0; j < 8; j++)
			{
				int value = bit & mask;
				if(value==1){ // il veut pas convertir en un booléen
					buffermap[piece] = true;
				}else{
					buffermap[piece] = false;
				}
				piece += 1;
				mask <<= 1;
			}
			index += 1;
		}
		return buffermap;
	}

	/**
	 * Function for printing a buffermap
	 * 
	 * @param buffermap 
	 * @return void
	 * */
	public void printBuffermap(boolean[] buffermap){
		lock.lock();
		String res = new String();
		for(boolean bit: buffermap){
			if(bit == true){res += "1";}
			if(bit == false){res += "0";}
		}
		System.out.println(res);
		PeerConfig.writeInLogs(res);
		lock.unlock();
		return;
	}

	/**
	 * Function for printing all the buffermaps in the fileManager
	 * @param 
	 * @return void
	 * */
	public void printAll(){
		String res;
		/*
		Iterator<Map.Entry<String, boolean[]>> entries = fileManager.entrySet().iterator();
		while (entries.hasNext()) {
		    Map.Entry<String, boolean[]> entry = entries.next();
		    res = entry.getKey() + " : ";	
			for(boolean bit: entry.getValue()){
				if(bit == true){res += "1";}
				if(bit == false){res += "0";}
			}
			System.out.println(res);
			return;
		}*/
		lock.lock();
		for(Map.Entry<String, boolean[]> entry: fileManager.entrySet()){
			res = entry.getKey() + " has the buffermap: ";	
			System.out.println(res);
			PeerConfig.writeInLogs(res);
			printBuffermap(entry.getValue());
		}
		lock.unlock();
		return;
		/*
		fileManager.forEach((hash, buffermap) -> res = tuple.getKey() + " : ";	
			for(boolean bit: tuple.getValue()){
				if(bit == true){res += "1";}
				if(bit == false){res += "0";}
			}
			System.out.println(res););
		*/
		
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Mehods to handle the peer manager

	/**
	 * Function for getting the coordinates of the peers who have part of a file
	 * 
	 * @param hash hash of the file 
	 * @return String[] Peer coordinates given as IP@:port
	 * */
	public String[] getPeers(String hash){
		lock.lock();
		String[] ret = peerManager.get(hash);
		lock.unlock();
		return ret;
	}

	/**
	 * Function for updating the peers who have a file
	 * 
	 * @param fileHash hash of the file 
	 * @param peers new peer coordinates
	 * @return void
	 * */
	public void peerUpdate(String fileHash, String[] peers){
		lock.lock();
	    peerManager.put(fileHash, peers);
	    lock.unlock();
		return;
	}

	/**
	 * Function for converting a buffermap into a string
	 * 
	 * @return void
	 * */
	public void printAllPeers(){
		String res;
		lock.lock();
		for(Map.Entry<String, String[]> entry: peerManager.entrySet()){
			res = entry.getKey() + " is owned by : ";
			for(String peer: entry.getValue()){
				res += peer + " ";
			}
			System.out.println(res);
			PeerConfig.writeInLogs(res);
		}
		lock.unlock();
		return;		
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Function for getting the path of a file
	 * 
	 * @param hash hash of the file which buffermap you want to get in string form
	 * @return String path of the file
	 * */
	public String getPath(String hash){
		if(fileMatch.containsKey(hash)){
			return fileMatch.get(hash);
		}
		return "";
	}

	/**
	 * Function for printing the paths of all the files
	 * 
	 * @return void
	 * */
	public void printAllPaths(){
		String res;
		lock.lock();
		for(Map.Entry<String, String> entry: fileMatch.entrySet()){
			res = entry.getKey() + " is at: " + entry.getValue();
			System.out.println(res);
			PeerConfig.writeInLogs(res);
		}
		lock.unlock();
		return;		
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Function for converting a buffermap into a string
	 * 
	 * @param message message with the pieces
	 * @return void
	 * */
	public void storePieces(String message){
		String key = message.split(" ")[1];
		String sub = message.split("\\[")[0];
		message = message.substring(sub.length(),message.length());
		if(message.length() <= 1){
			System.out.println("Error no given pieces");
			PeerConfig.writeInLogs("Error no given pieces");
			return;
		}
		message = message.substring(0,message.length()-1);
		String[] tableOfPieces = filePieces.get(key);
		if(tableOfPieces == null){
			System.out.println("You try to fill a file you were not interested in");
			PeerConfig.writeInLogs("You try to fill a file you were not interested in");
			return;
		}
		boolean[] buffermap = fileManager.get(key);
		while(message.length() >0){
			System.out.println("Le message :"+message);
			message = message.substring(1,message.length());
			String pieceNumber = message.split(":")[0];
			int ind = Integer.parseInt(pieceNumber);
			message = message.substring(pieceNumber.length()+1,message.length());

			byte[] bytesTable = null;
			try{bytesTable = message.getBytes("ASCII");}catch(Exception e){System.out.println("Salut salut");}
			System.out.println("Le message :" + message.length());
			System.out.println("Le message :" + Math.min(PeerConfig.pieceSize,message.length()));
			String text = new String(bytesTable, 0, Math.min(PeerConfig.pieceSize,message.length()));
			System.out.println("Le message :" + Math.min(PeerConfig.pieceSize,message.length()));
			if(tableOfPieces[ind-1].equals("")){
				tableOfPieces[ind-1] = text;
				buffermap[ind-1] = true;
			}
			System.out.println("Le message :" + Math.min(PeerConfig.pieceSize,message.length()));
			message= message.substring(text.length(),message.length());
			System.out.println("Le message :" + Math.min(PeerConfig.pieceSize,message.length()));
		}
		if(checkIfFull(tableOfPieces)){
			writeFile(key);
			leechToSeed(key);
		}
	}


	/**
	 * Function for checking if the peer has all the pieces of a file
	 * 
	 * @param pieces array with all the pieces
	 * @return boolean 
	 * */
	public boolean checkIfFull(String[] pieces){
	    boolean blank = false;
	    int index = 0;
		for(String piece: pieces){
			if(piece.equals("") && index<pieces.length-8){
				return false;
			}
			if(piece.equals("") && !blank){
				blank = true;
				System.out.println("2");
			}
			if(!piece.equals("") && blank){
				System.out.println("2");
				return false;
			}
			index+=1;
		}
		return true;
	}

	/**
	 * Function for labeling a file as a seed and no longer as a leech
	 * 
	 * @param key hash of the file 
	 * @return void
	 * */
	public void leechToSeed(String key){
		filePieces.remove(key);
		DatFileParser pars = new DatFileParser();
		String path = fileMatch.get(key);
		pars.addFileTo(PeerConfig.seedFile,PeerConfig.folderName+"/"+path);
		pars.removeFileTo(PeerConfig.leechFile,path);
		pars.addToFileMatch(super.seedFile);
		fileMatch.replace(key,PeerConfig.folderName + "/"+fileMatch.get(key));
	}

	/**
	 * Function for writing a file once all the pieces have been gathered
	 * 
	 * @param key hash of the file 
	 * @return void
	 * */
	public void writeFile(String key){
		String[] tableOfPieces = filePieces.get(key);
		String path = PeerConfig.folderName + "/"+fileMatch.get(key);
		File f = new File(path);
		/*if(f.exists() && !f.isDirectory()) {
			System.out.println("File "+path+" already exists");
			PeerConfig.writeInLogs("File "+path+" already exists");
		}else */{
			String toWrite = "";
			for(String piece : tableOfPieces){
				toWrite = toWrite + piece;
			}
			try {
				BufferedWriter out = new BufferedWriter(
						new FileWriter(path));
				out.write(toWrite);
				out.close();
			} catch (IOException e) {
				System.out.println("Error while writing file "+path);
				PeerConfig.writeInLogs("Error while writing file "+path);
			}
		}
	}

	void updatePieces(String hash, String[] pieces){
		// need to update
		String res;
		//lock.lock();

		//lock.unlock();

		return;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	public void run(){
		try{
			buffermapInit();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}