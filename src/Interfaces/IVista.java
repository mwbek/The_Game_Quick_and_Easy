package Interfaces;

import Modelos.Jugador;

public interface IVista {
    int mostrarMenuPrincipal();
    void mostrarHUDJugador();
    public void mostrarMensaje(String mensaje);
    public void mostrarMenuTurno();
}
