package Modelos;

import Enumerados.CartaDeIndicacion;
import Enumerados.Eventos;
import Interfaces.ICarta;
import Interfaces.IJugador;
import Interfaces.IMazo;
import Interfaces.ITheGame;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;
import exceptions.CantidadJugadoresException;
import exceptions.CartaNoExisteException;
import exceptions.MovimientoInvalidoException;
import exceptions.PartidaIniciadaException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/// Clase que representa el juego con todas sus reglas y acciones posibles
public class TheGame extends ObservableRemoto implements ITheGame {

    private Eventos estadoPartida;
    private Queue<IJugador> jugadores;
    private IJugador jugadorActual = null;
    private IMazo mazo;
    private static final int CARTAS_MAXIMAS = Mano.getCartasMaximas();
    private IJugador anfitrion;
    private IJugador ultimoQueSalio;
    private final int MINIMO_JUGADORES = 2;

    //Con las pilas aplico polimorfismo usando la intefaz 'PilaDeIndicacion'
    private PilaDeIndicacion pilaDescendente;
    private PilaDeIndicacion pilaAscendente;

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
    @Override
    public IJugador agregarJugador(String nombre) throws RemoteException {

        if(estadoPartida == Eventos.JUGANDO) throw new PartidaIniciadaException("No se pueden agregar jugadores cuando la partida ya comenzo");
        if(jugadores.size() >= 5) throw new CantidadJugadoresException("No puede haber mas de 5 jugadores en la partida");

        Jugador jugador = new Jugador(nombre);

        if(anfitrion == null){
            anfitrion = jugador; //El jugador 1 siempre va a ser el anfitrion
        }
        jugadores.add(jugador);

        notificarObservadores(Eventos.NUEVO_JUGADOR);
        return jugador;
    }


    public void cerrar(IObservadorRemoto controlador, IJugador jugador) throws RemoteException {
        removerObservador(controlador);
        if (!jugadores.contains(jugador)) return;

        boolean eraSuTurno = jugador.equals(jugadorActual);
        if(mazo != null){
            mazo.devolverCartas(Arrays.stream(jugador.getCartas()).toList());
        }

        jugadores.remove(jugador);
        ultimoQueSalio = jugador;

        if (jugador.equals(anfitrion)) {

            if(jugadores.isEmpty()) anfitrion = null;
            else anfitrion = jugadores.peek();
        }

        if (estadoPartida == Eventos.JUGANDO && jugadores.size() < MINIMO_JUGADORES) {
            estadoPartida = Eventos.JUGADORES_INSUFICIENTES;
            notificarObservadores(estadoPartida);
            return;
        }

        if (eraSuTurno) {
            jugadorActual = jugadores.element();
            if (jugadorActual.getCantidadCartasEnMano() == 0) pasarJugador();
        }


        notificarObservadores(Eventos.JUGADOR_SALIO);
        if (eraSuTurno) notificarObservadores(Eventos.CAMBIO_DE_RONDA);
    }


    ///
    /// @param mazo Es el mazo que se utilizara en la partida
    /// @throws IllegalStateException Se lanza al intentar inicializar una partida ya comenzada
    /// @throws IllegalArgumentException Se lanza cuando el mazo es nulo
    /// @throws CantidadJugadoresException Se lanza cuando la cantidad de jugadores es menor a 2
    ///
    @Override
    public void inicializarPartida(IMazo mazo) throws RemoteException{

        if(estadoPartida == Eventos.JUGANDO) throw new IllegalStateException("La partida ya fue inicializada");
        if(mazo == null) throw new IllegalArgumentException("El mazo no puede ser nulo");
        if(jugadores.size() < 2) throw new CantidadJugadoresException("Se necesitan al menos 2 jugadores para iniciar la partida");

        this.mazo = mazo;
        rondaActual = 1;
        estadoPartida = Eventos.JUGANDO;
        jugadorActual = jugadores.peek();

        //repartir cartas a cada jugador
        for(int i = 0; i < CARTAS_MAXIMAS; i++) {
            jugadores.forEach(j -> j.tomarCarta(mazo));
        }

        notificarObservadores(Eventos.INICIO_PARTIDA);
    }

    @Override
    public IJugador getUltimoJugadorAgregado() throws RemoteException{
        return jugadores.stream().toList().getLast();
    }

    @Override
    public IJugador getUltimoJugadorQueSalio() throws RemoteException{
        return ultimoQueSalio;
    }

