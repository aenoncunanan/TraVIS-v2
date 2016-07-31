package ph.edu.dlsu;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang.WordUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static javafx.scene.paint.Color.FIREBRICK;

public class Main extends Application{

    //BACKGROUND IMAGE INITIALIZATION
    //LOADING LOCAL IMAGE
//    public static String regularBG = "res/TraVIS.jpg";
//    public static String adjustedBG = "res/TraVIS_Others.jpg";

    //LOADING ONLINE IMAGE
    final saveImage saveImage = new saveImage();
    public static String regularBG = "https://www.github.com/aenoncunanan/TraVIS-v2/blob/master/res/TraVIS.jpg?raw=true";
    public static String adjustedBG = "https://www.github.com/aenoncunanan/TraVIS-v2/blob/master/res/TraVIS_Others.jpg?raw=true";
    //END OF BACKGROUND IMAGE INITIALIZATION

    //DATABASE ACCESS INITIALIZATION
    public static final String username = "root";
    public static final String hostname = "localhost";
//    public static final String hostname = "127.0.0.1";
    public static final String portNumber = "3306";
    public static final String databaseName = "travis";
    public static final String password = "";

    public static final String link = "jdbc:mysql://" + hostname + ":" + portNumber + "/" + databaseName;

    //END OF DATABASE ACCESS INITIALIZATION

    static Boolean connection = false;

    public static Connection con = null;
    public static Statement st = null;
    public static ResultSet rs = null;

    private static double displayWidth;
    private static double displayHeight;

