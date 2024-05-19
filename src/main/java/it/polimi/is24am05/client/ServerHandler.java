package it.polimi.is24am05.client;

import it.polimi.is24am05.client.model.DeckPov;
import it.polimi.is24am05.client.model.GamePov;
import it.polimi.is24am05.client.model.PlayerPov;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.enums.state.PlayerState;
import it.polimi.is24am05.model.objective.Objective;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class ServerHandler implements VirtualServer {
    private String nickname;
    private boolean isConnected = false;

    protected final String serverIP;
    protected final String serverPort;

    private GamePov gamePov;
    private final List<String> logs = new LinkedList<>();

    protected ServerHandler(String serverIP, String serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        gamePov = new GamePov();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void printText(String message) {
        System.out.println(nickname + ": " + message);
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void addLog(String log) {
        logs.add(log);
    }

    public void setGameCreated(DeckPov resourceDeck, DeckPov goldDeck, int turn, Color color, StarterCard starterCard, List<Map<String, Object>> players) {
        gamePov.setResourceDeck(resourceDeck);
        gamePov.setGoldDeck(goldDeck);
        gamePov.setPlayerTurn(turn);
        gamePov.setColor(color);
        gamePov.setStarterCard(starterCard);

        for (Map<String, Object> player : players) {
            gamePov.addPlayerPov(new PlayerPov(
                    (String) player.get("nickname"),
                    (int) player.get("turn"),
                    (Color) player.get("color"),
                    (StarterCard) player.get("starterCard"),
                    PlayerState.PLACE_STARTER_CARD
            ));
        }

        gamePov.setState(GameState.PLACE_STARTER_CARDS);
    }

    public void setPlacedStarterSide(Side[][] playArea) {
        gamePov.setPlayArea(playArea);

        gamePov.setPlayerState(PlayerState.CHOOSE_OBJECTIVE);
    }

    public void setOtherPlacedStarterSide(String nickname, Side[][] playArea) {
        PlayerPov playerPov = gamePov.getPlayerPov(nickname);
        playerPov.setPlayArea(playArea);

        playerPov.setState(PlayerState.CHOOSE_OBJECTIVE);
    }

    public void setHandsAndObjectivesDealt(
            DeckPov resourceDeck, DeckPov goldDeck, List<Objective> commonObjectives,
            List<Card> hand, List<Objective> objectives, List<Map<String, Object>> players
    ) {
        gamePov.setResourceDeck(resourceDeck);
        gamePov.setGoldDeck(goldDeck);
        gamePov.setObjectives(commonObjectives);
        gamePov.setHand(hand);
        gamePov.setPlayerObjectives(objectives);

        for (Map<String, Object> player : players) {
            PlayerPov playerPov = gamePov.getPlayerPov((String) player.get("nickname"));
            playerPov.setHand((List<Resource>) player.get("hand"));
        }

        gamePov.setState(GameState.CHOOSE_OBJECTIVE);
    }

    public void setChosenObjective(Objective objective) {
        gamePov.setPlayerObjectives(List.of(objective));

        gamePov.setPlayerState(PlayerState.PLACE);
    }

    public void setGameStarted() {
        gamePov.setState(GameState.GAME);
    }

    public void setPlacedSide(Side[][] playArea, int points) {
        gamePov.setPlayArea(playArea);
        gamePov.setPoints(points);

        gamePov.setPlayerState(PlayerState.DRAW);
    }

    public void setOtherPlacedSide(String nickname, Side[][] playArea, int points) {
        PlayerPov playerPov = gamePov.getPlayerPov(nickname);
        playerPov.setPlayArea(playArea);
        playerPov.setPoints(points);

        playerPov.setState(PlayerState.DRAW);
    }

    public void setDrawn(boolean isGold, DeckPov deck, List<Card> hand) {
        if (isGold) gamePov.setGoldDeck(deck);
        else gamePov.setResourceDeck(deck);
        gamePov.setHand(hand);

        gamePov.setPlayerState(PlayerState.PLACE);
        gamePov.incrementCurrentTurn();
    }

    public void setOtherDrawn(String nickname, boolean isGold, DeckPov deck, List<Resource> hand) {
        if (isGold) gamePov.setGoldDeck(deck);
        else gamePov.setResourceDeck(deck);
        PlayerPov playerPov = gamePov.getPlayerPov(nickname);
        playerPov.setHand(hand);

        gamePov.setPlayerState(PlayerState.PLACE);
        gamePov.incrementCurrentTurn();
    }

    public void setGameResumed(
            GameState state, int turn, DeckPov resourceDeck, DeckPov goldDeck, List<Objective> objectives,
            int playerTurn, Color color, StarterCard starterCard, List<Objective> playerObjectives, List<Card> hand, Side[][] playArea, int points,
            List<Map<String, Object>> players
    ) {
        gamePov.setState(state);
        gamePov.setTurn(turn);
        gamePov.setResourceDeck(resourceDeck);
        gamePov.setGoldDeck(goldDeck);
        gamePov.setObjectives(objectives);

        gamePov.setPlayerTurn(playerTurn);
        gamePov.setColor(color);
        gamePov.setStarterCard(starterCard);
        gamePov.setPlayerObjectives(playerObjectives);
        gamePov.setHand(hand);
        gamePov.setPlayArea(playArea);
        gamePov.setPoints(points);

        for (Map<String, Object> player : players)
            gamePov.addPlayerPov(new PlayerPov(
                    (String) player.get("nickname"),
                    (int) player.get("turn"),
                    (Color) player.get("color"),
                    (StarterCard) player.get("starterCard"),
                    PlayerState.PLACE_STARTER_CARD,
                    (List<Resource>) player.get("hand"),
                    (Side[][]) player.get("playArea"),
                    (int) player.get("points")
            ));

        gamePov.setPaused(false);
    }

    public void setOtherGameResumed(String nickname) {
        gamePov.removeDisconnected(nickname);
    }

    public void setOtherQuitGame(String nickname) {
        gamePov.addDisconnected(nickname);
    }

    public void setGamePaused() {
        gamePov.setPaused(true);
    }


}
