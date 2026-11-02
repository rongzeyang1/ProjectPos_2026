package pos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/** Factory methods for the few widgets used by both panels. */
final class Widgets {

    private Widgets() { }

    static JPanel card(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(Ui.CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Ui.LINE, 1),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        JLabel l = new JLabel(title);
        l.setFont(Ui.f(12, true));
        l.setForeground(Ui.MUTED);
        p.add(l, BorderLayout.NORTH);
        return p;
    }

    static JButton button(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(Ui.f(13, true));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    static DefaultTableModel model(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    static JTable table(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(28);
        t.setFont(Ui.f(13));
        t.getTableHeader().setFont(Ui.f(12, true));
        t.getTableHeader().setBackground(new Color(0xE8EFF7));
        t.getTableHeader().setForeground(Ui.INK);
        t.setGridColor(Ui.LINE);
        t.setShowVerticalLines(false);
        t.setSelectionBackground(new Color(0xDDEEF7));
        t.setSelectionForeground(Ui.INK);
        t.setIntercellSpacing(new Dimension(0, 1));
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable tab, Object v, boolean sel,
                    boolean focus, int row, int col) {
                java.awt.Component c = super.getTableCellRendererComponent(tab, v, sel, focus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return c;
            }
        });
        return t;
    }

    static void rightAlign(JTable t, int... cols) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.RIGHT);
        r.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 14));
        for (int c : cols) {
            t.getColumnModel().getColumn(c).setCellRenderer(r);
        }
    }

    static JLabel bigAmount(Font font) {
        JLabel l = new JLabel("\u00A50.00");
        l.setFont(font);
        l.setForeground(Ui.NAVY);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }
}
