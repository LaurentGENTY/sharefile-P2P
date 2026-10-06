import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

/**
 * Demo-only launcher for one peer. It starts the original Swing GUI (same as
 * `java gui`), places its window, opens a log window under it and, with
 * --auto, drives the GUI by clicking the real buttons so a recording is
 * reproducible. The peer code itself is untouched.
 *
 * Usage (from <peer>/build): java DemoPeer <name> <seed|leech> <x> <y> <w> <h> <startDelayMs> [--auto]
 */
public class DemoPeer {

    static final int STEP_MS = 900;

    static guiRunner runner;
    static DemoLog log;

    public static void main(String[] a) throws Exception {
        String name = a[0];
        boolean seeder = a[1].equals("seed");
        int x = Integer.parseInt(a[2]), y = Integer.parseInt(a[3]);
        int w = Integer.parseInt(a[4]), h = Integer.parseInt(a[5]);
        int delay = Integer.parseInt(a[6]);
        boolean auto = a.length > 7 && a[7].equals("--auto");

        PeerConfig.getElementFromConfig();
        SwingUtilities.invokeAndWait(() -> {
            runner = new guiRunner();
            runner.frame.setTitle("Peer " + name + " (port " + PeerConfig.inPort + ")");
            runner.run();
            runner.frame.setBounds(x, y, w, h * 2 / 5);
            runner.frame.setAlwaysOnTop(true);
        });
        log = new DemoLog("Peer " + name + " - protocol log", DemoLog.Kind.PEER,
                new Rectangle(x, y + h * 2 / 5, w, h - h * 2 / 5));
        log.tail(PeerConfig.logFile);

        if (!auto) return;
        Thread.sleep(delay);
        if (seeder) seed(); else leech();
    }

    // Seeder: Add files -> type path -> Add -> Back -> Send (announce)
    static void seed() throws Exception {
        click("Add files");
        type("../share/demo.txt");
        click("Add");
        click("Back");
        click("Send");
    }

    // Leecher: Send (announce) -> Look for a file -> criterion -> Add -> Send
    //          -> Back -> Get file loading informations -> key -> Send (interested + getpieces)
    static void leech() throws Exception {
        click("Send");
        click("Look for a file");
        type("filename=\"demo.txt\"");
        click("Add");
        click("Send");
        String key = keyOf("demo.txt");
        click("Back");
        click("Get file loading informations");
        type(key);
        click("Send");
        waitForDownload(key);
    }

    static String keyOf(String filename) {
        for (Map.Entry<String, String> e : FileManager.getInstance().fileMatch.entrySet()) {
            if (e.getValue().endsWith(filename)) return e.getKey();
        }
        throw new IllegalStateException("No key found for " + filename);
    }

    static void waitForDownload(String key) throws Exception {
        File f = new File(PeerConfig.folderName + "/demo.txt");
        for (int i = 0; i < 100 && !f.exists(); i++) Thread.sleep(100);
        // Let the log window catch up so this line comes after the protocol exchange
        Thread.sleep(1000);
        if (f.exists() && FileManager.getFileChecksumMD5(f).equals(key)) {
            log.append("== demo.txt downloaded: " + f.length() + " bytes, MD5 matches the key ==");
        } else {
            log.append("== download failed ==");
        }
    }

    static void click(String label) throws Exception {
        Thread.sleep(STEP_MS);
        SwingUtilities.invokeAndWait(() -> {
            AbstractButton b = find(runner.frame.getContentPane(), AbstractButton.class, label);
            if (b == null) throw new IllegalStateException("Button not found: " + label);
            b.doClick(250);
        });
    }

    static void type(String text) throws Exception {
        Thread.sleep(STEP_MS);
        JTextField[] field = new JTextField[1];
        SwingUtilities.invokeAndWait(() -> field[0] = find(runner.frame.getContentPane(), JTextField.class, null));
        for (int i = 1; i <= text.length(); i++) {
            String partial = text.substring(0, i);
            SwingUtilities.invokeAndWait(() -> field[0].setText(partial));
            Thread.sleep(35);
        }
    }

    /** Depth-first search of a visible component of the given type (and label, for buttons). */
    static <T extends Component> T find(Container root, Class<T> type, String label) {
        for (Component c : root.getComponents()) {
            if (!c.isShowing()) continue;
            if (type.isInstance(c) && (label == null || label.equals(((AbstractButton) c).getText()))) {
                return type.cast(c);
            }
            if (c instanceof Container) {
                T found = find((Container) c, type, label);
                if (found != null) return found;
            }
        }
        return null;
    }
}
