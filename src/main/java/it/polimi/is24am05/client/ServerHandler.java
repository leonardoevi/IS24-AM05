package it.polimi.is24am05.client;

import it.polimi.is24am05.client.model.DeckPov;
import it.polimi.is24am05.client.model.GamePov;
import it.polimi.is24am05.client.model.PlayerPov;
import it.polimi.is24am05.client.view.TUI;
import it.polimi.is24am05.client.view.View;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.enums.state.PlayerState;
import it.polimi.is24am05.model.objective.Objective;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstracts the socket and rmi server handler.
 */
public abstract class ServerHandler implements VirtualServer {
    /**
     * Nickname of this client.
     */
    private String nickname;

    /**
     * True if the client is connected to the server, false otherwise.
     */
    private boolean isConnected = false;

    /**
     * IP of the server.
     */
    protected final String serverIP;

    /**
     * Port of the server.
     */
    protected final String serverPort;

    /**
     * Local model of the game, restricted to this client's point of view.
     */
    private GamePov gamePov;

    /**
     * List of exceptions from the server, to be displayed on the view.
     */
    private final List<String> logs = new LinkedList<>();

    /**
     * View.
     */
    View view;

    /**
     * Constructor.
     * @param serverIP the IP of the server.
     * @param serverPort the port of the server
     * @param view "tui" for the TUI, "gui" for the GUI.
     */
    protected ServerHandler(String serverIP, String serverPort, String view) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;

        gamePov = new GamePov();

