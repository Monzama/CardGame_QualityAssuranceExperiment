package org.example;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    @DisplayName("check deck size is Adventure deck(100)")
    void RESP_01_test_01() {
        Main Game = new Main();

        int e_decksize = Game.GetAdventureDeckSize();
        assertEquals(100, e_decksize);
    }

    @Test
    @DisplayName("check deck size is event deck (17)")
    void RESP_01_test_02() {
        Main Game = new Main();

        int a_decksize = Game.GetEventDeckSize();
        assertEquals(17, a_decksize);
    }

    @Test
    @DisplayName("check foe cards = 50 and weapon cards = 50, match in count and correct amount of specified values")
    void RESP_01_test_03() {
        Main Game = new Main();
        AdventureCard c;

        Dictionary<String, Integer> a_cards = new Hashtable<>();

        int foe_count = 0, weapon_count = 0;
        for (int i = 0; i < 100; i++) {
            c = Game.DrawAdventureCard();
            if (a_cards.get((c.GetCardType()+c.GetCardValue()))!= null) {
                int count = a_cards.get((c.GetCardType() + c.GetCardValue()));
                count++;
                a_cards.put((c.GetCardType() + c.GetCardValue()), count);
            }else{
                a_cards.put((c.GetCardType() + c.GetCardValue()), 1);
            }
            if (Objects.equals(c.GetCardType(), "F")){
                foe_count++;
            } else {
                weapon_count++;
            }
        }
        assertEquals(50, foe_count, "Foe cards should be 50");
        assertEquals(50, weapon_count, "Weapon cards should be 50");
        // check values of foe cards
        assertEquals(8, a_cards.get("F5"), "8 F5 cards");
        assertEquals(7, a_cards.get("F10"), "7 F10 cards");
        assertEquals(8, a_cards.get("F15"), "8 F15 cards");
        assertEquals(7, a_cards.get("F20"), "7 F20 cards");
        assertEquals(7, a_cards.get("F25"), "7 F25 cards");
        assertEquals(4, a_cards.get("F30"), "4 F30 cards");
        assertEquals(4, a_cards.get("F35"), "4 F35 cards");
        assertEquals(2, a_cards.get("F40"), "2 F40 cards");
        assertEquals(2, a_cards.get("F50"), "2 F50 cards");
        assertEquals(1, a_cards.get("F70"), "1 F70 cards");

        //check the values of the weapon cards
        assertEquals(6, a_cards.get("D5"), "6 D5 cards");
        assertEquals(12, a_cards.get("H10"), "12 H10 cards");
        assertEquals(16, a_cards.get("S10"), "16 S10 cards");
        assertEquals(8, a_cards.get("B15"), "8 B15 cards");
        assertEquals(6, a_cards.get("L20"), "6 L20 cards");
        assertEquals(2, a_cards.get("E30"), "2 E30 cards");
        //by the time we get here, we know the deck is perfect
    }
    @Test
    @DisplayName("check Q cards = 12 and E cards = 5,  match in count and correct amount of specified values")
    void RESP_01_test_04() {
        Main Game = new Main();
        EventCard c;
        Dictionary<String, Integer> e_cards = new Hashtable<>();

        int Q_count = 0, E_count = 0;
        for (int i = 0; i < 17; i++) {
            c = Game.DrawEventCard();
            if (e_cards.get(c.GetCardName())!= null) {
                int count = e_cards.get(c.GetCardName());
                count++;
                e_cards.put(c.GetCardName(), count);
            }else{
                e_cards.put(c.GetCardName(), 1);
            }
            if (Objects.equals(c.GetCardType(), "Q")){
                Q_count++;
            } else{
                E_count++;
            }
        }
        assertEquals(12, Q_count, "Q cards should be 12");
        assertEquals(5, E_count, "E cards should be 5");
        // check values of the Q cards
        assertEquals(3, e_cards.get("Q2"), "3 Q2 cards");
        assertEquals(4, e_cards.get("Q3"), "4 Q3 cards");
        assertEquals(3, e_cards.get("Q4"), "3 Q4 cards");
        assertEquals(2, e_cards.get("Q5"), "2 Q5 cards");

        //check the values of the E cards
        assertEquals(1, e_cards.get("Plague"), "1 Plague card");
        assertEquals(2, e_cards.get("Queen's Favor"), "2 Queen's Favor cards");
        assertEquals(2, e_cards.get("Prosperity"), "2 Prosperity Cards");
        //by the time we get here, we know the deck is perfect
    }

    @Test
    @DisplayName("Distribute 12 cards to each player")
    void RESP_02_test_01() {
        Main Game = new Main();
        Game.distributeHands();
        Boolean lessThanTwelve = false;
        for (int i = 0; i < 4; i++) {
            Player p = Game.getPlayer(i);
            if (p.getHandSize()<12){lessThanTwelve = true;}
        }
        assertEquals(false, lessThanTwelve, "At least one player does not have 12 cards");
    }
    @Test
    @DisplayName("Deck is updated, should now have 52 cards")
    void RESP_02_test_02() {
        Main Game = new Main();
        Game.distributeHands();
        int deckSize = Game.main_deck.getA_deck_size();
        assertEquals(52, deckSize, "Deck size should be 52");
    }

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Test
    @DisplayName("The Game indicates whose turn it is and displays their hand")
    void RESP_03_test_01() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
        Game.distributeHands();
        Player p = new Player("p1",0);
        p.addCardToHand(new AdventureCard("F5","F",5));
        p.addCardToHand(new AdventureCard("F15","F",15));
        p.addCardToHand(new AdventureCard("F25","F",25));
        p.addCardToHand(new AdventureCard("F50","F",50));
        p.addCardToHand(new AdventureCard("F5","F",5));
        p.addCardToHand(new AdventureCard("D5","D",5));
        p.addCardToHand(new AdventureCard("E30","E",30));
        p.addCardToHand(new AdventureCard("B15","B",15));
        p.addCardToHand(new AdventureCard("S10","S",10));
        p.addCardToHand(new AdventureCard("H10","H",10));
        p.addCardToHand(new AdventureCard("S10","S",10));
        p.addCardToHand(new AdventureCard("H10","H",10));
        p.sortHand();
        Game.currentPlayer = p;
        Game.nextTurn();
        assertEquals("Current Player: p1\nHand:\nF5\nF5\nF15\nF25\nF50\nD5\nS10\nS10\nH10\nH10\nB15\nE30", outputStreamCaptor.toString().trim().replace("\r",""));
    }


    @Test
    @DisplayName("At the end of a turn, game checks and displays winner, if there is one")
    void RESP_04_test_01() {
        outputStreamCaptor.reset();
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
        Game.distributeHands();
        //manufacture win condition here
        //multiple winners
        Player p1 = new Player("p1",0);
        Player p2 = new Player("p2",1);
        Player p3 = new Player("p3",2);
        Player p4 = new Player("p4",3);
        p1.shields = 7;
        p2.shields = 7;
        p3.shields = 6;
        p4.shields = 0;
        ArrayList<Player> ps = new ArrayList<>(0);
        ps.add(p1);
        ps.add(p2);
        ps.add(p3);
        ps.add(p4);
        Game.players = ps;
        Game.endTurn();//check to see if game over
        assertEquals("End Of Turn:\nGame Over!\np1 & p2 Win The Game!", outputStreamCaptor.toString().trim().replace("\r",""));
        outputStreamCaptor.reset();
        //one winner
        Game = new Main();
        Game.distributeHands();
        p1.shields = 0;
        p2.shields = 0;
        p3.shields = 0;
        p4.shields = 7;
        ps = new ArrayList<>(0);
        ps.add(p1);
        ps.add(p2);
        ps.add(p3);
        ps.add(p4);
        Game.players = ps;
        Game.endTurn();
        assertEquals("End Of Turn:\nGame Over!\np4 Wins The Game!", outputStreamCaptor.toString().trim().replace("\r",""));
        assertEquals(false,Game.game_on);
    }

    @Test
    @DisplayName("The game draws and displays the next event card")
    void RESP_05_test_01() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
        Game.distributeHands();
        EventCard e = new EventCard("Q5","Q", 5);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        assertEquals("The Next Event Card Is: Q5,", outputStreamCaptor.toString().trim().replace("\r",""));
    }

    @Test
    @DisplayName("The current player has drawn the Plague card")
    void RESP_06_test_01() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
        Game.distributeHands();
        EventCard e = new EventCard("Plague","E",0);
        Game.currentPlayer.shields = 3;
        int cur_shields = Game.currentPlayer.shields;
        int cur_hand = Game.currentPlayer.handSize;
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean updated_hand_or_shields = false;
        if (cur_shields != Game.currentPlayer.shields) {
            updated_hand_or_shields = true;
        }
        if (cur_hand != Game.currentPlayer.handSize) {
            updated_hand_or_shields = true;
        }
        assertEquals(true,updated_hand_or_shields, "current player hand size or shields have updated based on E card");
        //check the minimum shields
    }

    @Test
    @DisplayName("The current player has drawn an E card, E- action and card are displayed")
    void RESP_06_test_02() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
        Game.distributeHands();
        EventCard e = new EventCard("Plague","E",0);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        //check the display
        assertEquals("The Next Event Card Is: Plague,\np1 loses 2 shields!\nEnd Of Turn:", outputStreamCaptor.toString().trim().replace("\r",""));
    }

    @Test
    @DisplayName("player gains or loses shields")
    void RESP_07_test_01() {
        Main Game = new Main();
        Game.distributeHands();
        Game.currentPlayer.shields = 1;
        Game.currentPlayer.adjustShields(-2);
        assertEquals(0, Game.currentPlayer.shields);
        Game.currentPlayer.adjustShields(10);
        assertEquals(10, Game.currentPlayer.shields);
    }

    @Test
    @DisplayName("if a player has >12 cards, they are directed to trim hand sequence")
    void RESP_08_test_01() {
        Main Game = new Main();
        Game.distributeHands();
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.currentPlayer.addCardToHand(Game.main_deck.DrawAdventureCard());
        String out = outputStreamCaptor.toString().trim();
        Boolean redirected = (out.contains(Game.currentPlayer.name + " please trim your hand:"));
        assertEquals(true, redirected);
    }

    @Test
    @DisplayName("if a player is in trim hand sequence, they discard until hand size =12")
    void RESP_09_test_01() {
        System.setOut(standardOut);
        Main Game = new Main();
        Game.distributeHands();
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.currentPlayer.hand.add(Game.main_deck.DrawAdventureCard());
        Game.currentPlayer.handSize++;
        String data = "1\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        Boolean h = Game.currentPlayer.trimHand();
        assertEquals(true, h);
    }



    //just to reset sysout
    @AfterEach
    void normalPrint(){
        System.setOut(standardOut);
    }
}
