package Vista;

import Controladores.Controlador;
import Interfaces.IJugador;
import Interfaces.IVista;

import Persistencia.EstadisticasJugador;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import exceptions.CantidadJugadoresException;


import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class VistaConsola implements IVista {

    public final Controlador controlador;

    private final Scanner sc;
    private boolean ejecutando;
    private boolean menuPendiente;

    public VistaConsola(Controlador controlador) {

        //Programacion defensiva
        if (controlador == null) throw new IllegalArgumentException("El controlador no puede ser nulo");

        this.controlador = controlador;
        this.controlador.setVista(this);

        sc = new Scanner(System.in);
    }

    @Override
    public void mostrarHUDJugador() {

        int ronda = controlador.getRondaActual();
        IJugador jugadorActual = controlador.getJugadorActual();
        String[] cartasEnMano = controlador.getCartas();

        String cartaPilaAscendente, cartaPilaDescendente;

        if(controlador.getCartaPilaAscendente() != null) cartaPilaAscendente = controlador.getCartaPilaAscendente().toString();
        else cartaPilaAscendente = "[]";

        if(controlador.getCartaPilaDescendente() != null) cartaPilaDescendente = controlador.getCartaPilaDescendente().toString();
        else cartaPilaDescendente = "[]";

        System.out.println("================================================================");
        System.out.println("RONDA " + ronda + " ·  Turno de " + jugadorActual.getNombre());
        System.out.println("================================================================");

        // ▲ Ascendente (12): [7 R]      ▼ Descendente (9): [3 V]
        System.out.println("▲ Ascendente:" + cartaPilaAscendente + " ▼ Descendente: " + cartaPilaDescendente);
        System.out.println("----------------------------------------------------------------");
        System.out.println("Tu mano:  1) " + cartasEnMano[0] + " | " + "2) " + cartasEnMano[1]);

        menuPendiente = controlador.esTurno(); //Reviso si es mi turno

    }
    @Override
    public void mostrarMensaje(String mensaje) {
        if (mensaje != null) {
            System.out.println(mensaje);
        }
    }

    public void registrarse() {
        while (this.controlador.getJugador() == null && this.ejecutando) {
            System.out.print("Ingrese su nombre: ");
            String nombre = this.sc.nextLine();
            try {
                controlador.registrarJugador(nombre);
            } catch (IllegalArgumentException e) {
                mostrarError("Nombre de usuario invalido");
            }
        }
    }

    @Override
    public void salir(){
        controlador.cerrarApp();
        this.ejecutando = false;

    }

    @Override
    public void iniciar() {

        this.ejecutando = true;
        boolean mensajeEsperando = false;
        System.out.println("===============================");
        System.out.println("--- THE GAME QUICK AND EASY ---");
        System.out.println("===============================");


        //1) Obligo a los jugadores a registrarse
        registrarse();

        //2) Bucle iterativo de comandos
        while (ejecutando) {

            if(!controlador.seInicioLaPartida() && controlador.esAnfitrion()) {
                mostrarMenuAnfitrion();
            }else if (menuPendiente){
                menuPendiente = false;
                mostrarMenuJugador();
            }


        }
    }



    @Override
    public void jugadoresGanaron() {
        mostrarMensaje("[PARTIDA FINALIZADA] - GANAN LOS JUGADORES!!!");
    }

    @Override
    public void jugadoresPerdieron() {
        mostrarMensaje("[PARTIDA FINALIZADA] - LOS JUGADORES PERDIERON");
    }

    @Override
    public void actualizarListaJugadores() {
        //En la consola no se utiliza porque el Scanner bloquea el hilo de ejecucion y no me permite
        //mostrar dinamicamente los jugadores que se conectan a la vez que decidir cuando comenzar la partida
    }

    @Override
    public void actualizarJugadorSalio() {

    }

    @Override
    public void jugadoresInsuficientes() {
        mostrarMensaje("[PARTIDA FINALIZADA] - CANTIDAD INSUFICIENTE DE JUGADORES");
    }

    @Override
    public void mostrarHistorial(List<EstadisticasJugador> estadisticas) {

    }


    private void mostrarMenuJugador(){
        System.out.println("1) Jugar carta  2) Pasar turno   0) Salir");
        int opcion = pedirInput("Ingresa una de las opciones: ");
        try {
            if (opcion == 1) {
                if(!controlador.seInicioLaPartida()) {
                    System.out.println("La partida no fue iniciada");
                    return;
                }
                int indiceCarta = pedirInput("¿Qué carta jugás? (1 o 2): ");
                int indicePila = pedirInput("¿En qué pila jugás?\n 1) Ascendente\n2) Descendente\n");
                controlador.jugarCarta(indiceCarta, indicePila);
                if(controlador.esTurno()) menuPendiente = true; //Si sigue siendo mi turno, vuelvo a mostrar el menu de opciones
            } else if (opcion == 2) {
                if(!controlador.seInicioLaPartida()) {
                    System.out.println("La partida no fue iniciada");
                    return;
                }
                controlador.pasarTurno();
                if(controlador.esTurno()) menuPendiente = true; //Si sigue siendo mi turno, vuelvo a mostrar el menu de opciones
            } else if (opcion == 0) {
                salir();
                //TODO Agregar metodo que permita salir del juego y del servidor
            } else {
                mostrarError("Opción inválida");
                menuPendiente = true;
            }
        } catch (CantidadJugadoresException e) {
            mostrarError("Faltan jugadores para empezar la partida");
            menuPendiente = true;
        }
    }

    private void mostrarMenuAnfitrion(){
        mostrarMensaje("ES EL ANFITRION DE LA SALA.");
        System.out.println("1) Iniciar partida  0) Salir");
        int opcion = pedirInput("Ingresa una de las opciones: ");
        try {
            if (opcion == 1) {
                controlador.iniciarPartida();
            } else if (opcion == 0) {
                ejecutando = false;
                salir();
                //TODO Agregar metodo que permita salir del juego y del servidor. A su vez que actualizar el anfitrion
            } else {
                mostrarError("Opción inválida");
            }
        } catch (CantidadJugadoresException e) {
            mostrarError("Faltan jugadores para empezar la partida");
        }
    }




    public void mostrarError(String mensajeError) {
        System.out.println(" [!] ERROR - " + mensajeError);
    }


    //=======================
    //SOLICITUD DE DATOS
    //=======================

    public int pedirInput(String mensaje) {
        int numero;
        while (true) {
            System.out.print(mensaje);
            try {
                numero = Integer.parseInt(sc.nextLine().trim()); //Valido que sea entero y le elimino espacios al comienzo y al final
                return numero;
            } catch (NumberFormatException e) {
                mostrarError("Ingresa un numero");
            }
        }
    }

}