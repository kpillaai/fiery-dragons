module org.openjfx.fierydragons {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.openjfx.fierydragons to javafx.fxml;
    exports org.openjfx.fierydragons;
}