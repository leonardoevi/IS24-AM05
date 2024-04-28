package it.polimi.is24am05.controller.socketServer;

import it.polimi.is24am05.controller.exceptions.KoException;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.card.resourceCard.ResourceCard;
import it.polimi.is24am05.model.objective.Objective;

import java.util.*;

public class MessageDecoder {
    public final static String  PLAY_STARTER_CARD = "psc",
                                CHOOSE_OBJECTIVE = "co",
                                PLACE_CARD = "pc",
                                DRAW_DECK = "dd",
                                DRAW_VISIBLE = "dv";


    /**
     * Decodes a message returning a List of objects, each object is a parameter for the method specified as the first value of the message
     * @param message to decode
     * @return All the parameters (each with the correct Dynamic Type) in a list. Yhe first is a string of the method to invoke
     * @throws KoException if something goes wrong when decoding the message
     */
    public static List<Object> decode(String message) throws KoException {
        List<Object> parameters = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(message);
            scanner.useDelimiter(",");

            String method = scanner.next();
            parameters.add(method);

            switch (method) {
                case PLAY_STARTER_CARD:
                    parameters.add(Boolean.parseBoolean(scanner.next()));
                    break;

                case CHOOSE_OBJECTIVE:
                    parameters.add(Objective.valueOf(scanner.next()));
                    break;

                case PLACE_CARD:
                    String cardName = scanner.next();
                    Card toPlace;
                    try {
                        toPlace = ResourceCard.valueOf(cardName);
                    } catch (IllegalArgumentException e) {
                        toPlace = GoldCard.valueOf(cardName);
                    }
                    parameters.add(toPlace);

                    parameters.add(Boolean.parseBoolean(scanner.next()));
                    parameters.add(Integer.parseInt(scanner.next()));
                    parameters.add(Integer.parseInt(scanner.next()));
                    break;

                case DRAW_DECK:
                    parameters.add(Boolean.parseBoolean(scanner.next()));
                    break;

                case DRAW_VISIBLE:
                    cardName = scanner.next();
                    Card toDraw;

                    try {
                        toDraw = ResourceCard.valueOf(cardName);
                    } catch (IllegalArgumentException e) {
                        toDraw = GoldCard.valueOf(cardName);
                    }
                    parameters.add(toDraw);
                    break;

                default:
                    throw new KoException(null);
            }
        } catch (Exception e) {
            throw new KoException("ko, Error decoding message");
        }

        return parameters;
    }
}