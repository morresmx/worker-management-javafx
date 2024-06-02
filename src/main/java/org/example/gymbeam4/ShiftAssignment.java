package org.example.gymbeam4;

import java.time.LocalDateTime;

public class ShiftAssignment {
    private Worker worker;
    private Shift shift;

    public ShiftAssignment(Worker worker, Shift shift, LocalDateTime assignmentTime) {
        this.worker = worker;
        this.shift = shift;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
