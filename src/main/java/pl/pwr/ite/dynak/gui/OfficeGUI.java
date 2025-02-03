package pl.pwr.ite.dynak.gui;

import interfaces.IOffice;
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
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class OfficeGUI extends Application implements IRedirector {
    @Override
    public void start(Stage stage) {
        var hBox = new HBox(10);
        var vBox = new VBox(10);
        vBox.getChildren().addAll(hBox);
        var portField = new TextField();
        var tankerIdField = new TextField();
        var logTextField = new TextArea();
        logTextField.setEditable(false);
        logTextField.setWrapText(true);
        redirectConsoleOutput(logTextField);
        var startButton = new Button("Start");
        var getStatusButton = new Button("Get status");
        var registryPortField = new TextField();
        registryPortField.setPromptText("Registry Port");
        getStatusButton.setDisable(true);
        startButton.setOnAction(e -> {
            try {
                int registryPort = Integer.parseInt(registryPortField.getText());
                int officePort = Integer.parseInt(portField.getText());
                String universalHost = "localhost";
                Office office = new Office();
                IOffice io = (IOffice) UnicastRemoteObject.exportObject(office, officePort);
                Registry registry = LocateRegistry.getRegistry(universalHost, registryPort);
                registry.rebind("Office", io);
                getStatusButton.setOnAction(f -> new Thread(() -> office.sendGetStatusRequest(Integer.parseInt(tankerIdField.getText()))).start());
            } catch (RemoteException eMSG) {
                System.out.println(eMSG.getMessage());
            }
            System.out.println("Office running on port " + portField.getText());
            Platform.runLater(() -> {
                vBox.getChildren().add(new Spot(90, 90, Color.DARKGREEN, "O"));
                vBox.getChildren().add(logTextField);
                startButton.setDisable(true);
            });
        });
        portField.setPromptText("Port");
        tankerIdField.setPromptText("Tanker ID");
        hBox.getChildren().addAll(portField, startButton);
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
