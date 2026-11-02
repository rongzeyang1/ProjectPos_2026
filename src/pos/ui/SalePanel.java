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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import pos.ProductDescription;
import pos.ReceiptPrinter;
import pos.Register;
import pos.Sale;
import pos.SalesLineItem;

/** The Process Sale screen: catalog on the left, the current sale on the right. */
public class SalePanel extends JPanel {

    private final PosFrame frame;
    private final Register register;
    private final DefaultTableModel catalogModel = Widgets.model(new String[]{"ID", "Description", "Price"});
    private final DefaultTableModel saleModel = Widgets.model(new String[]{"#", "Description", "Qty", "Unit", "Subtotal"});
    private final JTable catalogTable = Widgets.table(catalogModel);
    private final JTable saleTable = Widgets.table(saleModel);
    private final JLabel totalLabel = Widgets.bigAmount(Ui.f(30, true));
    private final JLabel countLabel = new JLabel("0 items");
    private final JSpinner qty = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));

    public SalePanel(PosFrame frame, Register register) {
        this.frame = frame;
        this.register = register;
        setLayout(new BorderLayout(14, 0));
        setBackground(Ui.BG);

        add(catalogCard(), BorderLayout.WEST);
        add(saleCard(), BorderLayout.CENTER);

        loadCatalog();
        refresh();
        Widgets.rightAlign(catalogTable, 2);
        Widgets.rightAlign(saleTable, 2, 3, 4);
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
        qty.setFont(Ui.f(13));
        foot.add(qty);
        JButton add = Widgets.button("Add to sale", Ui.NAVY, Color.WHITE);
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

    private JPanel saleCard() {
        JPanel card = Widgets.card("CURRENT SALE");

        JScrollPane sp = new JScrollPane(saleTable);
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
        JLabel t = new JLabel("TOTAL");
        t.setFont(Ui.f(14, true));
        t.setForeground(Ui.MUTED);
        bigTotal.add(t);
        bigTotal.add(totalLabel);
        totals.add(countLabel, BorderLayout.WEST);
        totals.add(bigTotal, BorderLayout.EAST);
        south.add(totals, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(1, 4, 8, 0));
        buttons.setOpaque(false);
        JButton newSale = Widgets.button("New Sale", new Color(0xE8EFF7), Ui.INK);
        newSale.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register.makeNewSale();
                refresh();
                frame.setStatus("New sale started.");
            }
        });
        JButton remove = Widgets.button("Remove Last", new Color(0xE8EFF7), Ui.INK);
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (register.getSale() != null) { register.getSale().removeLastLineItem(); refresh(); }
            }
        });
        JButton cash = Widgets.button("Cash Payment", Ui.ACCENT, Color.WHITE);
        cash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { payCash(); }
        });
        JButton cardBtn = Widgets.button("Card Payment", new Color(0x2B5F8F), Color.WHITE);
        cardBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { payCard(); }
        });
        buttons.add(newSale);
        buttons.add(remove);
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
        saleModel.setRowCount(0);
        Sale sale = register.getSale();
        int units = 0;
        if (sale != null) {
            int n = 1;
            for (SalesLineItem item : sale.getLineItems()) {
                saleModel.addRow(new Object[]{
                        String.valueOf(n++),
                        item.getDescription().getDescription(),
                        String.valueOf(item.getQuantity()),
                        Ui.money(item.getDescription().getPrice()),
                        Ui.money(item.getSubtotal())});
                units += item.getQuantity();
            }
            totalLabel.setText(Ui.money(sale.getTotal()));
        } else {
            totalLabel.setText(Ui.money(0));
        }
        countLabel.setText(units + (units == 1 ? " item" : " items"));
        int[] w = {34, 220, 50, 80, 100};
        for (int i = 0; i < w.length; i++) {
            saleTable.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        }
    }

    private void addSelected() {
        int row = catalogTable.getSelectedRow();
        if (row < 0) { frame.setStatus("Select a product in the catalog first."); return; }
        if (register.getSale() == null) register.makeNewSale();
        String id = (String) catalogModel.getValueAt(row, 0);
        int q = ((Integer) qty.getValue()).intValue();
        register.enterItem(id, q);
        refresh();
        frame.setStatus("Added " + q + " x " + catalogModel.getValueAt(row, 1) + ".");
    }

    private void payCash() {
        Sale sale = register.getSale();
        if (sale == null || sale.getLineItems().isEmpty()) {
            frame.setStatus("There is no sale to pay for.");
            return;
        }
        double total = sale.getTotal();
        String input = JOptionPane.showInputDialog(this,
                "Total due: " + Ui.money(total) + "\nCash tendered:", String.format("%.2f", Math.ceil(total)));
        if (input == null) return;
        double tendered;
        try {
            tendered = Double.parseDouble(input.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
            return;
        }
        if (tendered < total) {
            JOptionPane.showMessageDialog(this, "Cash tendered is less than the total.");
            return;
        }
        double change = register.makePayment(tendered);
        refresh();
        frame.setStatus("Sale complete. Change: " + Ui.money(change));
        frame.receiptDialog("Receipt", ReceiptPrinter.receipt(sale)).setVisible(true);
        register.makeNewSale();
        refresh();
    }

    private void payCard() {
        Sale sale = register.getSale();
        if (sale == null || sale.getLineItems().isEmpty()) {
            frame.setStatus("There is no sale to pay for.");
            return;
        }
        String cardNumber = JOptionPane.showInputDialog(this,
                "Total due: " + Ui.money(sale.getTotal()) + "\nCard number:");
        if (cardNumber == null) return;
        if (!register.makeCardPayment(cardNumber)) {
            JOptionPane.showMessageDialog(this, "Authorisation declined by the payment service.");
            return;
        }
        refresh();
        frame.setStatus("Card payment authorised.");
        frame.receiptDialog("Card receipt", ReceiptPrinter.receipt(sale)).setVisible(true);
        register.makeNewSale();
        refresh();
    }
}
