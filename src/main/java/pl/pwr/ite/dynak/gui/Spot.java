package pl.pwr.ite.dynak.gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
@lombok.Getter
@lombok.Setter
public class Spot extends StackPane {
    private Rectangle rectangle;
    private Text letter;
    public Spot(double width, double height, Color color, String initialLetter) {
        rectangle = new Rectangle(width, height);
        rectangle.setFill(color);
        rectangle.setStroke(Color.LIGHTGRAY);
        rectangle.setStrokeWidth(width/15);
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);
        letter = new Text(initialLetter);
        letter.setFont(Font.font(width/2));
        letter.setFill(Color.WHITE);
        this.getChildren().addAll(rectangle, letter);
        this.setStyle("-fx-alignment: center;");
    }
    public Spot(String initialLetter) {
        rectangle = new Rectangle(20, 20);
        rectangle.setFill(Color.LIGHTGREEN);
        rectangle.setStroke(Color.LIGHTGRAY);
        rectangle.setStrokeWidth(20/15);
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);
        letter = new Text(initialLetter);
        letter.setFont(Font.font(10));
        letter.setFill(Color.WHITE);
        this.getChildren().addAll(rectangle, letter);
        this.setStyle("-fx-alignment: center;");
    }
    public Spot() {
        rectangle = new Rectangle(20, 20);
        rectangle.setFill(Color.DARKGREEN);
        rectangle.setStroke(Color.LIGHTGRAY);
        rectangle.setStrokeWidth(20/15);
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);
        this.getChildren().addAll(rectangle);
        this.setStyle("-fx-alignment: center;");
    }
}