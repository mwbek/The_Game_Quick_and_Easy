package Vista.VistaGrafica;
import Controladores.Controlador;
import Interfaces.ICarta;
import Interfaces.IJugador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;

public class PartidaController  extends ControladorGrafico {


    @FXML
    private VBox contenedorJugador;
    @FXML
    private VBox contenedorPadreJugadores;
    @FXML
    private Label lbCantidadJugadores,lbCartasRestantes, lbEstadoTurno;

    //Botones con imagenes
    @FXML
    private Button btnPilaAscendente, btnPilaDescendente, btnCarta1, btnCarta2;

    //Botones derecha
    @FXML
    private Button btnPasarTurno, btnSalirDelJuego, btnVerReglas, btnVerHistorial;

    @FXML
    private TextArea textAreaLogs;


    private int indiceCarta = -1;
    private int indicePila = -1;

    //Metodo auxiliar
    private String obtenerPathImagen(ICarta carta) {
        return "/Vista/VistaGrafica/Imagenes_cartas/" + carta.getColor().name() + "_" + carta.getNumero() + ".png";
    }



    @Override
    public void setControlador(Controlador controlador){
        super.setControlador(controlador);
    }


    public void onVerHistorial(){
        controlador.verHistorial();
    }


    public void actualizarBarraSuperior()
    {
        if(controlador != null){
            int cantidadJugadores = controlador.getCantidadJugadores();
            int cartasRestantes = controlador.getCantidadRestanteMazo();

            lbCantidadJugadores.setText("Jugadores: " + cantidadJugadores);
            lbCartasRestantes.setText("Cartas restantes: " + cartasRestantes);
        }
    }


    public void actualizarTurno() {
        boolean esMiTurno = controlador.esTurno();

        //Si no mi turno, desabilito todos los botones
        btnCarta1.setDisable(!esMiTurno);
        btnCarta2.setDisable(!esMiTurno);
        btnPilaAscendente.setDisable(!esMiTurno);
        btnPilaDescendente.setDisable(!esMiTurno);
        btnPasarTurno.setDisable(!esMiTurno);

        if(esMiTurno){
            lbEstadoTurno.setText("¡Es tu turno!");
        }else{
            lbEstadoTurno.setText("Turno de: " + controlador.getJugadorActual().getNombre());
        }

        //Esto pinta de amarillo al jugador duenio de cada instancia
        for(Node nodo : contenedorPadreJugadores.getChildren()){
            nodo.getStyleClass().remove("turnoJugador");
            if(controlador.getJugador().getNombre().equals(nodo.getUserData())){
                nodo.getStyleClass().add("turnoJugador");
            }
        }

    }


    public void onSalir(){
        controlador.cerrarApp();
    }

    public void onPasarTurno(){
        controlador.pasarTurno();
    }

    private void setImagenEnBoton(Button boton, String path){


        Image imagen = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));

        ImageView imageView = new ImageView(imagen);
        imageView.setFitWidth(100);
        imageView.setFitHeight(145);
        imageView.setPreserveRatio(true);
        boton.setGraphic(imageView);

    }

    public void actualizarCartasMano(){
        ICarta[] cartas = controlador.getJugador().getCartas();
        Button[] botones = {btnCarta1, btnCarta2};

        //Re inicializo los botones
        for (Button b : botones) {
            b.setGraphic(null);
            b.setUserData(null);
            b.setVisible(false);
        }

        for(int i = 0; i < cartas.length; i++) {
            if(cartas[i] == null) continue;

            String path = obtenerPathImagen(cartas[i]);
            setImagenEnBoton(botones[i], path);
            botones[i].setUserData(i+1); //Le paso un indice porque el controlador juega por indice de carta.
            botones[i].setVisible(true);
        }
    }

    public void actualizarCartasPila() {

        ICarta[] cartas = {controlador.getCartaPilaAscendente(), controlador.getCartaPilaDescendente()};
        Button[] botones = {btnPilaAscendente, btnPilaDescendente};
        String[] pathPorDefecto = {"/Vista/VistaGrafica/Imagenes_cartas/ASCENDENTE.png","/Vista/VistaGrafica/Imagenes_cartas/DESCENDENTE.png"};
        String path;

        //Re inicializo los botones
        for (Button b : botones) {
            b.setGraphic(null);
            b.setUserData(null);
            b.setVisible(false);
        }

        for (int i = 0; i < cartas.length; i++) {

            if(cartas[i] != null) path = obtenerPathImagen(cartas[i]);
            else path = pathPorDefecto[i];

            setImagenEnBoton(botones[i], path);
            botones[i].setUserData(i + 1); //Le paso un indice porque el controlador juega por indice de carta.
            botones[i].setVisible(true);
        }
    }

    @FXML
    private void onCartaSeleccionada(ActionEvent event) {
        Button boton = (Button) event.getSource();
        indiceCarta = (int) boton.getUserData();
    }

    @FXML
    private void onPilaSeleccionada(ActionEvent event) {
        Button boton = (Button) event.getSource();
        indicePila = (int) boton.getUserData();

        //Juego la carta
        if(indiceCarta != -1){
            controlador.jugarCarta(indiceCarta,indicePila);
            indiceCarta = -1;
            indicePila = -1;
        }

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
        Label lbAnfitrion;
        //Le pongo un ID para poder eliminarlo despues segun el jugador que se vaya
        nuevoContenedorHijo.setId(jugador.getId().toString());

        nuevoContenedorHijo.getStyleClass().add("jugador");
        nuevoContenedorHijo.setUserData(jugador.getNombre());

        Label nombre = new Label(jugador.getNombre());
        nombre.getStyleClass().add("nombre-jugador");

        if(jugador.equals(controlador.getAnfitrion())) lbAnfitrion = new Label("ANFITRION");
        else lbAnfitrion = new Label("");

        lbAnfitrion.getStyleClass().add("cartas-jugador");

        VBox.setMargin(nuevoContenedorHijo, new Insets(2,8,2,8));
        nuevoContenedorHijo.getChildren().addAll(nombre, lbAnfitrion);

        contenedorJugador.getChildren().add(nuevoContenedorHijo);

        return nuevoContenedorHijo;
    }


    public void imprimirMensaje(String mensaje){
        textAreaLogs.appendText(mensaje);
        textAreaLogs.appendText("\n");
    }


}
