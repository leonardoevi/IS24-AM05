package it.polimi.is24am05.client.model;

import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.state.PlayerState;

import java.util.List;

public class PlayerPov {
    final String nickname;
    final int turn;
    final Color color;
    final StarterCard starterCard;

    PlayerState state;
    List<Resource> hand;
    Side[][] playArea;
    int points;

    public PlayerPov(String nickname, int turn, Color color, StarterCard starterCard, PlayerState state) {
        this.nickname = nickname;
        this.turn = turn;
        this.color = color;
        this.starterCard = starterCard;
        this.state = state;
    }

    public PlayerPov(String nickname, int turn, Color color, StarterCard starterCard, PlayerState state, List<Resource> hand, Side[][] playArea, int points) {
        this.nickname = nickname;
        this.turn = turn;
        this.color = color;
        this.starterCard = starterCard;
        this.state = state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setHand(List<Resource> hand) {
        this.hand = hand;
    }

    public void setPlayArea(Side[][] playArea) {
        this.playArea = playArea;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getNickname() {
        return nickname;
    }
}
