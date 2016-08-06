package ph.edu.dlsu;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.apache.commons.lang.WordUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ph.edu.dlsu.Main.*;

/**
 * Created by ${AenonCunanan} on 24/06/2016.
 */
public class LineGraph {

    MenuHBox menuBox;
    ScreenSize screen = new ScreenSize();
    double displayWidth = screen.getDisplayWidth();
    double displayHeight = screen.getDisplayHeight();

    public static String startDate = "";
    public static String endDate = "";

    final ComboBox violation = new ComboBox();

    final ObservableList<Item> data =
            FXCollections.observableArrayList();

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

        ImageView imgBackground = Utils.loadImage2View(destinationFile, displayWidth, displayHeight);
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

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        final LineChart<String, Number> lc =
                new LineChart<String, Number>(xAxis, yAxis);

        lc.setTitle("Traffic Violation Summary");
        lc.setLegendVisible(false);
        xAxis.setLabel("Time");
        yAxis.setLabel("Number");

        XYChart.Series series = new XYChart.Series();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text select = new Text();
        select.setText("Select a violation:");
        grid.add(select, 1, 0);

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
        grid.add(violation, 1, 1);

        Text text1 = new Text();
        Text text2 = new Text();

        DatePicker datePickerStart = new DatePicker();
        DatePicker datePickerEnd = new DatePicker();

        ToggleGroup group = new ToggleGroup();
        RadioButton button1 = new RadioButton();
        RadioButton button2 = new RadioButton();

        datePickerStart.setDisable(true);
        datePickerEnd.setDisable(true);

        text1.setText("from");
        grid.add(text1, 1, 4);

        datePickerStart.setValue(LocalDate.now());
        datePickerStart.setOnAction(event -> {
            if (button2.isSelected()) {
                startDate = datePickerStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                rangeTimeGraph(series, startDate, endDate);
            }
        });
        grid.add(datePickerStart, 1, 5);

        text2.setText("to");
        grid.add(text2, 1, 6);

        datePickerEnd.setValue(LocalDate.now());
        datePickerEnd.setOnAction(event -> {
            if (button2.isSelected()) {
                startDate = datePickerStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                endDate = datePickerEnd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                rangeTimeGraph(series, startDate, endDate);
            }
        });
        grid.add(datePickerEnd, 1, 7);

        button1.setText("Graph from all time");
        button1.setToggleGroup(group);
        button1.setOnMouseClicked(event -> {
            datePickerStart.setDisable(true);
            datePickerEnd.setDisable(true);
            allTimeGraph(series);
        });
        grid.add(button1, 1, 2);

        button2.setText("Graph from a range of time");
        button2.setToggleGroup(group);
        button2.setOnMouseClicked(event -> {
            datePickerStart.setDisable(false);
            datePickerEnd.setDisable(false);
            startDate = datePickerStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            endDate = datePickerEnd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            rangeTimeGraph(series, startDate, endDate);
        });
        grid.add(button2, 1, 3);

        grid.setTranslateX(displayWidth/40);
        grid.setTranslateY(displayHeight/2 - 50);

        lc.getData().addAll(series);
        lc.setTranslateX((displayWidth/2) - ((displayWidth/2)/2));
//        lc.setTranslateY((displayHeight/4) + (150));
        lc.setTranslateY((displayHeight/7) + (150));
//        lc.setPrefSize(displayWidth/2, displayHeight/2.6);
        lc.setPrefSize(displayWidth/2, displayHeight-390);

        final CustomMenuItem home = new CustomMenuItem("home");
        final CustomMenuItem back = new CustomMenuItem("back");
        final CustomMenuItem close = new CustomMenuItem("close");

        home.setOnMouseClicked(event -> {
            Main.onHome();
        });

        back.setOnMouseClicked(event -> {
            Main.onGraph();
        });

        close.setOnMouseClicked(event ->{
            Main.onExit();
        });

        menuBox = new MenuHBox(home, back, close);
//        menuBox.setTranslateX(725);
        menuBox.setTranslateX((displayWidth/2) - (150));
        menuBox.setTranslateY(630);

        rootNode.getChildren().addAll(menuBox, grid, lc);

        return rootNode;
    }

    private void rangeTimeGraph(XYChart.Series series, String startDate, String endDate) {
        try {
            String vio = (String) violation.getValue();
            String query = "select Violation, count(Violation), Date_Violated from violators where Date_Violated between " +
                    "'" + startDate + "'" + " and " + "'" + endDate + "'" + " group by Violation, Date_Violated order by Date_Violated ASC";
            rs = st.executeQuery(query);
            while (rs.next()) {
                if (rs.getString("Violation").equalsIgnoreCase(vio)) {
                    System.out.println(rs.getString("Violation"));
                    String date = rs.getString("Date_Violated");
                    System.out.println(date);
                    int redundant = rs.getInt(2); //get the count value
                    System.out.println(redundant);
                    data.add(new Item(date));
                    series.getData().add(new XYChart.Data(date, redundant));
                }
            }
        } catch (Exception ex) {
            System.out.println("Error accessing the table: " + ex);
        }
    }

    private void allTimeGraph(XYChart.Series series) {
        try {
            String vio = (String) violation.getValue();
            String query = "select Violation, count(Violation), Date_Violated from violators group by Violation, Date_Violated order by Date_Violated ASC";
            rs = st.executeQuery(query);
            while (rs.next()) {
                if (rs.getString("Violation").equalsIgnoreCase(vio)) {
                    System.out.println(rs.getString("Violation"));
                    String date = rs.getString("Date_Violated");
                    System.out.println(date);
                    int redundant = rs.getInt(2); //get the count value
                    System.out.println(redundant);
                    data.add(new Item(date));
                    series.getData().add(new XYChart.Data(date, redundant));
                }
            }

        } catch (Exception ex) {
            System.out.println("Error accessing the table: " + ex);
        }
    }

    public static class Item {
        private final SimpleStringProperty violation;

        private Item (String violation){
            this.violation = new SimpleStringProperty(violation);
        }

        public String getViolation(){
            return violation.get();
        }

        public void setViolation(String vio){
            violation.set(vio);
        }

    }

}
