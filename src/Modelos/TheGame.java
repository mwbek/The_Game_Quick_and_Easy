package Modelos;

import Enumerados.CartaDeIndicacion;
import Enumerados.Eventos;
import Interfaces.IJugador;
import Interfaces.IMazo;
import Observer.Observable;
import exceptions.CantidadJugadoresException;
import exceptions.CartaNoExisteException;
import exceptions.MovimientoInvalidoException;
import exceptions.PartidaIniciadaException;

import java.util.ArrayDeque;
import java.util.Queue;

/// Clase que representa el juego con todas sus reglas y acciones posibles
public class TheGame extends Observable {

    private Eventos estadoPartida;
    private final Queue<Jugador> jugadores;
    private Jugador jugadorActual = null;
    private IMazo mazo;
    private static final int CARTAS_MAXIMAS = Mano.getCartasMaximas();

    //Con las pilas aplico polimorfismo usando la intefaz 'PilaDeIndicacion'
    private final PilaDeIndicacion pilaDescendente;
    private final PilaDeIndicacion pilaAscendente;

    private int rondaActual;


    public TheGame() {
        pilaDescendente = new PilaDeIndicacion(CartaDeIndicacion.DESCENDENTE);
        pilaAscendente = new PilaDeIndicacion(CartaDeIndicacion.ASCENDENTE);
        jugadores = new ArrayDeque<>();
        estadoPartida = Eventos.SIN_INICIAR;
    }

    ///
    /// @param nombre nombre del jugador
    /// @return La instancia del jugador agregado
    /// @throws PartidaIniciadaException Agregar un jugador con la partida iniciada
    /// @throws IllegalArgumentException Intentar agregar mas de 5 jugadores
    public Jugador agregarJugador(String nombre){

        if(estadoPartida == Eventos.JUGANDO) throw new PartidaIniciadaException("No se pueden agregar jugadores cuando la partida ya comenzo");

        if(jugadores.size() >= 5) throw new IllegalArgumentException("No puede haber mas de 5 jugadores en la partida");

        Jugador jugador = new Jugador(nombre);
        jugadores.add(jugador);
        notificarObservadores(Eventos.NUEVO_JUGADOR);

        return jugador;
    }


    ///
    /// @param mazo Es el mazo que se utilizara en la partida
    /// @throws IllegalStateException Se lanza al intentar inicializar una partida ya comenzada
    /// @throws IllegalArgumentException Se lanza cuando el mazo es nulo
    /// @throws IllegalArgumentException Se lanza cuando la cantidad de jugadores es menor a 2
    public void inicializarPartida(IMazo mazo) {

        if(estadoPartida == Eventos.JUGANDO) throw new IllegalStateException("La partida ya fue inicializada");
        if(mazo == null) throw new IllegalArgumentException("El mazo no puede ser nulo");
        if(jugadores.size() < 2) throw new IllegalArgumentException("Se necesitan al menos 2 jugadores para iniciar la partida");

        this.mazo = mazo;
        rondaActual = 1;
        estadoPartida = Eventos.JUGANDO;
        jugadorActual = jugadores.peek();
        repartirCartas();

        notificarObservadores(Eventos.INICIO_PARTIDA);
    }

    /// Cambia el turno y cambia el jugador
    /// @throws MovimientoInvalidoException El jugador no puede pasar de ronda
    /// @throws IllegalArgumentException El jugador no puede ser null
    /// @throws MovimientoInvalidoException Se lanza cuando la partida no esta inicializada o cuando no se esta jugando.
    /// @throws MovimientoInvalidoException Se lanza cuando el jugador no puede pasar de ronda
    public void pasarDeRonda(Jugador jugador) {

        //Validaciones / programacion defensiva
        if(estadoPartida == Eventos.SIN_INICIAR) throw new MovimientoInvalidoException("La partida no inicio");
        if(estadoPartida != Eventos.JUGANDO) throw new MovimientoInvalidoException("Partida finalizada");

        if(jugador == null) throw new IllegalArgumentException("Jugador no puede ser nulo");
        if(jugador != jugadorActual) throw new MovimientoInvalidoException("No puede pasar de ronda actualmente");
        if(!jugadorPuedePasarRonda(jugador)) throw new MovimientoInvalidoException("Para pasar de ronda debe jugar al menos una carta");

        //1) Se reponen cartas, el jugador toma del mazo
        //El jugador que pasa la ronda toma del mazo la cantidad de cartas que haya jugado
        int cantidadDeCartasEnMano = jugador.getCantidadCartasEnMano();
        int cantidadDeCartasARobar = CARTAS_MAXIMAS - cantidadDeCartasEnMano;
        for(int i = 0; i < cantidadDeCartasARobar; i++){
            jugador.tomarCarta(mazo);
        }

        //2)Paso de ronda e incremento el contador de rondas
        pasarJugador();
        rondaActual++;

        //3) Chequeo estado de la partida
        chequearEstadoPartida();

        //4) Notifico al controlador si el estado de la partida no cambio
        if (estadoPartida == Eventos.JUGANDO) notificarObservadores(Eventos.CAMBIO_DE_RONDA);
    }


