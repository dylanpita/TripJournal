package sample;
import java.util.*;
import java.sql.*;

public class tripJournal {
    private String Truck_num, Driver_num, Co_driver_num, State_Code, Fuel_Receipt_Number, Station_Name, Station_Location, Trip_Number;
    private java.util.Date Depart_date, Return_Date;
    private int Miles_Driven;
    private double Gallons_Purchased, Taxes_Paid;

    public tripJournal(String Truck_num, String Driver_num, String Co_driver_num, java.util.Date Depart_date, java.util.Date Return_Date, String State_Code, int Miles_Driven, String Fuel_Receipt_Number, double Gallons_Purchased, double Taxes_Paid, String Station_Name, String Station_Location, String Trip_Number) {
        this.Truck_num = Truck_num;
        this.Driver_num = Driver_num;
        this.Co_driver_num = Co_driver_num;
        this.Depart_date = Depart_date;
        this.Return_Date = Return_Date;
        this.State_Code = State_Code;
        this.Miles_Driven = Miles_Driven;
        this.Fuel_Receipt_Number = Fuel_Receipt_Number;
        this.Gallons_Purchased = Gallons_Purchased;
        this.Taxes_Paid = Taxes_Paid;
        this.Station_Name = Station_Name;
        this.Station_Location = Station_Location;
        this.Trip_Number = Trip_Number;
    }

    @Override
    public String toString() {
        return Truck_num + " " + Driver_num + " " + Co_driver_num + " " + State_Code + " " + Fuel_Receipt_Number + " " + Station_Name + " " + Station_Location + " " + Trip_Number + " " + Depart_date + " " + Return_Date + " " + Miles_Driven + " " + Gallons_Purchased + " " + Taxes_Paid;
    }

    public String getTruck_num() {
        return Truck_num;
    }

    public String getDriver_num() {
        return Driver_num;
    }

    public String getCo_driver_num() {
        return Co_driver_num;
    }

    public String getState_Code() {
        return State_Code;
    }

    public String getFuel_Receipt_Number() {
        return Fuel_Receipt_Number;
    }

    public String getStation_Name() {
        return Station_Name;
    }

    public String getStation_Location() {
        return Station_Location;
    }

    public String getTrip_Number() {
        return Trip_Number;
    }

    public java.util.Date getDepart_date() {
        return Depart_date;
    }

    public java.util.Date getReturn_Date() {
        return Return_Date;
    }

    public int getMiles_Driven() {
        return Miles_Driven;
    }

    public double getGallons_Purchased() {
        return Gallons_Purchased;
    }

    public double getTaxes_Paid() {
        return Taxes_Paid;
    }



    public static void INSERT(tripJournal trip) throws SQLException {
        Connection connection = SQLUtil.getConnection();
        Statement statement = connection.createStatement();
        String sql = "insert into trip_journal (TRUCK_NUM, DRIVER_NUM, CO_DRIVER_NUM,DEPART_DATE,RETURN_DATE,STATE_CODE,MILES_DRIVEN,FUEL_RECEIPT_NUMBER,GALLONS_PURCHASED,TAXES_PAID,STATION_NAME,STATION_LOCATION,TRIP_NUMBER) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, trip.Truck_num);
        ps.setString(2, trip.Driver_num);
        ps.setString(3, trip.Co_driver_num);
        ps.setDate(4, (java.sql.Date) trip.Depart_date);
        ps.setDate(5, (java.sql.Date) trip.Return_Date);
        ps.setString(6, trip.State_Code);
        ps.setInt(7, trip.Miles_Driven);
        ps.setString(8, trip.Fuel_Receipt_Number);
        ps.setDouble(9, trip.Gallons_Purchased);
        ps.setDouble(10, trip.Taxes_Paid);
        ps.setString(11, trip.Station_Name);
        ps.setString(12, trip.Station_Location);
        ps.setString(13, trip.Trip_Number);
        ps.execute();
        SQLUtil.closeStatement(statement);
        connection.close();
    }

    public static ArrayList SELECT_ALL() throws SQLException {
        Connection connection = SQLUtil.getConnection();
        Statement statement = connection.createStatement();
        String sqlStatement = "select * from products";
        ArrayList<tripJournal> a = new ArrayList<>();
        ResultSet RS = statement.executeQuery(sqlStatement);
        tripJournal trip;
        while (RS.next()) {
            trip = new tripJournal(RS.getString(1), RS.getString(2), RS.getString(3), RS.getDate(4), RS.getDate(5), RS.getString(6), RS.getInt(7), RS.getString(8), RS.getDouble(9), RS.getDouble(10), RS.getString(11), RS.getString(12), RS.getString(13));
            a.add(trip);
        }
        return a;
    }

    public static double getSummary() throws SQLException {
        ArrayList<tripJournal> a = SELECT_ALL();
        double totTax = 0;
        double tot_miles_driven = 0;
        double tot_gallons_purchased = 0;
        int avg = 0;
        for (tripJournal trip : a) {
            tot_gallons_purchased += trip.getGallons_Purchased();
            totTax += trip.getTaxes_Paid();
            tot_miles_driven += trip.getMiles_Driven();
        }
        //create array of doubles containing the computer summary reports
        return avg;
    }
}
