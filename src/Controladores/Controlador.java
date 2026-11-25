package Controladores;

import Enumerados.CartaDeIndicacion;
import Enumerados.Eventos;
import Interfaces.*;
import Modelos.*;
import Persistencia.EstadisticasJugador;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import exceptions.CantidadJugadoresException;
import exceptions.CartaNoExisteException;
import exceptions.MovimientoInvalidoException;
import exceptions.PartidaIniciadaException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import static Enumerados.Eventos.JUGANDO;
import static Enumerados.Eventos.SIN_INICIAR;

public class Controlador implements IControladorRemoto {

    private IVista vista;
    private ITheGame juego;
    private  IJugador jugador;
    private boolean partidaReseteada = false;
    public Controlador(){
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        if(modeloRemoto == null) throw new IllegalArgumentException("El juego no puede ser nulo");
        this.juego = (ITheGame) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) {


        if (o instanceof Eventos ev && juego != null && vista != null) {
            try{
                if(jugador != null) this.jugador = juego.getJugador(jugador.getId());
            } catch (RemoteException e) {
                vista.mostrarError("No se pudo actualizar el jugador: " + e.getMessage() );
            }

            switch (ev)
            {
                case INICIO_PARTIDA:
                    try {
                            if(juego.getEstadoPartida() == JUGANDO){
                                vista.mostrarHUDJugador();
                                if(!esTurno()) vista.mostrarMensaje("Esperando turno de [" + getJugadorActual().getNombre() + "]");
                            }
                    } catch (RemoteException e) {
                        vista.mostrarError("Error al iniciar partida: " + e.getMessage());
                    }
                    break;

                case CARTA_JUGADA:
                        //Actualizo el HUD a todos los jugadores
                        vista.mostrarHUDJugador();
                    break;

                case CAMBIO_DE_RONDA:
                    try{
                        if(juego.getEstadoPartida() == JUGANDO){
                            vista.mostrarHUDJugador();
                        }
                    }catch (RemoteException e){
                        vista.mostrarError("Error en la jugada: " + e.getMessage());
                    }
                    break;

                case NUEVO_JUGADOR:
                    try {
                        //Si el jugador no es el anfitrion
                        if(jugador != null){
                            vista.actualizarMenuLobby();
                            vista.actualizarListaJugadores();
                            vista.mostrarMensaje("[" + juego.getJugadores()[juego.getJugadores().length-1].getNombre() + "] se unio a la partida:  (" + juego.getJugadores().length + "/ 5)");

                        }
                    } catch (RemoteException e) {
                        vista.mostrarError("Error al agregar nuevo jugador: " + e.getMessage());
                    }

                    break;
                case JUGADOR_SALIO:
                    try {
                        IJugador jugadorQueSalio = juego.getUltimoJugadorQueSalio();
                        if (jugadorQueSalio != null && !jugadorQueSalio.equals(jugador)) {
                            vista.mostrarMensaje("- [" + jugadorQueSalio.getNombre() + "] salió de la partida");
                        }
                        vista.actualizarJugadorSalio();
                        vista.actualizarListaJugadores();

                    } catch (RemoteException e) {
                        vista.mostrarError("Error al remover jugador: " + e.getMessage());
                    }
                    break;
                case GANARON:
                    vista.jugadoresGanaron();
                    break;
                case PERDIERON:
                    vista.jugadoresPerdieron();
                    break;

                case JUGADORES_INSUFICIENTES:
                    vista.jugadoresInsuficientes();
                    break;


            }
        }

    }



    public void verHistorial(){
        try{
            List<EstadisticasJugador> estadisticasJugadores = juego.getHistorial();
            vista.mostrarHistorial(estadisticasJugadores);
        }
        catch (RemoteException e) {
            vista.mostrarError("Error al conectar con el servidor: " + e.getMessage());
        }

    }


    public int getCantidadJugadores(){
        try {
            return juego.getJugadores().length;
        } catch (RemoteException e) {
            vista.mostrarError("Error al conectar con el servidor: " + e.getMessage());
            return 0;
        }
    }

    public boolean esTurno(){
        if(jugador == null ) return false;
        return jugador.equals(getJugadorActual());
    }

    public void setVista(IVista vista) {
        //Programacion defensiva
        if(vista == null) throw new IllegalArgumentException("La vista no puede ser null");
        this.vista = vista;
    }


    public void cerrarApp() {
        if (juego != null) {
            try {
                if (jugador != null) {
                    juego.cerrar(this, jugador);
                } else {
                    juego.removerObservador(this);
                }

                if(juego.getJugadores().length < 2){
                    resetearPartida();
                }

            } catch (RemoteException e) {
                // Si el servidor cerró o falló la red, ignoramos para poder terminar
            }
        }
        System.exit(0);
    }

    public ObservableList<IJugador> getListaJugadores(){
        try{
            List<IJugador> jugadores = Arrays.stream(juego.getJugadores()).toList();
            return FXCollections.observableList(jugadores);

        } catch (RemoteException e) {
            vista.mostrarError("No se pudo obtener la lista de jugadores: " + e.getMessage());
            return null;
        }

    }


