module it.polimi.is24am05 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens it.polimi.is24am05.is24am05 to javafx.fxml;
    exports it.polimi.is24am05;
    opens it.polimi.is24am05 to javafx.fxml;
}