package org.example;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Player {
    int handSize;
    String name;
    ArrayList<AdventureCard> hand;
    int shields;

    public Player(String n) {
        handSize = 0;
        name = n;
        hand = new ArrayList<>(0);
        shields = 0;
    }

    public int getShields(){
        return 0;
    }

    public void addCardToHand(AdventureCard c) {
        hand.add(c);
        handSize++;
    }

    public int getHandSize() {
        return handSize;
    }

    public String getName() {
        return name;
    }

    public ArrayList<AdventureCard> getHand() {
        return hand;
    }

    // Foes in increasing order, then weapons in increasing order, swords before horses.
    public void sortHand() {
        Collections.sort(hand);
    }
}

