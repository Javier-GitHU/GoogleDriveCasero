package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author javie
 */
public class ManagerFTP {

    private final FTPConection conexionFTP = new FTPConection();

    public void subirFichero(String path) {
        new Thread(() -> {
            FTPClient clienteFTP = null;
            try {
                clienteFTP = conexionFTP.conectar();
                File archivo = new File(path);
                try (InputStream is = new FileInputStream(archivo)) {
                    boolean enviado = clienteFTP.storeFile(archivo.getName(), is);
                    if (enviado) {
                        System.out.println("Archivo subido correctamente: " + archivo.getName());
                    } else {
                        System.err.println("Error al subir el archivo: " + path);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error durante la subida del archivo: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (clienteFTP != null && clienteFTP.isConnected()) {
                    try {
                        clienteFTP.logout();
                        clienteFTP.disconnect();
                    } catch (IOException e) {
                        System.err.println("Error al cerrar la conexión FTP: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void eliminarFichero(String nombreFichero) {
        new Thread(() -> {
            FTPClient clienteFTP = null;
            try {
                clienteFTP = conexionFTP.conectar();
                boolean eliminado = clienteFTP.deleteFile(nombreFichero);
                if (eliminado) {
                    System.out.println("Archivo eliminado correctamente: " + nombreFichero);
                } else {
                    System.err.println("Error al eliminar el archivo: " + nombreFichero);
                }
            } catch (IOException e) {
                System.err.println("Error durante la eliminación del archivo: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (clienteFTP != null && clienteFTP.isConnected()) {
                    try {
                        clienteFTP.logout();
                        clienteFTP.disconnect();
                    } catch (IOException e) {
                        System.err.println("Error al cerrar la conexión FTP: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
