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
        Main Game = new Main();
        Game.distributeHands(12);
        int deckSize = Game.main_deck.getA_deck_size();
        assertEquals(52, deckSize, "Deck size should be 52");
    }

    @Test
    @DisplayName("The Game indicates whose turn it is and displays their hand")
    void RESP_03_test_01() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
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
        Game.currentPlayer = p;
        Game.nextTurn();
        assertEquals("Current Player: p1\nHand:\n1: F5\n2: F5\n3: F15\n4: F25\n5: F50\n6: D5\n7: S10\n8: S10\n9: H10\n10: H10\n11: B15\n12: E30", outputStreamCaptor.toString().trim().replace("\r",""));
    }


    @Test
    @DisplayName("At the end of a turn, game checks and displays winner, if there is one")
    void RESP_04_test_01() {
        outputStreamCaptor.reset();
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
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
        Game.endTurn();//check to see if game over
        assertEquals("End Of Turn:\nGame Over!\np1 & p2 Win The Game!", outputStreamCaptor.toString().trim().replace("\r",""));
        outputStreamCaptor.reset();
        //one winner
        Game = new Main();
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
        Game.endTurn();
        assertEquals("End Of Turn:\nGame Over!\np4 Wins The Game!", outputStreamCaptor.toString().trim().replace("\r",""));
        assertEquals(false,Game.game_on);
    }

    @Test
    @DisplayName("The game draws and displays the next event card")
    void RESP_05_test_01() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
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
        Main Game = new Main();
        Game.distributeHands(12);
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
        Game.distributeHands(12);
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
        Main Game = new Main();
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
        Main Game = new Main();
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
        Main Game = new Main();
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
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
        Game.distributeHands(12);
        EventCard e = new EventCard("Q5","Q",10);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean quest_req= outputStreamCaptor.toString().trim().replace("\r","").contains("The Next Event Card Is: Q5,\n"+Game.currentPlayer.name+ " Would you like to sponsor this quest?:");
        //check the display
        assertEquals(true, quest_req);
    }

    @Test
    @DisplayName("The current player has drawn a Q card, no player sponsors Q and Q ends + current turn")
    void RESP_11_test_02() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "no").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
        Game.distributeHands(12);
        EventCard e = new EventCard("Q5","Q",5);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
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
        Main Game = new Main();
        Game.distributeHands(12);
        EventCard e = new EventCard("Q5","t",-1);
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
        Main Game = new Main();
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
        Main Game = new Main();
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
        Main Game = new Main();
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
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" +System.lineSeparator() + "1" +System.lineSeparator() + "2" +System.lineSeparator() + "3" +System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        AdventureCard a2 = new AdventureCard("D5","D",5);
        sponsor.hand.set(0,a1);
        sponsor.hand.set(1,a2);
        sponsor.hand.set(2,a2);
        Game.players.set(3,sponsor);
        EventCard e = new EventCard("Q5","t",1);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Invalid Input, cannot be duplicate weapon");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The sponsor has entered integer in range and card is valid")
    void RESP_13_test_04() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" +System.lineSeparator() + "1"+System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
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
        Main Game = new Main();
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        sponsor.hand.set(0,a1);
        Game.players.set(3,sponsor);
        EventCard e = new EventCard("Q5","t",1);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
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
        Main Game = new Main();
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
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "no" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" + System.lineSeparator()+ "1" + System.lineSeparator() + "Quit"+ System.lineSeparator() + "3"+ System.lineSeparator() + "Quit" + System.lineSeparator() + "2" + System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        AdventureCard a2 = new AdventureCard("D5","D",5);
        sponsor.hand.set(0,a1);
        sponsor.hand.set(1,a2);
        sponsor.hand.set(2,a1);
        sponsor.hand.set(3,a1);
        Game.players.set(3,sponsor);
        EventCard e = new EventCard("Q5","t",2);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("A stage cannot be less than the previous");
        sponsor_hand= sponsor_hand && outputStreamCaptor.toString().trim().replace("\r","").contains("Setup Finished");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The sponsor has entered 'Quit' and the stage is less than the previous. The sponsor now adds another card and the stage value is greater")
    void RESP_17_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("no" + System.lineSeparator() + "yes" + System.lineSeparator() + "no" + System.lineSeparator() + "yes" + System.lineSeparator()+ "1" + System.lineSeparator() + "Quit"+ System.lineSeparator() + "3"+ System.lineSeparator() + "Quit" + System.lineSeparator() + "2" + System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        AdventureCard a1 = new AdventureCard("F5","F",5);
        AdventureCard a2 = new AdventureCard("D5","D",5);
        sponsor.hand.set(0,a1);
        sponsor.hand.set(1,a2);
        sponsor.hand.set(2,a1);
        sponsor.hand.set(3,a1);
        Game.players.set(3,sponsor);
        Player badsponsor = new Player("p1", 1, Game.display);
        Game.players.set(1,badsponsor);
        EventCard e = new EventCard("Q5","t",2);
        Game.main_deck.event_cards.set(0,e);
        Game.nextEvent();
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Cannot sponsor with the current hand");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The game displays the list of eligible players, in this case (p1,p2,p3)")
    void RESP_18_test_01() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main Game = new Main();
        Game.distributeHands(12);
        Player sponsor = Game.getPlayer(3);
        Game.playStage(null,sponsor);
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Eligible Players:\np1\np2\np3");
        //check the display
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("The game asks each player if the withdraw or tackle")
    void RESP_19_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("t" + System.lineSeparator() + "t" + System.lineSeparator() + "t").getBytes());
        System.setIn(in);
        Main Game = new Main();
        Game.distributeHands(10);
        Player sponsor = Game.getPlayer(3);
        Quest q = new Quest(1);
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.playStage(q,sponsor);
        //check the display
        assertEquals("Eligible Players:\np1\np2\np3\np1 Withdraw (w) or Tackle (t)?:\np2 Withdraw (w) or Tackle (t)?:\np3 Withdraw (w) or Tackle (t)?:",outputStreamCaptor.toString().trim().replace("\r",""));
    }

    @Test
    @DisplayName("A participant who withdraws is no longer eligble for next stages")
    void RESP_19_test_02() {
        ByteArrayInputStream in = new ByteArrayInputStream(("t" + System.lineSeparator() + "t" + System.lineSeparator() + "w").getBytes());
        System.setIn(in);
        Main Game = new Main();
        Game.distributeHands(10);
        Player sponsor = Game.getPlayer(3);
        Quest q = new Quest(1);
        Game.playStage(q,sponsor);
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.playStage(null,sponsor);
        assertEquals("Eligible Players:\np1\np2",outputStreamCaptor.toString().trim().replace("\r",""));
    }

    @Test
    @DisplayName("An eligible participant who chooses to participate draws 1 adventure card")
    void RESP_20_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("t" + System.lineSeparator() + "t" + System.lineSeparator() + "w").getBytes());
        System.setIn(in);
        Main Game = new Main();
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
        Game.playStage(q,sponsor);
        assertEquals(11, p1.handSize);
        assertEquals(12, p2.handSize);
        assertEquals(11, p3.handSize);
    }

    @Test
    @DisplayName("The quest ends if there are no participants for the current stage\n")
    void RESP_21_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("w" + System.lineSeparator() + "w" + System.lineSeparator() + "w" + System.lineSeparator()).getBytes());
        System.setIn(in);
        Main Game = new Main();
        Game.distributeHands(10);
        Player sponsor = Game.getPlayer(3);
        Quest q = new Quest(1);
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.playStage(q,sponsor);
        Boolean sponsor_hand= outputStreamCaptor.toString().trim().replace("\r","").contains("Quest Finished!");
        assertEquals(true, sponsor_hand);
    }

    @Test
    @DisplayName("All cards used by the sponsor to build the quest are discarded by the game")
    void RESP_22_test_01() {
        Main Game = new Main();
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
        Game.endQuest(null,sponsor);
        int d = sponsor.getHandSize();
        assertEquals((s-2), d);
    }

    @Test
    @DisplayName("Who then draws the same number of cards + the number of stages, and then possibly trims their hand")
    void RESP_22_test_02() {
        Main Game = new Main();
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
        Game.endQuest(q,sponsor);
        int d = sponsor.getHandSize();
        assertEquals((s+2),d);
    }

    @Test
    @DisplayName("The game displays the hand of the player and prompts the participant for the position of the next card to include in the attack or ‘quit’ (to end building this attack)\n")
    void RESP_23_test_01() {
        Main Game = new Main();
        Game.distributeHands(1);
        Player p1 = Game.getPlayer(0);
        ArrayList<Player> eligible = new ArrayList<>(0);
        eligible.add(p1);
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.setupAttack(eligible,null);
        assertEquals("Setup Attack:\np1\nHand:\n1: "+p1.hand.get(0).name,outputStreamCaptor.toString().trim().replace("\r",""));
    }

    @Test
    @DisplayName("The selected card is included in the attack, which is displayed, ‘Quit’ is entered, in which case the cards (if any) used for this attack are displayed\n")
    void RESP_24_test_01() {
        ByteArrayInputStream in = new ByteArrayInputStream(("1" + System.lineSeparator() + "2" + System.lineSeparator() + "Quit").getBytes());
        System.setIn(in);
        Main Game = new Main();
        Game.distributeHands(10);
        Player p1 = Game.getPlayer(0);
        ArrayList<Player> eligible = new ArrayList<>(0);
        p1.hand.set(0, new AdventureCard("D5", "D", 5));
        p1.hand.set(1, new AdventureCard("L20", "L", 20));
        eligible.add(p1);
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.setupAttack(eligible,new Player("Stage 1",-1,Game.display));
        Boolean attack_display = outputStreamCaptor.toString().trim().replace("\r","").contains("Cards in attack:\n1: D5\n2: L20");
        assertEquals(true, attack_display);
    }

    @Test
    @DisplayName("participants with an attack less than the value of the current stage lose and become ineligible to further participate in this quest.\n")
    void RESP_25_test_01() {
        Main Game = new Main();
        Game.distributeHands(10);
        Player p1 = Game.getPlayer(0);
        ArrayList<Player> eligible = new ArrayList<>(0);
        Player stage = new Player("Stage 1", -1,Game.display);
        Player atk = new Player("Stage 1", -1,Game.display);
        atk.shields = 5;
        eligible.add(atk);
        stage.shields = 10;
        System.setOut(new PrintStream(outputStreamCaptor));
        Game.playAttack(eligible,stage);
        Boolean attack_display = outputStreamCaptor.toString().trim().replace("\r","").contains("p1 Loses, attack: 5 stage: 10\np1 fails the quest");
        assertEquals(true, attack_display);
    }
    //just to reset sysout
    @AfterEach
    void normalPrint() throws IOException {
        System.setOut(standardOut);
        System.setIn(standardin);
    }
}
