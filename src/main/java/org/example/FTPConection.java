package org.example;

import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author javie
 */
public class FTPConection {

    private static final String SERVIDOR = "localhost";
    private static final int PUERTO = 21;
    private static final String USUARIO = "Javier";
    private static final String PASSWORD = "1234";

    public FTPClient conectar() throws SocketException, IOException {
        FTPClient clienteFTP = new FTPClient();
        clienteFTP.connect(SERVIDOR, PUERTO);

        int replyCode = clienteFTP.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            throw new IOException("Conexión fallida con el servidor FTP: " + replyCode);
        }

        if (!clienteFTP.login(USUARIO, PASSWORD)) {
            throw new IOException("Credenciales FTP incorrectas");
        }

        clienteFTP.setFileType(FTP.BINARY_FILE_TYPE);
        clienteFTP.enterLocalPassiveMode();

        String directorioRemoto = "/";
        if (!clienteFTP.changeWorkingDirectory(directorioRemoto)) {
            throw new IOException("No se pudo cambiar al directorio remoto: " + directorioRemoto);
        }

        return clienteFTP;
    }
}
