module it.polimi.is24am05 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.rmi;

    opens it.polimi.is24am05 to javafx.fxml;
    exports it.polimi.is24am05;
}