    static Stage stage;
    static Scene homeScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        connectDatabase();
        initializeScreenSize();
        homeScene = new Scene(createHomeContent());
        stage = primaryStage;
        stage.setTitle("TraVIS: Traffic Information System");
        stage.setScene(homeScene);
        stage.setFullScreen(true);
//        stage.setFullScreenExitHint("");
        stage.show();

    }

    private void initializeScreenSize() {
        ph.edu.dlsu.ScreenSize screen = new ph.edu.dlsu.ScreenSize();
        displayWidth = screen.getDisplayWidth();
        displayHeight = screen.getDisplayHeight();
    }

    private Parent createHomeContent(){

        Pane rootNode = new Pane();
        rootNode.setPrefSize(displayWidth, displayHeight);

        ph.edu.dlsu.MenuHBox menuBox;

//LOADING ONLINE IMAGE
        String imageUrl = regularBG;
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
//        ImageView imgBackground = ph.edu.dlsu.Utils.loadImage2View(regularBG, displayWidth, displayHeight);
//        if(imgBackground != null){
//            rootNode.getChildren().add(imgBackground);
//        }
//END OF LOADING LOCAL IMAGE

        final CustomMenuItem about = new CustomMenuItem("About");
        final CustomMenuItem facts = new CustomMenuItem("facts");
        final CustomMenuItem graph = new CustomMenuItem("graph");
        final CustomMenuItem close = new CustomMenuItem("close");

        about.setOnMouseClicked(event -> {
            onAbout();
        });

        facts.setOnMouseClicked(event -> {
            onFacts();
        });

        graph.setOnMouseClicked(event -> {
            onGraph();
        });

        close.setOnMouseClicked(event ->{
            onExit();
        });

        menuBox = new ph.edu.dlsu.MenuHBox(about, facts, graph, close);
//        menuBox.setTranslateX(725); //Use this if menus are to be located beside the school logo
        menuBox.setTranslateX((displayWidth/2) - (200));
        menuBox.setTranslateY(630);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Button Sbtn = new Button("search");
        HBox ShbBtn = new HBox();
        ShbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        ShbBtn.getChildren().add(Sbtn);
        grid.add(ShbBtn, 3, 1);

        TextField plate = new TextField();
        plate.setPromptText("Enter a plate number");
        grid.add(plate, 1, 1);

        final ComboBox violation = new ComboBox();

        try{
//            String query = "select * from violators";
            String query = "select distinct Violation from violators";
            rs = st.executeQuery(query);
            while(rs.next()){
                String vio = rs.getString("Violation");
                violation.getItems().add(WordUtils.capitalize(vio));
            }
        }catch(Exception ex){
            System.out.println("Error accessing the table: " + ex);
        }

        if(connection) {
            violation.getItems().add(
                    "All Violations"
            );
        }

        violation.setValue("Select a violation");
        grid.add(violation, 2, 1);

        Sbtn.setOnAction(event ->{
            if (connection) {
                Display display = new Display(plate, violation);
                stage.setTitle("TraVIS: Results");
                stage.setScene(
                        new Scene(display.main(), displayWidth, displayHeight)
                );
                stage.setFullScreen(true);                                  //Set the stage in fullscreen mode
                stage.setFullScreenExitHint("");
            }
        });

        grid.setTranslateX(455);
        grid.setTranslateY(325);

        if (connection == true){
            Label updateMessage = new Label();
            updateMessage.setText("Connected to server!");
            updateMessage.setTranslateX(620);
            updateMessage.setTranslateY(480);
            updateMessage.setTextAlignment(TextAlignment.CENTER);
            updateMessage.setTextFill(FIREBRICK);
            FadeTransition fader = createFader(updateMessage);

            SequentialTransition fade = new SequentialTransition(
                    updateMessage,
                    fader
            );

            fade.play();
            rootNode.getChildren().addAll(grid, menuBox, updateMessage);
        }
        else {
            Label updateMessage = new Label();
            updateMessage.setText("Cannot connect to the server!\nSorry for the inconvenience!");
            updateMessage.setTranslateX(620);
            updateMessage.setTranslateY(480);
            updateMessage.setTextAlignment(TextAlignment.CENTER);
            updateMessage.setTextFill(FIREBRICK);
            FadeTransition fader = createFader(updateMessage);

            SequentialTransition fade = new SequentialTransition(
                    updateMessage,
                    fader
            );

            fade.play();
            rootNode.getChildren().addAll(grid, menuBox, updateMessage);
        }

        return rootNode;
    }

    public static void onHome(){
        stage.setTitle("TraVIS: Traffic Information System");
        stage.setScene(homeScene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
    }

    public static void onAbout(){
        ph.edu.dlsu.About about = new ph.edu.dlsu.About();
        stage.setTitle("TraVIS: About");
        stage.setScene(
                new Scene(about.main(), displayWidth, displayHeight)
        );
        stage.setFullScreen(true);                                  //Set the stage in fullscreen mode
        stage.setFullScreenExitHint("");
    }

    public static void onFacts(){
        ph.edu.dlsu.Facts facts = new ph.edu.dlsu.Facts();
        stage.setTitle("TraVIS: Facts");
        stage.setScene(
                new Scene(facts.main(), displayWidth, displayHeight)
        );
        stage.setFullScreen(true);                                  //Set the stage in fullscreen mode
        stage.setFullScreenExitHint("");
    }

    public static void onGraph(){
        if (connection) {
            ph.edu.dlsu.Graph graph = new ph.edu.dlsu.Graph();
            stage.setTitle("TraVIS: Graph");
            stage.setScene(
                    new Scene(graph.main(), displayWidth, displayHeight)
            );
            stage.setFullScreen(true);                                  //Set the stage in fullscreen mode
            stage.setFullScreenExitHint("");
        }
    }

    public static void connectDatabase(){

        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(link,username,password);
            st = con.createStatement();
            connection = true;

        }catch(Exception ex){
            connection = false;
        }

    }

    public static void onExit() {
        Platform.exit();
    }

    private static FadeTransition createFader(Node node){
        FadeTransition fade = new FadeTransition(Duration.seconds(10), node);
        fade.setFromValue(100);
        fade.setToValue(0);

        return fade;
    }

    public static void main(String[] args) {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

}

