package Modelos;

import Enumerados.CartaDeIndicacion;
import Enumerados.Eventos;
import Interfaces.IMazo;
import Observer.Observable;
import exceptions.CantidadJugadoresException;

import java.util.ArrayDeque;
import java.util.Queue;

/// Clase que representa el juego con todas sus reglas y acciones posibles
public class TheGame extends Observable {

    private Eventos estadoPartida;
    private final Queue<Jugador> jugadores;
    private Jugador jugadorActual = null;
    private final IMazo mazo;

    //Con las pilas aplico polimorfismo usando la intefaz 'PilaDeIndicacion'
    private final PilaDeIndicacion pilaDescendente;
    private final PilaDeIndicacion pilaAscendente;

    private static int rondaActual;


    public TheGame() {
        pilaDescendente = new PilaDeIndicacion(CartaDeIndicacion.DESCENDENTE);
        pilaAscendente = new PilaDeIndicacion(CartaDeIndicacion.ASCENDENTE);
        jugadores = new ArrayDeque<>();
        mazo = new Mazo();
    }

    public void agregarJugador(String nombre){
        if(nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacio");
        if(jugadores.size() > 5) throw new IllegalArgumentException("No puede haber mas de 5 jugadores en la partida");

        Jugador jugador = new Jugador(nombre);
        jugadores.add(jugador);
        notificarObservadores(Eventos.NUEVO_JUGADOR);
    }

    public void inicializarPartida() {
        if(jugadores.size() < 2) throw new IllegalArgumentException("Se necesitan al menos 2 jugadores para iniciar la partida");
        mazo.mezclarMazo();
        rondaActual = 1;
        pasarJugador(); //Agarro el primer jugador en la cola jugadores


        notificarObservadores(Eventos.INICIO_PARTIDA);

    }

    public void repartirCartas() {
        //A se entrega una carta a cada jugador y se repite 2 veces
        for(int i = 0; i < 2; i++) {
            jugadores.forEach(j -> j.tomarCarta(mazo));
        }
        notificarObservadores(Eventos.SE_REPARTIERON_CARTAS);
    }

    /// Cambia el turno y cambia el jugador
    /// @return true si se pudo pasar de ronda, falso caso contrario.
    public boolean pasarDeRonda() {
        if(!jugadorPuedePasarRonda()) return false;
        Jugador jugadorAnterior = jugadorActual;
        pasarJugador();
        //El jugador que pasa la ronda toma del mazo la cantidad de cartas que haya jugado
        if(jugadorAnterior != null){
            int cantidadDeCartasEnMano = jugadorAnterior.getCantidadCartasEnMano();
            for(int i = 0; i < cantidadDeCartasEnMano; i++){
                jugadorAnterior.tomarCarta(mazo);
            }
        }

        notificarObservadores(Eventos.CAMBIO_DE_RONDA);
        return true;
    }

    private void pasarJugador(){
        if (jugadores.isEmpty()) throw new CantidadJugadoresException("No hay suficientes jugadores");

        jugadorActual = jugadores.poll(); //Actualizo el jugador actual
        jugadores.add(jugadorActual); //Lo mando al final de la cola
    }

    public Jugador getJugadorActual(){
        return jugadorActual;
    }

    public Jugador[] getJugadores() {
        Jugador[] arrayJugadores = new Jugador[jugadores.size()];
        arrayJugadores = jugadores.toArray(arrayJugadores);
        return arrayJugadores;
    }

    public boolean jugarMano(Carta carta, CartaDeIndicacion pila){
        if(pila == null | carta == null) throw new IllegalArgumentException("Carta o Pila no pueden ser nulos");
        if(pila == CartaDeIndicacion.ASCENDENTE){
            return jugadorActual.jugarCarta(pilaAscendente,carta);
        }
        return jugadorActual.jugarCarta(pilaDescendente,carta);

    }


    public boolean esFinDeJuego() {

        Eventos estadoAnterior = estadoPartida;
        chequearEstadoPartida();

        //Si hubo un cambio de estado notifico al controlador
        if(estadoAnterior != estadoPartida){
            notificarObservadores(estadoPartida);
        }

        return estadoPartida != Eventos.JUGANDO;
    }

    public int getRondaActual(){
        return rondaActual;
    }

    public int getCantidadCartasMazo()
    {
        return mazo.getMazo().length;
    }


    private boolean jugadorPuedePasarRonda() {
        return jugadorActual.getCantidadCartasEnMano() < 2;
    }

    private void chequearEstadoPartida()
    {
        if(mazo.esVacio() && jugadoresNoTienenMasCartas()) {
            estadoPartida = Eventos.GANARON;
        }
        else if(!jugadorActual.hayMovimientosValidos(pilaAscendente, pilaDescendente)) {
            estadoPartida = Eventos.PERDIERON;
        }
        else{
            estadoPartida = Eventos.JUGANDO;
        }

    }


    private boolean jugadoresNoTienenMasCartas(){
        //Reviso que cada jugador se haya quedado sin cartas para poder dar la condicion de partida ganada
        for(Jugador j : jugadores){
            if(j.getCantidadCartasEnMano() > 0) return false;
        }
        return true;
    }




}
