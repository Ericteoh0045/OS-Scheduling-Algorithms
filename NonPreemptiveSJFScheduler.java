import java.util.*;

public class NonPreemptiveSJFScheduler {
    private double totalTurnaroundTime = 0;
    private double totalWaitingTime = 0;
    private final List<String> ganttChart = new ArrayList<>();
    private final List<Integer> timeMarkers = new ArrayList<>(); // For tracking the Gantt chart timeline

    public void schedule(List<Process> processes) {
        int currentTime = 0;
        Queue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt((Process p) -> p.burstTime)
                .thenComparingInt(p -> p.priority) // Compare burst time, then priority
                .thenComparingInt(p -> p.arrivalTime)); // Use arrival time as a tiebreaker

        boolean allProcessesHandled = false; // Track if all processes are scheduled
        timeMarkers.add(currentTime); // Add the initial start time

        while (!allProcessesHandled) {
            // Check all processes and add those that are ready to the ready queue
            for (Process process : processes) {
                if (!readyQueue.contains(process) && process.remainingTime > 0 && process.arrivalTime <= currentTime) {
                    readyQueue.add(process);
                }
            }

            if (!readyQueue.isEmpty()) {
                // Get the next process with the shortest burst time
                Process currentProcess = readyQueue.poll();

                // Execute the selected process
                ganttChart.add("P" + currentProcess.processID);
                currentTime += currentProcess.burstTime;
                timeMarkers.add(currentTime); // Add time marker after each process execution

                // Update process finishing time and calculate turnaround/waiting time
                currentProcess.finishingTime = currentTime;
                int turnaroundTime = currentProcess.finishingTime - currentProcess.arrivalTime;
                int waitingTime = turnaroundTime - currentProcess.burstTime;

                totalTurnaroundTime += turnaroundTime;
                totalWaitingTime += waitingTime;

                // Mark the process as completed
                currentProcess.remainingTime = 0;
            } else {
                // If no processes are ready, increment the current time
                currentTime++;
                timeMarkers.add(currentTime); // Add the idle time marker
            }

            // Check if all processes are handled
            allProcessesHandled = processes.stream().allMatch(p -> p.remainingTime == 0);
        }
    }

    public double getAverageTurnaroundTime() {
        return totalTurnaroundTime / ganttChart.size();
    }

    public double getAverageWaitingTime() {
        return totalWaitingTime / ganttChart.size();
    }

    public String getFormattedGanttChart() {
        // Build the Gantt chart in a neat and tidy format
        StringBuilder chartLine = new StringBuilder();
        StringBuilder timeLine = new StringBuilder();
    
        // Counter for two-digit numbers
        int twoDigitCount = 0;
    
        // Append process names in Gantt chart
        chartLine.append("|");
        for (String process : ganttChart) {
            chartLine.append(String.format(" %-7s|", process)); // Fixed-width block for process names
        }
    
        // Append aligned time markers below the Gantt chart
        timeLine.append(" "); // Add one space before the first number
        for (int i = 0; i < timeMarkers.size(); i++) {
            int marker = timeMarkers.get(i);
    
            if (marker >= 10) {
                // If the marker is a two-digit number, increment the counter
                twoDigitCount++;
            }
    
            if (i == 0) {
                // For the first time marker, add 8 spaces
                timeLine.append(String.format("%-8d", marker));
            } else if (i == 1) {
                // For the second time marker, add 10 spaces
                timeLine.append(String.format("%-10d", marker));
            } else if (twoDigitCount >= 2) {
                // After the second two-digit number, add 9 spaces
                timeLine.append(String.format("%-9d", marker));
            } else {
                // Default for other markers (before two-digit adjustment)
                timeLine.append(String.format("%-11d", marker));
            }
        }
    
        return chartLine.toString() + "\n" + timeLine.toString();
    }
    

    
}
