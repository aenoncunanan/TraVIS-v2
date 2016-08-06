package ph.edu.dlsu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.IOException;

import static ph.edu.dlsu.Main.adjustedBG;
import static ph.edu.dlsu.Main.stage;


/**
 * Created by ${AenonCunanan} on 03/07/2016.
 */
public class Graph {

    MenuHBox menuBox;
    ScreenSize screen = new ScreenSize();
    double displayWidth = screen.getDisplayWidth();
    double displayHeight = screen.getDisplayHeight();

    public Parent main(){
        Pane rootNode = new Pane();
        rootNode.setPrefSize(displayWidth, displayHeight);

//LOADING ONLINE IMAGE
        String imageUrl = adjustedBG;
        String destinationFile = "image.jpg";

        File image = new File(destinationFile);
        if (!image.exists()) {
            try {
                saveImage.saveImage(imageUrl, destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ImageView imgBackground = ph.edu.dlsu.Utils.loadImage2View(destinationFile, displayWidth, displayHeight);
        if(imgBackground != null){
            rootNode.getChildren().add(imgBackground);
        }
//END OF LOADING ONLINE IMAGE

//LOADING LOCAL IMAGE
//        ImageView imgBackground = Utils.loadImage2View(Main.regularBG, displayWidth, displayHeight);
//        if (imgBackground != null) {
//            rootNode.getChildren().add(imgBackground);
//        }
//END OF LOADING LOCAL IMAGE

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text text = new Text();
        text.setText("Please select how you want to see the graph");
        text.setTextAlignment(TextAlignment.CENTER);
        grid.add(text, 1, 1, 2, 1);

        Button barButton = new Button();
        barButton.setText("Bar Graph");
        grid.add(barButton, 1, 2);

        Button lineButton = new Button();
        lineButton.setText("Line Graph");
        grid.add(lineButton, 2, 2);

        grid.setTranslateX(displayWidth/2 - (grid.getScaleX() * 135));
        grid.setTranslateY(displayHeight/2);
        grid.setAlignment(Pos.CENTER);

        final CustomMenuItem home = new CustomMenuItem("home");
        final CustomMenuItem about = new CustomMenuItem("about");
        final CustomMenuItem facts = new CustomMenuItem("facts");
        final CustomMenuItem close = new CustomMenuItem("close");

        barButton.setOnMouseClicked(event -> {
            BarGraph barGraph = new BarGraph();
            stage.setTitle("TraVIS: Bar Graph");
            stage.setScene(
                    new Scene(barGraph.main(), displayWidth, displayHeight)
            );
            stage.setFullScreen(true);                                  //Set the stage in fullscreen mode
            stage.setFullScreenExitHint("");
        });

        lineButton.setOnMouseClicked(event -> {
            LineGraph lineGraph = new LineGraph();
            stage.setTitle("TraVIS: Line Graph");
            stage.setScene(
                    new Scene(lineGraph.main(), displayWidth, displayHeight)
            );
            stage.setFullScreen(true);                                  //Set the stage in fullscreen mode
            stage.setFullScreenExitHint("");
        });

        home.setOnMouseClicked(event -> {
            Main.onHome();
        });

        about.setOnMouseClicked(event -> {
            Main.onAbout();
        });

        facts.setOnMouseClicked(event -> {
            Main.onFacts();
        });

        close.setOnMouseClicked(event ->{
            Main.onExit();
        });

        menuBox = new MenuHBox(home, about, facts, close);
//        menuBox.setTranslateX(725);
        menuBox.setTranslateX((displayWidth/2) - (200));
        menuBox.setTranslateY(630);

        rootNode.getChildren().addAll(menuBox, grid);

        return rootNode;
    }

}
