package pos.ui;

import java.awt.Color;
import java.awt.Font;

/** Small styling helpers so every panel looks the same. */
public final class Ui {

    public static final Color NAVY   = new Color(0x1C3350);
    public static final Color INK    = new Color(0x243B56);
    public static final Color MUTED  = new Color(0x5C6F83);
    public static final Color BG     = new Color(0xF4F7FB);
    public static final Color CARD   = Color.WHITE;
    public static final Color ACCENT = new Color(0x1D6B57);
    public static final Color LINE   = new Color(0xD5E0EA);

    private Ui() { }

    public static Font f(int size)              { return new Font("Segoe UI", Font.PLAIN, size); }
    public static Font f(int size, boolean bold){ return new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size); }
    public static Font mono(int size)           { return new Font("Consolas", Font.PLAIN, size); }

    public static String money(double v) { return String.format("\u00A5%.2f", v); }
}
