package Interfaces;

import Modelos.Jugador;

public interface IVista {
    void mostrarHUDJugador();
    public void mostrarMensaje(String mensaje);
    public void mostrarError(String mensajeError);
    public void iniciar();
    public void actualizarVista();
}
