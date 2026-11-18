package Controladores;

import Enumerados.CartaDeIndicacion;
import Enumerados.Eventos;
import Interfaces.IJugador;
import Interfaces.IMazo;
import Modelos.*;
import Observer.IObservable;
import Observer.IObservador;
import Interfaces.IVista;
import exceptions.CantidadJugadoresException;
import exceptions.CartaNoExisteException;
import exceptions.MovimientoInvalidoException;
import exceptions.PartidaIniciadaException;

public class ControladorConsola implements IObservador {

    private IVista vista;
    private final TheGame juego;
    private  Jugador jugador;

    public ControladorConsola(TheGame juego){
        if(juego == null) throw new IllegalArgumentException("El juego no puede ser nulo");
        this.juego = juego;
        this.juego.agregarObservador(this);
    }

    @Override
    public void actualizar(IObservable o, Object e) {
        Eventos evento = (Eventos) e;
        switch (evento)
        {
            case INICIO_PARTIDA, CAMBIO_DE_RONDA,CARTA_JUGADA:
                vista.mostrarHUDJugador();
                if(juego.getJugadorActual() == jugador) vista.mostrarMensaje("Es tu turno...\n");
                else vista.mostrarMensaje("Esperando turno de: " + getJugadorActual().getNombre() + "\n");
                break;
            case NUEVO_JUGADOR:
                if(juego.getJugadorActual() != jugador){
                    vista.mostrarMensaje("Se agrego un nuevo jugador");
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


    public void setVista(IVista vista) {
        //Programacion defensiva
        if(vista == null) throw new IllegalArgumentException("La vista no puede ser null");
        this.vista = vista;
    }

    public void registrarJugador(String nombre){
        if(nombre == null) throw new IllegalArgumentException("El nombre del jugador no puede ser nulo");
        this.jugador = juego.agregarJugador(nombre);
    }

    public Jugador getJugador(){
        return this.jugador;
    }


    public IJugador getJugadorActual(){
        return juego.getJugadorActual();
    }

    public boolean partidaPuedeComenzar(){
        if(juego.getJugadores().length > 1){
            return true;
        }
        return false;
    }

    public int getRondaActual(){
        return juego.getRondaActual();
    }

    public String[] getCartas(){

        Carta[] cartas = jugador.getCartas();
        String[] cartasTexto = {"[]", "[]"};

        for(int i = 0; i < cartas.length; i++){
            cartasTexto[i] = cartas[i].toString();
        }

        return cartasTexto;
    }

    public String getCartaPilaAscendente(){

        Carta carta = juego.getUltimaCartaAscendente();
        if(carta == null) return "[]";
        return carta.toString();
    }

    public String getCartaPilaDescendente(){
        Carta carta = juego.getUltimaCartaDescendente();
        if(carta == null) return "[]";
        return carta.toString();
    }


    public void jugarCarta(int indiceCarta, int indicePila){
        //TODO: Agregar programacion defensiva/Validaciones a los parametros
        try{
            indiceCarta--; //Resto uno al indice de carta porque se pasa 1 y 2
            if (indicePila == 1)juego.jugarCarta(jugador,CartaDeIndicacion.ASCENDENTE, indiceCarta);
            else juego.jugarCarta(jugador,CartaDeIndicacion.DESCENDENTE, indiceCarta);
        }
        catch (CartaNoExisteException | MovimientoInvalidoException | PartidaIniciadaException e){
            vista.mostrarError(e.getMessage());
        }
    }

    public void pasarTurno(){
        try{
            if(jugador == null) throw new IllegalArgumentException("Jugador no puede ser nulo");
            juego.pasarDeRonda(jugador);
        }
        catch (MovimientoInvalidoException | IllegalArgumentException e){
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
        }
        catch (IllegalStateException | IllegalArgumentException e){
            vista.mostrarError(e.getMessage());
        }



    }


}
