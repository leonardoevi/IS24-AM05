package it.polimi.is24am05.client;

public abstract class ServerHandler implements VirtualServer{
    private String nickname;

    protected final String serverIP;
    protected final String serverPort;

    protected ServerHandler(String serverIP, String serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    // Methods invoked by the SERVER from the network (better be protected)

    protected void printText(String message){
        System.out.println(nickname + ": " + message);
    }
}
