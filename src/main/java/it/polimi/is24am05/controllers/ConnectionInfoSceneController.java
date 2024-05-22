package it.polimi.is24am05.controllers;

import it.polimi.is24am05.GUI;
import it.polimi.is24am05.GUIMain;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionInfoSceneController implements Initializable {



  private GUI gui;

    @FXML
    private TextField serverIpAddress;

    @FXML
    private TextField serverPort;

    @FXML
    private ComboBox<String> connectionTypeBox;

    @FXML
    private Button confirmButton;

    public void setGUI(GUI gui)
    {
        this.gui=gui;
    }
    @Override
    public void initialize (URL url, ResourceBundle resources) {

        connectionTypeBox.setItems(FXCollections.observableArrayList("Socket", "RMI"));
        serverPort.setPromptText("enter Server Port Number");
        serverIpAddress.setPromptText("enter Server Ip address ");
    }

    private String getPlayerIpAddress() {
        return serverIpAddress.getText();
    }

    private String getPlayerSocket() {
        return serverPort.getText();
    }

    /**
     * Method activated by the ActionEvent on the button.
     * Try to connect to the server.
     * @param event The event on the button.
     */
    @FXML
    public void askServerInformation(javafx.event.ActionEvent event) throws IOException {
        String ipAddress, defaultIpAddress = "127.0.0.1";
        int port;
        boolean rmiConnection;

        // Insert and verify the IP address and the port
        serverIpAddress.setStyle("-fx-text-fill: red;");
        serverPort.setStyle("-fx-text-fill: red;");

        if (getPlayerIpAddress().equalsIgnoreCase(""))
            ipAddress = defaultIpAddress;
        else
            ipAddress = getPlayerIpAddress();

        if (getPlayerSocket().equals(""))
            port =1024;
        else {
            try {
                port = Integer.parseInt(getPlayerSocket());
            } catch (NumberFormatException e) {
                port = 1024;
            }
        }

        rmiConnection = !connectionTypeBox.getValue().equalsIgnoreCase("Socket");
        gui.askNickname();




    }


}
