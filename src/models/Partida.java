package models;

import exceptions.JugadaInvalidaException;
import exceptions.JugadorPasaTurnoException;
import exceptions.JugadoresPerdieronException;

import java.util.*;

public class Partida {

    private EstadoDePartida estadoPartida;

    private Queue<Jugador> jugadores;
    private Mazo mazo;

    //Con las pilas aplico polimorfismo usando la intefaz 'PilaEnJuego'
    private final PilaEnJuego pilaDescendente;
    private final PilaEnJuego pilaAscendente;

    private static int rondaActual;
    private static int intentosFallidosPorTurno;

    private Jugador jugadorActual;

    public Partida(Queue<Jugador> jugadores, Mazo mazo)
    {
        if(jugadores == null) throw new IllegalArgumentException("La lista de jugadores no puede ser nula");
        if(jugadores.size() > 5) throw new IllegalArgumentException("No puede haber mas de 5 jugadores en la partida");
        if(jugadores.size() < 2) throw new IllegalArgumentException("No puede haber menos de 2 jugadores en la partida");


        this.jugadores = jugadores; //Lista de jugadores
        rondaActual = 1; //Inicializo en ronda 1
        this.mazo = mazo; //Inicializo el mazo

        //Inicializo las pilas ascendentes y descendentes
        pilaAscendente = new PilaAscendente();
        pilaDescendente = new PilaDescendente();

        //Reparto las cartas a cada jugar
        repartirCartas();

        //Actualizo el jugador actual
        jugadorActual = jugadores.peek();

        //Actualizo el estado de la partida
        estadoPartida = EstadoDePartida.JUGANDO;

    }


    public void repartirCartas() {
        //A se entrega una carta a cada jugador y se repite 2 veces
        for(int i = 0; i < 2; i++) {
           jugadores.forEach(j -> j.tomarCarta(mazo));
        }
    }

    public void jugarMano(Jugador jugador, CartaJugador cartaEnJuego, PilaEnJuego pilaEnJuego) {

        try{
            jugador.jugarCarta(pilaEnJuego, cartaEnJuego);
        }
        catch (JugadaInvalidaException e){
            //Si la mano no es valida, sumo uno a los intentosFallidos, que indican si el jugador puede o no puede jugar su mano
            intentosFallidosPorTurno++;
        }
    }


    public Jugador pasarRonda() {
        Jugador siguienteJugador = jugadores.poll(); //tomo el siguiente jugador
        jugadores.add(siguienteJugador); //Agrego el jugador al final de la cola
        jugadorActual = siguienteJugador;
        intentosFallidosPorTurno = 0; //Reseteo el contador ya que cambia de turno
        return siguienteJugador;
    }

    public Jugador getJugadorturnoActual(){
        return jugadorActual;
    }

    public EstadoDePartida getEstadoPartida(){
        return estadoPartida;
    }

    public List<Jugador> getJugadores()
    {
        return jugadores.stream().toList();
    }

    public int getRondaActual(){
        return rondaActual;
    }

    public PilaEnJuego getPilaAscendente() {
        return pilaAscendente;
    }

    public PilaEnJuego getPilaDescendente() {
        return pilaDescendente;
    }

    public Optional<CartaJugador> verCartaPilaAscendente(){
            return pilaAscendente.getUltimaCarta();
    }

    public Optional<CartaJugador> verCartaPilaDescendente(){
        return pilaDescendente.getUltimaCarta();
    }

    public boolean esFinDeJuego()
    {
        chequearEstadoPartida();;
        if(estadoPartida == EstadoDePartida.JUGANDO) return false;
        else return true;
    }


    private void chequearEstadoPartida()
    {
        if(mazo.esMazoVacio()) {
            estadoPartida = EstadoDePartida.GANARON;
        }
        else if(intentosFallidosPorTurno == 2) {
            estadoPartida = EstadoDePartida.PERDIERON;
        }
        else{
            estadoPartida = EstadoDePartida.JUGANDO;
        }
    }










}
