import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class RoundRobinScheduler {
    private final int timeQuantum;
    private final List<String> ganttChart = new ArrayList<>();
    private final List<Integer> timeMarkers = new ArrayList<>();
    private int totalTurnaroundTime = 0;
    private int totalWaitingTime = 0;

    public RoundRobinScheduler(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    public void schedule(List<Process> processes) {
        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;

        // Sort processes by arrival time and priority
        processes.sort(Comparator.comparingInt((Process p) -> p.arrivalTime)
                                    .thenComparingInt(p -> p.priority)); // Sort by arrival time, then by priority

        int index = 0;
        while (index < processes.size() || !queue.isEmpty()) {
            // Add processes to the queue based on arrival time
            while (index < processes.size() && processes.get(index).arrivalTime <= currentTime) {
                queue.add(processes.get(index));
                index++;
            }

            if (!queue.isEmpty()) {
                Process currentProcess = queue.poll();

                if (currentProcess.remainingTime <= timeQuantum) {
                    currentTime += currentProcess.remainingTime;
                    currentProcess.remainingTime = 0;
                    currentProcess.finishingTime = currentTime;

                    ganttChart.add("P" + currentProcess.processID);
                    timeMarkers.add(currentTime);

                    totalTurnaroundTime += currentProcess.finishingTime - currentProcess.arrivalTime;
                    totalWaitingTime += (currentProcess.finishingTime - currentProcess.arrivalTime - currentProcess.burstTime);
                } else {
                    currentProcess.remainingTime -= timeQuantum;
                    currentTime += timeQuantum;

                    ganttChart.add("P" + currentProcess.processID);
                    timeMarkers.add(currentTime);
                    queue.add(currentProcess);
                }
            } else {
                // If no processes are ready to be scheduled, increment time to move forward
                currentTime++;
            }
        }
    }

    public void showResults() {
        JFrame resultsFrame = new JFrame("Round Robin Results - Averages and Gantt Chart");
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setSize(600, 400);
        resultsFrame.setLayout(new BorderLayout());

        // Averages
        double avgTurnaroundTime = (double) totalTurnaroundTime / timeMarkers.size();
        double avgWaitingTime = (double) totalWaitingTime / timeMarkers.size();

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setText("Average Turnaround Time: " + avgTurnaroundTime + "\n");
        resultArea.append("Average Waiting Time: " + avgWaitingTime + "\n\n");

        // Gantt Chart representation
        resultArea.append("Gantt Chart:\n" + String.join(" -> ", ganttChart) + "\n");
        resultArea.append("Time Markers: " + timeMarkers + "\n\n");

        // Simple Gantt Chart representation
        resultArea.append("Simple Gantt Chart:\n");
        resultArea.append("| ");
        for (String process : ganttChart) {
            resultArea.append(process + " | ");
        }
        resultArea.append("\n");
        resultArea.append("0    ");
        for (int timeMarker : timeMarkers) {
            resultArea.append(timeMarker + "    ");
        }

        resultsFrame.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        resultsFrame.setVisible(true);
    }
}
