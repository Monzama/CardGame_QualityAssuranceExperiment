package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Main {
    //setup game variables
    Deck main_deck= new Deck();
    ArrayList<Player> players = new ArrayList<Player>(0);
    Player currentPlayer;
    Display display;
    Boolean game_on;
    EventCard current_event;
    public Main(){
        display = new Display();
        this.GenerateEventDeck();
        this.GenerateAdventureDeck();
        main_deck.shuffle();
        game_on = true;
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.distributeHands(12);
        while(main.game_on == true){
            //do the game
            main.nextTurn();
            main.currentPlayer.addCardToHand(main.main_deck.DrawAdventureCard());
            //main.currentPlayer.trimHand();
        }
    }

    public void nextTurn (){
        display.displayTurn(currentPlayer);
        currentPlayer.sortHand();
        display.displayHand(currentPlayer);
        if (currentPlayer.id == 3){
            currentPlayer = players.get(0);
        }else
            currentPlayer = players.get(currentPlayer.id+1);
    }

    public void nextEvent(){
        current_event = DrawEventCard();
        System.out.println("The Next Event Card Is: "+current_event.name + ",");
        switch (current_event.name){
            case "Plague":
                System.out.println(currentPlayer.name + " loses 2 shields!");
                //process loss of shields
                currentPlayer.adjustShields(-2);
                endTurn();
                break;
            case "Queen's Favor":
                //process 2 card draw
                currentPlayer.addCardToHand(main_deck.DrawAdventureCard());
                currentPlayer.addCardToHand(main_deck.DrawAdventureCard());
                //process trim, should be done from player hand. would make sense to trigger on players turn
                //to prevent peeking
                endTurn();
                break;
            case "Prosperity":
                Player p1 = players.get(0);
                Player p2 = players.get(1);
                Player p3 = players.get(2);
                Player p4 = players.get(3);
                p1.addCardToHand(main_deck.DrawAdventureCard());
                p1.addCardToHand(main_deck.DrawAdventureCard());
                p2.addCardToHand(main_deck.DrawAdventureCard());
                p2.addCardToHand(main_deck.DrawAdventureCard());
                p3.addCardToHand(main_deck.DrawAdventureCard());
                p3.addCardToHand(main_deck.DrawAdventureCard());
                p4.addCardToHand(main_deck.DrawAdventureCard());
                p4.addCardToHand(main_deck.DrawAdventureCard());
                players.set(0, p1);
                players.set(1, p2);
                players.set(2, p3);
                players.set(3, p4);
                endTurn();
                break;

        }
        if (!Objects.equals(current_event.GetCardType(), "E")){
            initiateQuest(current_event.GetCardValue());
        }
    }

    public void initiateQuest(int questValue){
        Player sponsor = null;
        Player offer = currentPlayer;
        if (questValue == 10){
            System.out.println(offer.name+ " Would you like to sponsor this quest?:");
            return;
        }
        while (true){
            String ans = display.getMessage(offer.name+ " Would you like to sponsor this quest?");
            if (Objects.equals(ans, "no")){
                if (offer.id == 3){
                    System.out.println("No Sponsorship, Quest Abandoned");
                    endTurn();
                    break;
                }else{
                    offer = players.get(offer.id+1);
                }
            }else if (Objects.equals(ans, "yes")){
                //check if sponsor is valid
                if (offer.canSponsor(questValue, 0, null)){
                    sponsor = offer;
                    break;
                }else{
                    System.out.println(offer.name + "Cannot sponsor with the current hand");
                    offer = players.get(offer.id+1);
                }

            }else {
                System.out.println("Invalid Input");
            }
        }
        if (sponsor != null){
            System.out.println(sponsor.name+ " Sponsors The Quest!");
            System.out.println("Setup Stage 1");
            display.displayHand(sponsor);
            //do the quest setup
            Quest q = new Quest(questValue);
            for (int i = 0; i < questValue; i++) {
                //do something
                Player s = setupStage((i+1), sponsor, q.previousStage);
                q.addStage(s);
            }
            if (!Objects.equals(current_event.type, "t")){
                display.clearScreen();
                //by this point, the quest should be setup
                playStage(q,sponsor);
            }
        }

    }
    public Player setupStage(int round, Player sponsor, Player prev){
        Player stage_obj = new Player("Stage" + round, -1, display);
        int value = 0;
        while (true){
            String response = display.getMessage(sponsor.name + " Select a card to add to the stage or 'Quit' if done:");
            if (Objects.equals(response, "Quit")){
                if (stage_obj.hand.isEmpty()){
                    //stage empty error
                    System.out.println("A stage cannot be empty");
                }else{
                    if (prev !=null){
                        if (prev.shields >= value){
                            System.out.println("A stage cannot be less than the previous");
                        }else{
                            //stage ready to play
                            stage_obj.sortHand();
                            stage_obj.shields = value;
                            System.out.println("Setup Finished");
                            display.displayHand(stage_obj);
                            return stage_obj;
                        }
                    }else{
                        //stage ready to play
                        stage_obj.sortHand();
                        stage_obj.shields = value;
                        System.out.println("Setup Finished");
                        display.displayHand(stage_obj);
                        return stage_obj;
                    }
                }
            }else{
                int index = -1;
                try {
                    index = Integer.parseInt(response);
                }catch (NumberFormatException e){
                    System.out.println("Invalid Input, must be an integer");
                    break;
                }
                if (index < 0 || index >= sponsor.handSize){
                    System.out.println("Invalid Input, must be within size of hand");
                }else{
                    AdventureCard card = sponsor.hand.get(index-1);
                    if (stage_obj.hand.contains(card) && !Objects.equals(card.GetCardType(), "F")){
                        System.out.println("Invalid Input, cannot be duplicate weapon");
                    }else if (Objects.equals(card.GetCardType(), "F")){
                        if (stage_obj.hand.contains(card)){
                            System.out.println("Invalid Card, a stage cannot have more than one foe");
                        }else{
                            System.out.println("Card Valid");
                            stage_obj.hand.add(card);
                            value = card.GetCardValue();
                            stage_obj.sortHand();
                            display.displayHand(stage_obj);
                        }

                    }else if (stage_obj.hand.isEmpty()){
                        System.out.println("Invalid Card, a stage cannot have a weapon and no foe");
                    }else{
                        System.out.println("Card Valid");
                        stage_obj.hand.add(card);
                        value = card.GetCardValue();
                        stage_obj.sortHand();
                        display.displayHand(stage_obj);
                    }
                }

            }
        }
        return null;
    }

    public void playStage(Quest q, Player sponsor){
        //do stuff
        ArrayList<Player> eligblep = players;
        eligblep.remove(sponsor);
        String playerlist = "Eligible Players:";
        for (int i = 0; i < eligblep.size(); i++){
            playerlist+= "\n"+eligblep.get(i).name;
        }
        System.out.println(playerlist);
        if (q ==null){
            return;
        }
        //prompt players here
        for (int i = 0; i < eligblep.size(); i++){
            String response = display.getMessage(eligblep.get(i).name+" Withdraw (w) or Tackle (t)?");
            if (Objects.equals(response, "w")){
                eligblep.remove(i);
                i--;
            }else if (!Objects.equals(response, "t")){
                i--;
                System.out.println("Incorrect response");
            }
            System.out.println();
        }
        //should only be the participants here
        if (eligblep.size()>0){
            for (int i = 0; i < eligblep.size(); i++){
                eligblep.get(i);
                eligblep.get(i).addCardToHand(main_deck.DrawAdventureCard());
            }
        }else{
            endQuest(q, sponsor);
        }
    }

    public void endQuest(Quest q, Player sponsor){
        System.out.println("Quest Finished!");
        if (q == null){
            sponsor.trimHand(2);
            sponsor.trimHand(2);
            return;
        }
        int counter = 0;
        for (int i = 0; i < q.stageCount; i++) {
            for (int j = 0; j < q.stages.get(i).handSize; j++) {
                counter++;
                sponsor.hand.remove(q.stages.get(i).hand.get(j));
                sponsor.handSize--;
            }
        }
        for (int i = 0; i < (counter+ q.stageCount); i++) {
            sponsor.addCardToHand(main_deck.DrawAdventureCard());
        }
    }


    public void endTurn(){
        System.out.println("End Of Turn:");

        //process return press request
        //better to do through display

        //find winner
        ArrayList<Player> winners= new ArrayList<>(0);
        for (int i = 0; i < 4; i++) {
            if (players.get(i).getShields() >=7){winners.add(players.get(i));}
        }
        if (winners.size() == 1) {
            System.out.println("Game Over!\n" + winners.get(0).name + " Wins The Game!");
            this.game_on = false;
        } else if (winners.size() == 2) {
            System.out.println("Game Over!\n" + winners.get(0).name + " & " + winners.get(1).name + " Win The Game!");
            this.game_on = false;
        }else if (winners.size() == 3) {
            System.out.println("Game Over!\n" + winners.get(0).name + " & " + winners.get(1).name + " & " + winners.get(2).name + " Win The Game!");
            this.game_on = false;
        }else if (winners.size() == 4) {
            System.out.println("Game Over!\nEveryone Wins?!?");
            this.game_on = false;
        }
    }

    public void distributeHands(int count){
        Player p1 = new Player("p1", 0, display);
        Player p2 = new Player("p2",1,display);
        Player p3 = new Player("p3",2,display);
        Player p4 = new Player("p4",3,display);
        for (int i = 1; i <=count ; i++) {
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