        if (view.equals("tui")) this.view = new TUI(this, gamePov);
        // else this.view = new GUI(this, gamePov)
        // TODO: figure out if this works with GUI
    }

    /**
     * Sets the nickname.
     * @param nickname the nickname to set.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the nickname.
     * @return the nickname of this client.
     */
    public String getNickname() {
        return nickname;
    }


    /**
     * Sets if the client is connected to the server.
     * @param connected true if the client is connected to the server, false otherwise.
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
        
        view.joinedServer();
    }

    /**
     * Adds a log to be displayed on the view.
     * @param log the log to add.
     */
    public void addLog(String log) {
        logs.add(log);

        view.addedLog();
    }

    /**
     * Sets the local model after the game is created.
     * @param resourceDeck the resource deck.
     * @param goldDeck the gold deck.
     * @param playerTurn the turn of this player.
     * @param color the color of this player.
     * @param starterCard the starter card of this player.
     * @param players the map of other players' nicknames, turns, colors and starter cards.
     */
    public void setGameCreated(DeckPov resourceDeck, DeckPov goldDeck, int playerTurn, Color color, StarterCard starterCard, List<Map<String, Object>> players) {
        gamePov.setResourceDeck(resourceDeck);
        gamePov.setGoldDeck(goldDeck);
        gamePov.setPlayerTurn(playerTurn);
        gamePov.setColor(color);
        gamePov.setStarterCard(starterCard);

        for (Map<String, Object> player : players) {
            gamePov.addPlayerPov(new PlayerPov(
                    (String) player.get("nickname"),
                    (int) player.get("turn"),
                    (Color) player.get("color"),
                    (StarterCard) player.get("starterCard")
            ));
        }

        gamePov.setState(GameState.PLACE_STARTER_CARDS);

        view.gameCreated();
    }

    /**
     * Sets the local model after this player placed a starter side.
     * @param playArea the play area.
     */
    public void setPlacedStarterSide(PlacedSide[][] playArea) {
        gamePov.setPlayArea(playArea);

        gamePov.setPlayerState(PlayerState.CHOOSE_OBJECTIVE);

        view.placedStarterSide();
    }

    /**
     * Sets the local model after another player placed a starter side.
     * @param nickname the nickname of the other player.
     * @param playArea the play area of the other player.
     */
    public void setOtherPlacedStarterSide(String nickname, PlacedSide[][] playArea) {
        PlayerPov playerPov = gamePov.getPlayerPov(nickname);
        playerPov.setPlayArea(playArea);

        playerPov.setState(PlayerState.CHOOSE_OBJECTIVE);

        view.otherPlacedStarterSide();
    }

    /**
     * Sets the local model after hand and objectives are dealt.
     * @param resourceDeck the resource deck.
     * @param goldDeck the gold deck.
     * @param objectives the objectives.
     * @param hand the hand of this player.
     * @param playerObjectives the objectives of this player.
     * @param players the map of other players' hands.
     */
    public void setHandsAndObjectivesDealt(
            DeckPov resourceDeck, DeckPov goldDeck, List<Objective> objectives,
            List<Card> hand, List<Objective> playerObjectives, List<Map<String, Object>> players
    ) {
        gamePov.setResourceDeck(resourceDeck);
        gamePov.setGoldDeck(goldDeck);
        gamePov.setObjectives(objectives);
        gamePov.setHand(hand);
        gamePov.setPlayerObjectives(playerObjectives);

        for (Map<String, Object> player : players) {
            PlayerPov playerPov = gamePov.getPlayerPov((String) player.get("nickname"));
            playerPov.setHand((List<Resource>) player.get("hand"));
        }

        gamePov.setState(GameState.CHOOSE_OBJECTIVE);

        view.handsAndObjectivesDealt();
    }

    /**
     * Sets the local model after this player chose the objective.
     * @param objective the objective.
     */
    public void setChosenObjective(Objective objective) {
        gamePov.setPlayerObjectives(List.of(objective));

        gamePov.setPlayerState(PlayerState.PLACE);

        view.chosenObjective();
    }

    /**
     * Sets the local model after the game started.
     */
    public void setGameStarted() {
        gamePov.setState(GameState.GAME);

        view.gameStarted();
    }

    /**
     * Sets the local model after this player placed a side.
     * @param playArea the play area of this player.
     * @param points the points of this player.
     */
    public void setPlacedSide(PlacedSide[][] playArea, int points) {
        gamePov.setPlayArea(playArea);
        gamePov.setPoints(points);

        gamePov.setPlayerState(PlayerState.DRAW);

        view.placedSide();
    }

    /**
     * Sets the local model after another player placed a side.
     * @param nickname the nickname of the other player.
     * @param playArea the play area of the other player.
     * @param points the points of the other player.
     */
    public void setOtherPlacedSide(String nickname, PlacedSide[][] playArea, int points) {
        PlayerPov playerPov = gamePov.getPlayerPov(nickname);
        playerPov.setPlayArea(playArea);
        playerPov.setPoints(points);

        playerPov.setState(PlayerState.DRAW);

        view.otherPlacedSide();
    }

    /**
     * Sets the local model after this player draws a visible card.
     * @param isGold true for the gold deck, false for the resource deck.
     * @param deck the deck.
     * @param hand the hand of this player.
     */
    public void setDrawnVisible(boolean isGold, DeckPov deck, List<Card> hand) {
        if (isGold) gamePov.setGoldDeck(deck);
        else gamePov.setResourceDeck(deck);
        gamePov.setHand(hand);

        gamePov.setPlayerState(PlayerState.PLACE);
        gamePov.incrementTurn();

        view.drawnVisible();
    }

    /**
     * Sets the local model after another player draws a visible card.
     * @param nickname the nickname of the other player.
     * @param isGold true for the gold deck, false for the resource deck.
     * @param deck the deck.
     * @param hand the hand of the other player.
     */
    public void setOtherDrawnVisible(String nickname, boolean isGold, DeckPov deck, List<Resource> hand) {
        if (isGold) gamePov.setGoldDeck(deck);
        else gamePov.setResourceDeck(deck);
        PlayerPov playerPov = gamePov.getPlayerPov(nickname);
        playerPov.setHand(hand);

        gamePov.setPlayerState(PlayerState.PLACE);
        gamePov.incrementTurn();

        view.otherDrawnVisible();
    }

    /**
     * Sets the local model after this player draws a visible card.
     * @param isGold true for the gold deck, false for the resource deck.
     * @param deck the deck.
     * @param hand the hand of this player.
     */
    public void setDrawnDeck(boolean isGold, DeckPov deck, List<Card> hand) {
        if (isGold) gamePov.setGoldDeck(deck);
        else gamePov.setResourceDeck(deck);
        gamePov.setHand(hand);

        gamePov.setPlayerState(PlayerState.PLACE);
        gamePov.incrementTurn();

        view.drawnDeck();
    }

    /**
     * Sets the local model after another player draws a visible card.
     * @param nickname the nickname of the other player.
     * @param isGold true for the gold deck, false for the resource deck.
     * @param deck the deck.
     * @param hand the hand of the other player.
     */
    public void setOtherDrawnDeck(String nickname, boolean isGold, DeckPov deck, List<Resource> hand) {
        if (isGold) gamePov.setGoldDeck(deck);
        else gamePov.setResourceDeck(deck);
        PlayerPov playerPov = gamePov.getPlayerPov(nickname);
        playerPov.setHand(hand);

        gamePov.setPlayerState(PlayerState.PLACE);
        gamePov.incrementTurn();

        view.otherDrawnDeck();
    }

    /**
     * Sets the local model after the game is resumed.
     * @param state the state.
     * @param turn the turn.
     * @param resourceDeck the resource deck.
     * @param goldDeck the gold deck.
     * @param objectives the objectives.
     * @param playerTurn the turn of this player.
     * @param color the color of this player.
     * @param starterCard the starter card of this player.
     * @param playerObjectives the objectives of this player.
     * @param hand the hand of this player.
     * @param playArea the play area of this player.
     * @param points the points of this player.
     * @param players the map of other players' nicknames, turns, colors, starter cards, hands, play areas and points.
     */
    public void setGameResumed(
            GameState state, int turn, DeckPov resourceDeck, DeckPov goldDeck, List<Objective> objectives,
            PlayerState playerState, int playerTurn, Color color, StarterCard starterCard, List<Objective> playerObjectives, List<Card> hand, PlacedSide[][] playArea, int points,
            List<Map<String, Object>> players
    ) {
        gamePov.setState(state);
        gamePov.setTurn(turn);
        gamePov.setResourceDeck(resourceDeck);
        gamePov.setGoldDeck(goldDeck);
        gamePov.setObjectives(objectives);

        gamePov.setPlayerTurn(playerTurn);
        gamePov.setPlayerState(playerState);
        gamePov.setColor(color);
        gamePov.setStarterCard(starterCard);
        gamePov.setPlayerObjectives(playerObjectives);
        gamePov.setHand(hand);
        gamePov.setPlayArea(playArea);
        gamePov.setPoints(points);

        for (Map<String, Object> player : players)
            gamePov.addPlayerPov(new PlayerPov(
                    (String) player.get("nickname"),
                    (PlayerState) player.get("state"),
                    (int) player.get("turn"),
                    (Color) player.get("color"),
                    (StarterCard) player.get("starterCard"),
                    (List<Resource>) player.get("hand"),
                    (PlacedSide[][]) player.get("playArea"),
                    (int) player.get("points")
            ));

        gamePov.setPaused(false);

        view.gameResumed();
    }

    /**
     * Sets the local model after another player reconnected.
     * @param nickname the nickname of the other player.
     */
    public void setOtherGameResumed(String nickname) {
        gamePov.removeDisconnected(nickname);

        view.otherGameResumed();
    }

    /**
     * Sets the local model after another player quit the game.
     * @param nickname the nickname of the other player.
     */
    public void setOtherQuitGame(String nickname) {
        gamePov.addDisconnected(nickname);

        view.otherQuitGame();
    }

    /**
     * Sets the game as paused.
     */
    public void setGamePaused() {
        gamePov.setPaused(true);

        view.gamePaused();
    }

    // TODO: to remove.
    public void printText(String message) {
        System.out.println(nickname + ": " + message);
    }
}
