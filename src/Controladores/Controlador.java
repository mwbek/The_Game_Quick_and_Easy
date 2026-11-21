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
                if(jugador == null) this.jugador = juego.getUltimoJugadorAgregado();
                else this.jugador = juego.getJugador(jugador.getId());
            } catch (RemoteException e) {
                vista.mostrarError("No se pudo actualizar el jugador: " + e.getMessage() );
            }

            switch (ev)
            {
                case INICIO_PARTIDA:
                    try {
                            if(juego.getEstadoPartida() == JUGANDO){
                                vista.mostrarHUDJugador();
                                if(esTurno()) vista.turnoDelJugador();
                                else vista.mostrarMensaje("Esperando turno de [" + getJugadorActual().getNombre() + "]");
                            }
                    } catch (RemoteException e) {
                        vista.mostrarError("Error al iniciar partida: " + e.getMessage());
                    }
                    break;

                case CARTA_JUGADA:
                        //Actualizo el HUD a todos los jugadores
                        vista.mostrarHUDJugador();
                        if(esTurno()) vista.turnoDelJugador();
                    break;

                case CAMBIO_DE_RONDA:
                    try{
                        if(juego.getEstadoPartida() == JUGANDO){
                            vista.mostrarHUDJugador();
                            if(esTurno()) vista.turnoDelJugador();
                        }
                    }catch (RemoteException e){
                        vista.mostrarError("Error en la jugada: " + e.getMessage());
                    }
                    break;

                case NUEVO_JUGADOR:
                    try {
                        System.out.println("[DEBUG] anfitrion = " + juego.getAnfitrion().getNombre());
                        vista.actualizarListaJugadores();

                        //Si el jugador no es el anfitrion
                        if(!jugador.equals(juego.getAnfitrion())){
                            vista.mostrarMensaje("Esperando a que el anfitrion (" + juego.getAnfitrion().getNombre() + ") inicie la partida...");
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

                        //Para la interfaz grafica
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


            }
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
            } catch (RemoteException e) {
                // Si el servidor cerró o falló la red, ignoramos para poder terminar
            }
        }
        System.exit(0);
    }

    public ObservableList<IJugador> getListaJugadores(){
        try{
            List<IJugador> jugadores = Arrays.stream(juego.getJugadores()).toList();
            ObservableList<IJugador> listaJugadores = FXCollections.observableList(jugadores);
            return  listaJugadores;

        } catch (RemoteException e) {
            vista.mostrarError("No se pudo obtener la lista de jugadores: " + e.getMessage());
            return null;
        }

    }


    public void registrarJugador(String nombre){
        try {
            if (nombre == null) throw new IllegalArgumentException("El nombre del jugador no puede ser nulo");
            this.jugador = juego.agregarJugador(nombre);
            int cantidadJugadores = juego.getJugadores().length;
            vista.mostrarMensaje("[" + juego.getJugadores()[cantidadJugadores-1].getNombre() + "] se unio a la partida:  (" + cantidadJugadores + "/ 5)");


        } catch (RemoteException e) {
            vista.mostrarError("Error al registrar nuevo jugador: " + e.getMessage());
        }
        catch (CantidadJugadoresException | PartidaIniciadaException e){
            vista.mostrarMensaje(e.getMessage());
            vista.mostrarMensaje("Saliendo de la partida...");
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
        // IJugador jugadorVista = juego.getJugador(jugador.getId());
        if (jugador != null) {
            ICarta[] cartas = jugador.getCartas();
            for (int i = 0; i < cartas.length && i < cartasTexto.length; i++) {
                cartasTexto[i] = cartas[i].toString();
            }
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
            vista.turnoDelJugador(); //Si hay un error, por movimiento invalido u otro tipo, vuelve a pedirle un input al jugador
        }
    }

    public void pasarTurno(){
        try{
            if(jugador == null) throw new IllegalArgumentException("Jugador no puede ser nulo");
            juego.pasarDeRonda(jugador);
        }
        catch (MovimientoInvalidoException | IllegalArgumentException | RemoteException e){
            vista.mostrarError(e.getMessage());
            vista.turnoDelJugador();
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

    public void reiniciarPartida(){
        try{
            juego.resetarPartida();
        }catch (RemoteException e) {
            vista.mostrarError("Hubo un error de conexion con el servidor: " + e.getMessage());
        }
    }


    public void iniciarPartida(){
        try{

            if(jugador.equals(juego.getAnfitrion())){
                IMazo mazo = new Mazo();
                mazo.mezclarMazo();
                juego.inicializarPartida(mazo);
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
