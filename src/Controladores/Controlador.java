package Controladores;

import Enumerados.CartaDeIndicacion;
import Enumerados.Eventos;
import Interfaces.*;
import Modelos.*;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import exceptions.CantidadJugadoresException;
import exceptions.CartaNoExisteException;
import exceptions.MovimientoInvalidoException;
import exceptions.PartidaIniciadaException;

import java.rmi.RemoteException;

import static Enumerados.Eventos.JUGANDO;
import static Enumerados.Eventos.SIN_INICIAR;

public class Controlador implements IControladorRemoto {

    private IVista vista;
    private ITheGame juego;
    private  Jugador jugador;

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
            switch (ev)
            {
                case INICIO_PARTIDA, CAMBIO_DE_RONDA,CARTA_JUGADA:
                    try {

                        if(!partidaPuedeComenzar()){
                            vista.mostrarMensaje("Esperando jugadores para comenzar partida...");
                        }
                        else{
                            if(juego.getEstadoPartida() == JUGANDO){
                                vista.mostrarHUDJugador();
                                if(juego.getJugadorActual() == jugador) vista.mostrarMensaje("Es tu turno...\n");
                                else vista.mostrarMensaje("Esperando turno de: " + getJugadorActual().getNombre() + "\n");
                            }

                        }

                    } catch (RemoteException e) {
                        vista.mostrarError("Error en la jugada: " + e.getMessage());
                    }
                    break;
                case NUEVO_JUGADOR:

                    try {

                        if(!partidaPuedeComenzar()){
                            vista.mostrarMensaje("Esperando jugadores para comenzar partida...");
                        }
                        else{
                            if(juego.getEstadoPartida() == JUGANDO){
                                vista.mostrarHUDJugador();
                                if(juego.getJugadorActual() != jugador){
                                    vista.mostrarMensaje(jugador.getNombre());
                                }
                            }

                        }


                    } catch (RemoteException e) {
                        vista.mostrarError("Error al agregar nuevo jugador: " + e.getMessage());
                    }

                    break;
                case GANARON:
                    //Hacer algo
                    break;
                case PERDIERON:
                    //Hacer algo
                    break;


            }
        }

    }


    public void setVista(IVista vista) {
        //Programacion defensiva
        if(vista == null) throw new IllegalArgumentException("La vista no puede ser null");
        this.vista = vista;
    }

    public void registrarJugador(String nombre){
        try {
            if (nombre == null) throw new IllegalArgumentException("El nombre del jugador no puede ser nulo");
            this.jugador = juego.agregarJugador(nombre);
        } catch (RemoteException e) {
            vista.mostrarError("Error al registrar nuevo jugador: " + e.getMessage());
        }
    }

    public Jugador getJugador(){
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
            if(juego.getEstadoPartida() == SIN_INICIAR) return false;
            return true;
        }catch (RemoteException e) {
            vista.mostrarError("Error al obtener el estado de la partida: " + e.getMessage());
            return true;
        }

    }

    public boolean partidaPuedeComenzar(){
        try{
            if(juego.getJugadores().length > 1){
                return true;
            }
            return false;
        } catch (RemoteException e) {
            vista.mostrarError("Error al comenzar partida: " + e.getMessage());
            return false;
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

    public String[] getCartas(){
        String[] cartasTexto = {"[]", "[]"};
        try {
            Jugador jugadorVista = juego.getJugador(jugador.getId());
            if (jugadorVista != null) {
                ICarta[] cartas = jugadorVista.getCartas();
                for (int i = 0; i < cartas.length && i < cartasTexto.length; i++) {
                    cartasTexto[i] = cartas[i].toString();
                }
            }
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener la mano del jugador : " +  e.getMessage());
        }
        return cartasTexto;
    }

    public String getCartaPilaAscendente(){
        try{
            ICarta carta = juego.getUltimaCartaAscendente();
            if(carta == null) return "[]";
            return carta.toString();
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener ultima carta ascendente: " + e.getMessage());
            return "[]";
        }

    }

    public String getCartaPilaDescendente(){
        try{
            ICarta carta = juego.getUltimaCartaDescendente();
            if(carta == null) return "[]";
            return carta.toString();
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener ultima carta descendente: " + e.getMessage());
            return "[]";
        }
    }


    public void jugarCarta(int indiceCarta, int indicePila){
        //TODO: Agregar programacion defensiva/Validaciones a los parametros
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


    public void iniciarPartida(){
        try{
            IMazo mazo = new Mazo();
            mazo.mezclarMazo();
            juego.inicializarPartida(mazo);
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
