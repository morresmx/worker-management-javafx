package org.example.gymbeam4;

public class MainWorkerFactory extends WorkerFactory {
    private static MainWorkerFactory instance;

    private MainWorkerFactory() {
    }


    //    SINGLETON TEMPLATE
    public static synchronized MainWorkerFactory getInstance() {
        if (instance == null) {
            instance = new MainWorkerFactory();
        }
        return instance;
    }


    //    FACTORY METHOD
    @Override
    public Worker createWorker(int workerId, String name, boolean isMain) {
        Worker worker = new Worker(workerId, name, true);
        if (isMain) {
            worker.typeProperty().setValue("Main");
        } else {
            worker.typeProperty().setValue("Regular");
        }
        return worker;
    }

}

