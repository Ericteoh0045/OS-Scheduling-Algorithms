import javax.swing.*; // Importing Swing classes for GUI components.
import javax.swing.table.DefaultTableModel; // Importing the table model for the JTable.
import java.awt.*; // Importing AWT classes for layout and graphics.
import java.util.ArrayList; // Importing ArrayList for dynamic array operations.
import java.util.List; // Importing List interface for defining list operations.

// Main class extending JFrame to create the GUI application.
public class NonPreemptiveSJFGui extends JFrame {

    // Constructor for the GUI application.
    public NonPreemptiveSJFGui() {
        // Setting the title of the application window.
        setTitle("Non-Preemptive SJF Scheduler");
        
        // Setting the size of the application window.
        setSize(600, 400);
        
        // Ensuring the application closes when the window is closed.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Creating a custom panel for the title with a gradient background.
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Overriding paintComponent to add a gradient background.
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Defining the gradient colors.
                Color color1 = new Color(70, 130, 180); // Start color.
                Color color2 = new Color(240, 248, 255); // End color.
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight()); // Filling the panel with the gradient.
            }
        };

        // Setting layout and preferred size for the title panel.
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(getWidth(), 80));

        // Adding a title label to the panel.
        JLabel titleLabel = new JLabel("Non-Preemptive SJF Scheduler", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22)); // Setting font style and size.
        titleLabel.setForeground(Color.WHITE); // Setting text color.
        titlePanel.add(titleLabel, BorderLayout.CENTER); // Centering the label in the panel.

        // Adding the title panel to the top of the frame.
        add(titlePanel, BorderLayout.NORTH);

        // Creating the input panel for user input.
        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5)); // GridLayout for vertical alignment.
        inputPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Adding padding around the panel.

        // Creating a text field for process input.
        JTextField processField = new JTextField();

        // Adding a label and text field to the input panel.
        inputPanel.add(new JLabel("Enter Number of Processes (3-10):"));
        inputPanel.add(processField);

        // Creating a styled button for proceeding to the next step.
        JButton nextButton = createStyledButton("Next");
        inputPanel.add(nextButton);

        // Adding the input panel to the center of the frame.
        add(inputPanel, BorderLayout.CENTER);

        // Adding an action listener to the "Next" button.
        nextButton.addActionListener(e -> {
            try {
                // Parsing the number of processes from the input field.
                int numProcesses = Integer.parseInt(processField.getText());

                // Validating the range of the input (3 to 10 processes).
                if (numProcesses < 3 || numProcesses > 10) {
                    JOptionPane.showMessageDialog(this, "Please enter a number of processes between 3 and 10.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Stop further execution if the input is invalid.
                }

                // Creating a list to store process details.
                List<Process> processes = new ArrayList<>();

                // Looping through the number of processes to collect input for each process.
                for (int i = 0; i < numProcesses; i++) {
                    int burstTime = Integer.parseInt(JOptionPane.showInputDialog("Enter Burst Time for P" + i + ":"));
                    int arrivalTime = Integer.parseInt(JOptionPane.showInputDialog("Enter Arrival Time for P" + i + ":"));
                    int priority = Integer.parseInt(JOptionPane.showInputDialog("Enter Priority for P" + i + ":"));

                    // Adding the process details to the list.
                    processes.add(new Process(i, arrivalTime, burstTime, priority));
                }

                // Creating an instance of the scheduler and scheduling the processes.
                NonPreemptiveSJFScheduler scheduler = new NonPreemptiveSJFScheduler();
                scheduler.schedule(processes);

                // Displaying the results in a table format.
                showTableGUI(processes, scheduler);

                // Closing the current input screen after clicking "Next".
                SwingUtilities.getWindowAncestor(nextButton).dispose();
            } catch (NumberFormatException ex) {
                // Handling invalid input and displaying an error message.
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Making the frame visible.
        setVisible(true);
    }

    // Method to display the results in a table format.
    private void showTableGUI(List<Process> processes, NonPreemptiveSJFScheduler scheduler) {
        JFrame tableFrame = new JFrame("Non-Preemptive SJF Results"); // Creating a new frame for the results.
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Setting the close operation.
        tableFrame.setSize(800, 500); // Setting the frame size.

        // Defining column names for the table.
        String[] columns = {"Process", "Arrival Time", "Burst Time", "Priority", "Finishing Time", "Turnaround Time", "Waiting Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0); // Creating a table model with the columns.
        JTable table = new JTable(model); // Creating a table with the model.

        // Populating the table with process data.
        for (Process process : processes) {
            int turnaroundTime = process.finishingTime - process.arrivalTime; // Calculating turnaround time.
            int waitingTime = turnaroundTime - process.burstTime; // Calculating waiting time.
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

        // Adding a scroll pane to the table for better display.
        JScrollPane scrollPane = new JScrollPane(table);
        tableFrame.add(scrollPane, BorderLayout.CENTER);

        // Creating a text area for additional results.
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false); // Making the text area non-editable.
        
        // Calculating and displaying average turnaround and waiting times.
        String averageTurnaround = String.format("%.2f", scheduler.getAverageTurnaroundTime());
        String averageWaiting = String.format("%.2f", scheduler.getAverageWaitingTime());
        resultArea.setText("Average Turnaround Time: " + averageTurnaround + "\n");
        resultArea.append("Average Waiting Time: " + averageWaiting + "\n\n");
        resultArea.append("Gantt Chart:\n" + scheduler.getFormattedGanttChart()); // Displaying the Gantt chart.

        // Adding the text area to the bottom of the frame.
        tableFrame.add(resultArea, BorderLayout.SOUTH);

        // Making the results frame visible.
        tableFrame.setVisible(true);
    }

    // Method to create a styled button.
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text); // Creating a button with the given text.
        button.setFocusPainted(false); // Disabling focus painting.
        button.setFont(new Font("Arial", Font.PLAIN, 18)); // Setting the font style and size.
        button.setBackground(new Color(70, 130, 180)); // Setting the background color.
        button.setForeground(Color.WHITE); // Setting the text color.
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 70, 120), 2), // Adding a line border.
                BorderFactory.createEmptyBorder(10, 15, 10, 15) // Adding padding inside the border.
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Changing the cursor to a hand icon when hovered.
        return button; // Returning the styled button.
    }

    // Main method to launch the application.
    public static void main(String[] args) {
        new NonPreemptiveSJFGui(); // Creating an instance of the GUI.
    }
}
