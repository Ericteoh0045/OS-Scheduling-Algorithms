import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("CPU Scheduling Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // Create a title panel with gradient background
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
        titlePanel.setPreferredSize(new Dimension(frame.getWidth(), 100));

        JLabel titleLabel = new JLabel("CPU Scheduling Simulator", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        frame.add(titlePanel, BorderLayout.NORTH);

        // Create buttons for algorithm selection with a modern look
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton roundRobinButton = createStyledButton("Round Robin");
        JButton srtButton = createStyledButton("Shortest Remaining Time (SRT)");
        JButton sjnButton = createStyledButton("Shortest Job Next (SJN)");
        JButton nonPreemptivePriorityButton = createStyledButton("Non-Preemptive Priority");

        // Add buttons to the panel
        buttonPanel.add(roundRobinButton);
        buttonPanel.add(srtButton);
        buttonPanel.add(sjnButton);
        buttonPanel.add(nonPreemptivePriorityButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        // Add action listeners to buttons
        roundRobinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the RoundRobin GUI when the Round Robin button is clicked
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new RoundRobinGUI(); // Open the Round Robin GUI
                    }
                });
            }
        });

        srtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle SJN button click
                JOptionPane.showMessageDialog(frame, "SRT Algorithm is under construction.", "SRT",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        sjnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle SRT button click
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new NonPreemptiveSJFGui(); // Open the SJN GUI
                    }
                });
            }
        });

        nonPreemptivePriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Non-Preemptive Priority button click
                JOptionPane.showMessageDialog(frame, "Non-Preemptive Priority Algorithm is under construction.",
                        "Non-Preemptive Priority", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Create a footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 248, 255));
        JLabel footerLabel = new JLabel("Select an algorithm to start", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        footerLabel.setForeground(Color.DARK_GRAY);
        footerPanel.add(footerLabel);
        frame.add(footerPanel, BorderLayout.SOUTH);

        // Display the frame
        frame.setLocationRelativeTo(null); // Center the frame on screen
        frame.setVisible(true);
    }

    // Helper method to create styled buttons
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 70, 120), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
