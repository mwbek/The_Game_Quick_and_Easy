import Controladores.Controlador;
import Modelos.TheGame;

import Vista.VistaGrafica.VistaGrafica;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Main extends Application {

    TheGame juego;
    Controlador controlador;

    @Override
    public void start(Stage stage) throws IOException {

        juego = new TheGame();
        controlador = new Controlador();

        //controlador.registrarJugador("Ludmila");

        VistaGrafica vistaGrafica = new VistaGrafica(stage, controlador);
        vistaGrafica.iniciar();

    }




    public static void main(String[] args)
    {
        launch(args);

        //TheGame juego = new TheGame();
        //ControladorConsola controladorMartin = new ControladorConsola(juego);
        //ControladorConsola controladorLudmila = new ControladorConsola(juego);

        //IVista vistaMartin = new VistaConsola(controladorMartin);
        //IVista vistaLudmila = new VistaConsola(controladorLudmila);

        //vistaMartin.iniciar();
        //vistaLudmila.iniciar();

    }
}
