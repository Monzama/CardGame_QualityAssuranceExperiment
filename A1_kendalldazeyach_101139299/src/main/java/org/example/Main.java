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
    Boolean loop;
    EventCard current_event;
    ArrayList<Integer> QuestBoard;
    public Main(boolean test) {
        display = new Display();
        this.GenerateEventDeck();
        this.GenerateAdventureDeck();
        main_deck.shuffle();
        game_on = !test;
        loop = true;
        QuestBoard = new ArrayList<>(0);
    }

    public Main(boolean test, boolean loop) {
        display = new Display();
        this.GenerateEventDeck();
        this.GenerateAdventureDeck();
        main_deck.shuffle();
        game_on = !test;
        loop = loop;
        QuestBoard = new ArrayList<>(0);
    }

    public static void main(String[] args) {
        Main main = new Main(false);
        main.distributeHands(12);
        //do the game
        main.begin(main);
        while (main.game_on &&main.loop) {
            main.nextTurn();
        }
    }

    public void begin(Main main) {
        System.out.print("p1 Starting ");
        main.currentPlayer.sortHand();
        main.display.displayHand(main.currentPlayer);
        System.out.println("Begin the game?");
        main.nextEvent();
    }

    public void nextTurn () {
        if (currentPlayer.id == 3) {
            currentPlayer = players.get(0);
        } else{
            currentPlayer = players.get(currentPlayer.id + 1);
        }
        display.displayTurn(currentPlayer);
        currentPlayer.sortHand();
        display.displayHand(currentPlayer);
        if (game_on) {
            nextEvent();
        }
    }

    public void nextEvent(){
        if (game_on) {
            display.clearScreen(true);
        }
        current_event = DrawEventCard();
        System.out.println("The Next Event Card Is: "+current_event.name + ",");
        switch (current_event.name){
            case "Plague":
                System.out.println(currentPlayer.name + " loses 2 shields!");
                //process loss of shields
                currentPlayer.adjustShields(-2);
                if (game_on) {
                    waitForEnter(false);
                    return;
                }else{
                    waitForEnter(true);
                    return;
                }
            case "Queen's Favor":
                //process 2 card draw
                currentPlayer.addCardToHand(main_deck.DrawAdventureCard());
                currentPlayer.addCardToHand(main_deck.DrawAdventureCard());
                //process trim, should be done from player hand. would make sense to trigger on players turn
                //to prevent peeking
                if (game_on) {
                    waitForEnter(false);
                    return;
                }else{
                    waitForEnter(true);
                    return;
                }
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
                p1.sortHand();
                p2.sortHand();
                p3.sortHand();
                p4.sortHand();
                players.set(0, p1);
                players.set(1, p2);
                players.set(2, p3);
                players.set(3, p4);
                currentPlayer = players.get(0);
                if (game_on) {
                    waitForEnter(false);
                    return;
                }else{
                    waitForEnter(true);
                    return;
                }

        }
        if (!Objects.equals(current_event.GetCardType(), "E")){
            initiateQuest(current_event.GetCardValue());
            waitForEnter(true);
            return;
        }
        if (!game_on) {
            waitForEnter(false);
        }
    }

    public void initiateQuest(int questValue){
        Player sponsor = null;
        int offset = currentPlayer.id;
        int end = currentPlayer.id-1;
        Player offer = currentPlayer;
        if (questValue == 10){
            System.out.println(offer.name+ " Would you like to sponsor this quest?:");
            return;
        }
        while (true){
            if (!offer.canSponsor(questValue, 0, null) && questValue !=-1){
                System.out.println(offer.name + " Cannot sponsor with the current hand");
                if (offer.id == 3){
                    break;
                }
                offer = players.get(offer.id+1);
                continue;
            }
            String ans = display.getMessage(offer.name+ " Would you like to sponsor this quest?");
            if (Objects.equals(ans, "no")){
                if (offer.id == 3 || offset ==-1){
                    if (offset>0){
                        offer = players.get(0);
                        offset = -1;
                    } else if (offer.id == end && offset ==-1) {
                        System.out.println("No Sponsorship, Quest Abandoned");
                        waitForEnter(false);
                        return;
                    }else if (offset == 0){
                        System.out.println("No Sponsorship, Quest Abandoned");
                        waitForEnter(false);
                        return;
                    }
                }else{
                    offer = players.get(offer.id+1);
                }
            }else if (Objects.equals(ans, "yes")){
                //check if sponsor is valid
                sponsor = offer;
                break;
            }else {
                System.out.println("Invalid Input");
            }
        }
        if (sponsor != null){
            System.out.println(sponsor.name+ " Sponsors The Quest!");
            display.clearScreen(false);
            System.out.println( sponsor.name + " Setup Stage 1");
            display.displayHand(sponsor);
            ArrayList<Integer> index_added= new ArrayList<>(0);
            //do the quest setup
            Quest q = new Quest(questValue);
            for (int i = 0; i < questValue; i++) {
                if (i!=0){
                    System.out.println("Setup Stage " + (i+1));
                    display.displayHand(sponsor);
                    System.out.println("Cannot use " + this.QuestBoard.toString());
                }
                //do something
                Player s = setupStage((i+1), sponsor, q.previousStage);
                q.addStage(s);
            }
            if (!Objects.equals(current_event.type, "t")){
                display.clearScreen(false);
                //by this point, the quest should be setup
                playStage(q,sponsor);
            }
        }

    }
    public Player setupStage(int round, Player sponsor, Player prev){
        Player stage_obj = new Player("Stage" + round, -1, display);
        while (true){
            String response = display.getMessage(sponsor.name + " Select a card to add to the stage or 'Quit' if done:");
            if (Objects.equals(response, "Quit")){
                if (stage_obj.hand.isEmpty()){
                    //stage empty error
                    System.out.println("A stage cannot be empty");
                }else{
                    if (prev !=null){
                        if (prev.shields >= stage_obj.shields ){
                            System.out.println("A stage cannot be less than the previous");
                        }else{
                            //stage ready to play
                            stage_obj.sortHand();
                            System.out.println("Setup Finished!");
                            display.displayHand(stage_obj);
                            return stage_obj;
                        }
                    }else{
                        //stage ready to play
                        stage_obj.sortHand();
                        System.out.println("Setup Finished!");
                        display.displayHand(stage_obj);
                        return stage_obj;
                    }
                }
            }else {
                int index = -1;
                try {
                    index = Integer.parseInt(response);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Input, must be an integer");
                    continue;
                }
                if (index < 0 || index > sponsor.handSize) {
                    System.out.println("Invalid Input, must be within size of hand");
                } else if (this.QuestBoard.contains(index)) {
                    System.out.println("Invalid Input, cannot use duplicate card");
                }else{
                    AdventureCard card = sponsor.hand.get(index-1);
                    if (stage_obj.hand.contains(card) && !Objects.equals(card.GetCardType(), "F")){
                        System.out.println("Invalid Input, cannot be duplicate weapon");
                    }else if (Objects.equals(card.GetCardType(), "F")){
                        if (stage_obj.hand.contains(card)){
                            System.out.println("Invalid Card, a stage cannot have more than one foe");
                        }else{
                            System.out.println("Card Valid");
                            this.QuestBoard.add(index);
                            stage_obj.addCardToHand(card);
                            stage_obj.shields += card.GetCardValue();
                            stage_obj.sortHand();
                            display.displayHand(stage_obj);
                        }

                    }else if (stage_obj.hand.isEmpty()){
                        System.out.println("Invalid Card, a stage cannot have a weapon and no foe");
                    }else{
                        System.out.println("Card Valid");
                        this.QuestBoard.add(index);
                        stage_obj.addCardToHand(card);
                        stage_obj.shields  += card.GetCardValue();
                        stage_obj.sortHand();
                        display.displayHand(stage_obj);
                    }
                }

            }
        }
    }

    public ArrayList<Player> playStage(Quest q, Player sponsor){
        //do stuff
        ArrayList<Player> eligblep = new ArrayList<>(0);
        eligblep.addAll(players);
        eligblep.remove(sponsor);
        String playerlist = "Eligible Players:";
        for (int i = 0; i < eligblep.size(); i++){
            playerlist+= "\n"+eligblep.get(i).name;
        }
        if (q ==null){
            System.out.println(playerlist);
            return eligblep;
        }
        //should only be the participants here
            for (int i = 0; i < q.stages.size(); i++){
                    String playerlist2 = "Eligible Players:";
                    for (int k = 0; k < eligblep.size(); k++) {
                        playerlist2 += "\n" + eligblep.get(k).name;
                    }
                    System.out.println(playerlist2);
                    //prompt players here
                    for (int o = 0; o < eligblep.size(); o++){
                        String response = display.getMessage(eligblep.get(o).name+" Withdraw (w) or Tackle (t)?");
                        if (Objects.equals(response, "w")){
                            eligblep.remove(o);
                            o--;
                        }else if (!Objects.equals(response, "t")){
                            o--;
                            System.out.println("Incorrect response");
                            continue;
                        }else{
                            AdventureCard draw = main_deck.DrawAdventureCard();
                            System.out.println(eligblep.get(o).name + " Draws a " + draw.GetCardName());
                            if (eligblep.get(o).handSize ==12){
                                eligblep.get(o).addCardToHand(draw);
                            }else {
                                eligblep.get(o).addCardToHand(draw);
                                display.clearScreen(true);
                            }
                        }
                    }
                    if (q.stageCount <=0){
                        return eligblep;
                    }
                if (eligblep.size()==0){
                    //everyone loses. except the sponsor i think
                    System.out.println("Quest Failed!");
                    endQuest(q,sponsor);
                    return eligblep;
                }
                //remove player from eligible list
                Player s = q.stages.get(i);
                ArrayList<Player> results = setupAttack(eligblep,s);
                for (int j = 0; j < results.size(); j++) {
                    Player r = results.get(j);
                    if (r.shields == -1){
                        for (int k = 0; k < players.size(); k++) {
                            Player p = players.get(k);
                            if (Objects.equals(p.name, r.name)){
                                eligblep.remove(p);
                            }
                        }
                    }
                }
                if (eligblep.size()==0){
                    //everyone loses. except the sponsor i think
                    System.out.println("Quest Failed!");
                    endQuest(q,sponsor);
                    return eligblep;
                }
                if (s == q.stages.getLast()){
                    for (int j = 0; j < eligblep.size(); j++) {
                        Player r = eligblep.get(j);
                            for (int k = 0; k < players.size(); k++) {
                                Player p = players.get(k);
                                if (Objects.equals(p.name, r.name)){
                                    System.out.println(p.name + " Completes the quest and earns " + q.stageCount + " shields!");
                                    p.adjustShields(q.stageCount);
                                }
                            }
                    }
                    endQuest(q,sponsor);
                    return eligblep;
                }
            }
        return eligblep;
        }

    public ArrayList<Player> setupAttack(ArrayList<Player> eligblep, Player stage){
        //prompt for next card to include in attack
        ArrayList<Player> attacks = new ArrayList<>(0);
        for (int i = 0; i <eligblep.size() ; i++) {
            display.clearScreen(false);
            Player atk = new Player(eligblep.get(i).name,-2,display);
           Player p = eligblep.get(i);
            //display.displayHand(stage);
            System.out.println("Setup Attack:");
            System.out.println(p.name);
            display.displayHand(p);
            while (attacks.size() < eligblep.size() && stage!=null) {
                String response = display.getMessage("Select a card to add to the attack or 'Quit' if done");
                if (response.equals("Quit")){
                        attacks.add(atk);
                        break;
                }else{
                    int index = -1;
                    try {
                        index = Integer.parseInt(response) -1;
                    }catch (NumberFormatException e){
                        System.out.println("Invalid Input, must be an integer");
                    }
                    if (index < 0 || index > p.hand.size()){
                        System.out.println("Invalid Input, must be within size of hand");
                    } else if (p.hand.get(index).GetCardType().equals("F")) {
                        System.out.println("Invalid Card, an attack cannot contain a foe");
                    } else{
                        boolean flag = false;
                        for (int j = 0; j < atk.hand.size(); j++) {
                            if (atk.hand.get(j).GetCardName().equals(p.hand.get(index).GetCardName())) {
                                System.out.println("Invalid Card, cannot have more than one weapon of the same type");
                                flag = true;
                            }
                        }
                        if (flag){
                            System.out.println();
                        }
                        atk.addCardToHand(p.hand.get(index));
                        atk.shields = atk.shields + p.hand.get(index).GetCardValue();
                        display.displayHand(atk);
                    }
                }
                display.displayHand(p);
            }

        }
        display.clearScreen(false);
        return playAttack(attacks, stage);
    }
    //p1 Loses, attack: 5 stage: 10
    //p1 fails the quest
    public ArrayList<Player> playAttack(ArrayList<Player> attacks, Player stage){
        //check if attacks win or lose
        for (int i = 0; i < attacks.size(); i++) {
            if (attacks.get(i).shields < stage.shields) {
                //display loss
                for (int j = 0; j < players.size(); j++) {
                    Player p = players.get(j);
                    if (Objects.equals(p.name, attacks.get(i).name)){
                        System.out.println();
                        System.out.println(p.name + " Loses, attack: " + attacks.get(i).shields);
                        System.out.println(p.name + " fails the quest");
                        break;
                    }
                }
                attacks.get(i).shields = -1;
            }else{
                //display win
                for (int j = 0; j < players.size(); j++) {
                    Player p = players.get(j);
                    if (Objects.equals(p.name, attacks.get(i).name)){
                        System.out.println();
                        System.out.println(p.name + " Wins, attack: " + attacks.get(i).shields);
                        break;
                    }
                }
            }
        }
        //remove cards
        for (Player attack : attacks) {
            for (int j = 0; j < players.size(); j++) {
                if (players.get(j).name == attack.name) {
                    while (attack.hand.size() > 0) {
                        for (int i = 0; i < players.get(j).hand.size(); i++) {
                            if (players.get(j).hand.get(i).name.equals(attack.hand.get(0).name)) {
                                players.get(j).hand.remove(i);
                                players.get(j).handSize--;
                                attack.hand.remove(0);
                                break;
                            }
                        }
                    }
                }
            }
        }
        //for attacks that lose,set shields to -1
        return attacks;
    }


    public void endQuest(Quest q, Player sponsor){
        System.out.println("Quest Finished!");
        this.QuestBoard.clear();
        if (q == null){
            sponsor.trimHand(2);
            sponsor.trimHand(2);
            return;
        }
        int counter = q.stageCount;
        for (int i = 0; i < q.stageCount; i++) {
            for (int j = 0; j < q.stages.get(i).hand.size(); j++) {
                counter++;
                sponsor.hand.remove(q.stages.get(i).hand.get(j));
                sponsor.handSize--;
            }
        }
        for (int l = 0; l < (counter-1); l++) {
            sponsor.hand.add(main_deck.DrawAdventureCard());
            sponsor.handSize++;
        }
        sponsor.addCardToHand(main_deck.DrawAdventureCard());
    }

    public boolean waitForEnter(boolean test){
        if (test){
            endTurn();
            return true;
        }else{
            display.getMessage("Press Enter to end your turn:");
            endTurn();
            return true;
        }
    }

    public void endTurn(){
        System.out.println("End Of Turn!");

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