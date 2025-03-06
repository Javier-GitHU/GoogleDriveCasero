package org.example;

public class Main {
    public static void main(String[] args) {
        MonitoreadorDeActividades watcher = new MonitoreadorDeActividades("C:\\Users\\javie\\Documents\\Servicios y procesos\\DriveProcesos", new ManagerFTP());
        watcher.startMonitoring();
    }
}