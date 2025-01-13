public class Process {
    int processID;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int finishingTime;
    int priority; // Add priority field

    public Process(int processID, int arrivalTime, int burstTime, int priority) {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.finishingTime = 0;
    }
}
