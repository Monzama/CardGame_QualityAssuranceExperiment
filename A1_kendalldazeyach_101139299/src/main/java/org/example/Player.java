package org.example;

import javax.smartcardio.Card;
import java.util.ArrayList;

public class Player {
    int handSize;
    String name;
    ArrayList<AdventureCard> hand;
    public Player(String n) {
        handSize = 0;
        name = n;
        hand = new ArrayList<>(0);
    }

    public void addCardToHand(AdventureCard c) {
        hand.add(c);
        handSize++;
    }
    public int getHandSize(){
        return handSize;
    }

    public String getName() {
        return name;
    }

    //foes in increasing order then weapons in increasing order, swords before horses.
    public void sortHand(){

    }
}
