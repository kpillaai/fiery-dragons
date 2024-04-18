module org.openjfx.fierydragons {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.openjfx.fierydragons to javafx.fxml;
    exports org.openjfx.fierydragons;
    exports org.openjfx.fierydragons.turnlogic;
    opens org.openjfx.fierydragons.turnlogic to javafx.fxml;
    exports org.openjfx.fierydragons.entities;
    opens org.openjfx.fierydragons.entities to javafx.fxml;
    exports org.openjfx.fierydragons.game;
    opens org.openjfx.fierydragons.game to javafx.fxml;
}