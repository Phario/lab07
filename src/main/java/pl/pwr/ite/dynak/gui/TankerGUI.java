package pl.pwr.ite.dynak.gui;

import interfaces.IHouse;
import interfaces.IOffice;
import interfaces.ISewagePlant;
import interfaces.ITanker;
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
import pl.pwr.ite.dynak.services.House;
import pl.pwr.ite.dynak.services.Tanker;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class TankerGUI extends Application implements IRedirector{
    @Override
    public void start(Stage stage) {
        var hBox = new HBox(10);
        var vBox = new VBox(10);
        vBox.getChildren().addAll(hBox);
        var portField = new TextField();
        var maxCapacityField = new TextField();
        var tankerNameField = new TextField();
        var registryPortField = new TextField();
        registryPortField.setPromptText("Registry Port");
        tankerNameField.setPromptText("Tanker name");
        var logTextField = new TextArea();
        logTextField.setEditable(false);
        logTextField.setWrapText(true);
        redirectConsoleOutput(logTextField);
        var startButton = new Button("Start");
        startButton.setOnAction(e -> {
            int registryPort = Integer.parseInt(registryPortField.getText());
            int tankerPort = Integer.parseInt(portField.getText());
            int maxCapacity = Integer.parseInt(maxCapacityField.getText());
            String name = tankerNameField.getText();
            String universalHost = "localhost";
            try {
                Registry registry = LocateRegistry.getRegistry(registryPort);
                IOffice iOffice = (IOffice) registry.lookup("Office");
                ISewagePlant iSewagePlant = (ISewagePlant) registry.lookup("SewagePlant");
                Tanker tanker = new Tanker(maxCapacity, iSewagePlant, iOffice, name);
                ITanker iTanker = (ITanker) UnicastRemoteObject.exportObject(tanker, tankerPort);
                registry = LocateRegistry.getRegistry(universalHost, registryPort);
                registry.rebind(name, iTanker);
                tanker.registerAtOffice(iTanker, name);
                Platform.runLater(() -> {
                    vBox.getChildren().add(new Spot(90, 90, Color.BLACK, "T" + tanker.getId()));
                    vBox.getChildren().add(logTextField);
                    startButton.setDisable(true);
                });
            } catch (NotBoundException | RemoteException eMSG) {
                System.out.println(eMSG.getMessage());
            }

        });
        portField.setPromptText("Port");
        maxCapacityField.setPromptText("Max Capacity");
        hBox.getChildren().addAll(portField, registryPortField, maxCapacityField);
        var hBox2 = new HBox(10);
        hBox2.getChildren().addAll(startButton);
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
