package Modelos;

import Enumerados.CartaDeIndicacion;
import Enumerados.Eventos;
import Observer.Observable;
import exceptions.CantidadJugadoresException;

import java.util.ArrayDeque;
import java.util.Queue;

/// Clase que representa el juego con todas sus reglas y acciones posibles
public class TheGame extends Observable {

    private Eventos estadoPartida;
    private Queue<Jugador> jugadores;
    private Jugador jugadorActual = null;
    private final Mazo mazo;

    //Con las pilas aplico polimorfismo usando la intefaz 'PilaDeIndicacion'
    private final PilaDeIndicacion pilaDescendente;
    private final PilaDeIndicacion pilaAscendente;

    private static int rondaActual;


    public TheGame() {
        pilaDescendente = new PilaDeIndicacion(CartaDeIndicacion.DESCENDENTE);
        pilaAscendente = new PilaDeIndicacion(CartaDeIndicacion.ASCENDENTE);
        jugadores = new ArrayDeque<>();
        mazo = new Mazo();
        mazo.mezclarMazo();
        rondaActual = 1;
    }

    public void agregarJugador(String nombre){
        if(nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacio");
        if(jugadores.size() > 5 || jugadores.size() < 2) throw new IllegalArgumentException("No puede haber mas de 5 jugadores en la partida");

        Jugador jugador = new Jugador(nombre);
        jugadores.add(jugador);

        notificarObservadores(Eventos.NUEVO_JUGADOR);
    }

    public void repartirCartas() {
        //A se entrega una carta a cada jugador y se repite 2 veces
        for(int i = 0; i < 2; i++) {
            jugadores.forEach(j -> j.tomarCarta(mazo));
        }
        notificarObservadores(Eventos.SE_REPARTIERON_CARTAS);
    }


    public boolean pasarDeRonda() {
        if (jugadores.isEmpty()) throw new CantidadJugadoresException("No hay suficientes jugadores");

        if(!jugadorPuedePasarRonda()) return false;

        if(jugadorActual == null) {
            jugadorActual = jugadores.poll(); //Tomo el primer jugador en la cola
        }
        else{
            jugadores.add(jugadorActual); //Lo mando al final de la cola
        }
        notificarObservadores(Eventos.CAMBIO_DE_RONDA);
        return true;
    }


    public Jugador getJugadorActual(){
        return jugadorActual;
    }

    public boolean jugarMano(Carta carta, PilaDeIndicacion pila){
        if(pila == null | carta == null) throw new IllegalArgumentException("Carta o Pila no pueden ser nulos");
        return jugadorActual.jugarCarta(pila,carta);
    }


    public boolean esFinDeJuego() {
        chequearEstadoPartida();
        return estadoPartida != Eventos.JUGANDO;
    }

    public int getRondaActual(){
        return rondaActual;
    }


    private boolean jugadorPuedePasarRonda() {
        return jugadorActual.getCartas().length < 2;
    }

    private void chequearEstadoPartida()
    {
        Eventos estadoAnterior = estadoPartida;

        if(mazo.esMazoVacio()) {
            estadoPartida = Eventos.GANARON;
        }
        else if(jugadorActual.tieneMovimientosValidos(pilaAscendente, pilaDescendente)) {
            estadoPartida = Eventos.PERDIERON;
        }
        else{
            estadoPartida = Eventos.JUGANDO;
        }

        //Si hubo un cambio de estado notifico al controlador
        if(estadoAnterior != estadoPartida){
            notificarObservadores(estadoPartida);
        }
    }




}
