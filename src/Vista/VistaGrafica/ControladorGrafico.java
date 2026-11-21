package Vista.VistaGrafica;

import Controladores.Controlador;

public class ControladorGrafico {

    protected Controlador controlador;

    public void setControlador(Controlador controlador){
        if(controlador == null) throw new IllegalArgumentException("El controlador no puede ser nulo");
        this.controlador = controlador;
    }
}
