package Persistencia;

import java.io.*;

public class PersistenciaPartida {
    public static final String ARCHIVO_POR_DEFECTO = "historialTheGame.dat";

    private PersistenciaPartida() {}

    public static void guardar(HistorialJugadores historial, String rutaArchivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(historial);
        }
    }

    public static HistorialJugadores recuperar(String rutaArchivo)
            throws IOException, ClassNotFoundException {
        if (!existeArchivo(rutaArchivo)) {
            //Si el archivo no existe, lo creo
            return new HistorialJugadores();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            return (HistorialJugadores) ois.readObject();
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
