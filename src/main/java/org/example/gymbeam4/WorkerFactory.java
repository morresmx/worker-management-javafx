package org.example.gymbeam4;

public abstract class WorkerFactory {

    //    FACTORY METHOD
    public abstract Worker createWorker(int workerId, String name, boolean isMain);
}
