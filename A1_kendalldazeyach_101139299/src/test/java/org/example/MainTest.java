package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        AdventureCard c = Game.DrawAdventureCard();

        Dictionary<String, Integer> a_cards = new Hashtable<>();

        int foe_count = 0, weapon_count = 0;
        for (int i = 0; i < 100; i++) {
            if (a_cards.get((c.GetCardType()+c.GetCardValue()))!= null) {
                int count = a_cards.get((c.GetCardType() + c.GetCardValue()));
                count++;
                a_cards.put((c.GetCardType() + c.GetCardValue()), count);
            }else{
                a_cards.put((c.GetCardType() + c.GetCardValue()), 1);
            }
            if (Objects.equals(c.GetCardType(), "F")){
                foe_count++;
            } else{
                weapon_count++;
            }
            c = Game.DrawAdventureCard();
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
        EventCard c = Game.DrawEventCard();
        Dictionary<String, Integer> e_cards = new Hashtable<>();

        int Q_count = 0, E_count = 0;
        for (int i = 0; i < 100; i++) {
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
            c = Game.DrawEventCard();
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

}



