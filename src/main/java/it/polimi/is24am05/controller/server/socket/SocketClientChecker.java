package it.polimi.is24am05.controller.server.socket;

import java.util.Map;
import java.util.UUID;

public class SocketClientChecker implements Runnable {
    private final SocketClientHandler clientHandler;
    private final int millisecondsToWait;

    private final Object lock = new Object();

    public SocketClientChecker(SocketClientHandler clientHandler, int millisecondsToWait) {
        this.clientHandler = clientHandler;
        this.millisecondsToWait = millisecondsToWait;
    }

    @Override
    public void run() {
        String id = UUID.randomUUID().toString();
        clientHandler.send(new Message("ping", Map.of("id", id)));
        try {
            synchronized (lock) {
                lock.wait(millisecondsToWait);
            }
        } catch (InterruptedException e) {
            // The user quits normally while the check is being performed
            System.out.println(clientHandler.getNickname() + " disconnected");
        }
        if(!clientHandler.getPong().equals(id)){
            clientHandler.setConnected(false);
            System.out.println(clientHandler.getNickname() + " disconnected");
            clientHandler.getClientChecker().shutdown();
        }
    }
}
