package ph.edu.dlsu;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;

import static ph.edu.dlsu.Main.adjustedBG;
import static ph.edu.dlsu.Main.stage;


/**
 * Created by ${AenonCunanan} on 03/07/2016.
 */
public class Graph {

    MenuHBox menuBox, menuBoxSub;
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

        final CustomMenuItem home = new CustomMenuItem("home");
        final CustomMenuItem about = new CustomMenuItem("about");
        final CustomMenuItem facts = new CustomMenuItem("facts");
        final CustomMenuItem close = new CustomMenuItem("close");

        final CustomMenuItemSub line = new CustomMenuItemSub("Line\nGraph");
        final CustomMenuItemSub bar = new CustomMenuItemSub("Bar\nGraph");

        menuBoxSub = new MenuHBox(bar, line);
        menuBoxSub.setTranslateX(displayWidth/2 - (menuBoxSub.getScaleX() * 100));
        menuBoxSub.setTranslateY(displayHeight/2 + 50);

        bar.setOnMouseClicked(event -> {
            BarGraph barGraph = new BarGraph();
            stage.setTitle("TraVIS: Bar Graph");
            stage.setScene(
                    new Scene(barGraph.main(), displayWidth, displayHeight)
            );
            stage.setFullScreen(true);                                  //Set the stage in fullscreen mode
            stage.setFullScreenExitHint("");
        });

        line.setOnMouseClicked(event -> {
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

        rootNode.getChildren().addAll(menuBox, menuBoxSub);

        return rootNode;
    }

}
