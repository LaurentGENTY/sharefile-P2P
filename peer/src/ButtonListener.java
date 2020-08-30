import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import javax.swing.*;
import java.awt.*;
public class ButtonListener implements ActionListener{
        int frameToCall;
        guiRunner runner;
        Sender send;
        ArrayList<JTextField> texts;
        /*1.main
        * 2.look
        * 3.interested
        * 4.getf
        * 5.getp
        * */
        public ButtonListener(guiRunner gR,int fTC,Sender send,ArrayList<JTextField> texts){
            this.runner = gR;
            this.frameToCall = fTC;
            this.send = send;
            this.texts = texts;
        }

        public void actionPerformed(ActionEvent clic) {
            send.flush(texts);
            this.runner.callChangeFrame(this.frameToCall);
        }
    }