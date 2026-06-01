package InvoiceGenerator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/* BUSINESS LOGIC */

class InvoiceCalculator {

    public static double calculateItemTotal(int qty, double price) {
        return qty * price;
    }

    public static double calculateTax(double subtotal) {
        return subtotal * 0.10;
    }

    public static double calculateDiscount(double subtotal) {
        return subtotal * 0.05;
    }

    public static double calculateGrandTotal(double subtotal, boolean tax, boolean discount) {
        double t = tax ? calculateTax(subtotal) : 0;
        double d = discount ? calculateDiscount(subtotal) : 0;
        return subtotal + t - d;
    }
}

/* VALIDATION LAYER */

class InvoiceValidator {

    public static void validateItem(String name, String qty, String price)
            throws EmptyFieldException, InvalidValueException {

        if (name.isEmpty() || qty.isEmpty() || price.isEmpty()) {
            throw new EmptyFieldException("All item fields are required!");
        }

        int q = Integer.parseInt(qty);
        double p = Double.parseDouble(price);

        if (q <= 0 || p <= 0) {
            throw new InvalidValueException("Quantity and Price must be greater than 0");
        }
    }

    public static void validateInvoice(String customer, String company, String date, String invNo)
            throws EmptyFieldException {

        if (customer.isEmpty() || company.isEmpty() || date.isEmpty() || invNo.isEmpty()) {
            throw new EmptyFieldException("Invoice fields cannot be empty");
        }
    }

    public static void validateDate(String date) throws DateTimeParseException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate.parse(date, fmt);
    }
}


/* CUSTOM EXCEPTIONS*/

class EmptyFieldException extends Exception {
    public EmptyFieldException(String msg) { super(msg); }
}

class InvalidValueException extends Exception {
    public InvalidValueException(String msg) { super(msg); }
}


/* GUI ( EVENT HANDLING) */

public class GUI extends JFrame {

    private JTextField txtItem, txtQty, txtPrice;
    private JTextField txtCustomer, txtCompany, txtDate, txtInv;
    private JCheckBox chkTax, chkDiscount;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;

    private double subtotal = 0;

    public GUI() {
        setTitle("Invoice Generator Refactored");
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /* INPUT */
        txtCustomer = new JTextField();
        txtCompany = new JTextField();
        txtDate = new JTextField();
        txtInv = new JTextField("INV-001");

        txtItem = new JTextField();
        txtQty = new JTextField();
        txtPrice = new JTextField();

        txtCustomer.setBounds(20, 20, 150, 25);
        txtCompany.setBounds(180, 20, 150, 25);
        txtDate.setBounds(340, 20, 120, 25);
        txtInv.setBounds(470, 20, 120, 25);

        txtItem.setBounds(20, 70, 150, 25);
        txtQty.setBounds(180, 70, 80, 25);
        txtPrice.setBounds(270, 70, 100, 25);

        add(txtCustomer); add(txtCompany); add(txtDate); add(txtInv);
        add(txtItem); add(txtQty); add(txtPrice);

        /* BUTTONS  */
        JButton btnAdd = new JButton("Add Item");
        btnAdd.setBounds(380, 70, 120, 25);
        add(btnAdd);

        chkTax = new JCheckBox("Tax");
        chkDiscount = new JCheckBox("Discount");
        chkTax.setBounds(520, 70, 70, 25);
        chkDiscount.setBounds(600, 70, 100, 25);
        add(chkTax); add(chkDiscount);

        
        model = new DefaultTableModel(new String[]{"Item", "Qty", "Price", "Total"}, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 120, 740, 250);
        add(sp);

      
        lblTotal = new JLabel("Total: 0");
        lblTotal.setBounds(20, 390, 200, 25);
        add(lblTotal);

        
        btnAdd.addActionListener(e -> addItem());

        setVisible(true);
    }

   
    private void addItem() {
        try {
            InvoiceValidator.validateItem(
                    txtItem.getText(),
                    txtQty.getText(),
                    txtPrice.getText()
            );

            int qty = Integer.parseInt(txtQty.getText());
            double price = Double.parseDouble(txtPrice.getText());

            double total = InvoiceCalculator.calculateItemTotal(qty, price);

            model.addRow(new Object[]{
                    txtItem.getText(), qty, price, total
            });

            subtotal += total;
            updateTotal();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateTotal() {
        double finalTotal = InvoiceCalculator.calculateGrandTotal(
                subtotal,
                chkTax.isSelected(),
                chkDiscount.isSelected()
        );
        lblTotal.setText("Total: " + finalTotal);
    }

    public static void main(String[] args) {
        new GUI();
    }
}

