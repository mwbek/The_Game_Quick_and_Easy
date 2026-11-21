package Vista.VistaGrafica;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MenuPrincipalController extends ControladorGrafico {


    @FXML
    private Button btnUnirsePartida, btnIniciarPartida;
    @FXML
    private Label lbMensaje;
    @FXML
    private TextField nombreJugador;

    @FXML
    private ListView<String> jugadoresEnSala;

    String nombre;




    @FXML
    private void initialize() {

        btnUnirsePartida.setDisable(true);
        btnIniciarPartida.setVisible(false);
        lbMensaje.setVisible(false);

        nombreJugador.textProperty().addListener((obs, oldValue, newValue) -> {
            btnUnirsePartida.setDisable(newValue.trim().isEmpty());
            nombre = nombreJugador.getText();
        });


    }

    public void actualizarListaLobby(){
        jugadoresEnSala.getItems().setAll(controlador.getListaJugadores().stream().map((j) -> j.getNombre()).toList());
    }

    public void actualizarMensajeAnfitrion(){
        if(controlador.esAnfitrion()){
            btnUnirsePartida.setVisible(false);
            btnIniciarPartida.setVisible(true);
            lbMensaje.setText("Sos el anfitrión. Iniciá la partida cuando estén todos listos.");
            btnIniciarPartida.setDisable(false);

        }


    }

    @FXML
    private void onComenzarPartida(){
        if(controlador.esAnfitrion() && controlador.getCantidadJugadores() > 1) {
            controlador.iniciarPartida();
        }
    }

    @FXML
    private void onUnirse(){
        controlador.registrarJugador(nombre);
        nombreJugador.setEditable(false);
        nombreJugador.setDisable(true);
        btnUnirsePartida.setDisable(true);
        lbMensaje.setText("Esperando al anfitrion para comenzar la partida.");
        actualizarMensajeAnfitrion();
        lbMensaje.setVisible(true);

    }

}
