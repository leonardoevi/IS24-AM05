package it.polimi.is24am05;

import it.polimi.is24am05.client.rmi.RmiServerHandler;
import it.polimi.is24am05.client.socket.SocketServerHandler;
import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.model.game.Game;

import java.io.IOException;

import static it.polimi.is24am05.controller.Controller.loadGame;

public class Main {
    public static void main(String[] args) {
        try{
            String type = args[0];

            if(type.equals("client")){
                String connection = args[1];
                String ip = args[2];
                String port = args[3];
                String view = args[4];

                if(connection.equals("rmi")){
                    new RmiServerHandler(ip, port, view);
                } else {
                    new SocketServerHandler(ip, port, view);
                }


            } else {
                try {
                    Game oldGame = loadGame(args[1]);
                    System.out.println("Old game loaded");
                    new Controller(oldGame);
                } catch (IOException | ClassNotFoundException | ArrayIndexOutOfBoundsException e) {
                    // Create a lobby for a new game if the file provided is not found or no file is provided
                    System.out.println("Starting a new game");
                    new Controller();
                }
            }

        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Unexpected parameters");
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
