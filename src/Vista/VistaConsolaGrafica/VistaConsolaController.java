package Vista.VistaConsolaGrafica;

import Controladores.Controlador;
import Interfaces.IJugador;
import Interfaces.IVista;
import Vista.ControladorGrafico;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.util.List;

public class VistaConsolaController extends ControladorGrafico {

    @FXML
    private TextArea areaTextoMensajes;
    @FXML
    private ListView<String> jugadoresEnSala;
    @FXML
    private TextField inputTexto;
    @FXML
    private Button btnEnviar;

    private int indiceCarta = -1;
    private int indicePila = -1;
    String inputRecibido;

    private VistaConsolaGrafica vista;

    @Override
    public void setControlador(Controlador controlador){
        super.setControlador(controlador);
    }

    public void setVista(VistaConsolaGrafica vista){
        this.vista = vista;
    }

    @FXML
    public void initialize(){
        inputTexto.setOnAction(event -> {
            onEnviar(event);
        });
    }

    public void agregarMensaje(String mensaje){
        areaTextoMensajes.appendText(mensaje + "\n");
    }

    public void actualizarListaLobby(){

        IJugador anfitrion = controlador.getAnfitrion();

        List<String> nombres = controlador.getListaJugadores().stream().map(j -> {
            if (j.equals(anfitrion)) {
                return j.getNombre() + " (Anfitrión)";
            } else {
                return j.getNombre();
            }
        }).toList();

        jugadoresEnSala.getItems().setAll(nombres);
    }

    @FXML
    public void onEnviar(ActionEvent event) {
        String input = inputTexto.getText().trim();
        inputTexto.clear();
        if (input.isEmpty() || vista == null) return;
        vista.procesarInput(input); //Le paso el input a la vista y que ella se arregle
    }

}
