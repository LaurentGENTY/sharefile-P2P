import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
public class buttonListenerSender implements ActionListener{
        Sender send;
        ArrayList<JTextField> texts;
        Boolean add;
        /*1.main
        * 2.look
        * 3.interested
        * 4.getf
        * 5.getp
        * */
        public buttonListenerSender(Sender s,ArrayList<JTextField> arr,Boolean add){
            this.send = s;
            this.texts = arr;
            this.add = add;
        }

        public void actionPerformed(ActionEvent clic) {
            if(!add){
                this.send.sendMessage(this.texts);
             }else{
                this.send.addValues(this.texts);
             }
        }
    }