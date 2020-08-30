import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
class loading {
    JPanel mainPanel;
    JMenuBar menuBar;
    JPanel lookPanel;
    JPanel intPanel;
    JPanel getPPanel;
    JPanel getFPanel;
    JPanel announcePanel;
    JPanel configPanel;
    JPanel filePanel;
    JPanel menuConfigPanel;

    public loading(guiRunner runner){
        loadMainPage(runner);
        loadLookPage(runner);
        loadGetFPage(runner);
        loadGetPPage(runner);
        loadIntPage(runner);
        loadAnnouncePanel(runner);
        loadConfigPanel(runner);
        loadFilePanel(runner);
        loadMenuConfigPanel(runner);
    }

    void loadConfigPanel(guiRunner runner){
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel mainText = new JLabel("You can change the announce config.                           "); // ALED j'arrive pas à rendre ça responsive
        JLabel portText = new JLabel("Peer's listening port :\n");
        JLabel ipTrackerText = new JLabel("Tracker's IP address :\n");
        JLabel portTrackerText = new JLabel("Tracker's listening port :\n");
        JLabel Text = new JLabel("Tracker's listening port :\n");
        JTextField portf = new JTextField(1000); // accepts upto 1000 characters
        JTextField tPortf = new JTextField(1000); // accepts upto 1000 characters
        JTextField iAddressf = new JTextField(1000); // accepts upto 1000 characters
        JButton bAddPort = new JButton("Change");
        JButton bAddPortT = new JButton("Change");
        JButton bAddAddrT = new JButton("Change");
        JButton bAuto = new JButton("Auto");
        JButton bBack = new JButton("Back");
        GridBagConstraints gbc = new GridBagConstraints();


        JLabel blank = new JLabel(""); // Sans ce bouton l'affichage des textfield collapse (l'espace est déjà rempli)


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 15, 20, 0);
        panel.add(mainText,gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(portText, gbc);


        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(portf, gbc);


        gbc.gridx = 2;
        //gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.insets = new Insets(10, 0, 5, 10);
        panel.add(bAuto, gbc);

        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 5);
        panel.add(bAddPort, gbc);



        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(blank, gbc);



        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(portTrackerText, gbc);


        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(tPortf, gbc);


        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 5);
        panel.add(bAddPortT, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(ipTrackerText, gbc);


        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(iAddressf, gbc);


        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 5);
        panel.add(bAddAddrT, gbc);



        gbc.gridy = 4;
        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        //gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.;
        gbc.insets = new Insets(10, 10, 0, 10);
        panel.add(bBack, gbc);



        Sender sendPort = new ConfigTrackerSender();
        Sender sendPortT = new ConfigTrackerSender();
        Sender sendAddress = new ConfigTrackerSender();
        ArrayList<JTextField> textPort = new ArrayList<JTextField>();
        ArrayList<JTextField> textPortT = new ArrayList<JTextField>();
        ArrayList<JTextField> textAddT = new ArrayList<JTextField>();
        ArrayList<JTextField> textAuto = new ArrayList<JTextField>();
        ArrayList<JTextField> texts = new ArrayList<JTextField>();
        textAddT.add(iAddressf);
        textPort.add(portf);
        textPortT.add(tPortf);
        texts.add(iAddressf);
        texts.add(tPortf);
        texts.add(portf);
        JTextField tf1 = new JTextField();
        JTextField tf2 = new JTextField();
        JTextField tf3 = new JTextField();
        JTextField tf0 = new JTextField();
        JTextField tf6 = new JTextField();
        tf1.setText("1");
        tf2.setText("2");
        tf3.setText("3");
        tf6.setText("6");
        tf0.setText("0");
        textAddT.add(tf1);
        textPort.add(tf2);
        textPortT.add(tf3);
        textAuto.add(tf0);
        textAuto.add(tf6);
        Sender flusher = new ConfigTrackerSender();
        bBack.addActionListener(new ButtonListener(runner,9,flusher,texts));
        bAuto.addActionListener(new buttonListenerSender(sendPort,textAuto,true));
        bAddAddrT.addActionListener(new buttonListenerSender(sendAddress,textAddT,true));
        bAddPort.addActionListener(new buttonListenerSender(sendPort,textPort,true));
        bAddPortT.addActionListener(new buttonListenerSender(sendPortT,textPortT,true));
        this.configPanel = panel;
    }


    void loadFilePanel(guiRunner runner){
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel mainText = new JLabel("You can add file for sharing.\n"); // ALED j'arrive pas à rendre ça responsive
        JLabel fileText = new JLabel("File you want to share :");
        JTextField fileTf = new JTextField(1000); // accepts upto 1000 characters
        JButton bAdd = new JButton("Add");
        JButton bBack = new JButton("Back");
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel blank = new JLabel(""); // Sans ce bouton l'affichage des textfield collapse (car l'espace est déjà rempli)

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 15, 20, 0);
        panel.add(mainText,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(fileText, gbc);


        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(fileTf, gbc);


        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 5);
        panel.add(bAdd, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(blank, gbc);

        gbc.gridy = 2;
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.;
        gbc.insets = new Insets(10, 10, 0, 10);
        panel.add(bBack, gbc);

        ArrayList<JTextField> fileArray = new ArrayList<JTextField>();
        Sender send= new AddFileSender();
        fileArray.add(fileTf);
        bBack.addActionListener(new ButtonListener(runner,9,send,fileArray));
        bAdd.addActionListener(new buttonListenerSender(send,fileArray,true));
        bAdd.addActionListener(new buttonListenerSender(send,fileArray,false));
        this.filePanel = panel;
    }

    void loadAnnouncePanel(guiRunner runner){
        Sender send = new AnnounceToTracker();
        ArrayList<JTextField> texts = new ArrayList<JTextField>();
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel mainText = new JLabel("Before being allowed to interact with the others you have to announce yourself");
        JButton buttonConfig = new JButton("Change config");
        buttonConfig.addActionListener(new ButtonListener(runner,6,send,texts));
        JButton buttonFiles = new JButton("Add files");
        buttonFiles.addActionListener(new ButtonListener(runner,7,send,texts));
        JButton buttonSend = new JButton("Send");
        buttonFiles.addActionListener(new ButtonListener(runner,1,send,texts));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 5, 0, 0);
        panel.add(mainText,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor =  GridBagConstraints.BASELINE_LEADING;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10,75,10,35);
        panel.add(buttonConfig,gbc);

        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10,35,100,10);
        panel.add(buttonFiles,gbc);

        gbc.gridx = 0;
        gbc.gridy =1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10,10,10,10);
        panel.add(buttonSend,gbc);


        buttonSend.addActionListener(new ButtonListener(runner,1,send,texts));
        buttonSend.addActionListener(new buttonListenerSender(send,texts,false));
        this.announcePanel = panel;

    }

    void loadMainPage(guiRunner runner) {
        //Creating Buttons
        JLabel mainText = new JLabel("Welcome on the peer interface.\n");
        JLabel blank = new JLabel("");
        Sender send = new AnnounceToTracker();
        ArrayList<JTextField> arr = new ArrayList<JTextField>();
        JPanel Buttons = new JPanel(new GridLayout(3, 2, 10, 10));
        JButton buttonLo = new JButton("Look for a file");
        buttonLo.addActionListener(new ButtonListener(runner,2,send,arr));
        JButton buttonGet = new JButton("Get file loading informations");
        buttonGet.addActionListener(new ButtonListener(runner,4,send,arr));
        JButton buttonInt = new JButton("Announce interestment");
        buttonInt.addActionListener(new ButtonListener(runner,3,send,arr));
        JButton buttonGetP = new JButton("Get Pieces");
        buttonGetP.addActionListener(new ButtonListener(runner,5,send,arr));


        // Adding buttons to the panel
        Buttons.add(mainText);
        Buttons.add(blank);
        Buttons.add(buttonLo);
        Buttons.add(buttonGet);
        Buttons.add(buttonGetP);
        Buttons.add(buttonInt);

        //Creating the menu bar
        JMenuBar mb = new JMenuBar();
        JMenuItem m1 = new JMenuItem("Config");
        JMenuItem m2 = new JMenuItem("Add files");
        mb.add(m1);
        mb.add(m2);

        m1.addActionListener(new ButtonListener(runner,8,send,arr));
        m2.addActionListener(new ButtonListener(runner,7,send,arr));
        this.mainPanel = Buttons;
        this.menuBar = mb;
    }

    void loadMenuConfigPanel(guiRunner runner){
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel mainText = new JLabel("You can change the config.");
        JLabel seedText = new JLabel("seed.dat location :\n");
        JLabel leechText = new JLabel("leech.dat location :\n");
        JTextField seedTf = new JTextField(1000); // accepts upto 1000 characters
        JTextField leechTf = new JTextField(1000); // accepts upto 1000 characters
        JButton bChangeSeed = new JButton("Change");
        JButton bChangeLeech = new JButton("Change");

        JButton bBack = new JButton("Back");
        GridBagConstraints gbc = new GridBagConstraints();


        JLabel blank = new JLabel(""); // Sans ce bouton l'affichage des textfield collapse (l'espace est déjà rempli)


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 15, 20, 0);
        panel.add(mainText,gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(seedText, gbc);


        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(seedTf, gbc);


        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 5);
        panel.add(bChangeSeed, gbc);






        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(leechText, gbc);


        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(leechTf, gbc);


        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 10, 0, 5);
        panel.add(bChangeLeech, gbc);


        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(blank, gbc);

        gbc.gridy = 4;
        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.;
        gbc.insets = new Insets(10, 10, 0, 10);
        panel.add(bBack, gbc);


        Sender sendSeed = new ConfigTrackerSender();
        Sender sendLeech = new ConfigTrackerSender();
        ArrayList<JTextField> textSeed = new ArrayList<JTextField>();
        ArrayList<JTextField> textLeech = new ArrayList<JTextField>();
        ArrayList<JTextField> texts = new ArrayList<JTextField>();
        textSeed.add(seedTf);
        textLeech.add(leechTf);
        texts.add(seedTf);
        texts.add(leechTf);
        JTextField tf4 = new JTextField();
        JTextField tf5 = new JTextField();
        tf4.setText("4");
        tf5.setText("5");
        textSeed.add(tf4);
        textLeech.add(tf5);
        Sender flusher = new ConfigTrackerSender();
        bBack.addActionListener(new ButtonListener(runner,9,flusher,texts));
        bChangeSeed.addActionListener(new buttonListenerSender(sendSeed,textSeed,true));
        bChangeLeech.addActionListener(new buttonListenerSender(sendLeech,textLeech,true));
        this.menuConfigPanel = panel;

    }

    void loadLookPage(guiRunner runner) {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel mainText = new JLabel("Choose the criterions you want to add.\n"); // ALED j'arrive pas à rendre ça responsive
        JLabel criterionText = new JLabel("Criterion :\n");
        JTextField tf = new JTextField(1000); // accepts upto 1000 characters
        JButton bAdd = new JButton("Add");
        JButton bSend = new JButton("Send");
        JButton bBack = new JButton("Back");
        GridBagConstraints gbc = new GridBagConstraints();


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 15, 0, 0);
        panel.add(mainText,gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(criterionText, gbc);


        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(tf, gbc);


        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 5);
        panel.add(bAdd, gbc);



        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(bSend, gbc);


        gbc.gridx = 3;
        gbc.weightx = 0.;
        gbc.insets = new Insets(0, 3, 0, 10);
        panel.add(bBack, gbc);



        Sender send = new lookTracker();
        ArrayList<JTextField> texts = new ArrayList<JTextField>();
        texts.add(tf);
        bBack.addActionListener(new ButtonListener(runner,9,send,texts));
        bSend.addActionListener(new buttonListenerSender(send,texts,false));
        bAdd.addActionListener(new buttonListenerSender(send,texts,true));
        this.lookPanel = panel;
    }

    void loadGetFPage(guiRunner runner) {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel mainText = new JLabel("Choose the key you want to get the file.\n"); // ALED j'arrive pas à rendre ça responsive
        JLabel criterionText = new JLabel("Key :\n");
        JTextField tf = new JTextField(1000); // accepts upto 1000 characters
        JButton bSend = new JButton("Send");
        JButton bBack = new JButton("Back");
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 15, 0, 0);
        panel.add(mainText,gbc);



        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(criterionText, gbc);



        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(tf, gbc);



        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(bSend, gbc);


        gbc.gridx = 3;
        gbc.weightx = 0.;
        gbc.insets = new Insets(0, 3, 0, 10);
        panel.add(bBack, gbc);


        Sender send = new GetFileTracker();
        ArrayList<JTextField> texts = new ArrayList<JTextField>();
        texts.add(tf);
        bBack.addActionListener(new ButtonListener(runner,9,send,texts));
        bSend.addActionListener(new buttonListenerSender(send,texts,false));
        this.getFPanel = panel;
    }

    void loadGetPPage(guiRunner runner) {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel mainText = new JLabel("Choose the key you want the file and the indexes of the file you want to get.\n"); // ALED j'arrive pas à rendre ça responsive
        JLabel keyText = new JLabel("Key :\n");
        JLabel indexText = new JLabel("Index :\n");
        JLabel addressText = new JLabel("Peer address :\n");
        JLabel portText = new JLabel("Peer port :\n");
        JTextField keyField = new JTextField(1000); // accepts upto 1000 characters
        JTextField indexField = new JTextField(1000); // accepts upto 1000 characters
        JTextField addressField = new JTextField(1000); // accepts upto 1000 characters
        JTextField portField = new JTextField(1000); // accepts upto 1000 characters
        JButton bAdd = new JButton("Add");
        JButton bSend = new JButton("Send");
        JButton bBack = new JButton("Back");
        GridBagConstraints gbc = new GridBagConstraints();


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 15, 0, 0);
        panel.add(mainText,gbc);



        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(keyText, gbc);



        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(keyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(addressText, gbc);



        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(portText, gbc);



        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(portField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(indexText, gbc);



        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(indexField, gbc);



        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 0, 5);
        panel.add(bAdd, gbc);



        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(bSend, gbc);



        gbc.gridx = 3;
        gbc.weightx = 0.;
        gbc.insets = new Insets(0, 3, 0, 10);
        panel.add(bBack, gbc);



        Sender send = new GetPiecesPeer();
        ArrayList<JTextField> texts = new ArrayList<JTextField>();
        texts.add(addressField);
        texts.add(portField);
        texts.add(keyField);
        ArrayList<JTextField> textFlush = new ArrayList<JTextField>(texts);
        textFlush.add(indexField);
        ArrayList<JTextField> addIndexTf = new ArrayList<JTextField>();
        addIndexTf.add(indexField);
        bBack.addActionListener(new ButtonListener(runner,9,send,textFlush));
        bSend.addActionListener(new buttonListenerSender(send,texts,false));
        bAdd.addActionListener(new buttonListenerSender(send,addIndexTf,true));
        this.getPPanel = panel;
    }

    void loadIntPage(guiRunner runner) {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel mainText = new JLabel("Choose the key you want to get the file.\n"); // ALED j'arrive pas à rendre ça responsive
        JLabel criterionText = new JLabel("Key :");
        JLabel portText = new JLabel("Peer Port :");
        JLabel addressText = new JLabel("Peer address :");
        JTextField keyTf = new JTextField(1000); // accepts upto 1000 characters
        JTextField portTf = new JTextField(1000); // accepts upto 1000 characters
        JTextField addressTf = new JTextField(1000); // accepts upto 1000 characters
        JButton bSend = new JButton("Send");
        JButton bBack = new JButton("Back");
        GridBagConstraints gbc = new GridBagConstraints();


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 15, 0, 0);
        panel.add(mainText,gbc);



        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(addressText, gbc);



        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(addressTf, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(portText, gbc);



        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(portTf, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(criterionText, gbc);



        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.insets = new Insets(10, 30, 10, 10);
        panel.add(keyTf, gbc);



        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(bSend, gbc);



        gbc.gridx = 3;
        gbc.weightx = 0.; /* remettons le poids à zéro. */
        gbc.insets = new Insets(0, 3, 0, 10);
        panel.add(bBack, gbc);


        //bBack.addActionListener(new ButtonListener(runner,9));

        Sender send = new InterestedPeer();
        ArrayList<JTextField> texts = new ArrayList<JTextField>();
        texts.add(addressTf);
        texts.add(portTf);
        texts.add(keyTf);
        bBack.addActionListener(new ButtonListener(runner,9,send,texts));
        bSend.addActionListener(new buttonListenerSender(send,texts,false));
        this.intPanel = panel;
    }

    /*****************************   Getters **************************/
    public JPanel getMainPanel(){
        return this.mainPanel;
    }

    public JMenuBar getMenuBar(){
        return this.menuBar;
    }

    public JPanel getLookPanel() {
        return this.lookPanel;
    }

    public JPanel getIntPanel() {
        return this.intPanel;
    }

    public JPanel getGetPPanel() {
        return this.getPPanel;
    }

    public JPanel getGetFPanel() {
        return this.getFPanel;
    }

    public JPanel getAnnouncePanel() {
        return announcePanel;
    }

    public JPanel getFilePanel() {
        return filePanel;
    }

    public JPanel getMenuConfigPanel() {
        return menuConfigPanel;
    }

    public JPanel getConfigPanel() {
        return configPanel;
    }
}