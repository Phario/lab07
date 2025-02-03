package pl.pwr.ite.dynak.gui;

import interfaces.IHouse;
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
import pl.pwr.ite.dynak.services.House;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class HouseGUI extends Application implements IRedirector{
    @Override
    public void start(Stage stage){
        var hBox = new HBox(10);
        var vBox = new VBox(10);
        vBox.getChildren().addAll(hBox);
        var portField = new TextField();
        var registryPortField = new TextField();
        var maxCapacityField = new TextField();
        var tickSpeedField = new TextField();
        var houseNameField = new TextField();
        var logTextField = new TextArea();
        logTextField.setEditable(false);
        logTextField.setWrapText(true);
        redirectConsoleOutput(logTextField);
        var startButton = new Button("Start");
        startButton.setOnAction(e -> {
            int registryPort = Integer.parseInt(registryPortField.getText());
            int housePort = Integer.parseInt(portField.getText());
            int maxCapacity = Integer.parseInt(maxCapacityField.getText());
            int tickSpeed = Integer.parseInt(tickSpeedField.getText());
            String name = houseNameField.getText();
            String universalHost = "localhost";
            try {
                Registry registry = LocateRegistry.getRegistry(universalHost, registryPort);
                IOffice iOffice = (IOffice) registry.lookup("Office");
                House house = new House(iOffice,maxCapacity, tickSpeed, name);
                IHouse ih = (IHouse) UnicastRemoteObject.exportObject(house, housePort);
                house.setIHouse(ih);
                registry.rebind(name, ih);
                Thread thread = new Thread(house::startSimulation);
                thread.setDaemon(true);
                thread.start();
            } catch (NotBoundException | RemoteException eMSG) {
                System.out.println(eMSG.getMessage());
            }
            System.out.println("House running on port " + portField.getText());
            Platform.runLater(() -> {
                vBox.getChildren().add(new Spot(90, 90, Color.BROWN, "H"));
                vBox.getChildren().add(logTextField);
                startButton.setDisable(true);
            });
        });
        houseNameField.setPromptText("House name");
        registryPortField.setPromptText("Registry Port");
        portField.setPromptText("Port");
        maxCapacityField.setPromptText("Max Capacity");
        tickSpeedField.setPromptText("Tick Speed");
        hBox.getChildren().addAll(portField, maxCapacityField, houseNameField);
        var hBox2 = new HBox(10);
        hBox2.getChildren().addAll(tickSpeedField, registryPortField, startButton);
        vBox.getChildren().add(hBox2);
        Scene scene = new Scene(vBox, 500, 300);
        stage.setTitle("House");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
