package org.example;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    int a_deck_size;
    int e_deck_size;
    ArrayList<AdventureCard> adventure_cards;
    ArrayList<EventCard> event_cards;
    public Deck() {
        a_deck_size = 0;
        e_deck_size = 0;
        adventure_cards = new ArrayList<>(0);
        event_cards = new ArrayList<>(0);
    }

    public int getE_deck_size() {
        return e_deck_size;
    }
    public int getA_deck_size() {
        return a_deck_size;
    }

    public void addCard(EventCard c) {
        event_cards.add(c);
        e_deck_size++;
    }
    public void addCard(AdventureCard c) {
        adventure_cards.add(c);
        a_deck_size++;
    }

    public void shuffle() {
        Collections.shuffle(adventure_cards);
        Collections.shuffle(event_cards);
    }

    public AdventureCard DrawAdventureCard(){
        if (!adventure_cards.isEmpty()) {
            a_deck_size--;
            return adventure_cards.removeFirst();
        }else {
            return null;
        }
    }
    public EventCard DrawEventCard(){
        if (!event_cards.isEmpty()) {
            e_deck_size--;
            return event_cards.removeFirst();
        }else {
            return null;
        }
    }
}
