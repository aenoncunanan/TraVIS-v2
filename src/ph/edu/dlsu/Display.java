package ph.edu.dlsu;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang.WordUtils;

import java.io.File;
import java.io.IOException;

import static ph.edu.dlsu.Main.*;

/**
 * Created by ${AenonCunanan} on 26/06/2016.
 */

public class Display {

    MenuHBox menuBox;
    ScreenSize screen = new ScreenSize();
    double displayWidth = screen.getDisplayWidth();
    double displayHeight = screen.getDisplayHeight();

    private String inputCombination = "";

    String plateNumber = "";
    String trafficViolation = "";
    String vehicleClass = "";
    String vehicleColor = "";
    String date = "";
    String time = "";
    String location = "";
    String penalty = "";

    final TableView table = new TableView();

    final ObservableList<Item> data =
            FXCollections.observableArrayList();

    public Display(TextField plate, ComboBox violation) {

        //no violation, no number
        if ((violation.getValue() == "Select a violation") && (plate.getText().isEmpty())){
            inputCombination = "00";
        }

        //no violation, with number //all violation, with number
        else if (((violation.getValue() == "Select a violation") || (violation.getValue() == "All Violations")) && (!plate.getText().isEmpty())){
            inputCombination = "01";
            plateNumber = plate.getText();
        }

        //with violation, no number
        else if (((violation.getValue() != "Select a violation") && (violation.getValue() != "All Violations")) && (plate.getText().isEmpty())){
            inputCombination = "10";
            trafficViolation = (String) violation.getValue();
        }

        //all violation, no number
        else if (violation.getValue() == "All Violations" && plate.getText().isEmpty()){
            inputCombination = "110";
        }

        //with violation, with number
        else {
            inputCombination = "11";
            plateNumber = plate.getText();
            trafficViolation = (String) violation.getValue();
        }

    }

