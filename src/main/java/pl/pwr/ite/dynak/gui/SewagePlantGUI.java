package pl.pwr.ite.dynak.gui;

import interfaces.ISewagePlant;
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
import pl.pwr.ite.dynak.services.SewagePlant;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SewagePlantGUI extends Application implements IRedirector {

    @Override
    public void start(Stage stage) {
        var hBox = new HBox(10);
        var vBox = new VBox(10);
        vBox.getChildren().addAll(hBox);
        var portField = new TextField();
        var startButton = new Button("Start");
        var logTextField = new TextArea();
        logTextField.setEditable(false);
        logTextField.setWrapText(true);
        var registryPortField = new TextField();
        registryPortField.setPromptText("Registry Port");
        redirectConsoleOutput(logTextField);
        startButton.setOnAction(e -> {
            try {
                int registryPort = Integer.parseInt(registryPortField.getText());
                int sewagePlantPort = Integer.parseInt(portField.getText());
                String universalHost = "localhost";
                SewagePlant sewagePlant = new SewagePlant();
                ISewagePlant isp = (ISewagePlant) UnicastRemoteObject.exportObject(sewagePlant, sewagePlantPort);
                Registry registry = LocateRegistry.getRegistry(universalHost, registryPort);
                registry.rebind("SewagePlant", isp);
                System.out.println("Sewage plant running on port " + portField.getText());
            } catch (RemoteException eMSG) {
                System.out.println(eMSG.getMessage());
            }
            Platform.runLater(() -> {
                vBox.getChildren().add(new Spot(90, 90, Color.DARKBLUE, "SP"));
                vBox.getChildren().add(logTextField);
                startButton.setDisable(true);
            });
        });
        portField.setPromptText("Port");
        hBox.getChildren().addAll(portField, registryPortField, startButton);
        Scene scene = new Scene(vBox, 400, 300);
        stage.setTitle("Sewage plant");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
