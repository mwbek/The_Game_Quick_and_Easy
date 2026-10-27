import Controladores.ControladorComenzarPartida;
import Controladores.ControladorJugarTurno;
import Controladores.Juego_Eliminar;
import Vista.Consola.VistaManoJugador;
import Vista.Consola.VistaMenuPrincipal;
import models.Jugador;
import models.Partida;

import java.util.ArrayList;
import java.util.List;

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
