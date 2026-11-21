package Interfaces;

public interface IVista {
    void mostrarHUDJugador();
    public void mostrarMensaje(String mensaje);
    public void mostrarError(String mensajeError);
    public void iniciar();
    public void salir();
    void jugadoresGanaron();
    void jugadoresPerdieron();
    void actualizarListaJugadores();
    void actualizarJugadorSalio();
    void jugadoresInsuficientes();
}
