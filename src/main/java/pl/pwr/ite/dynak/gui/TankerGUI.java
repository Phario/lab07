package pl.pwr.ite.dynak.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.pwr.ite.dynak.services.Tanker;

import java.io.IOException;

public class TankerGUI extends Application implements IRedirector{
    @Override
    public void start(Stage stage) {
        var hBox = new HBox(10);
        var vBox = new VBox(10);
        vBox.getChildren().addAll(hBox);
        var portField = new TextField();
        var maxCapacityField = new TextField();
        var officePortField = new TextField();
        var officeHostField = new TextField();
        var sewagePlantHostField = new TextField();
        var sewagePlantPortField = new TextField();
        var logTextField = new TextArea();
        logTextField.setEditable(false);
        logTextField.setWrapText(true);
        redirectConsoleOutput(logTextField);
        var startButton = new Button("Start");
        startButton.setOnAction(e -> {
            var tanker = new Tanker(
                    Integer.parseInt(maxCapacityField.getText()),
                    Integer.parseInt(portField.getText()),
                    Integer.parseInt(officePortField.getText()),
                    officeHostField.getText(),
                    Integer.parseInt(sewagePlantPortField.getText()),
                    sewagePlantHostField.getText());
            Thread thread = new Thread(() -> {
                try {
                    tanker.registerAtOffice();
                    tanker.startListening();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.start();
            System.out.println("Tanker running on port " + portField.getText());
            Platform.runLater(() -> {
                vBox.getChildren().add(new Spot(90, 90, Color.BLACK, "T" + tanker.getId()));
                vBox.getChildren().add(logTextField);
                startButton.setDisable(true);
            });
        });
        portField.setPromptText("Port");
        maxCapacityField.setPromptText("Max Capacity");
        officePortField.setPromptText("Office Port");
        officeHostField.setPromptText("Office Host");
        sewagePlantHostField.setPromptText("Sewage Plant Host");
        sewagePlantPortField.setPromptText("Sewage Plant Port");
        hBox.getChildren().addAll(portField, maxCapacityField, officePortField, officeHostField);
        var hBox2 = new HBox(10);
        hBox2.getChildren().addAll(sewagePlantHostField, sewagePlantPortField, startButton);
        vBox.getChildren().add(hBox2);
        Scene scene = new Scene(vBox, 500, 300);
        stage.setTitle("Tanker");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
