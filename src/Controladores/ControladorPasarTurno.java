package Controladores;

import Vista.Consola.VistaPasarTurno;
import Modelos.Partida;

public class ControladorPasarTurno {

    private final Partida partida;
    private final VistaPasarTurno vista;

    public ControladorPasarTurno(Partida partida, VistaPasarTurno vista)
    {
        this.partida = partida;
        this.vista = vista;
    }
}
