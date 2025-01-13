import javax.swing.*;
import java.awt.*;
import java.util.List;

class GanttChartPanel extends JPanel {
    private final List<String> ganttChart;
    private final List<Integer> timeMarkers;

    public GanttChartPanel(List<String> ganttChart, List<Integer> timeMarkers) {
        this.ganttChart = ganttChart;
        this.timeMarkers = timeMarkers;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Panel dimensions
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int barHeight = 50;  // Height of each process bar
        int spaceBetweenBars = 15;  // Increased space between bars for better readability
        int timeMarkerHeight = 20; // Height for time markers

        // Calculate the total execution time for scaling
        int totalExecutionTime = timeMarkers.isEmpty() ? 1 : timeMarkers.get(timeMarkers.size() - 1);
        int barWidth = (totalExecutionTime == 0) ? panelWidth / ganttChart.size() : panelWidth / totalExecutionTime;  // Bar width based on execution time

        int currentX = 0;  // X-coordinate for drawing bars

        // Draw background gridlines
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < timeMarkers.size(); i++) {
            int x = timeMarkers.get(i) * barWidth;
            g.drawLine(x, 0, x, panelHeight - timeMarkerHeight);
        }

        // Draw process bars
        for (int i = 0; i < ganttChart.size(); i++) {
            String processName = ganttChart.get(i);

            // Set color for each process bar
            g.setColor(getProcessColor(processName));
            int startTime = (i == 0) ? 0 : timeMarkers.get(i - 1);  // Start time for current process bar
            int endTime = timeMarkers.get(i);  // End time for current process bar
            int barEndX = currentX + (endTime - startTime) * barWidth;

            // Draw the process bar (rectangle)
            g.fillRect(currentX, 50, barEndX - currentX, barHeight);

            // Draw process name in the middle of the bar
            g.setColor(Color.WHITE);
            g.drawString(processName, currentX + (barEndX - currentX) / 4, 75);  // Center text inside the bar

            // Update currentX for next process
            currentX = barEndX + spaceBetweenBars;
        }

        // Draw time markers below the bars with vertical lines and labels
        int markerX = 0;  // Starting X-coordinate for time markers
        g.setColor(Color.BLACK);  // Set color for time markers
        for (int i = 0; i < timeMarkers.size(); i++) {
            int timeMarkerX = markerX + timeMarkers.get(i) * barWidth;

            // Draw a vertical line for the time marker
            g.drawLine(timeMarkerX, 100, timeMarkerX, 120);

            // Draw the time label below the time marker
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(timeMarkers.get(i)), timeMarkerX - 5, 135);  // Draw the time value below the marker

            markerX = timeMarkerX;
        }
    }

    // Helper method to assign colors to each process bar
    private Color getProcessColor(String processName) {
        int processId = Integer.parseInt(processName.replace("P", ""));  // Get process number
        int r = (processId * 50 + 100) % 256;
        int g = (processId * 80 + 50) % 256;
        int b = (processId * 110 + 150) % 256;
        return new Color(r, g, b);  // Return a unique color for each process
    }
}
