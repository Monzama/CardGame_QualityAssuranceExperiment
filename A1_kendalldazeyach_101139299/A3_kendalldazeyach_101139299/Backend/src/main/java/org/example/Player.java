package org.example;
import java.util.*;

public class Player {
    int handSize;
    String name;
    ArrayList<AdventureCard> hand;
    int shields;
    int id;
    int sleep = 1;
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

    public int getHandSize() {
        return handSize;
    }

    public String getName() {
        return name;
    }

    public ArrayList<AdventureCard> getHand() {
        return hand;
    }

    public void addCardToHand(AdventureCard c, boolean update) throws InterruptedException {
        hand.add(c);
        handSize++;
        if (handSize > 12) {
            trimHand(-2);
            display.clearScreen(true);
            Thread.sleep(sleep);
            sortHand();
        }
        // Send update for this player only
        if (update){
            display.sendMessage("", true);
            Thread.sleep(sleep);
            String status = "player-status:" + getName() + ": " + shields + " Shields | " + handSize + " Cards";
            display.sendMessage(status, true);
            Thread.sleep(sleep);
        }
    }

    public void addCardToHand(AdventureCard c) throws InterruptedException {
        hand.add(c);
        handSize++;
        if (handSize > 12) {
            trimHand(-2);
            display.clearScreen(true);
            Thread.sleep(sleep);
            sortHand();
        }
    }
    public void removeCard(int index, boolean update) throws InterruptedException {
                this.hand.remove(index);
                this.handSize--;
        if (update){
            display.sendMessage("", true);
            Thread.sleep(sleep);
            String status = "player-status:" + getName() + ": " + shields + " Shields | " + handSize + " Cards";
            display.sendMessage(status, true);
            Thread.sleep(sleep);
        }
    }

    public void adjustShields(int mod, boolean update) throws InterruptedException {
        shields += mod;
        if (shields < 0) shields = 0;
        if (update){
            display.sendMessage("", true);
            Thread.sleep(sleep);
            String status = "player-status:" + getName() + ": " + shields + " Shields | " + handSize + " Cards";
            display.sendMessage(status, true);
            Thread.sleep(sleep);
        }
    }

    public boolean trimHand(int index) throws InterruptedException {
        if (index==-1){
            display.sendMessage(this.name + " please trim your hand:",false);
        }
        if (index ==-2){
            int n = hand.size() -12;
            for (int i=0;i<n;i++) {
                sortHand();
                display.sendMessage(this.name + " please trim your hand:", false);
                display.displayHand(this);
                Thread.sleep(sleep);
                int remove = -1;
                    String r = display.getMessage("Please select your card (1-" + handSize + ")");
                    if (display.isResetInProgress()){
                        return false;
                    }
                    // Default value in case parsing fails
                    try {
                        remove = Integer.parseInt(r);
                        hand.remove(remove-1);
                        handSize--;
                    } catch (NumberFormatException e) {
                        trimHand(index);
                        System.err.println("Invalid input: '" + r + "' is not a valid integer.");
                        return false;
                    }
            }
            sortHand();
            display.sendMessage("Trimmed ", false);
            display.displayHand(this);
        } else if (index >=0 && index < handSize) {
            hand.remove(index-1);
            handSize--;
        }
        return handSize == 12;
    }
    public boolean canSponsor(int stageCount, int prevValue) {
        // A single stage is valid if the player has at least one foe card
        if (stageCount == 1) {
            return hand.stream().anyMatch(card -> "F".equals(card.type));
        }

        // Sort the hand for consistent processing
        sortHand();
        ArrayList<AdventureCard> availableCards = new ArrayList<>(hand);
        int firstFoe = 0;

        // Ensure there are enough cards to sponsor the stages
        if (availableCards.size() < stageCount) {
            return false;
        }

        // Process each stage
        for (int i = 0; i < stageCount; i++) {
            AdventureCard foeCard = null;
            int stageValue = 0;
            ArrayList<AdventureCard> usedCards = new ArrayList<>();

            // Find a foe card for the stage
            Iterator<AdventureCard> iterator = availableCards.iterator();
            while (iterator.hasNext()) {
                AdventureCard card = iterator.next();
                if (card.type.equals("F") && card.value > firstFoe) {
                    firstFoe = 0;
                    foeCard = card;
                    stageValue += card.value;
                    System.out.println(stageValue);
                    usedCards.add(card);
                    iterator.remove(); // Remove safely using the iterator
                    break;
                }
            }

            // If no foe card is found, this stage cannot be completed
            if (foeCard == null) {
                return false;
            }

            if (stageValue > prevValue) {
                // Update for the next stage
                prevValue = stageValue;
                continue;
            }

            // Add non-repeated weapons to the stage
            iterator = availableCards.iterator(); // Create a new iterator for the second loop
            while (iterator.hasNext()) {
                AdventureCard card = iterator.next();
                if (!"F".equals(card.type) && !usedCards.contains(card)) {
                    stageValue += card.value;
                    usedCards.add(card);
                    iterator.remove(); // Remove safely using the iterator
                }
                if (stageValue > prevValue) {
                    // Update for the next stage
                    break;
                }
            }

            // Ensure the stage is stronger than the previous one
            if (stageValue <= prevValue) {
                availableCards.addAll(usedCards);
                availableCards.add(foeCard);
                Collections.sort(availableCards);
                firstFoe = foeCard.value;
                i -= 1;
                continue;
            }

            // Update for the next stage
            prevValue = stageValue;
        }

        return true; // All stages are valid
    }



    // Foes in increasing order, then weapons in increasing order, swords before horses.
    public void sortHand() {
        Collections.sort(this.hand);
    }
}

