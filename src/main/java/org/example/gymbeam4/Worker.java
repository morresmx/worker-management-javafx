package org.example.gymbeam4;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Worker extends Person {

//    ADAPTER TEMPLATE FOR WORKER INFO
    private final SimpleIntegerProperty workerIdProperty;
    private final SimpleStringProperty nameProperty;
    private final SimpleStringProperty typeProperty;
    private final SimpleBooleanProperty mainProperty;

    public Worker(int id, String name, boolean isMain) {
        super(id, name);
        this.workerIdProperty = new SimpleIntegerProperty(id);
        this.nameProperty = new SimpleStringProperty(name);
        this.typeProperty = new SimpleStringProperty(isMain ? "Main" : "Regular");
        this.mainProperty = new SimpleBooleanProperty(isMain);
    }

    public boolean isMain() {
        return mainProperty.get();
    }

    public SimpleIntegerProperty workerIdProperty() {
        return workerIdProperty;
    }

    public SimpleStringProperty nameProperty() {
        return nameProperty;
    }

    public SimpleStringProperty typeProperty() {
        return typeProperty;
    }
}
