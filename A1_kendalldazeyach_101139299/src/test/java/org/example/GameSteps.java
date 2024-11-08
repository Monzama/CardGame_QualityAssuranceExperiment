package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameSteps {
    private  ArrayList<Player> eligible = null;
    private Main game;
    private final PrintStream standardOut = System.out;
    private final InputStream standardin = System.in;

    PipedOutputStream pipedOut = new PipedOutputStream();
    PipedInputStream pipedIn = new PipedInputStream(pipedOut);

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    public GameSteps() throws IOException {
    }
    //simply find card in hand based on target string
    public int findCard (List<Integer> exclude, String target, ArrayList<AdventureCard> hand) {
        for (int i = 0; i < hand.size(); i++) {
            AdventureCard card = hand.get(i);
            if (exclude != null){
                if (card.name.equals(target)){
                    if (!exclude.contains(i)){
                        return i;
                    }
                }
            }else{
                if (card.name.equals(target)){
                    return i;
                }
            }
        }
        return -1;
    }


    public void writeInput(String input) throws IOException {
     pipedOut.write(input.getBytes());
     pipedOut.flush();
    }
    @Given("a new card game starts")
    public void aNewCardGameStarts() {
        game = new Main(true,false, pipedIn);
        System.setOut(new PrintStream(outputStreamCaptor));
    }


    @When("the {int} starting hands are as posted")
    public void theStartingHandsAreAsPosted(int arg0) {
        game.distributeHands(12);
        //fix p1 hand
        Player p1 = new Player("p1",0, game.display);
        p1.addCardToHand(new AdventureCard("F5","F",5));
        p1.addCardToHand(new AdventureCard("F5","F",5));
        p1.addCardToHand(new AdventureCard("F15","F",15));
        p1.addCardToHand(new AdventureCard("F15","F",15));
        p1.addCardToHand(new AdventureCard("D5","D",5));
        p1.addCardToHand(new AdventureCard("S10","S",10));
        p1.addCardToHand(new AdventureCard("S10","S",10));
        p1.addCardToHand(new AdventureCard("H10","H",10));
        p1.addCardToHand(new AdventureCard("H10","H",10));
        p1.addCardToHand(new AdventureCard("B15","B",15));
        p1.addCardToHand(new AdventureCard("B15","B",15));
        p1.addCardToHand(new AdventureCard("L20","L",20));
        p1.sortHand();
        game.players.set(0,p1);
        game.currentPlayer =p1;
        //fix p2 hand
        Player p2 = new Player("p2",1, game.display);
        p2.addCardToHand(new AdventureCard("F5","F",5));
        p2.addCardToHand(new AdventureCard("F5","F",5));
        p2.addCardToHand(new AdventureCard("F15","F",15));
        p2.addCardToHand(new AdventureCard("F15","F",15));
        p2.addCardToHand(new AdventureCard("F40","F",40));
        p2.addCardToHand(new AdventureCard("D5","D",5));
        p2.addCardToHand(new AdventureCard("S10","S",10));
        p2.addCardToHand(new AdventureCard("H10","H",10));
        p2.addCardToHand(new AdventureCard("H10","H",10));
        p2.addCardToHand(new AdventureCard("B15","B",15));
        p2.addCardToHand(new AdventureCard("B15","B",15));
        p2.addCardToHand(new AdventureCard("E30","E",30));
        p2.sortHand();
        game.players.set(1,p2);
        //fix p3 hand
        Player p3 = new Player("p3",2, game.display);
        p3.addCardToHand(new AdventureCard("F5","F",5));
        p3.addCardToHand(new AdventureCard("F5","F",5));
        p3.addCardToHand(new AdventureCard("F5","F",5));
        p3.addCardToHand(new AdventureCard("F15","F",15));
        p3.addCardToHand(new AdventureCard("D5","D",5));
        p3.addCardToHand(new AdventureCard("S10","S",10));
        p3.addCardToHand(new AdventureCard("S10","S",10));
        p3.addCardToHand(new AdventureCard("S10","S",10));
        p3.addCardToHand(new AdventureCard("H10","H",10));
        p3.addCardToHand(new AdventureCard("H10","H",10));
        p3.addCardToHand(new AdventureCard("B15","B",15));
        p3.addCardToHand(new AdventureCard("L20","L",20));
        p3.sortHand();
        game.players.set(2,p3);
        //fix p4 hand
        Player p4 = new Player("p4",3, game.display);
        p4.addCardToHand(new AdventureCard("F5","F",5));
        p4.addCardToHand(new AdventureCard("F15","F",15));
        p4.addCardToHand(new AdventureCard("F15","F",15));
        p4.addCardToHand(new AdventureCard("F40","F",40));
        p4.addCardToHand(new AdventureCard("D5","D",5));
        p4.addCardToHand(new AdventureCard("D5","D",5));
        p4.addCardToHand(new AdventureCard("S10","S",10));
        p4.addCardToHand(new AdventureCard("H10","H",10));
        p4.addCardToHand(new AdventureCard("H10","H",10));
        p4.addCardToHand(new AdventureCard("B15","B",15));
        p4.addCardToHand(new AdventureCard("L20","L",20));
        p4.addCardToHand(new AdventureCard("E30","E",30));
        p4.sortHand();
        game.players.set(3,p4);
        //rig the deck
        game.main_deck.adventure_cards.set(0,new AdventureCard("F30", "F", 30));
        game.main_deck.adventure_cards.set(1,new AdventureCard("S10", "S", 10));
        game.main_deck.adventure_cards.set(2,new AdventureCard("B15", "B", 15));
        game.main_deck.adventure_cards.set(3,new AdventureCard("F10", "F", 10));
        game.main_deck.adventure_cards.set(4,new AdventureCard("L20", "L", 20));
        game.main_deck.adventure_cards.set(5,new AdventureCard("L20", "L", 20));
        game.main_deck.adventure_cards.set(6,new AdventureCard("B15", "B", 15));
        game.main_deck.adventure_cards.set(7,new AdventureCard("S10", "S", 10));
        game.main_deck.adventure_cards.set(8,new AdventureCard("F30", "F", 30));
        game.main_deck.adventure_cards.set(9,new AdventureCard("L20", "L", 20));
    }


    @And("P{int} draws a quest of {int} stages")
    public void pDrawsAQuestOfStages(int arg0, int arg1) {
        game.main_deck.event_cards.set(0,new EventCard("Q" + arg1, "Q", arg1));
        game.currentPlayer = game.getPlayer(arg0-1);
        game.nextEvent();
    }


    @And("p{int} chooses to sponsor")
    public void pChoosesToSponsor(int arg0) {
        int start =0;
        start = game.currentPlayer.id;
        String input = "";
        for (int i = start; i < 4; i++) {
            if (i == (arg0-1)){
                input += "yes\r\n";
                break;
            }else {
                input += "no\r\n";
            }
            if (i == 3){i=0;}
        }
        try {
          writeInput(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        game.initiateQuest(game.current_event.GetCardValue());
        String x = outputStreamCaptor.toString().trim().replace("\r","");
        if (arg0 == 1){
            Assert.assertTrue(x.contains("p1 Sponsors The Quest!"));
        }else if (arg0 == 2){
            Assert.assertTrue(x.contains("p2 Sponsors The Quest!"));
        }else if (arg0 == 3){
            Assert.assertTrue(x.contains("p3 Sponsors The Quest!"));
        }else if (arg0 == 4){
            Assert.assertTrue(x.contains("p4 Sponsors The Quest!"));
        }
    }


    @And("p{int} builds the stages of the quest as posted")
    public void pBuildsTheStagesOfTheQuestAsPosted(int arg0, List<String> stages) {
        //we will have this functions specify which cards based on the name "H10", "D5"...
        //here it will parse the input list
        game.sponsor.sortHand();
        String input = "";
        String s = "";
        List<Integer> exclude = new ArrayList<>();
        //process list.
        for (int i = 0; i < stages.size(); i++) {
            s = stages.get(i);
            if  (s.equals("Quit")){
                input += "Quit\r\n";
            }else{
                //here it will look for that card in hand
                int index = findCard(exclude, s, game.sponsor.hand);
                //here it will store indexes we have already used
                exclude.add(index);
                if (index == -1) {
                    System.exit(-1);
                }
                //here it will build our string based on the previous index or Quit if specified
                input += (index+1) + "\r\n";
            }
        }
        //end loop, once here, string should be built for all 4 stages
        try {
            writeInput(input);//stage should be set by now
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //initialize stage should take care of stage setup, should be able to call q.stage1, q.stage2...
        game.initializeStage(game.sponsor,game.current_event.GetCardValue());
        game.current_q.attacks = new ArrayList<>(0);
        //test the stage?
        String x = outputStreamCaptor.toString().trim().replace("\r","");
        Assert.assertTrue(x.contains("Quest has been setup!"));
    }


    @And("Other players decide to participate and draw a card")
    public void otherPlayersDecideToParticipateAndDrawACard(List<String> cards) throws IOException {
        String s = "";
        //process list.
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).length() == 3){
                s += "w\r\n";
            }else {
                if (cards.get(i).length() == 7){
                    game.main_deck.adventure_cards.set(i,new AdventureCard(cards.get(i).substring(3,5), cards.get(i).substring(3,4), Integer.parseInt(cards.get(i).substring(4,5))));
                }else{
                    game.main_deck.adventure_cards.set(i,new AdventureCard(cards.get(i).substring(3,6), cards.get(i).substring(3,4), Integer.parseInt(cards.get(i).substring(4,6))));
                }
                s += "t\r\n";
                String t = cards.get(i).substring(1,2);
                Player p = game.players.get(Integer.parseInt(t)-1);
                int hand_S = p.handSize;
                if (hand_S == 12){
                    if (cards.get(i).length() <= 6){
                        s += "1\r\n" + "\r\n";
                    }else{
                        s += cards.get(i).charAt(cards.get(i).length()-1) + "\r\n"  +"\r\n";
                    }
                }else{
                    s+= "\r\n";
                }
            }

        }
        try {
            writeInput(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //have to handle the trim
        eligible = game.decidePlayers(game.current_q, eligible);
        String x = outputStreamCaptor.toString().trim().replace("\r","");
    }


    @And("p{int} sets up their attack for stage {int}")
    public void pSetsUpTheirAttack(int arg0,int arg1, List<String> attack) {
        //we will have this functions specify which cards based on the name "H10", "D5"...
        //here it will parse the input list
        String input = "";
        String s = "";
        List<Integer> exclude = new ArrayList<>();
        //process list.
        Player p = game.getPlayer(arg0-1);
        for (int i = 0; i < attack.size(); i++) {
            s = attack.get(i);
            if  (s.equals("Quit")){
                input += "Quit\r\n";
            }else{
                //here it will look for that card in hand
                int index = findCard(exclude, s, p.hand);
                //here it will store indexes we have already used
                exclude.add(index);
                if (index == -1) {
                    System.exit(-1);
                }
                //here it will build our string based on the previous index or Quit if specified
                input += (index+1) + "\r\n";
            }
        }
        try {
            writeInput(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Player a = game.setupAttack(game.getPlayer(arg0-1),game.current_q.stages.get(arg1-1));
        game.current_q.attacks.add(a);
        String x = outputStreamCaptor.toString().trim().replace("\r","");
        Assert.assertTrue(x.contains(attack.getFirst()));
    }


    @And("Players are told if their attack for stage {int} is sufficient")
    public void playersAreToldIfTheirAttackIsSufficient(int arg0, List<String> wins) {
       ArrayList<Player> results = game.playAttack(game.current_q.stages.get(arg0-1));
        String x = outputStreamCaptor.toString().trim().replace("\r","");
        game.current_q.attacks = new ArrayList<>(0);
        for (int i = 0; i < wins.size(); i++) {
            String w = wins.get(i).substring(0,2) + " " + wins.get(i).substring(3);
            Assert.assertTrue(x.contains(w));
        }
        eligible = game.attackResult(results, eligible);
    }


    @And("p{int} has {int} shields and hand is below")
    public void pHasNoShieldsAndHandIsBelow(int arg0,int arg1, List<String> p_hand) {
        int shields = game.players.get(arg0-1).shields;
        Assert.assertEquals(arg1, shields);
        if (p_hand != null){
            ArrayList<AdventureCard> hand = game.getPlayer(arg0-1).hand;
            Boolean hand_correct = true;
            for (int i = 0; i < p_hand.size(); i++) {
                if (!p_hand.get(i).equals(hand.get(i).name)){
                    hand_correct = false;
                }
            }
            Assert.assertTrue(hand_correct);
        }
    }


    @And("The quest ends")
    public void theQuestEnds() {
        //add sponsor trims
        String input = "";
        for (int i = 0; i < game.current_q.stageCount; i++) {
            input += "1\r\n";
        }
        input += "\r\n";
        try {
            writeInput(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        eligible = game.endQuest(game.current_q, game.sponsor,eligible);
    }


    @And("p{int} has {int} cards")
    public void pHasCards(int arg0, int arg1) {
        Assert.assertEquals(arg1, game.players.get(arg0-1).hand.size());
    }


    @When("the {int} starting hands are as posted for scenario {int}")
    public void theStartingHandsAreAsPostedForScenario(int arg0, int arg1) {
        game.distributeHands(12);
        //fix p1 hand
        Player p1 = new Player("p1",0, game.display);
        p1.addCardToHand(new AdventureCard("F5","F",5));
        p1.addCardToHand(new AdventureCard("F40","F",40));
        p1.addCardToHand(new AdventureCard("F15","F",15));
        p1.addCardToHand(new AdventureCard("F15","F",15));
        p1.addCardToHand(new AdventureCard("D5","D",5));
        p1.addCardToHand(new AdventureCard("S10","S",10));
        p1.addCardToHand(new AdventureCard("S10","S",10));
        p1.addCardToHand(new AdventureCard("F5","F",5));
        p1.addCardToHand(new AdventureCard("H10","H",10));
        p1.addCardToHand(new AdventureCard("F15","F",15));
        p1.addCardToHand(new AdventureCard("B15","B",15));
        p1.addCardToHand(new AdventureCard("L20","L",20));
        p1.sortHand();
        game.players.set(0,p1);
        game.currentPlayer =p1;
        //fix p2 hand
        Player p2 = new Player("p2",1, game.display);
        p2.addCardToHand(new AdventureCard("F5","F",5));
        p2.addCardToHand(new AdventureCard("F5","F",5));
        p2.addCardToHand(new AdventureCard("F15","F",15));
        p2.addCardToHand(new AdventureCard("F15","F",15));
        p2.addCardToHand(new AdventureCard("F40","F",40));
        p2.addCardToHand(new AdventureCard("D5","D",5));
        p2.addCardToHand(new AdventureCard("S10","S",10));
        p2.addCardToHand(new AdventureCard("H10","H",10));
        p2.addCardToHand(new AdventureCard("H10","H",10));
        p2.addCardToHand(new AdventureCard("B15","B",15));
        p2.addCardToHand(new AdventureCard("B15","B",15));
        p2.addCardToHand(new AdventureCard("E30","E",30));
        p2.sortHand();
        game.players.set(1,p2);
        //fix p3 hand
        Player p3 = new Player("p3",2, game.display);
        p3.addCardToHand(new AdventureCard("F5","F",5));
        p3.addCardToHand(new AdventureCard("F5","F",5));
        p3.addCardToHand(new AdventureCard("F5","F",5));
        p3.addCardToHand(new AdventureCard("F15","F",15));
        p3.addCardToHand(new AdventureCard("D5","D",5));
        p3.addCardToHand(new AdventureCard("S10","S",10));
        p3.addCardToHand(new AdventureCard("S10","S",10));
        p3.addCardToHand(new AdventureCard("S10","S",10));
        p3.addCardToHand(new AdventureCard("H10","H",10));
        p3.addCardToHand(new AdventureCard("H10","H",10));
        p3.addCardToHand(new AdventureCard("B15","B",15));
        p3.addCardToHand(new AdventureCard("L20","L",20));
        p3.sortHand();
        game.players.set(2,p3);
        //fix p4 hand
        Player p4 = new Player("p4",3, game.display);
        p4.addCardToHand(new AdventureCard("F5","F",5));
        p4.addCardToHand(new AdventureCard("F15","F",15));
        p4.addCardToHand(new AdventureCard("F15","F",15));
        p4.addCardToHand(new AdventureCard("F40","F",40));
        p4.addCardToHand(new AdventureCard("D5","D",5));
        p4.addCardToHand(new AdventureCard("D5","D",5));
        p4.addCardToHand(new AdventureCard("S10","S",10));
        p4.addCardToHand(new AdventureCard("H10","H",10));
        p4.addCardToHand(new AdventureCard("H10","H",10));
        p4.addCardToHand(new AdventureCard("B15","B",15));
        p4.addCardToHand(new AdventureCard("L20","L",20));
        p4.addCardToHand(new AdventureCard("E30","E",30));
        p4.sortHand();
        game.players.set(3,p4);
        //rig the deck
        game.main_deck.adventure_cards.set(0,new AdventureCard("F30", "F", 30));
        game.main_deck.adventure_cards.set(1,new AdventureCard("S10", "S", 10));
        game.main_deck.adventure_cards.set(2,new AdventureCard("B15", "B", 15));
        game.main_deck.adventure_cards.set(3,new AdventureCard("F10", "F", 10));
        game.main_deck.adventure_cards.set(4,new AdventureCard("L20", "L", 20));
        game.main_deck.adventure_cards.set(5,new AdventureCard("L20", "L", 20));
        game.main_deck.adventure_cards.set(6,new AdventureCard("B15", "B", 15));
        game.main_deck.adventure_cards.set(7,new AdventureCard("S10", "S", 10));
        game.main_deck.adventure_cards.set(8,new AdventureCard("F30", "F", 30));
        game.main_deck.adventure_cards.set(9,new AdventureCard("L20", "L", 20));
        game.main_deck.adventure_cards.set(10,new AdventureCard("F5", "F", 5));
        game.main_deck.adventure_cards.set(11,new AdventureCard("S10", "S", 10));
        game.main_deck.adventure_cards.set(12,new AdventureCard("B15", "B", 15));
        game.main_deck.adventure_cards.set(13,new AdventureCard("F10", "F", 10));
        game.main_deck.adventure_cards.set(14,new AdventureCard("L20", "L", 20));
        game.main_deck.adventure_cards.set(15,new AdventureCard("L20", "L", 20));
        game.main_deck.adventure_cards.set(16,new AdventureCard("B15", "B", 15));
        game.main_deck.adventure_cards.set(17,new AdventureCard("F5", "F", 5));
        game.main_deck.adventure_cards.set(18,new AdventureCard("F5", "F", 5));
        game.main_deck.adventure_cards.set(19,new AdventureCard("F5", "F", 5));
        game.main_deck.adventure_cards.set(20,new AdventureCard("F5", "F", 5));
        game.main_deck.adventure_cards.set(21,new AdventureCard("F5", "F", 5));
        game.main_deck.adventure_cards.set(22,new AdventureCard("B15", "B", 15));
        game.main_deck.adventure_cards.set(23,new AdventureCard("F10", "F", 10));
        game.main_deck.adventure_cards.set(24,new AdventureCard("L20", "L", 20));
        game.main_deck.adventure_cards.set(25,new AdventureCard("L20", "L", 20));
        game.main_deck.adventure_cards.set(26,new AdventureCard("B15", "B", 15));
        game.main_deck.adventure_cards.set(27,new AdventureCard("S10", "S", 10));
        game.main_deck.adventure_cards.set(28,new AdventureCard("F30", "F", 30));
        game.main_deck.adventure_cards.set(29,new AdventureCard("L20", "L", 20));

    }


    @And("next turn")
    public void nextturn() {
        game.nextTurn();
        eligible = null;
    }


    @And("End Turn")
    public void endTurn(List<String> winners) {
        game.endTurn();
        String s = "";
        if (winners.size() == 1) {
            s = "Game Over!\n" + winners.get(0) + " Wins The Game!";
        } else if (winners.size() == 2) {
            s = "Game Over!\n" + winners.get(0)+ " & " + winners.get(1) + " Win The Game!";
        }else if (winners.size() == 3) {
            s = "Game Over!\n" + winners.get(0) + " & " + winners.get(1) + " & " + winners.get(2) + " Win The Game!";
        }else if (winners.size() == 4) {
            s = "Game Over!\nEveryone Wins?!?";
        }
        String x = outputStreamCaptor.toString().trim().replace("\r","");
        Assert.assertTrue(x.contains(s));
        for (int i = 0; i < winners.size(); i++) {
            Boolean b = game.players.get(Integer.parseInt(winners.get(i).substring(1,2))-1).shields >= 7;
            Assert.assertTrue(b);
        }
    }


    @And("p{int} has {int} shields")
    public void pHasShields(int arg0, int arg1) {
        int shields = game.players.get(arg0-1).shields;
        Assert.assertEquals(arg1, shields);
    }


    @And("p{int} draws plaque")
    public void pDrawsPlaque(int arg0) {
        game.main_deck.event_cards.set(0,new EventCard("Plague","E",0));
        game.currentPlayer = game.getPlayer(arg0-1);
        game.nextEvent();
    }


    @And("p{int} draws prosperity")
    public void pDrawsProsperity(int arg0) {
        game.main_deck.adventure_cards.set(0,new AdventureCard("F15", "F", 15));
        game.main_deck.adventure_cards.set(1,new AdventureCard("F5", "F", 5));
        game.main_deck.adventure_cards.set(2,new AdventureCard("F5", "F", 5));
        game.main_deck.adventure_cards.set(3,new AdventureCard("F5", "F", 5));
        game.main_deck.adventure_cards.set(4,new AdventureCard("F15", "F", 15));
        game.main_deck.adventure_cards.set(5,new AdventureCard("F15", "F", 15));
        game.main_deck.event_cards.set(0,new EventCard("Prosperity","E",0));
        game.currentPlayer = game.getPlayer(arg0-1);
        String s = "";
        for (int i = 0; i < 4; i++) {
            Player p = game.getPlayer(i);
            if (p.handSize >=11){
                s += "10\r\n\r\n8\r\n\r\n";
            }
        }
        try {
            writeInput(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        game.nextEvent();
    }


    @And("p{int} draws queens favor")
    public void pDrawsQueen(int arg0) {
        game.main_deck.adventure_cards.set(0,new AdventureCard("F5", "F", 5));
        game.main_deck.adventure_cards.set(1,new AdventureCard("F5", "F", 5));
        game.main_deck.event_cards.set(0,new EventCard("Queen's Favor","E",0));
        game.currentPlayer = game.getPlayer(arg0-1);
        String s = "";
        Player p = game.getPlayer(arg0-1);
        if (p.handSize >=11){
            s += "1\r\n\r\n1\r\n\r\n";
        }
        try {
            writeInput(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        game.nextEvent();
    }

}


