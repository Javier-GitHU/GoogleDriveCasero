-Proyecto: Google Drive pero barato.

Este proyecto es una implementación simple de un servicio de almacenamiento que pdoriamos decir que es similar a Google Drive, utilizando un servidor FTP (FileZilla en XAMPP) para la sincronización y gestión de archivos.

-Estructura
El proyecto se compone de las siguientes cuatro clases principales:

1. Main
   - Se encarga de iniciar el proyecto creando una instancia de `FolderWatcher` con la ruta de la carpeta local a monitorear y un `FTPManager` para gestionar las operaciones en el servidor FTP.

2. FTPConection
   - Esta clase establece la conexión con el servidor FTP.
   - Usa las credenciales del servidor FileZilla (usuario, contraseña, puerto y servidor `localhost`).
   - Valida la conexión y configura el cliente FTP en modo pasivo para evitar problemas con cortafuegos.
   - Establece el directorio raíz remoto (`/`), que en el contexto del servidor FileZilla se refiere a la carpeta predeterminada del usuario (en este caso, "CarpetaFTP").
   - Configura el tipo de archivo a `FTP.BINARY_FILE_TYPE` para permitir la transferencia de cualquier tipo de archivo, incluidos binarios y texto.

3. FTPManager (gestiona los archivos entre carpetas.
   - Usa `FTPConection` para conectarse al servidor y gestionar archivos remotos.
   - Implementa dos métodos principales: subirFichero y eliminarFichero.
   - Usa hilos para ejecutar las operaciones asegurando que la interfaz no se bloquee.
   - Los hilos permiten crear una conexión temporal, realizar la operación (subir o eliminar el archivo) y luego cerrar la conexión automáticamente.
   - Esta estrategia evita la sobrecarga del servidor FTP al mantener las conexiones abiertas solo durante el tiempo necesario.
   - Se usa la inicialización de `FTPClient clienteFTP = null` para asegurar que, incluso si hay una excepción, el cliente se desconecte correctamente.

4. MonitoreadorDeActividades(Que monitorea los cambios en la carpeta principal)
   - Se basa en la biblioteca `org.apache.commons.io.monitor` para monitorear los cambios en la carpeta local.
   - Captura eventos de creación, modificación y eliminación de archivos en tiempo real.
   - En cada evento llama al `FTPManager` para sincronizar el cambio con el servidor FTP.
   - Permite la sincronización bidireccional automática entre la carpeta local y el servidor remoto.


- Librerías utilizadas:
  - `commons-net` para la conexión FTP
  - `commons-io` para monitorear la carpeta local

-¿Como se ejecuta?
1. Configurar el servidor FileZilla con el usuario y contraseña adecuados.
2. Establecer la carpeta local a monitorear en `GoogleDrive.java`.
3. Compilar y ejecutar el proyecto.
4. Realizar operaciones (crear, modificar, eliminar archivos) en la carpeta local y verificar la sincronización automática con el servidor FTP.


