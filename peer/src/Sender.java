import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import javax.swing.*;
import java.awt.*;
public interface Sender {

    public void sendMessage(ArrayList<JTextField> arr);
    public void addValues(ArrayList<JTextField> arr);
    public void flush(ArrayList<JTextField> arr);
}
