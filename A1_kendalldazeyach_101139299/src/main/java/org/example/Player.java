package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Player {
    int handSize;
    String name;
    ArrayList<AdventureCard> hand;
    int shields;
    int id;
    Display display;
    public Player(String n, int index, Display d) {
        handSize = 0;
        display = d;
        id = index;
        name = n;
        hand = new ArrayList<>(0);
        shields = 0;
    }

    public int getShields(){
        return shields;
    }

    public void addCardToHand(AdventureCard c) {
        hand.add(c);
        handSize++;
        if (handSize > 12) {trimHand(-2);}
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

    public void adjustShields(int mod){
        shields += mod;
        if (shields < 0){shields = 0;}
    }

    public boolean trimHand(int index){
        if (index==-1){
            System.out.println(this.name + " please trim your hand:");
        }
        if (index ==-2){
            int n = hand.size() -12;
            for (int i=0;i<n;i++) {
                display.clearScreen();
                sortHand();
                System.out.println(this.name + " please trim your hand:");
                display.displayHand(this);
                String r = display.getMessage("Please select your card (1-" + handSize + ")");
                int remove = Integer.parseInt(r);
                hand.remove(remove-1);
                handSize--;
            }
            sortHand();
            System.out.print("Trimmed ");
            display.displayHand(this);
        } else if (index >=0 && index < handSize) {
            hand.remove(index-1);
            handSize--;
        }
        return handSize == 12;
        
    }

    // Foes in increasing order, then weapons in increasing order, swords before horses.
    public void sortHand() {
        Collections.sort(hand);
    }
}

