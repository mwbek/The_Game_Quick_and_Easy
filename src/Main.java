import Controladores.ControladorConsola;
import Interfaces.IVista;
import Modelos.TheGame;
import Vista.VistaConsola;

import Vista.VistaGrafica.MenuPrincipalController;
import Vista.VistaGrafica.VistaGrafica;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Main extends Application {

    TheGame juego;
    ControladorConsola controlador;

    @Override
    public void start(Stage stage) throws IOException {

        juego = new TheGame();
        controlador = new ControladorConsola(juego);

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
