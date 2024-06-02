package org.example.gymbeam4;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Shift {
    private int id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Worker mainWorker;
    private List<Worker> regularWorkers;

    public Shift(int id, LocalDate date, LocalTime startTime, LocalTime endTime, Worker mainWorker, List<Worker> regularWorkers) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.mainWorker = mainWorker;
        this.regularWorkers = regularWorkers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Worker getMainWorker() {
        return mainWorker;
    }

    public void setMainWorker(Worker mainWorker) {
        this.mainWorker = mainWorker;
    }

    public List<Worker> getRegularWorkers() {
        return regularWorkers;
    }

    public void setRegularWorkers(List<Worker> regularWorkers) {
        this.regularWorkers = regularWorkers;
    }
}
