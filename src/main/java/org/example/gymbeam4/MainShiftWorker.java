package org.example.gymbeam4;

import javafx.beans.property.SimpleStringProperty;

public class MainShiftWorker extends Worker {

    //    FACTORY METHOD

    private static MainShiftWorker instance;
    private MainShiftWorker(int workerId, String name) {
        super(workerId, name, true);
    }

    public static synchronized MainShiftWorker getInstance(int workerId, String name) {
        if (instance == null) {
            instance = new MainShiftWorker(workerId, name);
        }
        return instance;
    }

    @Override
    public SimpleStringProperty typeProperty() {
        return new SimpleStringProperty("Main");
    }
}
