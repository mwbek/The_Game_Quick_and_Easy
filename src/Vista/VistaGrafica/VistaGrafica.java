package Vista.VistaGrafica;

import Controladores.Controlador;
import Interfaces.IVista;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;

public class VistaGrafica implements IVista {

    private final Stage stage;
    private final Controlador controlador;

    public VistaGrafica(Stage stage, Controlador controlador){
        this.controlador = controlador;
        this.stage = stage;
        stage.setTitle("The Game");

    }

    //TODO: CUANDO IMPLEMENTE LAS DEMAS FUNCIONES USAR 'Platform.runLater(() -> {Instrucciones});
    @Override
    public void mostrarHUDJugador() {

    }

    @Override
    public void mostrarMensaje(String mensaje) {

    }

    @Override
    public void mostrarError(String mensajeError) {

    }

    @Override
    public void iniciar() {
        MenuPrincipalController controller = cargar("/Vista/VistaGrafica/GUI/menuPrincipal.fxml");
        controller.setControlador(controlador);
    }

    @Override
    public void actualizarVista() {

    }

    @Override
    public void salir() {

    }

    @Override
    public void mostrarMenuJugador() {

    }


    private <T> T cargar(String rutaFxml) {
        try {
            //FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
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
