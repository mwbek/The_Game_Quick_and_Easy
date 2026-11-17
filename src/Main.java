import Controladores.ControladorConsola;
import Interfaces.IMazo;
import Modelos.Jugador;
import Modelos.Mazo;
import Modelos.TheGame;
import Vista.VistaConsola;

public class Main {

    public static void main(String[] args)
    {
        TheGame juego = new TheGame();

        ControladorConsola controladorMartin = new ControladorConsola(juego);
        VistaConsola vistaMartin = new VistaConsola(controladorMartin);

        ControladorConsola controladorLudmila = new ControladorConsola(juego);
        VistaConsola vistaLudmila = new VistaConsola(controladorLudmila);

        juego.agregarObservador(controladorMartin);
        juego.agregarObservador(controladorLudmila);


        controladorMartin.registrarJugador("Martin");
        controladorLudmila.registrarJugador("Ludmila");
        controladorMartin.iniciarPartida();






    }
}
