package com.thread_csv_parser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TableView<Human> people_table;

    @FXML
    private TableColumn<Human, String> first_name, last_name, email, image_link, ip_address;

    @FXML
    private TableView<Human> people_table_thread;

    @FXML
    private TableColumn<Human, String> first_name_thread, last_name_thread, email_thread, image_link_thread, ip_address_thread;


    private ObservableList<Human> original_data_list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        first_name.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        last_name.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        image_link.setCellValueFactory(new PropertyValueFactory<>("image_link"));
        ip_address.setCellValueFactory(new PropertyValueFactory<>("ip_address"));

        String filename = "src\\main\\resources\\com\\thread_csv_parser\\MOCK_DATA.csv";
        readData(filename);
        people_table.setItems(original_data_list);
    }

    void readData(String filename) {
        original_data_list = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                original_data_list.add(new Human(data[0], data[1], data[2], data[3], data[4]));
                System.out.println(data[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}