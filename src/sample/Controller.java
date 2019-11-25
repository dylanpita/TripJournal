package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.sql.Connection;
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

        miles_driven.setTextFormatter(textFormatter);
        //gallons_purchased.setTextFormatter(textFormatter);
        //taxes_paid.setTextFormatter(textFormatter);
    }

    private void handleButtonAction(ActionEvent actionEvent) {
        Connection connection = SQLUtil.getConnection();

        if (connection != null) {
            System.out.println("works");
        }
        else {
            System.out.println("doesn't work");
        }
    }
}