    public void registrarJugador(String nombre){
        try {
            if (nombre == null) throw new IllegalArgumentException("El nombre del jugador no puede ser nulo");
            this.jugador = juego.agregarJugador(nombre);
            vista.actualizarMenuLobby();

        } catch (RemoteException e) {
            vista.mostrarError("Error al registrar nuevo jugador: " + e.getMessage());
        }
        catch (CantidadJugadoresException | PartidaIniciadaException e){
            vista.mostrarMensaje(e.getMessage());
            vista.mostrarError("Saliendo de la partida...");
            vista.salir();
        }
    }

    public IJugador getJugador(){
        return this.jugador;
    }


    public IJugador getJugadorActual(){
        try {
            return juego.getJugadorActual();
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener jugador actual: " + e.getMessage());
            return null;
        }

    }

    public boolean seInicioLaPartida(){
        try{
            return juego.getEstadoPartida() != SIN_INICIAR;
        }catch (RemoteException e) {
            vista.mostrarError("Error al obtener el estado de la partida: " + e.getMessage());
            return true;
        }

    }


    public int getRondaActual(){
        try{
            return juego.getRondaActual();
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener ronda actual: " + e.getMessage());
            return 0;
        }

    }

    //TODO Remover si no tiene uso
    public String[] getCartas(){
        String[] cartasTexto = {"[]", "[]"};
        // IJugador jugadorVista = juego.getJugador(jugador.getId());
        if (jugador != null) {
            ICarta[] cartas = jugador.getCartas();
            for (int i = 0; i < cartas.length && i < cartasTexto.length; i++) {
                cartasTexto[i] = cartas[i].toString();
            }
        }
        return cartasTexto;
    }

    public ICarta getCartaPilaAscendente(){
        try{
            return juego.getUltimaCartaAscendente();
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener ultima carta ascendente: " + e.getMessage());
            return null;
        }

    }

    public ICarta getCartaPilaDescendente(){
        try{
            return juego.getUltimaCartaDescendente();
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener ultima carta descendente: " + e.getMessage());
            return null;
        }
    }


    public void jugarCarta(int indiceCarta, int indicePila){
        try{
            indiceCarta--; //Resto uno al indice de carta porque se pasa 1 y 2
            if (indicePila == 1)juego.jugarCarta(jugador,CartaDeIndicacion.ASCENDENTE, indiceCarta);
            else juego.jugarCarta(jugador,CartaDeIndicacion.DESCENDENTE, indiceCarta);
        }
        catch (CartaNoExisteException | MovimientoInvalidoException | PartidaIniciadaException | RemoteException e){
            vista.mostrarError(e.getMessage());
        }
    }

    public void pasarTurno(){
        try{
            if(jugador == null) throw new IllegalArgumentException("Jugador no puede ser nulo");
            juego.pasarDeRonda(jugador);
        }
        catch (MovimientoInvalidoException | IllegalArgumentException | RemoteException e){
            vista.mostrarError(e.getMessage());
        }

    }

    public boolean esAnfitrion(){
        try{
            return jugador.equals(juego.getAnfitrion());
        } catch (RemoteException e) {
            vista.mostrarError("Hubo un error de conexion con el servidor: " + e.getMessage());
            return false;
        }

    }

    public int getCantidadRestanteMazo(){
        try{
            return juego.getCantidadCartasMazo();
        } catch (RemoteException e) {
            vista.mostrarError("Hubo un error de conexion con el servidor: " + e.getMessage());
            return 0;
        }
    }

    public IJugador getAnfitrion(){
        try{
            return juego.getAnfitrion();
        }catch (RemoteException e) {
            vista.mostrarError("Hubo un error de conexion con el servidor: " + e.getMessage());
            return null;
        }
    }

    private void resetearPartida(){
        try{
            juego.resetarPartida();
            partidaReseteada = true;
        } catch (RemoteException e) {
            vista.mostrarError("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    public boolean seReseteoLaPartida(){
        return partidaReseteada;
    }

    public void volverAJugar(){
        resetearPartida();
        jugador = null;
        vista.iniciar();
    }


    public void iniciarPartida(){
        try{

            if(jugador.equals(juego.getAnfitrion())){
                IMazo mazo = new Mazo();
                mazo.mezclarMazo();
                juego.inicializarPartida(mazo);
                partidaReseteada = false;
            }
            else{
                vista.mostrarError("Solo el anfitrion (" + juego.getAnfitrion().getNombre() + ") " + "puede comenzar la partida");
            }


        }
        catch (CantidadJugadoresException e){
            vista.mostrarError("Esperando mas jugadores para comenzar la partida...");
        } catch (RemoteException e) {
            vista.mostrarError("Error al iniciar la partida: " + e.getMessage());
        }
        catch (IllegalStateException | IllegalArgumentException e){
            vista.mostrarError(e.getMessage());
        }




    }


}
