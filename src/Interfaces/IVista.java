package Interfaces;

import Persistencia.EstadisticasJugador;

import java.util.List;

public interface IVista {
    void mostrarHUDJugador();
    void actualizarMenuLobby();
    public void mostrarMensaje(String mensaje);
    public void mostrarError(String mensajeError);
    public void iniciar();
    public void salir();
    void jugadoresGanaron();
    void jugadoresPerdieron();
    void actualizarListaJugadores();
    void actualizarJugadorSalio();
    void jugadoresInsuficientes();
    void mostrarHistorial(List<EstadisticasJugador> estadisticas);
}
