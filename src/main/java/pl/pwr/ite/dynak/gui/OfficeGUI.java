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
import pl.pwr.ite.dynak.services.Office;

import java.io.IOException;

public class OfficeGUI extends Application implements IRedirector {
    @Override
    public void start(Stage stage) {
        var hBox = new HBox(10);
        var vBox = new VBox(10);
        vBox.getChildren().addAll(hBox);
        var portField = new TextField();
        var sewagePlantHostField = new TextField();
        var sewagePlantPortField = new TextField();
        var tankerIdField = new TextField();
        var logTextField = new TextArea();
        logTextField.setEditable(false);
        logTextField.setWrapText(true);
        redirectConsoleOutput(logTextField);
        var startButton = new Button("Start");
        var getStatusButton = new Button("Get status");
        getStatusButton.setDisable(true);
        startButton.setOnAction(e -> {
            var office = new Office();
            getStatusButton.setDisable(false);
            Thread thread = new Thread(() -> {
            });
            getStatusButton.setOnAction(f -> new Thread(() -> office.sendGetStatusRequest(Integer.parseInt(tankerIdField.getText()))).start());
            thread.setDaemon(true);
            thread.start();
            System.out.println("Office running on port " + portField.getText());
            Platform.runLater(() -> {
                vBox.getChildren().add(new Spot(90, 90, Color.DARKGREEN, "O"));
                vBox.getChildren().add(logTextField);
                startButton.setDisable(true);
            });
        });
        portField.setPromptText("Port");
        sewagePlantHostField.setPromptText("Sewage plant's host");
        sewagePlantPortField.setPromptText("Sewage plant's port");
        tankerIdField.setPromptText("Tanker ID");
        hBox.getChildren().addAll(portField, sewagePlantPortField, sewagePlantHostField, startButton);
        var hBox2 = new HBox(10);
        hBox2.getChildren().addAll(tankerIdField, getStatusButton);
        vBox.getChildren().add(hBox2);
        Scene scene = new Scene(vBox, 550, 400);
        stage.setTitle("Office");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
