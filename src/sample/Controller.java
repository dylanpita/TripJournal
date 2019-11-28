package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class Controller implements Initializable {

    @FXML
    Button submit_Button;
    @FXML
    TextField truck_number;
    @FXML
    TextField driver_number;
    @FXML
    TextField co_driver_number;
    @FXML
    DatePicker depart_date;
    @FXML
    DatePicker return_date;
    @FXML
    TextField state_code;
    @FXML
    TextField station_location;
    @FXML
    TextField station_name;
    @FXML
    TextField miles_driven;
    @FXML
    TextField gallons_purchased;
    @FXML
    TextField taxes_paid;
    @FXML
    TextField trip_number;
    @FXML
    TextField fuel_receipt_number;

    @FXML
    Tab detailReportTab;
    @FXML
    TableView detailTable;

    @FXML
    Button search_Button;
    @FXML
    TextField truck_Search;
    @FXML
    TableView exceptionReportTable;

    @FXML
    Tab summaryReportTab;
    @FXML
    TableView averagesReportTable;
    @FXML
    TableView totalsReportTable;

    private ObservableList<ObservableList> data;

    UnaryOperator<TextFormatter.Change> filter = change -> {
        String text = change.getText();

        if (text.matches("-?\\d+(?:\\.\\d+)?")) {
            return change;
        }

        return null;
    };
    TextFormatter<String> textFormatter = new TextFormatter<>(filter);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submit_Button.setOnAction(this::handleButtonAction);
        detailReportTab.setOnSelectionChanged(this::handleDetailReport);
        summaryReportTab.setOnSelectionChanged(this::handleSummaryReport);
        search_Button.setOnAction(this::handleSearchAction);
        miles_driven.setTextFormatter(textFormatter);
        //gallons_purchased.setTextFormatter(textFormatter);
        //taxes_paid.setTextFormatter(textFormatter);
    }

    private void handleSearchAction(ActionEvent actionEvent){
        Connection c = SQLUtil.getConnection();

        if (c != null) {
            System.out.println("connection works");
            data = FXCollections.observableArrayList();
            try{
                c = SQLUtil.getConnection();
                //SQL FOR SELECTING ALL RECORDS THAT MATCH THE TRUCK_NUMBER ENTERED
                String SQL = "SELECT * from trip_journal WHERE TRUCK_NUM='" + truck_Search.getText() + "'";
                //ResultSet
                ResultSet rs = c.createStatement().executeQuery(SQL);

                for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });

                    exceptionReportTable.getColumns().addAll(col);
                    System.out.println("Column ["+i+"] ");
                }

                while(rs.next()){
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                        //Iterate Column
                        row.add(rs.getString(i));
                    }
                    System.out.println("Row [1] added "+row );
                    data.add(row);
                }
                //FINALLY ADDED TO TableView
                exceptionReportTable.setItems(data);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else {
            System.out.println("connection doesn't work");
        }
    }
    private void handleSummaryReport(Event actionEvent) {
        Connection c ;
        data = FXCollections.observableArrayList();
        try{
            c = SQLUtil.getConnection();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT round(sum(GALLONS_PURCHASED), 2) as Total_Gallons_Purchased," +
                    " round(sum(TAXES_PAID), 2) as Total_Taxes_Paid," +
                    " round(sum(MILES_DRIVEN),2) as Total_Miles_Driven from trip_journal";
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                totalsReportTable.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            totalsReportTable.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }

        data = FXCollections.observableArrayList();
        try{
            c = SQLUtil.getConnection();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT round(sum(GALLONS_PURCHASED) / COUNT(GALLONS_PURCHASED), 2) as AVG_Gallons_Purchased," +
                    " round(sum(TAXES_PAID) / COUNT(TAXES_PAID), 2) as AVG_Taxes_Paid," +
                    " round(sum(MILES_DRIVEN) / COUNT(MILES_DRIVEN),2) as AVG_Miles_Driven from trip_journal";
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                averagesReportTable.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            averagesReportTable.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    private void handleDetailReport(Event actionEvent) {
        Connection c ;
        data = FXCollections.observableArrayList();
        try{
            c = SQLUtil.getConnection();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from trip_journal";
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                detailTable.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            detailTable.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    private void handleButtonAction(ActionEvent actionEvent) {
        Connection connection = SQLUtil.getConnection();

        if (connection != null) {

            System.out.println("connection works");
            tripJournal t = new tripJournal(truck_number.getText(), driver_number.getText(), co_driver_number.getText(), java.sql.Date.valueOf(depart_date.getValue()), java.sql.Date.valueOf(return_date.getValue()), state_code.getText(), Integer.parseInt(miles_driven.getText()), fuel_receipt_number.getText(), Double.parseDouble(gallons_purchased.getText()), Double.parseDouble(taxes_paid.getText()), station_name.getText(), station_location.getText(), trip_number.getText());
            try {
                tripJournal.INSERT(t);
                truck_number.clear();
                driver_number.clear();
                co_driver_number.clear();
                depart_date.getEditor().clear();
                return_date.getEditor().clear();
                miles_driven.clear();
                gallons_purchased.clear();
                taxes_paid.clear();
                fuel_receipt_number.clear();
                trip_number.clear();

            } catch (SQLException e) {
                System.out.println("Insert Failed");
            }
        }
        else {
            System.out.println("connection doesn't work");
        }
    }
}
