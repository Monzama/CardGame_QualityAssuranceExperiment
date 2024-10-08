package org.example;

import javax.smartcardio.Card;
import java.io.ByteArrayInputStream;
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
    public Player(String n, int index) {
        handSize = 0;
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
        if (handSize > 12) {trimHand();}
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

    public boolean trimHand(){
        System.out.println(this.name + " please trim your hand:");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()){
            String index = sc.next();
            int i = Integer.parseInt(index);
            sc.close();
            return false;
        }else{
            return handSize == 12;
        }
    }

    // Foes in increasing order, then weapons in increasing order, swords before horses.
    public void sortHand() {
        Collections.sort(hand);
    }
}

