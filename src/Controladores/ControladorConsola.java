package Controladores;

import Enumerados.Eventos;
import Modelos.TheGame;
import Observer.IObservable;
import Observer.IObservador;
import Interfaces.IVista;

public class ControladorConsola implements IObservador {

    private IVista vista;
    private final TheGame juego;

    public ControladorConsola(TheGame juego){
        if(juego == null) throw new IllegalArgumentException("El juego no puede ser nulo");
        this.juego = juego;


    }

    public void setVista(IVista vista) {
        //Programacion defensiva
        this.vista = vista;
    }

    public void iniciarPartida(){

        int respuesta;
        respuesta= vista.mostrarMenuPrincipal();
        if(respuesta == 1){

        }

    }

    @Override
    public void actualizar(IObservable o, Object e) {
        Eventos evento = (Eventos) e;
        switch (evento)
        {
            case INICIO_PARTIDA:
                this.vista.mostrarMenuPrincipal();
                break;
            case CAMBIO_LISTA_USUARIOS:
                this.vista.mostrarListaUsuario(this.chat.getUsuarios());
                break;
        }
    }

    //    public void jugarMano()
//    {
//        jugador = partida.getJugadorturnoActual();
//
//        Carta[] cartasEnMano = jugador.getCartas();
//        Carta cartaEnJuego;
//
//        vistaManoJugador.mostrarHUDJugador(partida);
//
//        int respuesta = vistaManoJugador.solicitarCartaAJugar();
//
//        //Valido jugada
//        if(respuesta == 1 || respuesta == 2) {
//            cartaEnJuego = cartasEnMano[respuesta-1];
//            vistaManoJugador.mostrarMensaje("Elegiste la carta [" + respuesta + "]: " + cartaEnJuego);
//        }
//        //Si el jugador puso al menos una carta, puede pasar. Caso contrario debe elegir una si o si
//        else if (respuesta == 3) {
//            if (cartasEnMano.length == 2) throw new MovimientoInvalidoException("Para pasar de turno se debe jugar al menos una carta");
//            else throw new JugadorPasaTurnoException(); //No la controlo para que pase sola al metodo que la llame
//        }
//      }

}
