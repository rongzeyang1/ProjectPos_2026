package pos;

import javax.swing.UIManager;
import pos.ui.PosFrame;

/** Entry point of the POS application. */
public final class Main {

    private Main() { }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignore) {
            // fall back to the default look and feel
        }
        Register register = new Register(new ProductCatalog());
        new PosFrame(register).setVisible(true);
    }
}
