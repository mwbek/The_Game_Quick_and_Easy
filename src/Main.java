import Controladores.ControladorConsola;
import Interfaces.IMazo;
import Interfaces.IVista;
import Modelos.Jugador;
import Modelos.Mazo;
import Modelos.TheGame;
import Vista.VistaConsola;

public class Main {

    public static void main(String[] args)
    {
        TheGame juego = new TheGame();

        ControladorConsola controladorMartin = new ControladorConsola(juego);
        ControladorConsola controladorLudmila = new ControladorConsola(juego);

        IVista vistaMartin = new VistaConsola(controladorMartin);
        IVista vistaLudmila = new VistaConsola(controladorLudmila);

        vistaMartin.iniciar();
        vistaLudmila.iniciar();






    }
}
