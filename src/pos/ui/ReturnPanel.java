package pos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import pos.ProductDescription;
import pos.ReceiptPrinter;
import pos.Register;
import pos.Return;
import pos.ReturnLineItem;

/** The Handle Returns screen: same layout as the sale screen, but produces a refund. */
public class ReturnPanel extends JPanel {

    private final PosFrame frame;
    private final Register register;
    private final DefaultTableModel catalogModel = Widgets.model(new String[]{"ID", "Description", "Price"});
    private final DefaultTableModel returnModel = Widgets.model(new String[]{"#", "Description", "Qty", "Unit", "Refund"});
    private final JTable catalogTable = Widgets.table(catalogModel);
    private final JTable returnTable = Widgets.table(returnModel);
    private final JLabel totalLabel = Widgets.bigAmount(Ui.f(30, true));
    private final JLabel countLabel = new JLabel("0 items");
    private final JSpinner qty = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));

    public ReturnPanel(PosFrame frame, Register register) {
        this.frame = frame;
        this.register = register;
        setLayout(new BorderLayout(14, 0));
        setBackground(Ui.BG);
        add(catalogCard(), BorderLayout.WEST);
        add(returnCard(), BorderLayout.CENTER);
        loadCatalog();
        refresh();
        Widgets.rightAlign(catalogTable, 2);
        Widgets.rightAlign(returnTable, 2, 3, 4);
    }

    private JPanel catalogCard() {
        JPanel card = Widgets.card("PRODUCT CATALOG");
        catalogTable.setFillsViewportHeight(true);
        catalogTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        catalogTable.getColumnModel().getColumn(1).setPreferredWidth(190);
        catalogTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        JScrollPane sp = new JScrollPane(catalogTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        card.add(sp, BorderLayout.CENTER);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        foot.setOpaque(false);
        JLabel l = new JLabel("Qty");
        l.setFont(Ui.f(12));
        l.setForeground(Ui.MUTED);
        foot.add(l);
        foot.add(qty);
        JButton add = Widgets.button("Add to return", Ui.NAVY, Color.WHITE);
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { addSelected(); }
        });
        foot.add(add);
        card.add(foot, BorderLayout.SOUTH);
        card.setPreferredSize(new Dimension(372, 0));
        catalogTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) addSelected();
            }
        });
        return card;
    }

    private JPanel returnCard() {
        JPanel card = Widgets.card("CURRENT RETURN");
        JScrollPane sp = new JScrollPane(returnTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        card.add(sp, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(0, 10));
        south.setOpaque(false);

        JPanel totals = new JPanel(new BorderLayout());
        totals.setOpaque(false);
        totals.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Ui.LINE));
        countLabel.setFont(Ui.f(12));
        countLabel.setForeground(Ui.MUTED);
        JPanel bigTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        bigTotal.setOpaque(false);
        JLabel t = new JLabel("REFUND");
        t.setFont(Ui.f(14, true));
        t.setForeground(Ui.MUTED);
        bigTotal.add(t);
        bigTotal.add(totalLabel);
        totals.add(countLabel, BorderLayout.WEST);
        totals.add(bigTotal, BorderLayout.EAST);
        south.add(totals, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(1, 3, 8, 0));
        buttons.setOpaque(false);
        JButton newReturn = Widgets.button("New Return", new Color(0xE8EFF7), Ui.INK);
        newReturn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register.makeNewReturn();
                refresh();
                frame.setStatus("New return started.");
            }
        });
        JButton cash = Widgets.button("Refund Cash", Ui.ACCENT, Color.WHITE);
        cash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { refund(true); }
        });
        JButton cardBtn = Widgets.button("Refund Card", new Color(0x2B5F8F), Color.WHITE);
        cardBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { refund(false); }
        });
        buttons.add(newReturn);
        buttons.add(cash);
        buttons.add(cardBtn);
        south.add(buttons, BorderLayout.SOUTH);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        wrap.add(south, BorderLayout.CENTER);
        card.add(wrap, BorderLayout.SOUTH);
        return card;
    }

    private void loadCatalog() {
        catalogModel.setRowCount(0);
        for (ProductDescription d : register.getCatalog().getAll()) {
            catalogModel.addRow(new Object[]{d.getItemID(), d.getDescription(), Ui.money(d.getPrice())});
        }
    }

    public void refresh() {
        returnModel.setRowCount(0);
        Return ret = register.getCurrentReturn();
        int units = 0;
        if (ret != null) {
            int n = 1;
            for (ReturnLineItem item : ret.getLineItems()) {
                returnModel.addRow(new Object[]{
                        String.valueOf(n++),
                        item.getDescription().getDescription(),
                        String.valueOf(item.getQuantity()),
                        Ui.money(item.getDescription().getPrice()),
                        Ui.money(item.getSubtotal())});
                units += item.getQuantity();
            }
            totalLabel.setText(Ui.money(ret.getTotal()));
        } else {
            totalLabel.setText(Ui.money(0));
        }
        countLabel.setText(units + (units == 1 ? " item" : " items"));
        int[] w = {34, 220, 50, 80, 100};
        for (int i = 0; i < w.length; i++) {
            returnTable.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        }
    }

    private void addSelected() {
        int row = catalogTable.getSelectedRow();
        if (row < 0) { frame.setStatus("Select a product in the catalog first."); return; }
        if (register.getCurrentReturn() == null) register.makeNewReturn();
        String id = (String) catalogModel.getValueAt(row, 0);
        int q = ((Integer) qty.getValue()).intValue();
        register.enterReturnItem(id, q);
        refresh();
        frame.setStatus("Returned " + q + " x " + catalogModel.getValueAt(row, 1) + ".");
    }

    private void refund(boolean cash) {
        Return ret = register.getCurrentReturn();
        if (ret == null || ret.getLineItems().isEmpty()) {
            frame.setStatus("There is no return to refund.");
            return;
        }
        double amount = register.makeRefund(cash);
        refresh();
        frame.setStatus("Refund of " + Ui.money(amount) + " issued by " + (cash ? "cash." : "card."));
        frame.receiptDialog("Refund receipt", ReceiptPrinter.refundReceipt(ret)).setVisible(true);
        register.makeNewReturn();
        refresh();
    }
}
