package com.thread_csv_parser;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Initializable {
    @FXML
    private TableView<Human> people_table;

    @FXML
    private TableColumn<Human, String> first_name, last_name, email, image_link, ip_address;

    @FXML
    private TableView<Human> people_table_thread;

    @FXML
    private TableColumn<Human, String> first_name_thread, last_name_thread, email_thread, image_link_thread, ip_address_thread;

    @FXML
    private TableColumn<Human, Void> action_thread;

    private final static int MAX_THREADS = 10;

    private ObservableList<Human> original_data_list;
    private ObservableList<Human> thread_data_list;


    private final String output_path = "src\\main\\resources\\com\\thread_csv_parser\\converted_files\\";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        first_name.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        last_name.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        image_link.setCellValueFactory(new PropertyValueFactory<>("image_link"));
        ip_address.setCellValueFactory(new PropertyValueFactory<>("ip_address"));

        first_name_thread.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        last_name_thread.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        email_thread.setCellValueFactory(new PropertyValueFactory<>("email"));
        image_link_thread.setCellValueFactory(new PropertyValueFactory<>("image_link"));
        ip_address_thread.setCellValueFactory(new PropertyValueFactory<>("ip_address"));
        //action_thread.setCellValueFactory(new PropertyValueFactory<>("action"));


        String filename = "src\\main\\resources\\com\\thread_csv_parser\\MOCK_DATA.csv";
        readData(filename);
        people_table.setItems(original_data_list);

        action_thread.setCellFactory(column -> {
            TableCell<Human, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        Human human = getTableRow().getItem();

                        getTableView().getItems().remove(human);
                        delete(human);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        });
    }

    void readData(String filename) {
        original_data_list = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                original_data_list.add(new Human(data[0], data[1], data[2], data[3], data[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAll() {
        new Thread(() -> createFiles()).start();
    }

    private void createFiles() {
        thread_data_list = FXCollections.observableArrayList(original_data_list);
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

        for (Human human : thread_data_list) {
            executorService.execute(() -> {

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Platform.runLater(() -> people_table_thread.getItems().add(human));

                String output_filename = output_path + human.getFilename() + ".csv";

                try (FileWriter fileWriter = new FileWriter(output_filename)) {
                    String output = human.getFirst_name() + ";" + human.getLast_name() + ";" + human.getEmail() + ";" + human.getImage_link() + ";" + human.getIp_address();
                    fileWriter.write(output);
                    fileWriter.flush();
                } catch (Exception e) {
                    System.out.println("Error writing data to a file: " + e.getMessage());
                }

                Platform.runLater(() -> people_table.getItems().remove(human));
            });
        }

        executorService.shutdown();
    }

    public void removeAll() {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

        synchronized (thread_data_list) {
            for (Human human : thread_data_list) {
                executorService.execute(() -> {
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }



                    Platform.runLater(() -> {
                        synchronized (people_table_thread) {
                            if (!people_table_thread.getItems().isEmpty()) {
                                people_table_thread.getItems().remove(human);
                            }
                        }
                    });
                    delete(human);
                });
            }
        }
        executorService.shutdown();
    }

    private void delete(Human human){
        original_data_list.add(human);
        //people_table.getItems().add(human);
        String delete_filename = output_path + human.getFilename() + ".csv";
        File delete_file = new File(delete_filename);

        if (delete_file.exists()) {
            if (!delete_file.delete()) {
                System.out.println("Deletion of : " + delete_filename +  " was unsuccessful!");
            }
        } else {
            System.out.println(delete_filename + " does not exist!");
        }
    }
}