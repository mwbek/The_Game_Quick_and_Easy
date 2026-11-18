package Vista.VistaGrafica;

import Controladores.ControladorConsola;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MenuPrincipalController {


    @FXML
    private Button btnUnirsePartida;
    @FXML
    private Label lbUniendose;
    @FXML
    private TextField nombreJugador;

    private ControladorConsola controlador;
    String nombre;


    public void setControlador(ControladorConsola controlador){
        if(controlador == null) throw new IllegalArgumentException("El controlador no puede ser nulo");
        this.controlador = controlador;
    }

    @FXML
    private void initialize() {

        btnUnirsePartida.setDisable(true);
        lbUniendose.setVisible(false);

        nombreJugador.textProperty().addListener((obs, oldValue, newValue) -> {
            btnUnirsePartida.setDisable(newValue.trim().isEmpty());
            nombre = nombreJugador.getText();
        });
    }

//    @FXML
//    private void onNombre(){
//        nombre = nombreJugador.getText();
//        if(nombre != null && !nombre.isEmpty()){
//            btnUnirsePartida.setDisable(false);
//        }
//    }

    @FXML
    private void onUnirse(){
        controlador.registrarJugador(nombre);
        if(controlador.partidaPuedeComenzar()) {
            controlador.iniciarPartida();
        }
        else lbUniendose.setVisible(true);

        nombreJugador.setEditable(false);
        nombreJugador.setDisable(true);
    }

}
