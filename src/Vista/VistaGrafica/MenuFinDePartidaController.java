package Vista.VistaGrafica;

import Controladores.Controlador;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MenuFinDePartidaController extends ControladorGrafico {


    @FXML
    private Label lbFinPartida;

    @Override
    public void setControlador(Controlador controlador) {
        super.setControlador(controlador);
    }




    public void setMensaje(String mensaje){
        lbFinPartida.setText(mensaje);
    }

    public void onSalir(){
        controlador.cerrarApp();;
    }

    public void onVolverAJugar(){
        //Puedo volver a iniciar la partida porque el reseteo del juego se hace en cada evento que finalice el juego
        controlador.volverAJugar();
    }
}
