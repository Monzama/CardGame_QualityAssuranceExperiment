package org.example;
import java.util.ArrayList;

public class Deck {

    public Deck() {

    }

    public Deck(int size, String deckName) {

    }

    public int getDeckSize() {
        return 0;
    }

    public AdventureCard DrawAdventureCard(){
        return new AdventureCard();
    }
    public EventCard DrawEventCard(){
        return new EventCard();
    }
}
