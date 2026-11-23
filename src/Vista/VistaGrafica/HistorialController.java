package Vista.VistaGrafica;

import Controladores.Controlador;
import Persistencia.EstadisticasJugador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class HistorialController extends ControladorGrafico {

    @FXML private TableView<EstadisticasJugador> tablaHistorial;
    @FXML private TableColumn<EstadisticasJugador, String> CNombre;
    @FXML private TableColumn<EstadisticasJugador, Integer> CGanadas;
    @FXML private TableColumn<EstadisticasJugador, Integer> CPerdidas;
    @FXML private TableColumn<EstadisticasJugador, Integer> CAbandonos;

    @FXML
    public void initialize() {
        CNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        CGanadas.setCellValueFactory(new PropertyValueFactory<>("Ganadas"));
        CPerdidas.setCellValueFactory(new PropertyValueFactory<>("Perdidas"));
        CAbandonos.setCellValueFactory(new PropertyValueFactory<>("Abandonos"));
    }
    @Override
    public void setControlador(Controlador controlador){
        super.setControlador(controlador);
    }

    public void mostrarHistorial(List<EstadisticasJugador> estadisticas) {
        ObservableList<EstadisticasJugador> datos = FXCollections.observableArrayList(estadisticas);
        tablaHistorial.setItems(datos);
    }
}
