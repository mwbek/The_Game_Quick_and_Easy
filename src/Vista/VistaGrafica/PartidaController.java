package Vista.VistaGrafica;
import Controladores.Controlador;
import Interfaces.IJugador;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class PartidaController  extends ControladorGrafico {


    @FXML
    private VBox contenedorJugador;
    @FXML
    private VBox contenedorPadreJugadores;

    @FXML
    private Label lbCantidadJugadores,lbCartasRestantes;



    @Override
    public void setControlador(Controlador controlador){
        super.setControlador(controlador);
        inicializar();
    }

    @FXML
    private void initialize() {
    }


    private void inicializar(){
        int cantidadJugadores = controlador.getCantidadJugadores();
        int cartasRestantes = controlador.getCantidadRestanteMazo();

        lbCantidadJugadores.setText("Jugadores: " + cantidadJugadores);
        lbCartasRestantes.setText("Cartas restantes: " + cartasRestantes);

        actualizarListaDeJugadores();

    }

    public void actualizarListaDeJugadores(){
        contenedorPadreJugadores.getChildren().clear();

        List<IJugador> jugadores = controlador.getListaJugadores();

        jugadores.forEach((j) ->{
            contenedorPadreJugadores.getChildren().add(agregarJugador(j));
        });
    }


    private VBox agregarJugador(IJugador jugador) {
        VBox nuevoContenedorHijo = new VBox();

        //Le pongo un ID para poder eliminarlo despues segun el jugador que se vaya
        nuevoContenedorHijo.setId(jugador.getId().toString());

        nuevoContenedorHijo.getStyleClass().add("jugador");

        Label nombre = new Label(jugador.getNombre());
        nombre.getStyleClass().add("nombre-jugador");

        Label cartas = new Label("Cartas: " + jugador.getCantidadCartasEnMano());
        cartas.getStyleClass().add("cartas-jugador");

        VBox.setMargin(nuevoContenedorHijo, new Insets(2,8,2,8));
        nuevoContenedorHijo.getChildren().addAll(nombre, cartas);

        contenedorJugador.getChildren().add(nuevoContenedorHijo);

        return nuevoContenedorHijo;
    }

}
