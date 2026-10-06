import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Demo-only log viewer: tails a log file in a Swing window and keeps only the
 * protocol lines (file payloads and periodic "update" heartbeats are hidden).
 */
public class DemoLog {

    enum Kind { PEER, TRACKER }

    private final JTextArea area = new JTextArea();
    private final Kind kind;

    DemoLog(String title, Kind kind, Rectangle bounds) {
        this.kind = kind;
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setBackground(new Color(0x1e1e1e));
        area.setForeground(new Color(0xd4d4d4));
        area.setMargin(new Insets(6, 8, 6, 8));
        JFrame frame = new JFrame(title);
        frame.add(new JScrollPane(area));
        frame.setBounds(bounds);
        // Keep demo windows above other apps so the recording only shows the demo
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }

    void append(String line) {
        SwingUtilities.invokeLater(() -> {
            area.append(line + "\n");
            area.setCaretPosition(area.getDocument().getLength());
        });
    }

    /** Returns the line to display, or null to hide it. */
    private String filter(String line) {
        // Tracker answers may carry NUL or CR characters that render as boxes
        line = line.replaceAll("\\p{Cntrl}", "");
        if (kind == Kind.TRACKER) {
            if (line.startsWith("Run on port")) return line;
            int i = line.indexOf("Here is the message: ");
            if (i < 0) return null;
            String msg = line.substring(i + "Here is the message: ".length()).trim();
            if (msg.startsWith("update")) return null;
            return "<- " + shorten(msg);
        }
        if (line.startsWith("<update") || line.equals(">ok ") || line.equals(">ok")) return null;
        if (line.startsWith("<") || line.startsWith(">")
                || line.startsWith("interested") || line.startsWith("getpieces")) {
            return shorten(line);
        }
        return null;
    }

    private static String shorten(String s) {
        return s.length() > 90 ? s.substring(0, 87) + "..." : s;
    }

    void tail(String path) {
        Thread t = new Thread(() -> {
            File f = new File(path);
            long pos = 0;
            while (true) {
                try {
                    if (f.length() > pos) {
                        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
                            raf.seek(pos);
                            byte[] buf = new byte[(int) (f.length() - pos)];
                            raf.readFully(buf);
                            // Only consume complete lines; a partial last line is re-read next time
                            int end = new String(buf, StandardCharsets.ISO_8859_1).lastIndexOf('\n') + 1;
                            pos += end;
                            for (String line : new String(buf, 0, end, StandardCharsets.ISO_8859_1).split("\r?\n")) {
                                String shown = filter(line);
                                if (shown != null) append(shown);
                            }
                        }
                    }
                    Thread.sleep(200);
                } catch (Exception e) {
                    try { Thread.sleep(500); } catch (InterruptedException ignored) { return; }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /** Standalone entry point, used for the tracker log: DemoLog <title> <file> <x> <y> <w> <h> */
    public static void main(String[] a) {
        DemoLog log = new DemoLog(a[0], Kind.TRACKER, new Rectangle(
                Integer.parseInt(a[2]), Integer.parseInt(a[3]), Integer.parseInt(a[4]), Integer.parseInt(a[5])));
        log.tail(a[1]);
    }
}
