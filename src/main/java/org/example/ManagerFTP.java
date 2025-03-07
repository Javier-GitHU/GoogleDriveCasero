package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.net.ftp.FTPClient;
/**
 *
 * @author javie
 */
public class ManagerFTP {
    private final FTPConection conexionFTP = new FTPConection();
    private static final String ALGORITMO = "AES";
    private static final byte[] CLAVE_SECRETA = "1234567890123456".getBytes();

    public void subirFichero(String path) {
        new Thread(() -> {
            FTPClient clienteFTP = null;
            try {
                clienteFTP = conexionFTP.conectar();
                File archivoOriginal = new File(path);
                File archivoCifrado = new File("encrypted_" + archivoOriginal.getName());

                cifrarArchivo(archivoOriginal, archivoCifrado);

                try (InputStream is = new FileInputStream(archivoCifrado)) {
                    boolean enviado = clienteFTP.storeFile(archivoCifrado.getName(), is);
                    if (enviado) {
                        System.out.println("Archivo subido correctamente: " + archivoCifrado.getName());
                    } else {
                        System.err.println("Error al subir el archivo: " + path);
                    }
                }

                archivoCifrado.delete();

            } catch (IOException e) {
                System.err.println("Error durante la subida del archivo: " + e.getMessage());
                e.printStackTrace();
            } finally {
                cerrarConexion(clienteFTP);
            }
        }).start();
    }

    public void eliminarFichero(String nombreFichero) {
        new Thread(() -> {
            FTPClient clienteFTP = null;
            try {
                clienteFTP = conexionFTP.conectar();
                boolean eliminado = clienteFTP.deleteFile("encrypted_" + nombreFichero);
                if (eliminado) {
                    System.out.println("Archivo eliminado correctamente: " + nombreFichero);
                } else {
                    System.err.println("Error al eliminar el archivo: " + nombreFichero);
                }
            } catch (IOException e) {
                System.err.println("Error durante la eliminación del archivo: " + e.getMessage());
                e.printStackTrace();
            } finally {
                cerrarConexion(clienteFTP);
            }
        }).start();
    }

    private void cifrarArchivo(File archivoOriginal, File archivoCifrado) throws IOException {
        try (FileInputStream fis = new FileInputStream(archivoOriginal);
             FileOutputStream fos = new FileOutputStream(archivoCifrado);
             CipherOutputStream cos = new CipherOutputStream(fos, obtenerCifrador(Cipher.ENCRYPT_MODE))) {
            byte[] buffer = new byte[1024];
            int bytesLeidos;
            while ((bytesLeidos = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesLeidos);
            }
        }
    }

    private Cipher obtenerCifrador(int modo) throws IOException {
        try {
            Cipher cifrador = Cipher.getInstance(ALGORITMO);
            cifrador.init(modo, new SecretKeySpec(CLAVE_SECRETA, ALGORITMO));
            return cifrador;
        } catch (Exception e) {
            throw new IOException("Error al inicializar el cifrador: " + e.getMessage(), e);
        }
    }

    private void cerrarConexion(FTPClient clienteFTP) {
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
}
