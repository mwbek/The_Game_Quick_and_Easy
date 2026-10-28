package Controladores;

import Vista.Consola.VistaMenuPrincipal;
import Modelos.Jugador;
import Modelos.Mazo;
import Modelos.Partida;

import java.util.Queue;

public class ControladorComenzarPartida {

    private final VistaMenuPrincipal vista;

    public ControladorComenzarPartida(VistaMenuPrincipal vista){
        this.vista = vista;
    }

    public Partida comenzarPartida() {
        int resultado = vista.mostrarMenuPrincipal();
        Partida nuevaPartida = null;
        if(resultado == 1){

            Queue<Jugador> jugadores = vista.solicitarJugadores();
            Mazo mazo = new Mazo();
            mazo.llenarMazo();
            nuevaPartida = new Partida(jugadores, mazo);
        }

        return nuevaPartida;
    }
}
