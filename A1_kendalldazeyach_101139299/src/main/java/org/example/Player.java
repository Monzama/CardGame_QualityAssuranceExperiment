package org.example;

import javax.smartcardio.Card;
import java.util.ArrayList;

public class Player {
    int handSize;
    ArrayList<AdventureCard> hand;
    public Player() {
        handSize = 0;
        hand = new ArrayList<>(0);
    }

    public void addCardToHand(AdventureCard c) {
        hand.add(c);
        handSize++;
    }
    public int getHandSize(){
        return handSize;
    }
}
