package org.example;

import java.util.ArrayList;

public class Display {

    public void displayTurn(Player p){
        System.out.println("Current Player: " + p.getName());
    }
    public void displayHand(Player p){
        System.out.println("Hand:");
        ArrayList<AdventureCard> hand = p.getHand();
        for (int i = 0; i < p.getHandSize(); i++) {
            System.out.println(hand.get(i).GetCardName());
        }
    }
}