    @Override
    public IJugador getAnfitrion() throws RemoteException{
        return anfitrion;
    }
    /// Cambia el turno y cambia el jugador
    /// @throws MovimientoInvalidoException El jugador no puede pasar de ronda
    /// @throws IllegalArgumentException El jugador no puede ser null
    /// @throws MovimientoInvalidoException Se lanza cuando la partida no esta inicializada o cuando no se esta jugando.
    /// @throws MovimientoInvalidoException Se lanza cuando el jugador no puede pasar de ronda
    @Override
    public void pasarDeRonda(IJugador jugadorTurno) throws RemoteException {

        //Validaciones / programacion defensiva
        if(estadoPartida == Eventos.SIN_INICIAR) throw new MovimientoInvalidoException("La partida no inicio");
        if(estadoPartida != Eventos.JUGANDO) throw new MovimientoInvalidoException("Partida finalizada");

        IJugador jugador = getJugador(jugadorTurno.getId());
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


    @Override
    public IJugador getJugadorActual() throws RemoteException{
        return jugadorActual;
    }

    @Override
    public IJugador[] getJugadores() throws RemoteException{
        IJugador[] arrayJugadores = new IJugador[jugadores.size()];
        arrayJugadores = jugadores.toArray(arrayJugadores);
        return arrayJugadores;
    }

    @Override
    public Eventos getEstadoPartida() throws RemoteException{ return estadoPartida;}

    /// @throws  MovimientoInvalidoException Cuando la partida ya finalizo por algun motivo
    /// @throws PartidaIniciadaException La partida no inicio
    /// @throws CartaNoExisteException Si la carta no esta en la mano
    /// @throws MovimientoInvalidoException No fue posible agregar la carta a la pila
    @Override
    public void jugarCarta(IJugador jugadorTurno, CartaDeIndicacion tipoPila, int indiceCarta) throws RemoteException{

        if(estadoPartida == Eventos.SIN_INICIAR) throw new PartidaIniciadaException("La partida no inicio");
        if(estadoPartida != Eventos.JUGANDO) throw new MovimientoInvalidoException("Partida finalizada");

        IJugador jugador = getJugador(jugadorTurno.getId());
        if(!jugador.equals(jugadorActual)) throw new MovimientoInvalidoException("[Jugada invalida] - Es turno del jugador " + jugadorActual.getNombre());


        ICarta carta = getCarta(jugador, tipoPila, indiceCarta);

        //Tomo pila. No controlo las excepciones con un Catch para que las reciba un metodo superior
        if(tipoPila == CartaDeIndicacion.ASCENDENTE) jugador.jugarCarta(pilaAscendente,carta);
        else jugador.jugarCarta(pilaDescendente,carta);

        notificarObservadores(Eventos.CARTA_JUGADA);
    }

    @Override
    public int getRondaActual()throws RemoteException{
        return rondaActual;
    }

    @Override
    public int getCantidadCartasMazo()throws RemoteException {
        if(mazo == null) return 0;
        return mazo.getCantidad();
    }

    @Override
    public ICarta getUltimaCartaAscendente()throws RemoteException{
        return pilaAscendente.getUltimaCarta();
    }

    @Override
    public ICarta getUltimaCartaDescendente()throws RemoteException{
        return pilaDescendente.getUltimaCarta();
    }

    private void chequearEstadoPartida() throws RemoteException
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

    @Override
    public IJugador getJugador(UUID id) throws RemoteException {
        for (IJugador j : jugadores) {
            if (j.getId().equals(id)) return j;
        }
        return null;
    }

    @Override
    public void resetarPartida() throws RemoteException {
        pilaDescendente = new PilaDeIndicacion(CartaDeIndicacion.DESCENDENTE);
        pilaAscendente = new PilaDeIndicacion(CartaDeIndicacion.ASCENDENTE);
        jugadores = new ArrayDeque<>();
        anfitrion = null;
        mazo = null;
        jugadorActual = null;
        ultimoQueSalio = null;
        estadoPartida = Eventos.SIN_INICIAR;
    }

//  METODOS PRIVADOS

    private void pasarJugador(){
    if (jugadores.isEmpty()) throw new CantidadJugadoresException("No hay suficientes jugadores");

    if(mazo.esVacio() && jugadoresNoTienenMasCartas()) return;

    for(int i = 0; i < jugadores.size(); i++){
        jugadores.add(jugadores.poll());
        jugadorActual = jugadores.element();
        if(jugadorActual.getCantidadCartasEnMano() > 0) return;
    }
}


    private ICarta getCarta(IJugador jugador, CartaDeIndicacion tipoPila, int indiceCarta) {

        ICarta[] manoCartas = jugador.getCartas();

        if(manoCartas.length == 0) throw new CartaNoExisteException("La mano de cartas esta vacia");
        if(indiceCarta < 0 || indiceCarta >= manoCartas.length) throw new MovimientoInvalidoException("No es posible jugar la carta seleccionada");
        if(tipoPila == null) throw new MovimientoInvalidoException("No es posible jugar en la pila seleccionada");

        return manoCartas[indiceCarta];
    }

    private boolean jugadorPuedePasarRonda(IJugador jugador) {
        return jugador.getCantidadCartasEnMano() < CARTAS_MAXIMAS;
    }



    private boolean jugadoresNoTienenMasCartas(){
        //Reviso que cada jugador se haya quedado sin cartas para poder dar la condicion de partida ganada
        for(IJugador j : jugadores){
            if(j.getCantidadCartasEnMano() > 0) return false;
        }
        return true;
    }

    /// Reviso todas las combinaciones posibles de la mano del jugador.
    /// @return Retorna true si tiene al menos un movimiento valido, caso contrario retorna false
    private boolean elJugadorTieneAlgunMovimientoValido(){
        for (ICarta c : jugadorActual.getCartas()) {
            if (pilaAscendente.puedeAgregarCarta(c) || pilaDescendente.puedeAgregarCarta(c)) return true;
        }
        //Si no se cumple nada, no tiene movimiento valido
        return false;
    }

}
