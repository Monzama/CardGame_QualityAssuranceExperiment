package org.example;

import javax.annotation.processing.Generated;
import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    //setup game variables
    Deck main_deck= new Deck();
    ArrayList<Player> players = new ArrayList<Player>(0);
    Player currentPlayer;
    Display display;
    public Main(){
        this.GenerateEventDeck();
        this.GenerateAdventureDeck();
        main_deck.shuffle();
        display = new Display();
    }
    public static void main(String[] args) {
        Main main = new Main();
    }

    public void nextTurn (){
        display.displayTurn(getPlayer(0));
        display.displayHand(getPlayer(0));
    }

    public void distributeHands(){
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        Player p3 = new Player("p3");
        Player p4 = new Player("p4");
        for (int i = 1; i <=12 ; i++) {
            for (int j = 0; j <4 ; j++) {
                switch (j){
                    case 0:
                        p1.addCardToHand(this.DrawAdventureCard());
                        break;
                        case 1:
                            p2.addCardToHand(this.DrawAdventureCard());
                            break;
                            case 2:
                                p3.addCardToHand(this.DrawAdventureCard());
                                break;
                                case 3:
                                    p4.addCardToHand(this.DrawAdventureCard());
                                    break;

                }
            }
        }
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        currentPlayer = players.getFirst();
    }
    public Player getPlayer(int x){
        return players.get(x);
    }
    private void GenerateEventDeck(){
        for (int i = 0; i <= 16; i++) {
            if (i<= 2){
                EventCard e = new EventCard("Q2","Q",2);
                main_deck.addCard(e);
            }
            else if (i<= 6){
                EventCard e = new EventCard("Q3","Q",3);
                main_deck.addCard(e);
            }
            else if (i<= 9){
                EventCard e = new EventCard("Q4","Q",4);
                main_deck.addCard(e);
            }
            else if (i<= 11){
                EventCard e = new EventCard("Q5","Q",5);
                main_deck.addCard(e);
            }
            else if (i<= 12){
                EventCard e = new EventCard("Plague","E",0);
                main_deck.addCard(e);
            }
            else if (i<= 14){
                EventCard e = new EventCard("Queen's Favor","E",0);
                main_deck.addCard(e);
            }
            else{
                EventCard e = new EventCard("Prosperity","E",0);
                main_deck.addCard(e);
            }
        }
    }
    public void GenerateAdventureDeck(){
        for (int i = 1; i <= 100; i++) {
            if (i<= 8){
                AdventureCard a = new AdventureCard("F5","F", 5);
                main_deck.addCard(a);
            }
            else if (i<= 15){
                AdventureCard a = new AdventureCard("F10","F", 10);
                main_deck.addCard(a);
            }
            else if (i<= 23){
                AdventureCard a = new AdventureCard("F15","F", 15);
                main_deck.addCard(a);
            }
            else if (i<= 30){
                AdventureCard a = new AdventureCard("F20","F", 20);
                main_deck.addCard(a);
            }
            else if (i<= 37){
                AdventureCard a = new AdventureCard("F25","F", 25);
                main_deck.addCard(a);
            }
            else if (i<= 41){
                AdventureCard a = new AdventureCard("F30","F", 30);
                main_deck.addCard(a);
            }
            else if (i<= 45){
                AdventureCard a = new AdventureCard("F35","F", 35);
                main_deck.addCard(a);
            }
            else if (i<= 47){
                AdventureCard a = new AdventureCard("F40","F", 40);
                main_deck.addCard(a);
            }
            else if (i<= 49){
                AdventureCard a = new AdventureCard("F50","F", 50);
                main_deck.addCard(a);
            }
            else if (i == 50){
                AdventureCard a = new AdventureCard("F70","F", 70);
                main_deck.addCard(a);
            }
            else if (i<= 56){
                AdventureCard a = new AdventureCard("D5","D", 5);
                main_deck.addCard(a);
            }
            else if (i<= 68){
                AdventureCard a = new AdventureCard("H10","H", 10);
                main_deck.addCard(a);
            }
            else if (i<= 84){
                AdventureCard a = new AdventureCard("S10","S", 10);
                main_deck.addCard(a);
            }
            else if (i<= 92){
                AdventureCard a = new AdventureCard("B15","B",15 );
                main_deck.addCard(a);
            }
            else if (i<= 98){
                AdventureCard a = new AdventureCard("L20","L", 20);
                main_deck.addCard(a);
            }
            else{
                AdventureCard a = new AdventureCard("E30","E", 30);
                main_deck.addCard(a);
            }
        }
    }
    public int GetEventDeckSize(){
        return main_deck.getE_deck_size();
    }
    public int GetAdventureDeckSize(){
        return main_deck.getA_deck_size();
    }
    public AdventureCard DrawAdventureCard(){
        return main_deck.DrawAdventureCard();
    }
    public EventCard DrawEventCard(){
        return main_deck.DrawEventCard();
    }

}