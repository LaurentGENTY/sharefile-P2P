import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class guiRunner {
    JFrame frame;
    loading loader;
    JPanel mainCurrentPanel;

    public guiRunner(){
        //Creating the Frame
        frame = new JFrame("Peer interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        loader = new loading(this);
    }
    public void run() {
        this.mainCurrentPanel = this.loader.getAnnouncePanel();
        JPanel currentPanel = this.loader.getAnnouncePanel();
        JMenuBar menuBar = this.loader.getMenuBar();
        this.frame.getContentPane().add(BorderLayout.NORTH,menuBar);
        this.frame.getContentPane().add(currentPanel);
        this.frame.setVisible(true);
    }

    void callChangeFrame(int toCall){
        switch(toCall){
            case 1 :
                if(PeerConfig.okAnnounce) {
                    this.changePage(loader.getMainPanel());
                    this.mainCurrentPanel = loader.getMainPanel();
                }
                break;
            case 2 :
                this.changePage(loader.getLookPanel());
                break;
            case 3 :
                this.changePage(loader.getIntPanel());
                break;
            case 4 :
                this.changePage(loader.getGetFPanel());
                break;
            case 5 :
                this.changePage(loader.getGetPPanel());
                break;
            case 6 :
                this.changePage(loader.getConfigPanel());
                break;
            case 7 :
                this.changePage(loader.getFilePanel());
                break;
            case 8 :
                this.changePage(loader.getMenuConfigPanel());
                break;
            case 9 :
                this.changePage(this.mainCurrentPanel);
                break;
            default :
                System.out.println("Error the page you want to load doesn't exist");
                PeerConfig.writeInLogs("Error the page you want to load doesn't exist");
        }
    }

    void changePage(JPanel newPanel){
        this.frame.getContentPane().removeAll();
        JPanel currentPanel = newPanel;
        JMenuBar menuBar = loader.getMenuBar();
        this.frame.getContentPane().add(BorderLayout.NORTH,menuBar);
        this.frame.getContentPane().add(currentPanel);
        this.frame.revalidate();
        this.frame.repaint();
    }
}