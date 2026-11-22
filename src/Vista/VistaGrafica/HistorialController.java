package Vista.VistaGrafica;

import Controladores.Controlador;
import Persistencia.EstadisticaJugador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.List;

public class HistorialController extends ControladorGrafico {

    @FXML
    private TableView<EstadisticaJugador> tablaHistorial;

    @Override
    public void setControlador(Controlador controlador){
        super.setControlador(controlador);
    }

    public void mostrarHistorial(List<EstadisticaJugador> estadisticas) {
        ObservableList<EstadisticaJugador> datos = FXCollections.observableArrayList(estadisticas);
        tablaHistorial.setItems(datos);
    }
}
