package org.example;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private final PrintStream standardOut = System.out;
    private final InputStream standardin = System.in;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Test
    @DisplayName("check deck size is Adventure deck(100)")
    void RESP_01_test_01() {
        Main Game = new Main(true,true);

        int e_decksize = Game.GetAdventureDeckSize();
        assertEquals(100, e_decksize);
    }

    @Test
    @DisplayName("check deck size is event deck (17)")
    void RESP_01_test_02() {
        Main Game = new Main(true,true);

        int a_decksize = Game.GetEventDeckSize();
        assertEquals(17, a_decksize);
    }

    @Test
    @DisplayName("check foe cards = 50 and weapon cards = 50, match in count and correct amount of specified values")
    void RESP_01_test_03() {
        Main Game = new Main(true,true);
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
        Main Game = new Main(true,true);
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
        Main Game = new Main(true,true);
        Game.distributeHands(12);
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
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        int deckSize = Game.main_deck.getA_deck_size();
        assertEquals(52, deckSize, "Deck size should be 52");
    }

    @Test
    @DisplayName("The Game indicates whose turn it is and displays their hand")
    void RESP_03_test_01() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        Player p = new Player("p1",0, Game.display);
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
        Game.players.set(1,p);
        Game.nextTurn();
        assertEquals("Current Player: p1\nHand:\n1: F5\n2: F5\n3: F15\n4: F25\n5: F50\n6: D5\n7: S10\n8: S10\n9: H10\n10: H10\n11: B15\n12: E30", outputStreamCaptor.toString().trim().replace("\r",""));
    }


    @Test
    @DisplayName("At the end of a turn, game checks and displays winner, if there is one")
    void RESP_04_test_01() {
        outputStreamCaptor.reset();
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        //manufacture win condition here
        //multiple winners
        Player p1 = new Player("p1",0, Game.display);
        Player p2 = new Player("p2",1,Game.display);
        Player p3 = new Player("p3",2,Game.display);
        Player p4 = new Player("p4",3,Game.display);
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
        Game.waitForEnter(true);//check to see if game over
        assertEquals("End Of Turn!\nGame Over!\np1 & p2 Win The Game!", outputStreamCaptor.toString().trim().replace("\r",""));
        outputStreamCaptor.reset();
        //one winner
        Game = new Main(true,true);
        Game.distributeHands(12);
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
        Game.waitForEnter(true);
        assertEquals("End Of Turn!\nGame Over!\np4 Wins The Game!", outputStreamCaptor.toString().trim().replace("\r",""));
        assertEquals(false,Game.game_on);
    }

    @Test
    @DisplayName("The game draws and displays the next event card")
    void RESP_05_test_01() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        EventCard e = new EventCard("Q5","Q", 10);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean q_response = outputStreamCaptor.toString().trim().replace("\r","").contains("The Next Event Card Is: Q5,");
        assertTrue(q_response);
    }

    @Test
    @DisplayName("The current player has drawn the Plague card")
    void RESP_06_test_01() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(true,true);
        Game.distributeHands(11);
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
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        EventCard e = new EventCard("Plague","E",0);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        //check the display
        assertEquals("The Next Event Card Is: Plague,\np1 loses 2 shields!\nEnd Of Turn!", outputStreamCaptor.toString().trim().replace("\r",""));
    }

    @Test
    @DisplayName("player gains or loses shields")
    void RESP_07_test_01() {
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        Game.currentPlayer.shields = 1;
        Game.currentPlayer.adjustShields(-2);
        assertEquals(0, Game.currentPlayer.shields);
        Game.currentPlayer.adjustShields(10);
        assertEquals(10, Game.currentPlayer.shields);
    }

    @Test
    @DisplayName("if a player has >12 cards, they are directed to trim hand sequence")
    void RESP_08_test_01() {
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.currentPlayer.hand.add(Game.main_deck.DrawAdventureCard());
        Game.currentPlayer.handSize++;
        Game.currentPlayer.trimHand(-1);
        String out = outputStreamCaptor.toString().trim();
        Boolean redirected = (out.contains(Game.currentPlayer.name + " please trim your hand:"));
        assertEquals(true, redirected);
    }

    @Test
    @DisplayName("if a player is in trim hand sequence, they correctly discard a card")
    void RESP_09_test_01() {
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        Game.currentPlayer.hand.add(Game.main_deck.DrawAdventureCard());
        Game.currentPlayer.handSize++;
        String data = "1\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        Boolean h = Game.currentPlayer.trimHand(1);
        assertEquals(true, h);
    }

    @Test
    @DisplayName("if a player is in trim hand sequence, they kleep discarding until handsize =12")
    void RESP_10_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("1" + System.lineSeparator() + "2").getBytes());
        System.setIn(in);
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        Game.currentPlayer.hand.add(Game.main_deck.DrawAdventureCard());
        Game.currentPlayer.handSize++;
        Game.currentPlayer.hand.add(Game.main_deck.DrawAdventureCard());
        Game.currentPlayer.handSize++;
        Game.currentPlayer.trimHand(-2);
        assertEquals(12, Game.currentPlayer.handSize);
    }

    @Test
    @DisplayName("The current player has drawn a Q card, prompts current player for sponsorship")
    void RESP_11_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("" +System.lineSeparator()).getBytes());
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        EventCard e = new EventCard("Q5","Q",10);
        Game.main_deck.event_cards.set(0,e);
        Game.initiateQuest(10);
        Boolean quest_req= outputStreamCaptor.toString().trim().replace("\r","").contains(Game.currentPlayer.name+ " Would you like to sponsor this quest?:");
        //check the display
        assertEquals(true, quest_req);
    }

    @Test
    @DisplayName("The current player has drawn a Q card, no player sponsors Q and Q ends + current turn")
    void RESP_11_test_02() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(false,true);
        Game.distributeHands(12);
        EventCard e = new EventCard("Q5","Q",1);
        Game.main_deck.event_cards.set(0,e);
        Game.initiateQuest(1);
        Boolean quest_fail= outputStreamCaptor.toString().trim().replace("\r","").contains("No Sponsorship, Quest Abandoned");
        //check the display
        assertEquals(true, quest_fail);
    }

    @Test
    @DisplayName("The sponsor has been selected and hand displayed")
    void RESP_12_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "yes").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(false,true);
        Game.distributeHands(12);
        EventCard e = new EventCard("Q5","t",0);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Hand:");
        sponsor_hand = sponsor_hand && outputStreamCaptor.toString().trim().replace("\r","").contains("Sponsors The Quest!");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The sponsor has been prompted for the position of the next card or 'Quit'")
    void RESP_12_test_02() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" + System.lineSeparator()+ "1" + System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(false,true);
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        AdventureCard a2 = new AdventureCard("D5","D",5);
        sponsor.hand.set(0,a1);
        sponsor.hand.set(1,a2);
        Game.players.set(3,sponsor);
        EventCard e = new EventCard("Q5","t",1);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Select a card to add to the stage or 'Quit' if done");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The sponsor has entered 'wads' and gets error message")
    void RESP_13_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" +System.lineSeparator() + "wads" +System.lineSeparator() +  "1"+System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(false,true);
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        AdventureCard a2 = new AdventureCard("D5","D",5);
        sponsor.hand.set(0,a1);
        sponsor.hand.set(1,a2);
        Game.players.set(3,sponsor);
        EventCard e = new EventCard("Q5","t",1);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Invalid Input, must be an integer");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The sponsor has entered integer out of range and gets error message")
    void RESP_13_test_02() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" +System.lineSeparator() + "13" + System.lineSeparator() + "1"+System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(false,true);
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        AdventureCard a2 = new AdventureCard("D5","D",5);
        sponsor.hand.set(0,a1);
        sponsor.hand.set(1,a2);
        Game.players.set(3,sponsor);
        EventCard e = new EventCard("Q5","t",1);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Invalid Input, must be within size of hand");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The sponsor has entered integer in range and card is invalid (repeated)")
    void RESP_13_test_03() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" +System.lineSeparator() + "" +System.lineSeparator() + "1" +System.lineSeparator() + "12" +System.lineSeparator() + "12" +System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(true,true);
        Game.distributeHands(10);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        AdventureCard a2 = new AdventureCard("E30","E",30);
        AdventureCard a3 = new AdventureCard("E30","E",30);
        sponsor.hand.set(0,a1);
        sponsor.hand.set(1,a2);
        sponsor.hand.set(2,a3);
        EventCard e = new EventCard("Q5","Q",1);
        Quest q = new Quest(-1);
        Game.setupStage(0,sponsor,null);
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Invalid Input, must be an integer");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The sponsor has entered integer in range and card is valid")
    void RESP_13_test_04() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" +System.lineSeparator() + "1"+System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(false,true);
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        sponsor.hand.set(0,a1);
        Game.players.set(3,sponsor);
        EventCard e = new EventCard("Q5","t",1);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Card Valid");
        //check the display
        assertEquals(true, sponsor_hand);
    }


    @Test
    @DisplayName("The sponsor has entered integer in range and card is valid")
    void RESP_14_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" +System.lineSeparator() + "1"+System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        sponsor.hand.set(0,a1);
        Game.players.set(3,sponsor);
        EventCard e = new EventCard("Q1","t",1);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Game.setupStage(1, sponsor, null);
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Stage:");
        sponsor_hand=sponsor_hand && outputStreamCaptor.toString().trim().replace("\r","").contains("F5");
        //check the display
        assertEquals(true, sponsor_hand);
    }


    @Test
    @DisplayName("The sponsor has entered 'Quit' and the stage is empty, err message should appear")
    void RESP_15_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" + System.lineSeparator()+ "Quit" + System.lineSeparator() + "1"+ System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(false,true);
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        AdventureCard a2 = new AdventureCard("D5","D",5);
        sponsor.hand.set(0,a1);
        sponsor.hand.set(1,a2);
        Game.players.set(3,sponsor);
        EventCard e = new EventCard("Q5","t",1);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("A stage cannot be empty");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The sponsor has entered 'Quit' and the stage is less than the previous. The sponsor now adds another card and the stage value is greater")
    void RESP_16_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("1" + System.lineSeparator() + "Quit"+ System.lineSeparator() + "2"+ System.lineSeparator() + "Quit" + System.lineSeparator() + "3" + System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        Player sponsor = new Player("p3",3,Game.display);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        AdventureCard a2 = new AdventureCard("D5","D",5);
        AdventureCard a3 = new AdventureCard("F5","F",5);
        AdventureCard a4 = new AdventureCard("D5","D",5);
        sponsor.hand.clear();
        sponsor.handSize =4;
        sponsor.hand.add(a1);
        sponsor.hand.add(a2);
        sponsor.hand.add(a3);
        sponsor.hand.add(a4);
        sponsor.sortHand();
        Game.players.set(3,sponsor);
        EventCard e = new EventCard("Q5","t",2);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Player s = Game.setupStage(1, sponsor, null);
        Game.setupStage(2,sponsor,s);
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("A stage cannot be less than the previous");
        sponsor_hand= sponsor_hand && outputStreamCaptor.toString().trim().replace("\r","").contains("Setup Finished");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The sponsor is valid")
    void RESP_17_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "yes" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" + System.lineSeparator()+ "1" + System.lineSeparator() + "Quit"+ System.lineSeparator() + "3"+ System.lineSeparator() + "Quit" + System.lineSeparator() + "2" + System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        AdventureCard a2 = new AdventureCard("D5","D",5);
        sponsor.hand.clear();
        sponsor.hand.add(a1);
        sponsor.hand.add(a2);
        sponsor.handSize =2;
        Game.players.set(3,sponsor);
        Player badsponsor = new Player("p1", 1, Game.display);
        Game.players.set(1,badsponsor);
        EventCard e = new EventCard("Q5","t",2);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Game.initiateQuest(2);
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Cannot sponsor with the current hand");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The game displays the list of eligible players, in this case (p1,p2,p3)")
    void RESP_18_test_01() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        Game.sponsor = sponsor;
        Game.decidePlayers(null, null);
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Eligible Players:\np1\np2\np3");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The game asks each player if the withdraw or tackle")
    void RESP_19_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("w" + System.lineSeparator() + "w" + System.lineSeparator() + "w"+ System.lineSeparator() + "t").getBytes());
        System.setIn(in);
        Main Game = new Main(true,true);
        Game.distributeHands(8);
        Game.sponsor = Game.getPlayer(3);
        Quest q = new Quest(-1);
        q.stages.add(new Player("Stage1",-1,Game.display));
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.decidePlayers(q,null);
        //check the display
        assertEquals("Eligible Players:\n" +
                "p1\n" +
                "p2\n" +
                "p3\n" +
                "p1 Withdraw (w) or Tackle (t)?p2 Withdraw (w) or Tackle (t)?p3 Withdraw (w) or Tackle (t)?", outputStreamCaptor.toString().trim().replace("\r",""));
    }

    @Test
    @DisplayName("A participant who withdraws is no longer eligble for next stages")
    void RESP_19_test_02() {
        ByteArrayInputStream in = new ByteArrayInputStream(("t" + System.lineSeparator() + "" + System.lineSeparator() + "t"+ System.lineSeparator() + ""+ System.lineSeparator() + "w").getBytes());
        System.setIn(in);
        Main Game = new Main(true,true);
        Game.distributeHands(10);
        Player sponsor = Game.getPlayer(3);
        Game.sponsor = sponsor;
        Quest q = new Quest(-2);
        q.stageCount = -1;
        q.stages.add(new Player("Stage1",-1,Game.display));
        ArrayList<Player> e = new ArrayList<>(0);
        e.addAll(Game.players);
        e.remove(sponsor);
        e.removeLast();
        assertEquals(2,Game.decidePlayers(q,null).size());
    }

    @Test
    @DisplayName("An eligible participant who chooses to participate draws 1 adventure card")
    void RESP_20_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("t" + System.lineSeparator() + "" + System.lineSeparator() + "t" + System.lineSeparator() + ""+ System.lineSeparator() + "w").getBytes());
        System.setIn(in);
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        Player p1 = Game.getPlayer(0);
        Player p2 = Game.getPlayer(1);
        Player p3 = Game.getPlayer(2);
        p1.trimHand(2);
        p1.trimHand(2);
        p2.trimHand(2);
        p3.trimHand(2);
        Quest q = new Quest(1);
        q.stages.add(new Player("Stage1",-1,Game.display));
        Game.sponsor = sponsor;
        Game.decidePlayers(q,null);
        assertEquals(11, p1.handSize);
        assertEquals(12, p2.handSize);
        assertEquals(11, p3.handSize);
    }

    @Test
    @DisplayName("The quest ends if there are no participants for the current stage\n")
    void RESP_21_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("w" + System.lineSeparator() + "w" + System.lineSeparator() + "w" + System.lineSeparator()).getBytes());
        System.setIn(in);
        Main Game = new Main(true,true);
        Game.distributeHands(10);
        Player sponsor = Game.getPlayer(3);
        Quest q = new Quest(1);
        Player s1 = new Player("Stage 1", -1,Game.display);
        q.addStage(s1);
        Game.current_q = q;
        Game.sponsor = sponsor;
        System.setOut(new PrintStream(outputStreamCaptor));
        q.stageCount = 1;
        ArrayList<Player> e = Game.decidePlayers(q,new ArrayList<Player>(0));
        Game.endQuest(Game.current_q, Game.sponsor, e);
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Quest Finished!");
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("All cards used by the sponsor to build the quest are discarded by the game")
    void RESP_22_test_01() {
        Main Game = new Main(true,true);
        Game.distributeHands(10);
        Player sponsor = Game.getPlayer(3);
        Quest q = new Quest(2);
        Player s1 = new Player("Stage 1", -1,Game.display);
        s1.addCardToHand(new AdventureCard("F5", "F", 5));
        sponsor.addCardToHand(new AdventureCard("F5", "F", 5));
        Player s2 = new Player("Stage 2", -1,Game.display);
        s2.addCardToHand(new AdventureCard("F10", "F", 5));
        sponsor.addCardToHand(new AdventureCard("F10", "F", 5));
        q.addStage(s1);
        q.addStage(s2);
        int s = sponsor.getHandSize();
        ArrayList<Player> players = new ArrayList<>(0);
        Game.endQuest(null,sponsor, players);
        int d = sponsor.getHandSize();
        assertEquals((s-2), d);
    }

    @Test
    @DisplayName("Who then draws the same number of cards + the number of stages, and then possibly trims their hand")
    void RESP_22_test_02() {
        Main Game = new Main(true,true);
        Game.distributeHands(8);
        Player sponsor = Game.getPlayer(3);
        Quest q = new Quest(2);
        Player s1 = new Player("Stage 1", -1,Game.display);
        s1.addCardToHand(new AdventureCard("F5", "F", 5));
        sponsor.addCardToHand(new AdventureCard("F5", "F", 5));
        Player s2 = new Player("Stage 2", -1,Game.display);
        s2.addCardToHand(new AdventureCard("F10", "F", 5));
        sponsor.addCardToHand(new AdventureCard("F10", "F", 5));
        q.addStage(s1);
        q.addStage(s2);
        int s = sponsor.getHandSize();
        Game.endQuest(q,sponsor, Game.players);
        int d = sponsor.getHandSize();
        assertEquals((s+2),d);
    }

    @Test
    @DisplayName("The game displays the hand of the player and prompts the participant for the position of the next card to include in the attack or ‘quit’ (to end building this attack)\n")
    void RESP_23_test_01() {
        Main Game = new Main(true,true);
        Game.distributeHands(1);
        Player p1 = Game.getPlayer(0);
        ArrayList<Player> eligible = new ArrayList<>(0);
        eligible.add(p1);
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.setupAttack(p1,null);
        assertEquals("Setup Attack:\np1\nHand:\n1: "+p1.hand.get(0).name,outputStreamCaptor.toString().trim().replace("\r",""));
    }

    @Test
    @DisplayName("The selected card is included in the attack, which is displayed, ‘Quit’ is entered, in which case the cards (if any) used for this attack are displayed\n")
    void RESP_24_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("1" + System.lineSeparator() + "2" + System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        Main Game = new Main(true,true);
        Game.distributeHands(0);
        Player p1 = new Player("P1", 0,Game.display);
        ArrayList<Player> eligible = new ArrayList<>(0);
        p1.hand.add( new AdventureCard("D5", "D", 5));
        p1.hand.add( new AdventureCard("L20", "L", 20));
        eligible.add(p1);
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.setupAttack(p1,new Player("Stage 1",-1,Game.display));
        String attack_displa = outputStreamCaptor.toString().trim().replace("\r","").replace("\n","");
        Boolean attack_display = outputStreamCaptor.toString().trim().replace("\r","").replace("\n","").contains("Cards in attack: attack value: 251: D52: L20");
        assertEquals(true, attack_display);
    }

    @Test
    @DisplayName("participants with an attack less than the value of the current stage lose and become ineligible to further participate in this quest.\n")
    void RESP_25_test_01() {
        Main Game = new Main(true,true);
        Game.distributeHands(10);
        Player p1 = Game.getPlayer(0);
        ArrayList<Player> eligible = new ArrayList<>(0);
        Player stage = new Player("Stage 1", -1,Game.display);
        Player atk = new Player("p1", -1,Game.display);
        atk.shields = 5;
        eligible.add(atk);
        stage.shields = 10;
        Game.current_q = new Quest(1);
        Game.current_q.addStage(stage);
        Game.current_q.attacks = eligible;
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.playAttack(stage);
        Boolean attack_display = outputStreamCaptor.toString().trim().replace("\r","").contains("p1 Loses, attack: 5\np1 fails the quest");
        assertEquals(true, attack_display);
    }

    @Test
    @DisplayName("participants with an attack equal or greater to the value of the current stage are eligible for the next stage (if any). If this is the last stage, they are winners of this quest and earn as many shields as there are stages to this quest\n")
    void RESP_26_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("8" + System.lineSeparator()+ "Quit" + System.lineSeparator()+ "7" + System.lineSeparator()+ "Quit" + System.lineSeparator()).getBytes());
        System.setIn(in);
        Main Game = new Main(true,true);
        Game.distributeHands(8);
        Player sponsor = Game.getPlayer(3);
        Quest q = new Quest(1);
        Player s1 = new Player("Stage 1", -1,Game.display);
        s1.addCardToHand(new AdventureCard("F5", "F", 5));
        s1.shields =5;
        sponsor.addCardToHand(new AdventureCard("F5", "F", 5));
        q.addStage(s1);
        Player p1 = Game.getPlayer(0);
        p1.hand.set(0, new AdventureCard("E30", "E", 30));
        p1.hand.set(1, new AdventureCard("S10", "S", 10));
        Game.current_q = q;
        ArrayList<Player> eligible = new ArrayList<>();
        eligible.add(p1);
        eligible = Game.endQuest(q, sponsor, Game.attackResult(Game.playAttack(Game.setupAttack(p1,s1)),eligible));
        assertEquals(1,eligible.size());
        assertEquals(1,p1.shields);
    }

    @Test
    @DisplayName("All participants of the current stage have all the cards they used for their attack of the current stage discarded by the game.")
    void RESP_27_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(( "8" + System.lineSeparator()+ "Quit"+ System.lineSeparator()+ "").getBytes());
        System.setIn(in);
        Main Game = new Main(true,true);
        Game.distributeHands(8);
        Player sponsor = Game.getPlayer(3);
        Quest q = new Quest(1);
        Game.sponsor = sponsor;
        Game.current_q = q;
        Player s1 = new Player("Stage 1", -1,Game.display);
        s1.addCardToHand(new AdventureCard("F5", "F", 5));
        s1.shields =5;
        q.addStage(s1);
        Player p1 = Game.getPlayer(0);
        p1.hand.set(7, new AdventureCard("E35", "E", 350));
        p1.sortHand();
        ArrayList<Player> eligible = new ArrayList<>();
        eligible.add(Game.setupAttack(p1,s1));
        Game.current_q.attacks = eligible;
        Game.playAttack(s1);
        boolean test = true;
        for (int i = 0; i < p1.hand.size(); i++) {
            if (Objects.equals(p1.hand.get(i).name, "E35")){
                test = false;
            }
        }
        assertEquals(true, test);
    }

    @Test
    @DisplayName("The game indicates the turn of the current player has ended and clears the ‘hotseat’ display once that player presses the <return> key")
    void RESP_28_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("\n\r").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main(true,true);
        Game.distributeHands(12);
        Game.waitForEnter(false);
        assertEquals("Press Enter to end your turn:End Of Turn!", outputStreamCaptor.toString().trim().replace("\r",""));
    }

    @Test
    @DisplayName("Compulsory A-TEST")
    void A_TEST_JP_Scenario(){
        System.setOut(standardOut);
        System.out.flush();
        //setup mock input based on scenario
        ByteArrayInputStream in = new ByteArrayInputStream(("" +System.lineSeparator() + "no" +System.lineSeparator() + "yes"
                +System.lineSeparator() +"1" +System.lineSeparator() +"8" +System.lineSeparator()+"Quit" +
                System.lineSeparator()+"3" +System.lineSeparator()+"7" + System.lineSeparator() +"Quit"
                + System.lineSeparator()+"4" +System.lineSeparator()+"6" +System.lineSeparator()+"11" +System.lineSeparator() + "Quit"
                +System.lineSeparator() + "5" + System.lineSeparator() +"10"+System.lineSeparator() + "Quit"
                +System.lineSeparator()+ "t" +System.lineSeparator()+ "1" +System.lineSeparator()+ ""
                +System.lineSeparator() + "t" +System.lineSeparator()+ "1" +System.lineSeparator()+ ""
                +System.lineSeparator() + "t" +System.lineSeparator()+ "1" +System.lineSeparator()+ ""
                +System.lineSeparator()+ "5"+System.lineSeparator()+ "6" +System.lineSeparator()+ "Quit"
                +System.lineSeparator()+ "5"+System.lineSeparator()+ "4" +System.lineSeparator()+ "Quit"
                +System.lineSeparator()+ "5"+System.lineSeparator()+ "8" +System.lineSeparator()+ "Quit"
                +System.lineSeparator()+ "t"+System.lineSeparator()+ ""
                +System.lineSeparator()+ "t"+System.lineSeparator()+ ""
                +System.lineSeparator()+ "t"+System.lineSeparator()+ ""
                +System.lineSeparator()+ "7"+System.lineSeparator()+ "6"+System.lineSeparator()+ "Quit"
                +System.lineSeparator()+ "9"+System.lineSeparator()+ "4"+System.lineSeparator()+ "Quit"
                +System.lineSeparator()+ "6" +System.lineSeparator()+ "8"+System.lineSeparator()+ "Quit"
                +System.lineSeparator()+ "t"+System.lineSeparator()+ ""
                +System.lineSeparator()+ "t"+System.lineSeparator()+ ""
                +System.lineSeparator()+ "10"+System.lineSeparator()+ "6"+System.lineSeparator()+ "5"+System.lineSeparator()+ "Quit"
                +System.lineSeparator()+ "7"+System.lineSeparator()+ "5" +System.lineSeparator()+ "9"+System.lineSeparator()+ "Quit"
                +System.lineSeparator()+ "t"+System.lineSeparator()+ ""
                +System.lineSeparator()+ "t"+System.lineSeparator()+ ""
                + System.lineSeparator()+ "7"+System.lineSeparator()+ "6" +System.lineSeparator()+ "8"+System.lineSeparator()+ "Quit"
                + System.lineSeparator()+ "4"+System.lineSeparator()+ "5" +System.lineSeparator()+ "6"+System.lineSeparator()+ "8"+System.lineSeparator()+ "Quit"
                +"" +System.lineSeparator()+"1" +System.lineSeparator()+"1" +System.lineSeparator()+"1" +System.lineSeparator() + "1"+System.lineSeparator() + ""

        ).getBytes());
        System.setIn(in);
        Main Game = new Main(false,false);
        Game.distributeHands(12);
        //fix p1 hand
        Player p1 = new Player("p1",0, Game.display);
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
        Game.players.set(0,p1);
        Game.currentPlayer =p1;
        //fix p2 hand
        Player p2 = new Player("p2",1, Game.display);
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
        Game.players.set(1,p2);
        //fix p3 hand
        Player p3 = new Player("p3",2, Game.display);
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
        Game.players.set(2,p3);
        //fix p4 hand
        Player p4 = new Player("p4",3, Game.display);
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
        Game.players.set(3,p4);
        Game.game_on = true;
        //rig the deck
        Game.main_deck.event_cards.set(0,new EventCard("Q4", "Q", 4));
        Game.main_deck.adventure_cards.set(0,new AdventureCard("F30", "F", 30));
        Game.main_deck.adventure_cards.set(1,new AdventureCard("S10", "S", 10));
        Game.main_deck.adventure_cards.set(2,new AdventureCard("B15", "B", 15));
        Game.main_deck.adventure_cards.set(3,new AdventureCard("F10", "F", 10));
        Game.main_deck.adventure_cards.set(4,new AdventureCard("L20", "L", 20));
        Game.main_deck.adventure_cards.set(5,new AdventureCard("L20", "L", 20));
        Game.main_deck.adventure_cards.set(6,new AdventureCard("B15", "B", 15));
        Game.main_deck.adventure_cards.set(7,new AdventureCard("S10", "S", 10));
        Game.main_deck.adventure_cards.set(8,new AdventureCard("F30", "F", 30));
        Game.main_deck.adventure_cards.set(9,new AdventureCard("L20", "L", 20));
        //run the game
        Game.begin(Game);
        Game.current_q.setupPlay();
        Game.decidePlayers(Game.current_q,null);

        //all the asserts here
        //assert p1 has no shields and the proper cards
        assertEquals(0,p1.shields);
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.display.displayHand(p1);
        assertEquals("Hand:\n1: F5\n2: F10\n3: F15\n4: F15\n5: F30\n6: H10\n7: B15\n8: B15\n9: L20",outputStreamCaptor.toString().trim().replace("\r",""));

        //assert p3 has no shields and proper cards
        assertEquals(0,p3.shields);
        outputStreamCaptor.reset();
        Game.display.displayHand(p3);
        assertEquals("Hand:\n1: F5\n2: F5\n3: F15\n4: F30\n5: S10",outputStreamCaptor.toString().trim().replace("\r",""));

        //assert p4 has 4 shields and proper cards
        assertEquals(4,p4.shields);
        outputStreamCaptor.reset();
        Game.display.displayHand(p4);
        assertEquals("Hand:\n1: F15\n2: F15\n3: F40\n4: L20",outputStreamCaptor.toString().trim().replace("\r",""));

        //assert p4 hase 4 shields and proper number of cards
        assertEquals(0,p2.shields);
        assertEquals(12, p2.handSize);
    }

    //just to reset sysout
    @AfterEach
    void normalPrint() throws IOException {
        System.setOut(standardOut);
        System.setIn(standardin);
    }
}
