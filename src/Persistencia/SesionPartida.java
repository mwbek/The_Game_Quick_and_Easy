package Persistencia;

import Enumerados.Eventos;
import Interfaces.IJugador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SesionPartida implements Serializable {

    private final List<IJugador> jugadores;

    public SesionPartida(List<IJugador> jugadores, Eventos estadoPartida) {
        if(jugadores == null) this.jugadores = new ArrayList<>();
        else this.jugadores = jugadores;
    }

    public List<IJugador> getJugadores(){
        return new ArrayList<>(jugadores); //Retorno una copia de jugadores
    }

}
