package s.s;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        Vars vars = new Vars(visualBounds.getWidth(), visualBounds.getHeight());
        Control ctrl = new Control();
        ContainerPane root = new ContainerPane();
        Vars.root = root;
        Scene scene = new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());
        ctrl.loadData();
        stage.setScene(scene);
        stage.show();
    }
}



