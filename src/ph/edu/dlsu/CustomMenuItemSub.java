package ph.edu.dlsu;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Created by ${AenonCunanan} on 21/06/2016.
 */
public class CustomMenuItemSub extends StackPane {

    public final static float width = 155;

    public CustomMenuItemSub(String name){                                         //This class is made to design the menu base

        Rectangle bg = new Rectangle(245, 40);
        bg.setArcHeight(50);
        bg.setArcWidth(50);
        bg.setWidth(width);
        bg.setHeight(100);
        bg.setFill(Color.web("#ffffff"));
        bg.setOpacity(0.4);

        Text text = new Text(name);                                             //Font and color of text
        text.setFill(Color.web("#2c3e50"));
        text.setFont(Font.font("Asimov", FontWeight.SEMI_BOLD, 30));
        text.setTextAlignment(TextAlignment.CENTER);

        setAlignment(Pos.CENTER);                                               //Align to center

        getChildren().addAll(bg, text);

        setOnMouseEntered(event -> {
            text.setFill(Color.web("#009fe0"));
        });

        setOnMouseExited(event -> {
            text.setFill(Color.web("#2c3e50"));
        });

        setOnMousePressed(event -> {
            bg.setFill(Color.DARKGREEN);
        });

        setOnMouseReleased(event -> {
            bg.setFill(Color.web("#ffffff"));
        });

    }

}
