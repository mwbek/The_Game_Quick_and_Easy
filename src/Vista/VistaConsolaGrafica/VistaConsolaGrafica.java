package Vista.VistaConsolaGrafica;

import Controladores.Controlador;
import Interfaces.IVista;
import Persistencia.EstadisticasJugador;
import Vista.VistaGrafica.MenuPrincipalController;
import exceptions.CantidadJugadoresException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class VistaConsolaGrafica implements IVista {

    private final Controlador controlador;
    private final Stage stagePrincipal;
    private VistaConsolaController vistaConsolaController;

    private boolean menuMostrado = false;
    private String separador = "================================================================";

    private int cartaElegida = -1;




    public VistaConsolaGrafica(Stage stage, Controlador controlador){
        if(controlador == null) throw new IllegalArgumentException("El controlador no puede ser null");
        this.controlador = controlador;
        this.stagePrincipal = stage;
    }


    @Override
    public void mostrarHUDJugador() {

        if(controlador.seReseteoLaPartida()){
            vistaConsolaController = null;
        }

        Platform.runLater(() -> {
            //Cargo la vista una sola vez, despues voy actualizando
            if (vistaConsolaController == null) {
                vistaConsolaController = cargar(stagePrincipal, "/Vista/VistaGrafica/GUI/vistaConsolaGrafica.fxml");
                vistaConsolaController.setControlador(controlador);
                stagePrincipal.setTitle("THE GAME");

                vistaConsolaController.agregarMensaje(separador);
                vistaConsolaController.agregarMensaje("--- THE GAME QUICK AND EASY ---");
                vistaConsolaController.agregarMensaje(separador);
            }



            if(controlador.seInicioLaPartida()){
                String mensajeCompleto = getMensajesHud();
                vistaConsolaController.agregarMensaje(mensajeCompleto);
                if(controlador.esTurno()) mostrarMensaje("Ingrese una de las siguientes opciones:\n1) Jugar carta\n2) Pasar turno");
            }
        });

    }

    @Override
    public void actualizarMenuLobby() {
        if(controlador.esAnfitrion() && !menuMostrado){
            mostrarMensaje("ES EL ANFITRION DE LA SALA.");
            mostrarMensaje("Ingrese un (1) para comenzar la partida");
            mostrarMensaje("Ingrese un (2) para ver el historial del juego");
            menuMostrado = true;
        }
        else{
            mostrarMensaje("Esperando al anfitrion (" + controlador.getAnfitrion().getNombre() + ") a que inicie la partida...");
        }
        actualizarListaJugadores();
    }

    private String getMensajesHud(){
        String[] cartasEnMano = controlador.getCartas();
        String cartaPilaAscendente, cartaPilaDescendente;
        if(controlador.getCartaPilaAscendente() != null) cartaPilaAscendente = controlador.getCartaPilaAscendente().toString();
        else cartaPilaAscendente = "[]";

        if(controlador.getCartaPilaDescendente() != null) cartaPilaDescendente = controlador.getCartaPilaDescendente().toString();
        else cartaPilaDescendente = "[]";

        String ronda = "RONDA " + controlador.getRondaActual() + "\nCARTAS RESTANTES: " + controlador.getCantidadRestanteMazo();

        if(controlador.esTurno()){
            ronda += "\nTU TURNO ";
        }
        else{
            ronda += "\nTurno de " + controlador.getJugadorActual().getNombre();
        }

        String pilaCartas = "1) ▲ Ascendente:" + cartaPilaAscendente + "\n2) ▼ Descendente: " + cartaPilaDescendente;
        String mano = "Tu mano:\n1) " + cartasEnMano[0] + "\n2) " + cartasEnMano[1];

        return separador + "\n" + ronda + "\n" + separador + "\n" + pilaCartas + "\n" + mano;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        if(vistaConsolaController != null){
            vistaConsolaController.agregarMensaje("[MENSAJE DE SERVIDOR] - " + mensaje);
        }

    }

    @Override
    public void mostrarError(String mensajeError) {
        vistaConsolaController.agregarMensaje(mensajeError);
    }

    @Override
    public void iniciar() {

        if(controlador.seInicioLaPartida()){
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("THE GAME");
            alerta.setHeaderText("Partida en curso");
            alerta.setContentText("La partida ya comenzó y no se pueden unir nuevos jugadores.");
            alerta.showAndWait();
            controlador.cerrarApp();
            return;
        }

        stagePrincipal.setOnCloseRequest(e -> this.salir());
        vistaConsolaController = cargar(stagePrincipal,"/Vista/VistaGrafica/GUI/vistaConsolaGrafica.fxml");
        vistaConsolaController.setControlador(controlador);
        vistaConsolaController.setVista(this);
        stagePrincipal.setTitle("THE GAME - LOBBY");

        mostrarMensaje("===============================");
        mostrarMensaje("--- THE GAME QUICK AND EASY ---");
        mostrarMensaje("===============================");

        mostrarMensaje("Ingrese su nombre para unirse al lobby ");
        Platform.runLater(() ->{
            vistaConsolaController.actualizarListaLobby();
        });
    }

    @Override
    public void salir() {
        controlador.cerrarApp();
    }

    @Override
    public void jugadoresGanaron() {
        vistaConsolaController.agregarMensaje("[PARTIDA FINALIZADA] - GANAN LOS JUGADORES!!!");
    }

    @Override
    public void jugadoresPerdieron() {
        vistaConsolaController.agregarMensaje("[PARTIDA FINALIZADA] - LOS JUGADORES PERDIERON");
    }

    @Override
    public void actualizarListaJugadores() {

        Platform.runLater(() -> {
            if(vistaConsolaController != null){
                vistaConsolaController.actualizarListaLobby();
            }
        });


    }

    @Override
    public void actualizarJugadorSalio() {
        if(vistaConsolaController != null){
            vistaConsolaController.actualizarListaLobby();

        }
    }

    @Override
    public void jugadoresInsuficientes() {
        vistaConsolaController.agregarMensaje("[PARTIDA FINALIZADA] - CANTIDAD INSUFICIENTE DE JUGADORES");
    }

    @Override
    public void mostrarHistorial(List<EstadisticasJugador> estadisticas) {
        mostrarMensaje("HISTORIAL DE JUGADORES\n");
        mostrarMensaje(separador);
        for(EstadisticasJugador e : estadisticas){
            mostrarMensaje(e.toString() + "\n");
        }
    }



    private <T> T cargar(Stage stage, String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
            return loader.getController();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar " + rutaFxml, e);
        }
    }


    public void procesarInput(String input) {
        mostrarMensaje("> " + input);

        // 1) Registro
        if (controlador.getJugador() == null) {
            controlador.registrarJugador(input);
            return;
        }

        // 2) Lobby
        if (!controlador.seInicioLaPartida()) {
            if (input.equals("1")) controlador.iniciarPartida();   // el controlador ya valida si es anfitrión
            else if (input.equals("2")) controlador.verHistorial();
            else {
                mostrarError("Opción inválida");
                mostrarMensaje("ES EL ANFITRION DE LA SALA.");
                mostrarMensaje("Ingrese un (1) para comenzar la partida");
                mostrarMensaje("Ingrese un (2) para ver el historial del juego");
            }
            return;
        }

        // 3) Partida
        if (!controlador.esTurno()) {
            mostrarError("No es tu turno");
            return;
        }
        procesarTurno(input);
    }

    private void procesarTurno(String input) {
        if (cartaElegida > 0) {                       // esperando pila
            if (!esOpcion(input)) { mostrarError("Pila inválida\n" + getMensajesHud()); return; }
            int carta = cartaElegida;
            cartaElegida = -1;
            controlador.jugarCarta(carta, Integer.parseInt(input));
        } else if (cartaElegida == 0) {               // esperando carta
            if (!esOpcion(input)) { mostrarError("Carta inválida\n" + getMensajesHud()); return; }
            cartaElegida = Integer.parseInt(input);
            mostrarMensaje("¿En qué pila? 1) Ascendente  2) Descendente");
        } else {                                      // menú
            switch (input) {
                case "1" -> {
                    cartaElegida = 0;
                    mostrarMensaje("¿Qué carta jugás? (1 o 2)");
                }
                case "2" -> {
                    controlador.pasarTurno();
                    if(controlador.esTurno()){
                        mostrarMensaje("Ingrese una de las siguientes opciones:\n1) Jugar carta\n2) Pasar turno");

                    }

                }
                default -> {
                    mostrarError("Opción inválida");
                    mostrarMensaje("Ingrese una de las siguientes opciones:\n1) Jugar carta\n2) Pasar turno");
                }
            }
        }
    }

    private boolean esOpcion(String s) {
        return s.equals("1") || s.equals("2");
    }

}
