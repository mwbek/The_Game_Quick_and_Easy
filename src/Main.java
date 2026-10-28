import Controladores.ControladorComenzarPartida;
import Controladores.ControladorJugarTurno;
import Vista.Consola.VistaManoJugador;
import Vista.Consola.VistaMenuPrincipal;
import Modelos.Partida;

public class Main {

    public static void main(String[] args)
    {
        ControladorComenzarPartida controladorComenzarPartida = new ControladorComenzarPartida(new VistaMenuPrincipal());
        Partida partida = controladorComenzarPartida.comenzarPartida();

        if(partida == null) return;

        ControladorJugarTurno controladorJugarTurno = new ControladorJugarTurno(partida, new VistaManoJugador());


        controladorJugarTurno.jugarMano();


    }
}
