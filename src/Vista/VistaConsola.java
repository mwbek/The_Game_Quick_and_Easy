package Vista;

import Controladores.ControladorConsola;
import Enumerados.CartaDeIndicacion;
import Interfaces.IJugador;
import Interfaces.IVista;

import exceptions.CantidadJugadoresException;


import java.util.Scanner;

public class VistaConsola implements IVista {

    public final ControladorConsola controlador;

    private final Scanner sc;
    private boolean ejecutando;

    public VistaConsola(ControladorConsola controlador) {

        //Programacion defensiva
        if (controlador == null) throw new IllegalArgumentException("El controlador no puede ser nulo");

        this.controlador = controlador;
        this.controlador.setVista(this);

        sc = new Scanner(System.in);
    }


    public void mostrarHUDJugador() {
        int ronda = controlador.getRondaActual();
        IJugador jugadorActual = controlador.getJugadorActual();
        String[] cartasEnMano = controlador.getCartas();

        System.out.println("================================================================");
        System.out.println("RONDA " + ronda + " ·  Turno de " + jugadorActual.getNombre());
        System.out.println("================================================================");

        // ▲ Ascendente (12): [7 R]      ▼ Descendente (9): [3 V]
        System.out.println("▲ Ascendente:" + controlador.getCartaPilaAscendente() + " ▼ Descendente: " + controlador.getCartaPilaDescendente());
        System.out.println("----------------------------------------------------------------");
        System.out.println("Tu mano:  1) " + cartasEnMano[0] + " | " + "2) " + cartasEnMano[1]);

    }

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


    public void iniciar() {

        this.ejecutando = true;

        System.out.println("===============================");
        System.out.println("-- THE GAME QUICK AND EASY --");
        System.out.println("===============================");


        //1) Obligo a los jugadores a registrarse
        registrarse();

        try {

            //2) Bucle iterativo de comandos
            while (ejecutando) {
                System.out.println("1) Jugar carta  2) Pasar turno  3) Iniciar partida  0) Salir");
                int opcion = pedirInput("Ingresa una de las opciones: ");
                if (opcion == 1) {
                    int indiceCarta = pedirInput("¿Qué carta jugas? (1 o 2): ");
                    int indicePila = pedirInput("¿En que pila jugas?\n 1) Ascendente\n2)Descendente\nIngresa la pila: ");
                    controlador.jugarCarta(indiceCarta, indicePila);

                } else if (opcion == 2) {
                    controlador.pasarTurno();
                } else if (opcion == 3) {
                    controlador.iniciarPartida();
                } else if (opcion == 0) {
                    ejecutando = false;
                } else {
                    mostrarError("Opcion invalida");
                }
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