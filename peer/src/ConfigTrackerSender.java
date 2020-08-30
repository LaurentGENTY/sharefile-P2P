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

public class ConfigTrackerSender extends PeerConfig implements Sender{


    public void sendMessage(ArrayList<JTextField> texts){ // on devrait pas passer le dossier en paramètre ?

    }

    public void addValues(ArrayList<JTextField> texts){
        if(texts.get(1).getText().equals("6") ||!texts.get(0).getText().equals("")){
            switch(texts.get(1).getText()){
                case "1" :
                    super.trackerIp = texts.get(0).getText();
                    break;
                case "2" :
                    super.inPort=Integer.parseInt(texts.get(0).getText());
                    break;
                case "3" :
                    super.trackerPort=Integer.parseInt(texts.get(0).getText());
                    break;
                case "4" :
                    super.seedFile=texts.get(0).getText();
                    break;
                case "5" :
                    super.leechFile=texts.get(0).getText();
                    break;
                case "6" :
                    super.inPort=0;
                    break;
                default :
                    System.out.println("Error while changing config values");
                    PeerConfig.writeInLogs("Error while changing config values");
            }
            texts.get(0).setText("");
        }
    }

    public void flush(ArrayList<JTextField> texts){
        for(JTextField text : texts) {
            text.setText("");
        }
    }


}
