package pos.ui;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.UIManager;
import pos.ProductCatalog;
import pos.ReceiptPrinter;
import pos.Register;

/** Renders the screens of the running application to PNG files for the report. */
public final class Shot {

    private Shot() { }

    public static void main(String[] args) throws Exception {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignore) {
            // default look and feel
        }

        Register register = new Register(new ProductCatalog());
        PosFrame frame = new PosFrame(register);
        frame.setSize(1060, 690);
        frame.setLocation(30, 30);
        frame.setVisible(true);
        Thread.sleep(500);

        frame.demoSale();
        Thread.sleep(400);
        save(frame, "shots/01_process_sale.png");

        frame.demoReturn();
        Thread.sleep(400);
        save(frame, "shots/02_handle_return.png");

        // receipt of a completed cash sale
        frame.demoSale();
        register.makePayment(60.00);
        JDialog dialog = frame.receiptDialog("Receipt", ReceiptPrinter.receipt(register.getSale()));
        dialog.setModal(false);
        dialog.setVisible(true);
        Thread.sleep(400);
        save(dialog.getContentPane(), "shots/03_receipt.png");
        dialog.dispose();
        frame.dispose();
        System.out.println("screens saved");
        System.exit(0);
    }

    private static void save(Component c, String path) throws Exception {
        BufferedImage img = new BufferedImage(Math.max(c.getWidth(), 1), Math.max(c.getHeight(), 1),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        c.paint(g);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("wrote " + path + " (" + img.getWidth() + "x" + img.getHeight() + ")");
    }
}
