package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

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
    //yes...this is a wicked algorithm
    //basically it just outlines the scenarios that would limit a player from being able to fully stage the quest
    //note, this tells IF it is possible
    //a player can still make a mistake and not stage the quest properly
    //there will be a reset input added to redo staging
    public boolean canSponsor(int stageCount, int prevvalue, Quest quest) {
        if (stageCount <=1){
            return true;
        }
        sortHand();
        ArrayList<AdventureCard> cards = new ArrayList<>(hand);
        //allows us to call for mid quest stages.
        //basically, before we continue, is the quest completable with the current stages setup and cards in hand
        if (quest != null){
            for (int i = 0; i < quest.stages.size(); i++) {
                for (int j = 0; j < quest.stages.get(i).hand.size(); j++) {
                    cards.remove(quest.stages.get(i).hand.get(j));
                }
            }
        }

        //here we determine if the player has enough cards to sponsor
        if (stageCount > handSize) {
            return false;
        }
        //loop through hand, form the worst case scenario.
        if (!Objects.equals(cards.get(stageCount - 1).type, "F")){
            return false;
        }
        int prevVal = prevvalue;
        int val = 0;
        int indexsearch = 0;
        AdventureCard c;
        int s = 0;
        while (s < stageCount){
            if (cards.isEmpty()){
                return false;
            }else {
                c = cards.get(indexsearch);
                if (val > prevVal) {
                    prevVal = val;
                    val = 0;
                    s++;
                }else if (Objects.equals(c.type, "F") && val ==0){
                    cards.removeFirst();
                    val +=c.value;
                } else if (Objects.equals(c.type, "F") && val !=0) {
                    if (cards.size() > indexsearch){
                        indexsearch++;
                        c = cards.get(indexsearch);
                    }else{
                        return false;
                    }
                }else{
                    if (c.value+val > prevVal) {
                        c = cards.remove(indexsearch);
                        prevVal = (c.value+val);
                        val = 0;
                        indexsearch = 0;
                        s++;
                    }else{
                        if (cards.size() <= indexsearch+2){return false;}
                        //now we have to check for multiple weapons
                        if (c.value + cards.get(indexsearch+1).value >= cards.get(indexsearch+2).value) {
                            if (cards.get(indexsearch+2).value + val > prevVal) {
                                prevVal = (c.value+val+cards.get(indexsearch+2).value);
                                cards.remove(c);
                                c = cards.remove(indexsearch+2);
                                val = 0;
                                indexsearch = 0;
                                s++;
                            } else if (c.value + cards.get(indexsearch+1).value > prevVal) {
                                prevVal = (c.value + val + cards.get(indexsearch + 1).value);
                                cards.remove(c);
                                c = cards.remove(indexsearch + 1);
                                val = 0;
                                indexsearch = 0;
                                s++;
                            }else if (c.value + cards.get(indexsearch+1).value + cards.get(indexsearch+2).value > prevVal) {
                                prevVal = (c.value+val + cards.get(indexsearch+2).value + cards.get(indexsearch+1).value);
                                cards.remove(c);
                                c = cards.remove(indexsearch+1);
                                c = cards.remove(indexsearch+2);
                                val = 0;
                                indexsearch = 0;
                                s++;
                            }else{
                                return false;
                            }
                        }else {
                            if (c.value + cards.get(indexsearch+1).value > prevVal) {
                                prevVal = (c.value+val+ cards.get(indexsearch+1).value);
                                cards.remove(c);
                                c = cards.remove(indexsearch+1);
                                val = 0;
                                indexsearch = 0;
                                s++;
                            }else if (cards.get(indexsearch+2).value + val > prevVal) {
                                prevVal = (c.value+val+cards.get(indexsearch+2).value);
                                cards.remove(c);
                                c = cards.remove(indexsearch+2);
                                val = 0;
                                indexsearch = 0;
                                s++;
                            } else if (c.value + cards.get(indexsearch+2).value> prevVal) {
                                prevVal = (c.value+val + cards.get(indexsearch+2).value + cards.get(indexsearch+1).value);
                                cards.remove(c);
                                c = cards.remove(indexsearch+1);
                                c = cards.remove(indexsearch+2);
                                val = 0;
                                indexsearch = 0;
                                s++;
                            } else if (cards.get(indexsearch+1).value + cards.get(indexsearch+2).value> prevVal) {
                                prevVal = (val + cards.get(indexsearch + 2).value + cards.get(indexsearch + 1).value);
                                c = cards.remove(indexsearch + 1);
                                c = cards.remove(indexsearch + 2);
                                val = 0;
                                indexsearch = 0;
                                s++;
                            }else if (c.value + cards.get(indexsearch+1).value + cards.get(indexsearch+2).value > prevVal) {
                                prevVal = (val + cards.get(indexsearch + 2).value + cards.get(indexsearch + 1).value);
                                c = cards.remove(indexsearch + 1);
                                c = cards.remove(indexsearch + 2);
                                val = 0;
                                indexsearch = 0;
                                s++;
                            }else{
                                return false;
                            }

                        }
                    }
                }
            }
        }
        return true;
    }


    // Foes in increasing order, then weapons in increasing order, swords before horses.
    public void sortHand() {
        Collections.sort(hand);
    }
}

