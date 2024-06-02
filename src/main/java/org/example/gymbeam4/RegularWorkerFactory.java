package org.example.gymbeam4;

public class RegularWorkerFactory extends WorkerFactory {

    //    FACTORY METHOD
    @Override
    public Worker createWorker(int workerId, String name, boolean isMain) {
        return new Worker(workerId, name, false);
    }
}
