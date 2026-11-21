package Vista.VistaGrafica;

import Controladores.Controlador;
import Interfaces.IVista;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;

public class VistaGrafica implements IVista {

    private final Stage stage;
    private final Controlador controlador;
    MenuPrincipalController menuPrincipalController;
    PartidaController partidaController;
    MenuFinDePartidaController menuFinDePartidaController;

    public VistaGrafica(Stage stage, Controlador controlador){
        this.controlador = controlador;
        this.stage = stage;
    }

    @Override
    public void mostrarHUDJugador() {

        Platform.runLater(() ->{
            partidaController = cargar("/Vista/VistaGrafica/GUI/partida.fxml");
            partidaController.setControlador(controlador);

            stage.setTitle("THE GAME");
        });


    }

    @Override
    public void mostrarMensaje(String mensaje) {

    }

    @Override
    public void mostrarError(String mensajeError) {
        Platform.runLater(() -> {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(stage);      //Este metodo hace que se muestre encima de la ventana padre (stage)
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText(mensajeError);
            alerta.showAndWait();
        });
    }
    @Override
    public void iniciar() {

        stage.setOnCloseRequest(e -> this.salir());
        menuPrincipalController = cargar("/Vista/VistaGrafica/GUI/menuPrincipal.fxml");
        menuPrincipalController.setControlador(controlador);
        stage.setTitle("THE GAME - Registro de jugador");

        Platform.runLater(() ->{
            menuPrincipalController.actualizarListaLobby();
        });



    }

    @Override
    public void salir() {
        controlador.cerrarApp();
    }

    @Override
    public void turnoDelJugador() {

    }

    @Override
    public void jugadoresGanaron() {
        Platform.runLater(() ->{
            menuFinDePartidaController = cargar("/Vista/VistaGrafica/GUI/partida.fxml");
            menuFinDePartidaController.setControlador(controlador);
            menuFinDePartidaController.setMensaje("PARTIDA TERMINADA: LOS JUGADORES GANARON");
        });

    }

    @Override
    public void jugadoresPerdieron() {
        Platform.runLater(() ->{
            menuFinDePartidaController = cargar("/Vista/VistaGrafica/GUI/partida.fxml");
            menuFinDePartidaController.setControlador(controlador);
            menuFinDePartidaController.setMensaje("PARTIDA TERMINADA: LOS JUGADORES PERDIERON");
        });

    }

    @Override
    public void jugadoresInsuficientes() {
        Platform.runLater(() ->{
            menuFinDePartidaController = cargar("/Vista/VistaGrafica/GUI/menuFinDePartida.fxml");
            menuFinDePartidaController.setControlador(controlador);
            menuFinDePartidaController.setMensaje("PARTIDA TERMINADA: Cantidad de jugadores insuficiente");
        });
    }


    @Override
    public void actualizarListaJugadores() {
        Platform.runLater(() -> {
            if(menuPrincipalController != null && !controlador.seInicioLaPartida()){
                menuPrincipalController.actualizarListaLobby();
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
            }
        });

    }


    private <T> T cargar(String rutaFxml) {
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
