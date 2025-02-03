package pl.pwr.ite.dynak.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.pwr.ite.dynak.services.Tailor;
public class TailorGUI extends Application implements IRedirector {
    @Override
    public void start(Stage stage){
        var vBox = new VBox(10);
        var logTextField = new TextArea();
        logTextField.setEditable(false);
        logTextField.setWrapText(true);
        redirectConsoleOutput(logTextField);
        var startTailorButton = new Button("Start tailor service");
        startTailorButton.setOnAction(e -> {
            new Thread(() -> Tailor.main(new String[]{})).start();
            Platform.runLater(() -> {
                vBox.getChildren().add(new Spot(90, 90, Color.BLUE, "REG"));
                vBox.getChildren().add(logTextField);
                startTailorButton.setDisable(true);
            });
        });
        vBox.getChildren().addAll(startTailorButton);
        Scene scene = new Scene(vBox, 300, 300);
        stage.setTitle("Tailor");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
