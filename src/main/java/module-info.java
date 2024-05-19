module it.polimi.is24am05 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.rmi;

    opens it.polimi.is24am05 to javafx.fxml;
    exports it.polimi.is24am05;

    exports it.polimi.is24am05.client.rmi to java.rmi;
    exports it.polimi.is24am05.controller.server.rmi to java.rmi;
}