    public IJugador getJugadorActual(){
        return jugadorActual;
    }

    public IJugador[] getJugadores() {
        IJugador[] arrayJugadores = new IJugador[jugadores.size()];
        arrayJugadores = jugadores.toArray(arrayJugadores);
        return arrayJugadores;
    }

    public void jugarCarta(Jugador jugador, CartaDeIndicacion tipoPila, int indiceCarta) {

        if(estadoPartida == Eventos.SIN_INICIAR) throw new MovimientoInvalidoException("La partida no inicio");
        if(estadoPartida != Eventos.JUGANDO) throw new MovimientoInvalidoException("Partida finalizada");

        Carta carta = getCarta(jugador, tipoPila, indiceCarta);

        //Tomo pila. No controlo las excepciones con un Catch para que las reciba un metodo superior
        if(tipoPila == CartaDeIndicacion.ASCENDENTE) jugador.jugarCarta(pilaAscendente,carta);
        else jugador.jugarCarta(pilaDescendente,carta);

        notificarObservadores(Eventos.CARTA_JUGADA);
    }

    public int getRondaActual(){
        return rondaActual;
    }

    public int getCantidadCartasMazo() {
        if(mazo == null) return 0;
        return mazo.getCantidad();
    }

    public Carta getUltimaCartaAscendente(){
        return pilaAscendente.getUltimaCarta();
    }

    public Carta getUltimaCartaDescendente(){
        return pilaDescendente.getUltimaCarta();
    }


//  METODOS PRIVADOS

    private void repartirCartas() {
        //A se entrega una carta a cada jugador y se repite 2 veces
        for(int i = 0; i < CARTAS_MAXIMAS; i++) {
            jugadores.forEach(j -> j.tomarCarta(mazo));
        }
        notificarObservadores(Eventos.SE_REPARTIERON_CARTAS);
    }


    private void pasarJugador(){
    if (jugadores.isEmpty()) throw new CantidadJugadoresException("No hay suficientes jugadores");

    if(mazo.esVacio() && jugadoresNoTienenMasCartas()) return;

    for(int i = 0; i < jugadores.size(); i++){
        jugadores.add(jugadores.poll());
        jugadorActual = jugadores.element();
        if(jugadorActual.getCantidadCartasEnMano() > 0) return;
    }
}


    private Carta getCarta(Jugador jugador, CartaDeIndicacion tipoPila, int indiceCarta) {
        if(jugador != jugadorActual) throw new MovimientoInvalidoException("[Jugada invalida] - Es turno del jugador " + jugadorActual.getNombre());

        Carta[] manoCartas = jugador.getCartas();

        if(manoCartas.length == 0) throw new CartaNoExisteException("La mano de cartas esta vacia");
        if(indiceCarta < 0 || indiceCarta >= manoCartas.length) throw new MovimientoInvalidoException("No es posible jugar la carta seleccionada");
        if(tipoPila == null) throw new MovimientoInvalidoException("No es posible jugar en la pila seleccionada");

        return manoCartas[indiceCarta];
    }

    private boolean jugadorPuedePasarRonda(Jugador jugador) {
        return jugador.getCantidadCartasEnMano() < CARTAS_MAXIMAS;
    }

    private void chequearEstadoPartida()
    {
        if(mazo.esVacio() && jugadoresNoTienenMasCartas()) {
            estadoPartida = Eventos.GANARON;
        }
        else if(!elJugadorTieneAlgunMovimientoValido()) {
            estadoPartida = Eventos.PERDIERON;
        }

        //Si hubo un cambio de estado notifico al controlador
        if (estadoPartida != Eventos.JUGANDO){
            notificarObservadores(estadoPartida);
        }

    }

    private boolean jugadoresNoTienenMasCartas(){
        //Reviso que cada jugador se haya quedado sin cartas para poder dar la condicion de partida ganada
        for(Jugador j : jugadores){
            if(j.getCantidadCartasEnMano() > 0) return false;
        }
        return true;
    }

    /// Reviso todas las combinaciones posibles de la mano del jugador.
    /// @return Retorna true si tiene al menos un movimiento valido, caso contrario retorna false
    private boolean elJugadorTieneAlgunMovimientoValido(){
        for (Carta c : jugadorActual.getCartas()) {
            if (pilaAscendente.puedeAgregarCarta(c) || pilaDescendente.puedeAgregarCarta(c)) return true;
        }
        //Si no se cumple nada, no tiene movimiento valido
        return false;
    }

}
