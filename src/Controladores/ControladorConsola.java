package Controladores;

import Enumerados.CartaDeIndicacion;
import Enumerados.Eventos;
import Interfaces.IJugador;
import Interfaces.IMazo;
import Modelos.*;
import Observer.IObservable;
import Observer.IObservador;
import Interfaces.IVista;

public class ControladorConsola implements IObservador {

    private IVista vista;
    private final TheGame juego;
    private  Jugador jugador;

    public ControladorConsola(TheGame juego){
        if(juego == null) throw new IllegalArgumentException("El juego no puede ser nulo");
        this.juego = juego;
    }

    public void registrarJugador(String nombre){
        if(nombre == null) throw new IllegalArgumentException("El nombre del jugador no puede ser nulo");
        this.jugador = juego.agregarJugador(nombre);
    }

    public Jugador getJugador(){
        return this.jugador;
    }

    public void setVista(IVista vista) {
        //Programacion defensiva
        this.vista = vista;
    }

    public IJugador getJugadorActual(){
        return juego.getJugadorActual();
    }


    public int getRondaActual(){
        return juego.getRondaActual();
    }

    public Carta[] getCartas(){
        return jugador.getCartas();
    }

    public String getCartaPilaAscendente(){

         //Carta carta = juego.getPilaAscendente().getUltimaCarta();
         //if(carta == null) return "[]";
         //return carta.toString();
    }

    public String getCartaPilaDescendente(){
        //Carta carta = juego.getPilaDescendente().getUltimaCarta();
        //if(carta == null) return "[]";
        //return carta.toString();
    }


    public void jugarCarta(int indiceCarta, int indicePila){

        PilaDeIndicacion pilaAJugar = null;
        CartaDeIndicacion tipoDePila;
        boolean manoJugada = false;
        if(indicePila == 1) manoJugada = juego.jugarMano(jugador.getCartas()[indiceCarta], CartaDeIndicacion.ASCENDENTE);
        else manoJugada = juego.jugarMano(jugador.getCartas()[indiceCarta], CartaDeIndicacion.DESCENDENTE);

    }


    public void iniciarPartida(){
        IMazo mazo = new Mazo();
        mazo.mezclarMazo();
        juego.inicializarPartida(mazo);

    }

    @Override
    public void actualizar(IObservable o, Object e) {
        Eventos evento = (Eventos) e;
        switch (evento)
        {
            case INICIO_PARTIDA:
                vista.mostrarHUDJugador();
                if(juego.getJugadorActual() == jugador){
                    vista.mostrarMenuTurno();
                }
                else{
                    vista.mostrarMensaje("Esperando turno de: " + getJugadorActual().getNombre());
                }
                break;
            case NUEVO_JUGADOR:
                if(juego.getJugadorActual() != jugador){
                    vista.mostrarMensaje("Se agrego un nuevo jugador");
                }

                break;
            case CAMBIO_DE_RONDA:
                if(juego.getJugadorActual() == jugador){
                    vista.mostrarMenuTurno();
                }
                else{
                    vista.mostrarMensaje("Esperando turno de: " + getJugadorActual().getNombre());
                }
                break;

            case CARTA_JUGADA:
                if(juego.getJugadorActual() != jugador){
                    vista.mostrarMensaje("Se jugo la carta");
                }
                break;
        }
    }
}
