package Interfaces;

import Enumerados.CartaDeIndicacion;
import Enumerados.Eventos;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface ITheGame extends IObservableRemoto {

    public IJugador agregarJugador(String nombre) throws RemoteException;
    public void inicializarPartida(IMazo mazo) throws RemoteException;
    public void pasarDeRonda(IJugador jugadorTurno) throws RemoteException;
    public IJugador getJugadorActual() throws RemoteException;
    public IJugador[] getJugadores() throws RemoteException;
    public Eventos getEstadoPartida() throws RemoteException;
    public void jugarCarta(IJugador jugadorTurno, CartaDeIndicacion tipoPila, int indiceCarta) throws RemoteException;
    public int getRondaActual() throws RemoteException;
    public int getCantidadCartasMazo() throws RemoteException;
    public ICarta getUltimaCartaAscendente() throws RemoteException;
    public ICarta getUltimaCartaDescendente() throws RemoteException;
    public IJugador getJugador(UUID id) throws RemoteException;
    public IJugador getAnfitrion() throws RemoteException;
    public IJugador getUltimoJugadorAgregado() throws RemoteException;
    public void cerrar(IObservadorRemoto controlador, IJugador jugador) throws RemoteException;
    public IJugador getUltimoJugadorQueSalio() throws RemoteException;
    public void resetarPartida() throws RemoteException;
    void guardarPartida(IJugador jugador) throws RemoteException;

}
