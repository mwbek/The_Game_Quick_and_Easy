package Persistencia;

import java.io.Serializable;
import java.util.*;

/// Esta clase representa el historial, conteniendo las estadisticas de todos los jugadores
public class HistorialJugadores implements Serializable {

    // Uso un diccionario con el nombre del jugador como clave
    private final Map<String, EstadisticasJugador> estadisticas = new HashMap<>();

    // Devuelve las estadísticas del jugador, creándolas si no existen
    private EstadisticasJugador getEstadisticas(String nombre) {
        EstadisticasJugador estadisticasJugador = estadisticas.get(nombre); //Busco las estadisticas del jugador
        if (estadisticasJugador == null) { //Si no existe inicializo las estadisticas con to?do en 0
            estadisticasJugador = new EstadisticasJugador(nombre);
            estadisticas.put(nombre, estadisticasJugador);
        }
        return estadisticasJugador;

    }

    public void registrarVictoria(String nombre)  { getEstadisticas(nombre).registrarVictoria(); }
    public void registrarDerrota(String nombre)   { getEstadisticas(nombre).registrarDerrota(); }
    public void registrarAbandono(String nombre)  { getEstadisticas(nombre).registrarAbandono(); }

    public List<EstadisticasJugador> getEstadisticas() {
        return new ArrayList<>(estadisticas.values());
    }

}
