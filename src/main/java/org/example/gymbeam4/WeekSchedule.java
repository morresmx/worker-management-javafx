package org.example.gymbeam4;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class WeekSchedule {
    protected Map<DayOfWeek, List<Shift>> schedule;

    public WeekSchedule() {
        this.schedule = new HashMap<>();
    }

    public void addShift(DayOfWeek dayOfWeek, Shift shift) {
        if (!schedule.containsKey(dayOfWeek)) {
            schedule.put(dayOfWeek, new ArrayList<>());
        }
        schedule.get(dayOfWeek).add(shift);
    }

    public abstract List<Worker> getMainWorkersForShift(Shift shift);

    public abstract List<Worker> getRegularWorkersForShift(Shift shift);
}