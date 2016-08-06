package ph.edu.dlsu;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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

import static ph.edu.dlsu.Main.adjustedBG;
import static ph.edu.dlsu.Main.rs;
import static ph.edu.dlsu.Main.st;

/**
 * Created by ${AenonCunanan} on 24/06/2016.
 */
public class BarGraph {

    MenuHBox menuBox;
    ScreenSize screen = new ScreenSize();
    double displayWidth = screen.getDisplayWidth();
    double displayHeight = screen.getDisplayHeight();

    public static String startDate = "";
    public static String endDate = "";


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

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        final BarChart<String, Number> bc =
                new BarChart<String, Number>(xAxis, yAxis);

        bc.setTitle("Traffic Violation Summary");
        bc.setLegendVisible(false);
        xAxis.setLabel("Violation");
        yAxis.setLabel("Number");

        XYChart.Series series = new XYChart.Series();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

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
        grid.add(text1, 1, 3);

        datePickerStart.setValue(LocalDate.now());
        datePickerStart.setOnMouseClicked(event -> {
            if (button2.isSelected()) {
                startDate = datePickerStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                rangeTimeGraph(series, startDate, endDate);
            }
        });
        grid.add(datePickerStart, 1, 4);

        text2.setText("to");
        grid.add(text2, 1, 5);

        datePickerEnd.setValue(LocalDate.now());
        datePickerEnd.setOnMouseClicked(event -> {
            if (button2.isSelected()) {
                startDate = datePickerStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                endDate = datePickerEnd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                rangeTimeGraph(series, startDate, endDate);
            }
        });
        grid.add(datePickerEnd, 1, 6);

        button1.setText("Graph from all time");
        button1.setToggleGroup(group);
        button1.setOnMouseClicked(event -> {
            datePickerStart.setDisable(true);
            datePickerEnd.setDisable(true);
            allTimeGraph(series);
        });
        grid.add(button1, 1, 1);

        button2.setText("Graph from a range of time");
        button2.setToggleGroup(group);
        button2.setOnMouseClicked(event -> {
            datePickerStart.setDisable(false);
            datePickerEnd.setDisable(false);
            startDate = datePickerStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            endDate = datePickerEnd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            rangeTimeGraph(series, startDate, endDate);
        });
        grid.add(button2, 1, 2);

        grid.setTranslateX(displayWidth/40);
        grid.setTranslateY(displayHeight/2 - 50);

        bc.getData().addAll(series);
        bc.setTranslateX((displayWidth/2) - ((displayWidth/2)/2));
//        bc.setTranslateY((displayHeight/4) + (150));
        bc.setTranslateY((displayHeight/7) + (150));
//        bc.setPrefSize(displayWidth/2, displayHeight/2.6);
        bc.setPrefSize(displayWidth/2, displayHeight-390);

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

        rootNode.getChildren().addAll(menuBox, grid, bc);

        return rootNode;
    }

    private void rangeTimeGraph(XYChart.Series series, String startDate, String endDate) {
        try {
            String query = "select Violation, count(Violation) from violators where Date_Violated between " +
                    "'" + startDate + "'" + " and " + "'" + endDate + "'" + " group by Violation";
            rs = st.executeQuery(query);
            while (rs.next()) {
                String trafficVio = rs.getString("Violation");
                int redundant = rs.getInt(2); //2 is the index of Violation column
                data.add(new Item(WordUtils.capitalize(trafficVio)));
                series.getData().add(new XYChart.Data(WordUtils.capitalizeFully(trafficVio), redundant));
            }
        } catch (Exception ex) {
            System.out.println("Error accessing the table: " + ex);
        }
    }

    private void allTimeGraph(XYChart.Series series) {
        try {
            String query = "select Violation, count(Violation) from violators group by Violation";
            rs = st.executeQuery(query);
            while (rs.next()) {
                String trafficVio = rs.getString("Violation");
                int redundant = rs.getInt(2); //2 is the index of Violation column
                data.add(new Item(WordUtils.capitalize(trafficVio)));
                series.getData().add(new XYChart.Data(WordUtils.capitalizeFully(trafficVio), redundant));
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
