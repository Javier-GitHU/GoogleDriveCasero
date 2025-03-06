package org.example;

import java.io.File;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 *
 * @author javie
 */
public class MonitoreadorDeActividades {

    private final String carpetaLocal;
    private final ManagerFTP manejador;
    private static final long INTERVALO = 5000;

    public MonitoreadorDeActividades(String carpetaLocal, ManagerFTP manejador) {
        this.carpetaLocal = carpetaLocal;
        this.manejador = manejador;
    }

    public void startMonitoring() {
        try {
            FileAlterationObserver observer = new FileAlterationObserver(carpetaLocal);
            FileAlterationMonitor monitor = new FileAlterationMonitor(INTERVALO);

            observer.addListener(new FileAlterationListenerAdaptor() {
                @Override
                public void onFileCreate(File file) {
                    System.out.println("Archivo creado: " + file.getName());
                    manejador.subirFichero(file.getAbsolutePath());
                }

                @Override
                public void onFileChange(File file) {
                    System.out.println("Archivo modificado: " + file.getName());
                    manejador.subirFichero(file.getAbsolutePath());
                }

                @Override
                public void onFileDelete(File file) {
                    System.out.println("Archivo eliminado: " + file.getName());
                    manejador.eliminarFichero(file.getName());
                }
            });

            monitor.addObserver(observer);
            monitor.start();
            System.out.println("Monitoreando la carpeta: " + carpetaLocal);
        } catch (Exception e) {
            System.err.println("Error al iniciar el monitoreo de la carpeta: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
