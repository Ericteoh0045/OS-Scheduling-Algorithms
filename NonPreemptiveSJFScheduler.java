import java.util.*; // Importing utility classes for data structures and collections.

// Main class for the Non-Preemptive Shortest Job First (SJF) Scheduler.
public class NonPreemptiveSJFScheduler {

    // Variables to store total turnaround and waiting times for all processes.
    private double totalTurnaroundTime = 0;
    private double totalWaitingTime = 0;

    // List to maintain the Gantt chart representation of the schedule.
    private final List<String> ganttChart = new ArrayList<>();

    // List to store time markers for the Gantt chart timeline.
    private final List<Integer> timeMarkers = new ArrayList<>();

    // Method to perform scheduling based on Non-Preemptive SJF algorithm.
    public void schedule(List<Process> processes) {
        int currentTime = 0; // Initialize the current time to zero.

        // Priority queue to select the process with the shortest burst time first.
        Queue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt((Process p) -> p.burstTime) // Compare by burst time.
                .thenComparingInt(p -> p.priority) // If burst times are equal, compare by priority.
                .thenComparingInt(p -> p.arrivalTime)); // If priority is also equal, compare by arrival time.

        boolean allProcessesHandled = false; // Flag to indicate if all processes are scheduled.
        timeMarkers.add(currentTime); // Add the initial start time to the timeline.

        while (!allProcessesHandled) { // Loop until all processes are handled.

            // Add processes to the ready queue if they have arrived and are not yet completed.
            for (Process process : processes) {
                if (!readyQueue.contains(process) && process.remainingTime > 0 && process.arrivalTime <= currentTime) {
                    readyQueue.add(process);
                }
            }

            if (!readyQueue.isEmpty()) { // If the ready queue has processes.

                // Select the process with the shortest burst time.
                Process currentProcess = readyQueue.poll();

                // Add the process to the Gantt chart.
                ganttChart.add("P" + currentProcess.processID);

                // Update the current time by adding the burst time of the selected process.
                currentTime += currentProcess.burstTime;

                // Add the current time to the time markers after executing the process.
                timeMarkers.add(currentTime);

                // Calculate finishing time, turnaround time, and waiting time for the process.
                currentProcess.finishingTime = currentTime;
                int turnaroundTime = currentProcess.finishingTime - currentProcess.arrivalTime; // Total time from arrival to completion.
                int waitingTime = turnaroundTime - currentProcess.burstTime; // Time spent waiting in the ready queue.

                // Accumulate the turnaround and waiting times.
                totalTurnaroundTime += turnaroundTime;
                totalWaitingTime += waitingTime;

                // Mark the process as completed by setting its remaining time to zero.
                currentProcess.remainingTime = 0;

            } else {
                // If no process is ready, increment the current time.
                currentTime++;

                // Add an idle time marker to the timeline.
                timeMarkers.add(currentTime);
            }

            // Check if all processes are completed.
            allProcessesHandled = processes.stream().allMatch(p -> p.remainingTime == 0);
        }
    }

    // Method to calculate and return the average turnaround time.
    public double getAverageTurnaroundTime() {
        return totalTurnaroundTime / ganttChart.size(); // Divide total turnaround time by the number of processes.
    }

    // Method to calculate and return the average waiting time.
    public double getAverageWaitingTime() {
        return totalWaitingTime / ganttChart.size(); // Divide total waiting time by the number of processes.
    }

    // Method to format and return the Gantt chart representation.
    public String getFormattedGanttChart() {
        StringBuilder chartLine = new StringBuilder(); // Line for process names.
        StringBuilder timeLine = new StringBuilder(); // Line for time markers.

        // Counter for two-digit numbers
        int twoDigitCount = 0;

        // Initialize Gantt chart formatting.
        chartLine.append("|");
        for (String process : ganttChart) {
            chartLine.append(String.format(" %-7s|", process)); // Format process name in a fixed-width block.
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
        // Return the Gantt chart with aligned process names and time markers.
        return chartLine.toString() + "\n" + timeLine.toString();
    }
}
