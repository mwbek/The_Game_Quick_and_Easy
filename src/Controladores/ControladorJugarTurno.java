package Controladores;

import Vista.Consola.VistaManoJugador;
import exceptions.JugadaInvalidaException;
import exceptions.JugadorPasaTurnoException;
import models.*;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class ControladorJugarTurno {

    private final Partida partida;
    private final VistaManoJugador vistaManoJugador;

    Queue<ManoJugada> manosJugadas = new ArrayDeque<>();
    private Jugador jugador;
    public ControladorJugarTurno(Partida partida, VistaManoJugador vistaManoJugador)
    {
        this.partida = partida;
        this.vistaManoJugador = vistaManoJugador;
    }


    public void jugarMano()
    {
        jugador = partida.getJugadorturnoActual();

        List<CartaJugador> cartasEnMano = jugador.getCartas();
        CartaJugador cartaEnJuego;

        vistaManoJugador.mostrarHUDJugador(partida);

        int respuesta = vistaManoJugador.solicitarCartaAJugar();

        //Valido jugada
        if(respuesta == 1 || respuesta == 2) {
            cartaEnJuego = cartasEnMano.get(respuesta-1);
            vistaManoJugador.mostrarMensaje("Elegiste la carta [" + respuesta + "]: " + cartaEnJuego);
        }
        //Si el jugador puso al menos una carta, puede pasar. Caso contrario debe elegir una si o si
        else if (respuesta == 3) {
            if (cartasEnMano.size() == 2) throw new JugadaInvalidaException("Para pasar de turno se debe jugar al menos una carta");
            else throw new JugadorPasaTurnoException(); //No la controlo para que pase sola al metodo que la llame
        }





    }









    }

}
