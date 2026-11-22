package Persistencia;

import java.io.*;

public class PersistenciaPartida {
    public static final String ARCHIVO_POR_DEFECTO = "chat_sesion.dat";

    private PersistenciaPartida() {
    }

    public static void guardar(SesionPartida sesion, String rutaArchivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(sesion);
        }
    }

    public static SesionPartida recuperar(String rutaArchivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            return (SesionPartida) ois.readObject();
        }
    }

    public static boolean existeArchivo(String rutaArchivo) {
        if (rutaArchivo == null) {
            return false;
        }
        File f = new File(rutaArchivo);
        return f.exists() && f.isFile() && f.length() > 0;
    }
}
