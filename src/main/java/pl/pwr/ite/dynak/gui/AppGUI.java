package pl.pwr.ite.dynak.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AppGUI extends Application {
    @Override
    public void start(Stage stage){
        var gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        var sewagePlantButton = new Button("Sewage Plant");
        var houseButton = new Button("House");
        var officeButton = new Button("Office");
        var tankerButton = new Button("Tanker");
        houseButton.setOnAction(e -> new HouseGUI().start(new Stage()));
        sewagePlantButton.setOnAction(e -> new SewagePlantGUI().start(new Stage()));
        officeButton.setOnAction(e -> new OfficeGUI().start(new Stage()));
        tankerButton.setOnAction(e -> new TankerGUI().start(new Stage()));
        gridPane.add(sewagePlantButton, 0, 0);
        gridPane.add(houseButton, 1, 0);
        gridPane.add(officeButton, 2, 0);
        gridPane.add(tankerButton, 3, 0);
        Scene scene = new Scene(gridPane, 300, 50);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
