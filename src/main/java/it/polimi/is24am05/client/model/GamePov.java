package it.polimi.is24am05.client.model;

import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.enums.state.PlayerState;
import it.polimi.is24am05.model.objective.Objective;

import java.util.ArrayList;
import java.util.List;

public class GamePov {
    boolean isPaused = false;
    GameState state;
    int turn = 0;

    DeckPov resourceDeck;
    DeckPov goldDeck;
    List<Objective> objectives;

    PlayerState playerState;
    int playerTurn;
    Color color;
    StarterCard starterCard;
    List<Objective> playerObjectives;
    List<Card> hand;
    Side[][] playArea;
    int points;

    List<PlayerPov> playerPovs = new ArrayList<>();
    List<Integer> disconnected = new ArrayList<>();

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void incrementCurrentTurn() {
        do {
            turn += 1;
        } while (disconnected.contains(turn));
    }

    public void setResourceDeck(DeckPov resourceDeck) {
        this.resourceDeck = resourceDeck;
    }

    public void setGoldDeck(DeckPov goldDeck) {
        this.goldDeck = goldDeck;
    }

    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    public void setPlayerObjectives(List<Objective> playerObjectives) {
        this.playerObjectives = playerObjectives;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public void setPlayArea(Side[][] playArea) {
        this.playArea = playArea;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPlayerPov(PlayerPov playerPov) {
        playerPovs.add(playerPov);
    }

    public void addDisconnected(String nickname) {
        PlayerPov playerPov = playerPovs.stream().filter(p -> p.getNickname().equals(nickname)).findFirst().orElseThrow();
        disconnected.add(playerPov.turn);
    }

    public void removeDisconnected(String nickname) {
        PlayerPov playerPov = playerPovs.stream().filter(p -> p.getNickname().equals(nickname)).findFirst().orElseThrow();
        disconnected.remove(playerPov.turn);
    }

    public PlayerPov getPlayerPov(String nickname) {
        return playerPovs.stream().filter(p -> p.getNickname().equals(nickname)).findFirst().orElseThrow();
    }
}
