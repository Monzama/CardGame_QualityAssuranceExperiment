package org.example;

import java.util.ArrayList;

public class Display {

    private void clearScreen (){
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
    public void displayTurn(Player p){
        clearScreen();
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
