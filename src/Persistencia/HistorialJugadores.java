package Persistencia;

import java.util.*;

public class HistorialJugadores {

    // Uso un diccionario con el nombre del jugador como clave
    private final Map<String, EstadisticaJugador> estadisticas = new HashMap<>();

    // Devuelve las estadísticas del jugador, creándolas si no existen
    private EstadisticaJugador obtenerOCrear(String nombre) {
        EstadisticaJugador estadisticasJugador = estadisticas.get(nombre); //Busco las estadisticas del jugador
        if (estadisticasJugador == null) { //Si no existe inicializo las estadisticas con todo en 0
            estadisticasJugador = new EstadisticaJugador(nombre);
            estadisticas.put(nombre, estadisticasJugador);
        }
        return estadisticasJugador;

    }

    public void registrarVictoria(String nombre)  { obtenerOCrear(nombre).registrarVictoria(); }
    public void registrarDerrota(String nombre)   { obtenerOCrear(nombre).registrarDerrota(); }
    public void registrarAbandono(String nombre)  { obtenerOCrear(nombre).registrarAbandono(); }

    public List<EstadisticaJugador> getEstadisticas() {
        return new ArrayList<>(estadisticas.values()); // copia, respeta encapsulamiento
    }

}
