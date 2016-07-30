package ph.edu.dlsu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static ph.edu.dlsu.Main.link;
import static ph.edu.dlsu.Main.password;
import static ph.edu.dlsu.Main.username;

/**
 * Created by ${AenonCunanan} on 29/07/2016.
 */
public class DBConnect {

    private Connection con;
    private Statement st;
    private ResultSet rs;


    public DBConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection(link,username,password);
            st = con.createStatement();

//            Main.internet = true;

        }catch(Exception ex){
//            System.out.println("Cannot connect to the server!\nError: " + ex);
//            Main.internet = true;
        }
    }

    public void getData(){
        try{
            String query = "select * from violators";
            rs = st.executeQuery(query);
            System.out.println("Records from database:\n");
            while(rs.next()){
                String plate = rs.getString("Plate Number");
                String violation = rs.getString("Violation");
                String vcolor = rs.getString("Vehicle Color");
                String vclass = rs.getString("Vehicle class");
                String date = rs.getString("Date Violated");
                String time = rs.getString("Time Violated");
                String location = rs.getString("Location Violated");
                String penalty = rs.getString("Penalty");
                System.out.println("" +
                        "Plate Number: " + plate  +
                        "\nViolation: " + violation +
                        "\nVehicle Color: " + vcolor +
                        "\nVehicle Class: " + vclass +
                        "\nDate Violated: " + date +
                        "\nTime Violated: " + time +
                        "\nLocation Violated: " + location +
                        "\nPenalty: " + penalty +
                        "\n"
                );
            }
        }catch(Exception ex){
            System.out.println("Error accessing the table: " + ex);
        }
    }

}