    public Parent main(){
        Pane rootNode = new Pane();
        rootNode.setPrefSize(displayWidth, displayHeight);

//LOADING ONLINE IMAGE
        String imageUrl = adjustedBG;
        String destinationFile = "image2.jpg";

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
//        ImageView imgBackground = Utils.loadImage2View(Main.adjustedBG, displayWidth, displayHeight);
//        if (imgBackground != null) {
//            rootNode.getChildren().add(imgBackground);
//        }
//END OF LOADING LOCAL IMAGE

        switch (inputCombination){
            case "00": // no violation, no plate number
                break;
            case "01": // no violation, with plate number // all violation, with number

                try{
                    String query = "select * from violators";
                        rs = st.executeQuery(query);
                        while(rs.next()){
                            if (rs.getString("Plate Number").equals(plateNumber)){
                                trafficViolation = rs.getString("Violation");
                                vehicleClass = rs.getString("Vehicle Class");
                                vehicleColor = rs.getString("Vehicle Color");
                                date = rs.getString("Date_Violated");
                                time = rs. getString("Time Violated");
                                location = rs.getString("Location Violated");
                                penalty = rs.getString("Penalty");

                                data.add(new Item(trafficViolation, plateNumber, vehicleClass, vehicleColor, date, time, location, penalty));
                            }
                        }
                    }catch(Exception ex){
                        System.out.println("Error accessing the table: " + ex);
                    }

                break;
            case "10": // with violation, no plate number

                try{
                    String query = "select * from violators";
                    rs = st.executeQuery(query);
                    while(rs.next()){
                        if (rs.getString("Violation").equalsIgnoreCase(trafficViolation)){
                            plateNumber = rs.getString("Plate Number");
                            vehicleClass = rs.getString("Vehicle Class");
                            vehicleColor = rs.getString("Vehicle Color");
                            date = rs.getString("Date_Violated");
                            time = rs. getString("Time Violated");
                            location = rs.getString("Location Violated");
                            penalty = rs.getString("Penalty");

                            data.add(new Item(trafficViolation, plateNumber, vehicleClass, vehicleColor, date, time, location, penalty));
                        }
                    }
                }catch(Exception ex){
                    System.out.println("Error accessing the table: " + ex);
                }

                break;
            case "11": // with violation, with plate number

                try{
                    String query = "select * from violators";
                    rs = st.executeQuery(query);
                    while(rs.next()){
                        if (rs.getString("Violation").equalsIgnoreCase(trafficViolation) && rs.getString("Plate Number").equalsIgnoreCase(plateNumber)){
                            vehicleClass = rs.getString("Vehicle Class");
                            vehicleColor = rs.getString("Vehicle Color");
                            date = rs.getString("Date_Violated");
                            time = rs. getString("Time Violated");
                            location = rs.getString("Location Violated");
                            penalty = rs.getString("Penalty");

                            data.add(new Item(trafficViolation, plateNumber, vehicleClass, vehicleColor, date, time, location, penalty));
                        }
                    }
                }catch(Exception ex){
                    System.out.println("Error accessing the table: " + ex);
                }

                break;
            case "110":// All violations, no number

                try{
                    String query = "select * from violators";
                    rs = st.executeQuery(query);
                    while(rs.next()){
                            trafficViolation = rs.getString("Violation");
                            plateNumber = rs.getString("Plate Number");
                            vehicleClass = rs.getString("Vehicle Class");
                            vehicleColor = rs.getString("Vehicle Color");
                            date = rs.getString("Date_Violated");
                            time = rs. getString("Time Violated");
                            location = rs.getString("Location Violated");
                            penalty = rs.getString("Penalty");

                            data.add(new Item(trafficViolation, plateNumber, vehicleClass, vehicleColor, date, time, location, penalty));
                    }
                }catch(Exception ex){
                    System.out.println("Error accessing the table: " + ex);
                }

                break;
        }

        final CustomMenuItem home = new CustomMenuItem("home");
        final CustomMenuItem facts = new CustomMenuItem("facts");
        final CustomMenuItem about = new CustomMenuItem("about");
        final CustomMenuItem close = new CustomMenuItem("close");

        home.setOnMouseClicked(event -> {
            Main.onHome();
        });

        facts.setOnMouseClicked(event -> {
            Main.onFacts();
        });

        about.setOnMouseClicked(event -> {
            Main.onAbout();
        });

        close.setOnMouseClicked(event ->{
            Main.onExit();
        });

        menuBox = new MenuHBox(home, facts, about, close);
//        menuBox.setTranslateX(725);
        menuBox.setTranslateX((displayWidth/2) - (200));
        menuBox.setTranslateY(630);

        TableColumn vio = new TableColumn("VIOLATION");
        vio.setCellValueFactory(new PropertyValueFactory<>("violation"));
        vio.setPrefWidth(150);

        TableColumn num = new TableColumn("PLATE NUMBER");
        num.setCellValueFactory(new PropertyValueFactory<>("number"));
        num.setPrefWidth(115);

        TableColumn vclass = new TableColumn("VEHICLE CLASS");
        vclass.setCellValueFactory(new PropertyValueFactory<>("vclass"));
        vclass.setPrefWidth(110);

        TableColumn color = new TableColumn("VEHICLE COLOR");
        color.setCellValueFactory(new PropertyValueFactory<>("color"));
        color.setPrefWidth(100);

        TableColumn date = new TableColumn("DATE VIOLATED");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        date.setPrefWidth(107);

        TableColumn time = new TableColumn("TIME VIOLATED");
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        time.setPrefWidth(100);

        TableColumn location = new TableColumn("LOCATION");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        location.setPrefWidth(100);

        TableColumn penalty = new TableColumn("PENALTY");
        penalty.setCellValueFactory(new PropertyValueFactory<>("penalty"));
        penalty.setPrefWidth(100);

        table.getColumns().addAll(vio, num, vclass, color, date, time, location, penalty);
//        table.getColumns().addAll(vio, num, vclass, color, date, time);
        table.setItems(data);
        table.setPrefSize(displayWidth/1.55, displayHeight/2.5);
//        table.setPrefSize(displayWidth/2, displayHeight/3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        vbox.setTranslateX((displayWidth/2)-450);
//        vbox.setTranslateX((displayWidth/2)-350);
        vbox.setTranslateY(295);

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
                String vioList = rs.getString("Violation");
                violation.getItems().add(WordUtils.capitalize(vioList));
            }
        }catch(Exception ex){
            System.out.println("Error accessing the table: " + ex);
        }
        if (connection) {
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
        grid.setTranslateY(230);

        rootNode.getChildren().addAll(menuBox, vbox, grid);

        return rootNode;
    }

    public static class Item {

        private final SimpleStringProperty violation;
        private final SimpleStringProperty number;
        private final SimpleStringProperty vclass;
        private final SimpleStringProperty color;
        private final SimpleStringProperty date;
        private final SimpleStringProperty time;
        private final SimpleStringProperty location;
        private final SimpleStringProperty penalty;

        private Item(String violation, String number, String vclass, String color, String date, String time, String location, String penalty) {
            this.violation = new SimpleStringProperty(violation);
            this.number = new SimpleStringProperty(number);
            this.vclass = new SimpleStringProperty(vclass);
            this.color = new SimpleStringProperty(color);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.location = new SimpleStringProperty(location);
            this.penalty = new SimpleStringProperty(penalty);
        }

        public String getViolation() {
            return violation.get();
        }

        public void setViolation(String vio) {
            violation.set(vio);
        }

        public String getVclass(){
            return vclass.get();
        }

        public void setVclass(String vio){
            vclass.set(vio);
        }

        public String getNumber() {
            return number.get();
        }

        public void setNumber(String vio) {
            number.set(vio);
        }

        public String getColor() {
            return color.get();
        }

        public void setColor(String vio) {
            color.set(vio);
        }

        public String getDate() {
            return date.get();
        }

        public void setDate(String vio) {
            date.set(vio);
        }

        public String getTime() {
            return time.get();
        }

        public void setTime(String vio) {
            time.set(vio);
        }

        public String getLocation() {
            return location.get();
        }

        public void setLocation(String vio) {
            location.set(vio);
        }

        public String getPenalty() {
            return penalty.get();
        }

        public void setPenalty(String vio) {
            penalty.set(vio);
        }

    }

}
