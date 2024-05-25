
module it.polimi.is24am05 {
        requires javafx.controls;
        requires javafx.fxml;

        requires org.controlsfx.controls;
        requires java.rmi;

        opens it.polimi.is24am05 to javafx.fxml;
        exports it.polimi.is24am05.client.model;
        exports  it.polimi.is24am05.client;

        exports it.polimi.is24am05.client.view.gui.controllers;
        opens it.polimi.is24am05.client.view.gui.controllers to javafx.fxml;
        exports it.polimi.is24am05.client.view.gui;

        opens it.polimi.is24am05.client.view.gui to javafx.fxml;
        
        exports it.polimi.is24am05.client.rmi to java.rmi;
        exports it.polimi.is24am05.controller.server.rmi to java.rmi;
}