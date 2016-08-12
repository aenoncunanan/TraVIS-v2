package ph.edu.dlsu;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Created by ${AenonCunanan} on 24/06/2016.
 */
public class MenuHBox extends HBox{

    public MenuHBox(CustomMenuItem... items){

//        getChildren().add(createSeparator());

        for(CustomMenuItem item : items){
//            getChildren().addAll(item, createSeparator());
            getChildren().addAll(item);
        }

    }

    public MenuHBox(CustomMenuItemSub item1, CustomMenuItemSub item2){

        getChildren().addAll(item1, item2);

    }

    private Line createSeparator(){                                         //line separates the item menu

        Line sep = new Line();
        sep.setEndY(40);
        sep.setStroke(Color.DARKGREY);

        return sep;

    }

}
