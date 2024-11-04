package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.io.*;
import java.io.IOException;

public class GameSteps {

    private Main game;
    private final PrintStream standardOut = System.out;
    private final InputStream standardin = System.in;

    PipedOutputStream pipedOut = new PipedOutputStream();
    PipedInputStream pipedIn = new PipedInputStream(pipedOut);

    String dynamicInput = "";
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    public GameSteps() throws IOException {
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
        game.nextEvent();
    }
    @Then("p{int} is asked to sponsor")
    public void pIsAskedToSponsor(int arg0) {
        try {
            writeInput("no"+"\r\n" + "yes"+"\r\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        game.initiateQuest(game.current_event.GetCardValue());
        String x = outputStreamCaptor.toString().trim().replace("\r","");
        if (arg0 == 1){
            Assert.assertTrue(x.contains("p1 Would you like to sponsor this quest?"));
        }else if (arg0 == 2){
            Assert.assertTrue(x.contains("p2 Would you like to sponsor this quest?"));
        }else if (arg0 == 3){
            Assert.assertTrue(x.contains("p3 Would you like to sponsor this quest?"));
        }else if (arg0 == 4){
            Assert.assertTrue(x.contains("p4 Would you like to sponsor this quest?"));
        }
    }

    @And("p{int} chooses to sponsor")
    public void pChoosesToSponsor(int arg0) {
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

    @And("p{int} builds the {int} stages of the quest as posted")
    public void pBuildsTheStagesOfTheQuestAsPosted(int arg0, int arg1) {
        if (arg0 == 2){
            try {
                writeInput("1"+"\r\n" + "8"+"\r\n"+ "Quit"+"\r\n"+ "3"+"\r\n"+ "7"+"\r\n"+ "Quit"+"\r\n"+ "4"+"\r\n"+ "6"+"\r\n"+ "11"+"\r\n"+ "Quit"+"\r\n"+ "5"+"\r\n"+ "10"+"\r\n"+ "Quit"+"\r\n");//stage should be set by now
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            game.initializeStage(game.sponsor,game.current_event.GetCardValue());
            //test the stage?
            String x = outputStreamCaptor.toString().trim().replace("\r","");
            Assert.assertTrue(x.contains("Quest has been setup!"));

        }

    }

    @And("Other players decide to participate and draw a card")
    public void otherPlayersDecideToParticipateAndDrawACard() throws IOException {
        try {
            if (game.players.get(0).handSize ==12){
                writeInput("t"+"\r\n" + "1"+"\r\n"+ "t"+"\r\n"+ "1"+"\r\n"+ "t"+"\r\n"+ "1" + "\r\n");
            }else{
                writeInput("t" + "\r\n"+ "t"+"\r\n"+ "t"+"\r\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        game.playStage(game.current_q, game.sponsor);
        String x = outputStreamCaptor.toString().trim().replace("\r","");
        Assert.assertTrue(x.contains("p2 Draws a"));
        Assert.assertTrue(x.contains("p3 Draws a"));
        Assert.assertTrue(x.contains("p4 Draws a"));
    }
}
