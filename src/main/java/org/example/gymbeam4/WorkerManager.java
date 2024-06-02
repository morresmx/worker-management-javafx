package org.example.gymbeam4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkerManager extends WeekSchedule {

    private Map<Integer, Shift> shiftMap;
    private List<Worker> workers;
    private List<ShiftAssignment> shiftAssignments;

    public WorkerManager() {
        this.shiftMap = new HashMap<>();
        this.workers = new ArrayList<>();
        this.shiftAssignments = new ArrayList<>();
    }

    public void addShift(Shift shift) {
        shiftMap.put(shift.getId(), shift);
    }

    public Shift getShiftById(int shiftId) {
        return shiftMap.get(shiftId);
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void addShiftAssignment(ShiftAssignment shiftAssignment) {
        shiftAssignments.add(shiftAssignment);
    }

    public List<ShiftAssignment> getShiftAssignments() {
        return shiftAssignments;
    }

    public List<Shift> getShifts() {
        return new ArrayList<>(shiftMap.values());
    }

    @Override
    public List<Worker> getRegularWorkersForShift(Shift shift) {
        List<Worker> regularWorkers = new ArrayList<>();
        for (Worker worker : workers) {
            if (!worker.isMain()) {
                regularWorkers.add(worker);
            }
        }
        return regularWorkers;
    }

    @Override
    public List<Worker> getMainWorkersForShift(Shift shift) {
        List<Worker> mainWorkers = new ArrayList<>();
        for (Worker worker : workers) {
            if (worker.isMain()) {
                mainWorkers.add(worker);
            }
        }
        return mainWorkers;
    }

    public void updateScheduleShift(Shift shift, List<Worker> selectedWorkers) {
        if (!selectedWorkers.isEmpty()) {
            shift.setMainWorker(selectedWorkers.get(0));
            if (selectedWorkers.size() > 1) {
                shift.setRegularWorkers(new ArrayList<>(selectedWorkers.subList(1, selectedWorkers.size())));
            }
        } else {
            shift.setMainWorker(null);
            shift.setRegularWorkers(null);
        }

        shiftAssignments.clear();
    }
}