module org.openjfx.fierydragons {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    opens org.openjfx.fierydragons.game to com.fasterxml.jackson.databind;

    opens org.openjfx.fierydragons to javafx.fxml;
    exports org.openjfx.fierydragons;
    exports org.openjfx.fierydragons.turnlogic;
    opens org.openjfx.fierydragons.turnlogic to javafx.fxml;
    exports org.openjfx.fierydragons.entities;
    opens org.openjfx.fierydragons.entities to javafx.fxml;
    exports org.openjfx.fierydragons.render;
    opens org.openjfx.fierydragons.render to javafx.fxml, com.fasterxml.jackson.databind;
}