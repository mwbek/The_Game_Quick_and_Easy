package Vista.VistaGrafica;

import Controladores.Controlador;
import Interfaces.IVista;
import Persistencia.EstadisticasJugador;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class VistaGrafica implements IVista {

    private final Stage stagePrincipal;
    private Stage stageHistorial;
    private final Controlador controlador;
    MenuPrincipalController menuPrincipalController;
    PartidaController partidaController;
    MenuFinDePartidaController menuFinDePartidaController;
    HistorialController historialController;

    public VistaGrafica(Stage stage, Controlador controlador){
        this.controlador = controlador;
        this.stagePrincipal = stage;
    }

    @Override
    public void mostrarHUDJugador() {

        Platform.runLater(() -> {
            //Cargo la vista una sola vez, despues voy actualizando
            if (partidaController == null) {
                partidaController = cargar(stagePrincipal,"/Vista/VistaGrafica/GUI/partida.fxml");
                partidaController.setControlador(controlador);
                stagePrincipal.setTitle("THE GAME");
            }

            partidaController.actualizarListaDeJugadores();
            partidaController.actualizarCartasMano();
            partidaController.actualizarCartasPila();
            partidaController.actualizarTurno();
            partidaController.actualizarBarraSuperior();
        });
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        Platform.runLater(() -> {
            if(partidaController != null)  partidaController.imprimirMensaje(mensaje);
        });
    }


    @Override
    public void mostrarError(String mensajeError) {
        Platform.runLater(() -> {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(stagePrincipal);      //Este metodo hace que se muestre encima de la ventana padre (stage)
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText(mensajeError);
            alerta.showAndWait();
        });
    }
    @Override
    public void iniciar() {

        stagePrincipal.setOnCloseRequest(e -> this.salir());
        menuPrincipalController = cargar(stagePrincipal,"/Vista/VistaGrafica/GUI/menuPrincipal.fxml");
        menuPrincipalController.setControlador(controlador);
        stagePrincipal.setTitle("THE GAME - Registro de jugador");

        Platform.runLater(() ->{
            menuPrincipalController.actualizarListaLobby();
        });



    }

    @Override
    public void salir() {
        //Cierro la ventana del historial si quedo abierta
        if(stageHistorial != null){
            stageHistorial.close();
        }
        controlador.cerrarApp();
    }

    @Override
    public void jugadoresGanaron() {
        Platform.runLater(() ->{
            menuFinDePartidaController = cargar(stagePrincipal,"/Vista/VistaGrafica/GUI/menuFinDePartida.fxml");
            menuFinDePartidaController.setControlador(controlador);
            menuFinDePartidaController.setMensaje("PARTIDA TERMINADA: LOS JUGADORES GANARON");
        });

    }

    @Override
    public void jugadoresPerdieron() {
        Platform.runLater(() ->{
            menuFinDePartidaController = cargar(stagePrincipal,"/Vista/VistaGrafica/GUI/menuFinDePartida.fxml");
            menuFinDePartidaController.setControlador(controlador);
            menuFinDePartidaController.setMensaje("PARTIDA TERMINADA: LOS JUGADORES PERDIERON");
        });

    }

    @Override
    public void jugadoresInsuficientes() {
        Platform.runLater(() ->{
            menuFinDePartidaController = cargar(stagePrincipal,"/Vista/VistaGrafica/GUI/menuFinDePartida.fxml");
            menuFinDePartidaController.setControlador(controlador);
            menuFinDePartidaController.setMensaje("PARTIDA TERMINADA: Cantidad de jugadores insuficiente");
        });
    }

    @Override
    public void mostrarHistorial(List<EstadisticasJugador> estadisticas) {
        if(stageHistorial != null){
            stageHistorial.close();
        }
        stageHistorial = new Stage();
        Platform.runLater(() ->{
            historialController = cargar(stageHistorial,"/Vista/VistaGrafica/GUI/historial.fxml");

            historialController.setControlador(controlador);
            historialController.mostrarHistorial(estadisticas);
        });
    }


    @Override
    public void actualizarListaJugadores() {
        Platform.runLater(() -> {
            if(menuPrincipalController != null && !controlador.seInicioLaPartida()){
                menuPrincipalController.actualizarListaLobby();
            }
            if(partidaController != null && controlador.seInicioLaPartida()){
                partidaController.actualizarTurno();
            }
        });
    }

    @Override
    public void actualizarJugadorSalio() {
        //En caso que haya salido el anfitrion, se actualiza el mensaje tambien
        Platform.runLater(() ->{
            menuPrincipalController.actualizarMensajeAnfitrion();

            if(partidaController != null  && controlador.seInicioLaPartida()){
                partidaController.actualizarListaDeJugadores();
                partidaController.actualizarBarraSuperior();
            }
        });

    }


    private <T> T cargar(Stage stage, String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
            return loader.getController();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar " + rutaFxml, e);
        }
    }
}
