module org.example.gymbeam4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens org.example.gymbeam4 to javafx.fxml;
    exports org.example.gymbeam4;
}