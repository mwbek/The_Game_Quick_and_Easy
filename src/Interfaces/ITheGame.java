package Interfaces;

import Enumerados.CartaDeIndicacion;
import Enumerados.Eventos;
import Modelos.Carta;
import Modelos.Jugador;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface ITheGame extends IObservableRemoto {

    Jugador agregarJugador(String nombre) throws RemoteException;
    public void inicializarPartida(IMazo mazo) throws RemoteException;
    public void pasarDeRonda(IJugador jugador) throws RemoteException;
    public IJugador getJugadorActual() throws RemoteException;
    public IJugador[] getJugadores() throws RemoteException;
    public Eventos getEstadoPartida() throws RemoteException;
    public void jugarCarta(Jugador jugador, CartaDeIndicacion tipoPila, int indiceCarta) throws RemoteException;
    public int getRondaActual() throws RemoteException;
    public int getCantidadCartasMazo() throws RemoteException;
    public ICarta getUltimaCartaAscendente() throws RemoteException;
    public ICarta getUltimaCartaDescendente() throws RemoteException;
    public void chequearEstadoPartida() throws RemoteException;
    public Jugador getJugador(UUID id) throws RemoteException;
}
