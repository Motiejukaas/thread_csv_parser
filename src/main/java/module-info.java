module com.thread_csv_parser {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.thread_csv_parser to javafx.fxml;
    exports com.thread_csv_parser;
}