import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoundRobinGUI extends JFrame {
    public RoundRobinGUI() {
        setTitle("Round Robin Scheduler");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Title panel setup
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(70, 130, 180);
                Color color2 = new Color(240, 248, 255);
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel titleLabel = new JLabel("Round Robin Scheduler", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // Input panel setup
        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JTextField quantumField = new JTextField();
        inputPanel.add(new JLabel("Enter Time Quantum:"));
        inputPanel.add(quantumField);

        JTextField processField = new JTextField();
        inputPanel.add(new JLabel("Enter Number of Processes:"));
        inputPanel.add(processField);

        JButton nextButton = createStyledButton("Next");
        inputPanel.add(nextButton);

        add(inputPanel, BorderLayout.CENTER);

        nextButton.addActionListener(e -> {
            try {
                int numProcesses = Integer.parseInt(processField.getText());
                int timeQuantum = Integer.parseInt(quantumField.getText());

                if (numProcesses <= 0 || timeQuantum <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter valid positive numbers for processes and quantum.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<Process> processes = new ArrayList<>();
                for (int i = 0; i < numProcesses; i++) {
                    int burstTime = Integer.parseInt(JOptionPane.showInputDialog("Enter Burst Time for P" + i + ":"));
                    int arrivalTime = Integer.parseInt(JOptionPane.showInputDialog("Enter Arrival Time for P" + i + ":"));
                    int priority = Integer.parseInt(JOptionPane.showInputDialog("Enter Priority for P" + i + ":"));
                    processes.add(new Process(i, arrivalTime, burstTime, priority));
                }

                // Pass the processes and time quantum to the scheduler
                RoundRobinScheduler scheduler = new RoundRobinScheduler(timeQuantum);
                scheduler.schedule(processes);

                // Show the result table
                showTableGUI(processes, scheduler);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true); 
    }

    private void showTableGUI(List<Process> processes, RoundRobinScheduler scheduler) {
        JFrame tableFrame = new JFrame("Round Robin Results");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setSize(800, 400);

        String[] columns = {"Process", "Arrival Time", "Burst Time", "Priority", "Finishing Time", "Turnaround Time", "Waiting Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        // Populate table
        for (Process process : processes) {
            int turnaroundTime = process.finishingTime - process.arrivalTime;
            int waitingTime = turnaroundTime - process.burstTime;
            model.addRow(new Object[]{
                    "P" + process.processID,
                    process.arrivalTime,
                    process.burstTime,
                    process.priority,
                    process.finishingTime,
                    turnaroundTime,
                    waitingTime
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        tableFrame.add(scrollPane, BorderLayout.CENTER);

        JButton avgButton = createStyledButton("Show Averages and Gantt Chart");
        tableFrame.add(avgButton, BorderLayout.SOUTH);

        avgButton.addActionListener(e -> scheduler.showResults());

        tableFrame.setVisible(true);
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 70, 120), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        new RoundRobinGUI();
    }
}

