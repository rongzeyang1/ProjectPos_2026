package pos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import pos.Register;
import pos.ReceiptPrinter;

/** Main window of the POS terminal: a Sale tab and a Returns tab. */
public class PosFrame extends JFrame {

    private final Register register;
    private final SalePanel salePanel;
    private final ReturnPanel returnPanel;
    private final JTabbedPane tabs;
    private final JLabel status = new JLabel(" Ready.");

    public PosFrame(Register register) {
        super("POS Terminal 01  -  Riverside Supermarket");
        this.register = register;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        add(header(), BorderLayout.NORTH);
        salePanel = new SalePanel(this, register);
        returnPanel = new ReturnPanel(this, register);
        tabs = new JTabbedPane();
        tabs.setFont(Ui.f(14, true));
        tabs.addTab("   Sale   ", salePanel);
        tabs.addTab("   Returns   ", returnPanel);
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Ui.BG);
        body.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 12));
        body.add(tabs, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
        add(statusBar(), BorderLayout.SOUTH);

        setSize(1060, 690);
        setLocationRelativeTo(null);
    }

    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Ui.NAVY);
        p.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("RIVERSIDE SUPERMARKET");
        title.setFont(Ui.f(19, true));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("POS Terminal 01   |   Cashier: A. Chen   |   "
                + new SimpleDateFormat("dd MMM yyyy", java.util.Locale.ENGLISH).format(new Date()));
        sub.setFont(Ui.f(12));
        sub.setForeground(new Color(0xC6D6E4));

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(title, BorderLayout.NORTH);
        left.add(sub, BorderLayout.SOUTH);

        JLabel shift = new JLabel("SHIFT OPEN");
        shift.setFont(Ui.f(12, true));
        shift.setForeground(new Color(0xA8E0C8));
        shift.setHorizontalAlignment(SwingConstants.RIGHT);

        p.add(left, BorderLayout.WEST);
        p.add(shift, BorderLayout.EAST);
        return p;
    }

    private JPanel statusBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p.setBackground(new Color(0xE8EFF7));
        status.setFont(Ui.f(12));
        status.setForeground(Ui.INK);
        p.add(status);
        return p;
    }

    public void setStatus(String text) { status.setText(" " + text); }

    public SalePanel getSalePanel()     { return salePanel; }
    public ReturnPanel getReturnPanel() { return returnPanel; }

    /** Fills a demo sale so the screen can be photographed for the report. */
    public void demoSale() {
        register.makeNewSale();
        register.enterItem("1001", 2);
        register.enterItem("1003", 1);
        register.enterItem("1004", 1);
        register.enterItem("1009", 2);
        salePanel.refresh();
        tabs.setSelectedIndex(0);
        setStatus("4 products, 6 units on the current sale.  End the sale to take payment.");
    }

    /** Fills a demo return. */
    public void demoReturn() {
        register.makeNewReturn();
        register.enterReturnItem("1002", 1);
        register.enterReturnItem("1009", 2);
        returnPanel.refresh();
        tabs.setSelectedIndex(1);
        setStatus("Return in progress: 3 units.  Choose the refund method to finish.");
    }

    /** Builds the receipt window (kept as a method so it can also be screenshotted). */
    public JDialog receiptDialog(String title, String text) {
        final JDialog d = new JDialog(this, title, true);
        JTextArea area = new JTextArea(text);
        area.setFont(Ui.mono(13));
        area.setEditable(false);
        area.setBackground(Color.WHITE);
        area.setForeground(new Color(0x24303C));
        area.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(BorderFactory.createEmptyBorder());
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(Color.WHITE);
        d.add(sp, BorderLayout.CENTER);
        int lines = text.split("\n", -1).length;
        d.setSize(430, Math.min(560, 60 + lines * 19));
        d.setLocationRelativeTo(this);
        return d;
    }
}
