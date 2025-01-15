import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NonPreemptiveSJFGui extends JFrame {
    public NonPreemptiveSJFGui() {
        setTitle("Non-Preemptive SJF Scheduler");
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

        JLabel titleLabel = new JLabel("Non-Preemptive SJF Scheduler", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // Input panel setup
        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JTextField processField = new JTextField();
        inputPanel.add(new JLabel("Enter Number of Processes (3-10):"));
        inputPanel.add(processField);

        JButton nextButton = createStyledButton("Next");
        inputPanel.add(nextButton);

        add(inputPanel, BorderLayout.CENTER);

        nextButton.addActionListener(e -> {
            try {
                int numProcesses = Integer.parseInt(processField.getText());
        
                // Validate the range for the number of processes
                if (numProcesses < 3 || numProcesses > 10) {
                    JOptionPane.showMessageDialog(this, "Please enter a number of processes between 3 and 10.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                List<Process> processes = new ArrayList<>();
                for (int i = 0; i < numProcesses; i++) {
                    int burstTime = Integer.parseInt(JOptionPane.showInputDialog("Enter Burst Time for P" + i + ":"));
                    int arrivalTime = Integer.parseInt(JOptionPane.showInputDialog("Enter Arrival Time for P" + i + ":"));
                    int priority = Integer.parseInt(JOptionPane.showInputDialog("Enter Priority for P" + i + ":"));
                    processes.add(new Process(i, arrivalTime, burstTime, priority));
                }
        
                NonPreemptiveSJFScheduler scheduler = new NonPreemptiveSJFScheduler();
                scheduler.schedule(processes);
        
                // Show the result table
                showTableGUI(processes, scheduler);
        
                // Close the current GUI screen after clicking "Next"
                SwingUtilities.getWindowAncestor(nextButton).dispose();
        
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        

        setVisible(true);
    }

    private void showTableGUI(List<Process> processes, NonPreemptiveSJFScheduler scheduler) {
        JFrame tableFrame = new JFrame("Non-Preemptive SJF Results");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setSize(800, 500);
    
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
    
        // Add average times and Gantt chart
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        String averageTurnaround = String.format("%.2f", scheduler.getAverageTurnaroundTime());
        String averageWaiting = String.format("%.2f", scheduler.getAverageWaitingTime());
        resultArea.setText("Average Turnaround Time: " + averageTurnaround + "\n");
        resultArea.append("Average Waiting Time: " + averageWaiting + "\n\n");
        resultArea.append("Gantt Chart:\n" + scheduler.getFormattedGanttChart());
        tableFrame.add(resultArea, BorderLayout.SOUTH);
    
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
        new NonPreemptiveSJFGui();
    }
}
