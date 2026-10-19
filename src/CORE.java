import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

    public class CORE extends JFrame {

        private final JTextField display = new JTextField("0");
        private final JLabel expression = new JLabel(" ");

        private boolean startNewNumber = true;
        private boolean errorState = false;

        private double storedValue = 0;
        private double lastOperand = 0;

        private String pendingOperator = null;
        private String lastOperator = null;

        private boolean hasPendingOperation = false;

        private static final Color BG =
                new Color(22, 24, 30);

        private static final Color NUMBER =
                new Color(52, 56, 66);

        private static final Color FUNCTION =
                new Color(108, 114, 128);

        private static final Color ORANGE =
                new Color(255, 159, 67);

        private static final Color WHITE =
                new Color(248, 249, 252);

        private static final Color MUTED =
                new Color(166, 172, 184);


        public CORE() {

            super("Modern Calculator");

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(370, 570);
            setMinimumSize(new Dimension(330, 500));
            setLocationRelativeTo(null);

            JPanel root = new JPanel(
                    new BorderLayout(0, 12)
            );

            root.setBackground(BG);

            root.setBorder(
                    new EmptyBorder(18, 16, 18, 16)
            );

            setContentPane(root);


            JPanel displayPanel = new JPanel(
                    new BorderLayout(0, 8)
            );

            displayPanel.setBackground(BG);

            displayPanel.setBorder(
                    new EmptyBorder(18, 4, 18, 4)
            );


            JLabel title = new JLabel(
                    "CALCULATOR",
                    SwingConstants.CENTER
            );

            title.setFont(
                    new Font("SansSerif", Font.BOLD, 12)
            );

            title.setForeground(MUTED);


            expression.setFont(
                    new Font("SansSerif", Font.PLAIN, 17)
            );

            expression.setForeground(MUTED);

            expression.setHorizontalAlignment(
                    SwingConstants.RIGHT
            );


            display.setEditable(false);
            display.setFocusable(false);

            display.setHorizontalAlignment(
                    SwingConstants.RIGHT
            );

            display.setFont(
                    new Font("SansSerif", Font.PLAIN, 42)
            );

            display.setForeground(WHITE);
            display.setBackground(BG);
            display.setBorder(null);


            JPanel displayContent = new JPanel(
                    new BorderLayout(0, 8)
            );

            displayContent.setBackground(BG);

            displayContent.add(
                    expression,
                    BorderLayout.NORTH
            );

            displayContent.add(
                    display,
                    BorderLayout.CENTER
            );

            displayPanel.add(
                    title,
                    BorderLayout.NORTH
            );

            displayPanel.add(
                    displayContent,
                    BorderLayout.CENTER
            );


            JPanel buttons = new JPanel(
                    new GridLayout(5, 4, 10, 10)
            );

            buttons.setBackground(BG);


            String[][] keys = {
                    {"AC", "±", "%", "÷"},
                    {"7", "8", "9", "×"},
                    {"4", "5", "6", "−"},
                    {"1", "2", "3", "+"},
                    {"⌫", "0", ".", "="}
            };


            for (String[] row : keys) {

                for (String key : row) {

                    JButton button = makeButton(key);

                    button.addActionListener(
                            e -> handleKey(key)
                    );

                    buttons.add(button);
                }
            }


            root.add(
                    displayPanel,
                    BorderLayout.NORTH
            );

            root.add(
                    buttons,
                    BorderLayout.CENTER
            );

            installKeyboardActions();
        }


        private JButton makeButton(String key) {

            JButton b = new JButton(key);

            b.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            key.length() > 1 ? 18 : 23
                    )
            );

            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setOpaque(true);

            b.setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );


            Color normal =
                    isOperator(key) || key.equals("=")
                            ? ORANGE
                            : (key.equals("AC")
                            || key.equals("±")
                            || key.equals("%"))
                              ? FUNCTION
                              : NUMBER;


            Color hover =
                    normal.equals(ORANGE)
                            ? new Color(255, 178, 100)
                            : normal.equals(FUNCTION)
                              ? new Color(130, 136, 149)
                              : new Color(68, 73, 85);


            b.setBackground(normal);

            b.setForeground(
                    normal.equals(ORANGE)
                            ? new Color(28, 30, 35)
                            : WHITE
            );


            b.addMouseListener(
                    new java.awt.event.MouseAdapter() {

                        public void mouseEntered(
                                java.awt.event.MouseEvent e) {

                            b.setBackground(hover);
                        }

                        public void mouseExited(
                                java.awt.event.MouseEvent e) {

                            b.setBackground(normal);
                        }
                    }
            );

            return b;
        }


        private boolean isOperator(String s) {

            return s.equals("+")
                    || s.equals("−")
                    || s.equals("×")
                    || s.equals("÷");
        }


        private void handleKey(String key) {

            if (key.matches("[0-9]")) {

                inputDigit(key);

            } else {

                switch (key) {

                    case ".":
                        inputDecimal();
                        break;

                    case "AC":
                        clearAll();
                        break;

                    case "⌫":
                        backspace();
                        break;

                    case "±":
                        toggleSign();
                        break;

                    case "%":
                        percent();
                        break;

                    case "+":
                    case "−":
                    case "×":
                    case "÷":
                        chooseOperator(key);
                        break;

                    case "=":
                        calculateEquals();
                        break;
                }
            }
        }


        private void inputDigit(String digit) {

            if (errorState) {
                clearAll();
            }

            String current = display.getText();

            if (startNewNumber || current.equals("0")) {

                display.setText(digit);
                startNewNumber = false;

            } else if (current.length() < 16) {

                display.setText(current + digit);
            }

            if (hasPendingOperation) {

                expression.setText(
                        format(storedValue)
                                + " "
                                + pendingOperator
                );
            }
        }


        private void inputDecimal() {

            if (errorState) {
                clearAll();
            }

            if (startNewNumber) {

                display.setText("0.");
                startNewNumber = false;

            } else if (!display.getText().contains(".")) {

                display.setText(
                        display.getText() + "."
                );
            }

            if (hasPendingOperation) {

                expression.setText(
                        format(storedValue)
                                + " "
                                + pendingOperator
                );
            }
        }


        private void chooseOperator(String op) {

            if (errorState) {
                return;
            }

            double current = value();

            if (hasPendingOperation && !startNewNumber) {

                Double result = apply(
                        storedValue,
                        current,
                        pendingOperator
                );

                if (result == null) {
                    return;
                }

                storedValue = result;
                display.setText(format(result));

            } else if (!hasPendingOperation) {

                storedValue = current;
            }

            pendingOperator = op;
            hasPendingOperation = true;
            startNewNumber = true;

            expression.setText(
                    format(storedValue) + " " + op
            );
        }


        private void calculateEquals() {

            if (errorState) {
                return;
            }

            if (hasPendingOperation) {

                double operand = value();

                Double result = apply(
                        storedValue,
                        operand,
                        pendingOperator
                );

                if (result == null) {
                    return;
                }

                lastOperator = pendingOperator;
                lastOperand = operand;

                expression.setText(
                        format(storedValue)
                                + " "
                                + pendingOperator
                                + " "
                                + format(operand)
                                + " ="
                );

                display.setText(format(result));

                storedValue = result;
                pendingOperator = null;
                hasPendingOperation = false;
                startNewNumber = true;

            } else if (lastOperator != null) {

                double current = value();

                Double result = apply(
                        current,
                        lastOperand,
                        lastOperator
                );

                if (result == null) {
                    return;
                }

                expression.setText(
                        format(current)
                                + " "
                                + lastOperator
                                + " "
                                + format(lastOperand)
                                + " ="
                );

                display.setText(format(result));

                storedValue = result;
                startNewNumber = true;
            }
        }


        private Double apply(
                double a,
                double b,
                String op) {

            double result;

            switch (op) {

                case "+":
                    result = a + b;
                    break;

                case "−":
                    result = a - b;
                    break;

                case "×":
                    result = a * b;
                    break;

                case "÷":

                    if (b == 0.0) {

                        showError("Cannot divide by zero");
                        return null;
                    }

                    result = a / b;
                    break;

                default:
                    return b;
            }


            if (Double.isNaN(result)
                    || Double.isInfinite(result)) {

                showError("Result is out of range");
                return null;
            }

            return result;
        }


        private void percent() {

            if (errorState) {
                return;
            }

            display.setText(
                    format(value() / 100.0)
            );

            startNewNumber = true;
            expression.setText(" ");
        }


        private void toggleSign() {

            if (errorState) {
                return;
            }

            display.setText(
                    format(-value())
            );

            startNewNumber = false;
        }


        private void backspace() {

            if (errorState) {

                clearAll();
                return;
            }

            if (startNewNumber) {
                return;
            }

            String s = display.getText();

            if (s.length() <= 1
                    || (s.length() == 2
                    && s.startsWith("-"))) {

                display.setText("0");
                startNewNumber = true;

            } else {

                display.setText(
                        s.substring(0, s.length() - 1)
                );
            }
        }


        private double value() {

            try {

                return Double.parseDouble(
                        display.getText()
                );

            } catch (NumberFormatException e) {

                return 0;
            }
        }


        private String format(double value) {

            if (value == 0) {
                return "0";
            }

            BigDecimal n = new BigDecimal(
                    value,
                    new MathContext(
                            12,
                            RoundingMode.HALF_UP
                    )
            ).stripTrailingZeros();


            String plain = n.toPlainString();

            return plain.length() <= 16
                    ? plain
                    : String.format("%.8E", value)
                    .replace("E", "e");
        }


        private void showError(String message) {

            display.setText("Error");
            expression.setText(message);

            errorState = true;
            startNewNumber = true;

            pendingOperator = null;
            hasPendingOperation = false;
        }


        private void clearAll() {

            display.setText("0");
            expression.setText(" ");

            storedValue = 0;
            lastOperand = 0;

            pendingOperator = null;
            lastOperator = null;

            hasPendingOperation = false;
            startNewNumber = true;
            errorState = false;
        }


        private void installKeyboardActions() {

            bind("0", "D0", () -> handleKey("0"));
            bind("1", "D1", () -> handleKey("1"));
            bind("2", "D2", () -> handleKey("2"));
            bind("3", "D3", () -> handleKey("3"));
            bind("4", "D4", () -> handleKey("4"));
            bind("5", "D5", () -> handleKey("5"));
            bind("6", "D6", () -> handleKey("6"));
            bind("7", "D7", () -> handleKey("7"));
            bind("8", "D8", () -> handleKey("8"));
            bind("9", "D9", () -> handleKey("9"));

            bind(".", "DECIMAL", () -> handleKey("."));
            bind("+", "PLUS", () -> handleKey("+"));
            bind("-", "MINUS", () -> handleKey("−"));
            bind("*", "MULTIPLY", () -> handleKey("×"));
            bind("/", "DIVIDE", () -> handleKey("÷"));

            bind("ENTER", "ENTER", () -> handleKey("="));
            bind("=", "EQUALS", () -> handleKey("="));

            bind(
                    "BACK_SPACE",
                    "BACKSPACE",
                    () -> handleKey("⌫")
            );

            bind(
                    "ESCAPE",
                    "CLEAR",
                    () -> handleKey("AC")
            );

            bind("%", "PERCENT", () -> handleKey("%"));
        }


        private void bind(
                String key,
                String name,
                Runnable task) {

            getRootPane()
                    .getInputMap(
                            JComponent.WHEN_IN_FOCUSED_WINDOW
                    )
                    .put(
                            KeyStroke.getKeyStroke(key),
                            name
                    );

            getRootPane()
                    .getActionMap()
                    .put(
                            name,
                            new AbstractAction() {

                                public void actionPerformed(
                                        ActionEvent e) {

                                    task.run();
                                }
                            }
                    );
        }


        public static void main(String[] args) {

            SwingUtilities.invokeLater(() -> {

                try {

                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName()
                    );

                } catch (Exception ignored) {
                }

                new CORE().setVisible(true);
            });
        }
    }