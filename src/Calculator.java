import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame {

    private JTextField firstNumberField;
    private JTextField secondNumberField;
    private JComboBox<String> operatorBox;
    private JLabel resultLabel;

    public Calculator() {
        setTitle("Simple Calculator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 12));
        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        panel.add(new JLabel("First Number:"));
        firstNumberField = new JTextField();
        panel.add(firstNumberField);

        panel.add(new JLabel("Operator:"));
        operatorBox = new JComboBox<>(
                new String[]{"+", "-", "*", "/"}
        );
        panel.add(operatorBox);

        panel.add(new JLabel("Second Number:"));
        secondNumberField = new JTextField();
        panel.add(secondNumberField);

        JButton calculateButton = new JButton("Calculate");
        panel.add(calculateButton);

        JButton clearButton = new JButton("Clear");
        panel.add(clearButton);

        resultLabel = new JLabel("Result: ");
        panel.add(resultLabel);

        calculateButton.addActionListener(e -> calculate());
        clearButton.addActionListener(e -> clearFields());

        add(panel);
    }

    private void calculate() {
        try {
            double num1 = Double.parseDouble(
                    firstNumberField.getText().trim()
            );

            double num2 = Double.parseDouble(
                    secondNumberField.getText().trim()
            );

            String operator = (String) operatorBox.getSelectedItem();
            double result;

            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;

                case "-":
                    result = num1 - num2;
                    break;

                case "*":
                    result = num1 * num2;
                    break;

                case "/":
                    if (num2 == 0) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Cannot divide by zero!",
                                "Math Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    result = num1 / num2;
                    break;

                default:
                    return;
            }

            resultLabel.setText("Result: " + result);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void clearFields() {
        firstNumberField.setText("");
        secondNumberField.setText("");
        operatorBox.setSelectedIndex(0);
        resultLabel.setText("Result: ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Calculator().setVisible(true);
        });
    }
}

