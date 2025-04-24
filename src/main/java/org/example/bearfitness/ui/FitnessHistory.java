package org.example.bearfitness.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;


public class FitnessHistory extends JFrame {
    private DefaultTableModel workoutTableModel;
    static RoundedButton addWorkoutButton;
    static String[] colNames = {"Workout", "Duration", "Date"};
    private JTable workoutTable;
    static JTextField nameTextField;
    static JTextField durationTextField;
    static JTextField dateTextField;


    /*public FitnessHistory() {
        setTitle("Fitness History");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the window

        JLabel titleLabel = new JLabel("Workout History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        workoutListModel = new DefaultListModel<>();
        workoutList = new JList<>(workoutListModel);
        workoutList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(workoutList);

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addWorkoutButton = new JButton("Add Workout");
        addWorkoutButton.addActionListener(e -> {
            String newWorkout = JOptionPane.showInputDialog(this, "Enter new workout:");
            String date = JOptionPane.showInputDialog(this, "Enter date:");
            String time = JOptionPane.showInputDialog(this, "Enter duration:");
            if (newWorkout != null && !newWorkout.trim().isEmpty()) {
                addWorkoutEntry(date.trim() + " -- " + newWorkout.trim() + " -- " + time.trim());
            }
        });

        add(addWorkoutButton, BorderLayout.SOUTH);
    }*/

    public FitnessHistory() {
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Workout History");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 0, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        workoutTableModel = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        workoutTable = new JTable(workoutTableModel);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(workoutTableModel);
        workoutTable.setRowSorter(sorter);

        JTextField filterTextField = new JTextField();
        filterTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {filter();}
            public void removeUpdate(DocumentEvent e) {filter();}
            public void changedUpdate(DocumentEvent e) {filter();}

            private void filter() {
                String text = filterTextField.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        JPanel panel = new JPanel();

        add(titleLabel, BorderLayout.NORTH);
        nameTextField = new JTextField(15);
        durationTextField = new JTextField(10);
        dateTextField = new JTextField(10);

        addWorkoutButton = new RoundedButton("Add Workout");
        panel.add(addWorkoutButton);

        addWorkoutButton.addActionListener(e -> {
            String workoutName = nameTextField.getText();
            String duration = durationTextField.getText();
            String date = dateTextField.getText();

            if (workoutName != null && !workoutName.trim().isEmpty()) {
                addWorkoutEntry(workoutName, date, duration);
                JOptionPane.showMessageDialog(this, "Workout Saved Successfully!");
            }

            nameTextField.setText("");
            durationTextField.setText("");
            dateTextField.setText("");
        });

        panel.add(new JLabel("Workout Name:"));
        panel.add(nameTextField);
        panel.add(new JLabel("Duration:"));
        panel.add(durationTextField);
        panel.add(new JLabel("Date:"));
        panel.add(dateTextField);
        panel.add(filterTextField);

        panel.add(addWorkoutButton);
        add(new JScrollPane(workoutTable), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        /*JProgressBar progressBar = new JProgressBar(0, 300); // 300 mins weekly goal
        progressBar.setValue(120); // dynamically update this
        progressBar.setStringPainted(true);
        progressBar.setForeground(accent);
        progressBar.setBackground(cardColor);*/
    }

    public void addWorkoutEntry(String workout, String duration, String date) {
        workoutTableModel.addRow(new Object[]{workout, date, duration});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FitnessHistory historyPage = new FitnessHistory();
            historyPage.setVisible(true);
        });
    }
}

class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setFocusable(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getModel().isArmed() ? getBackground().darker() : getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getBackground());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
    }
}