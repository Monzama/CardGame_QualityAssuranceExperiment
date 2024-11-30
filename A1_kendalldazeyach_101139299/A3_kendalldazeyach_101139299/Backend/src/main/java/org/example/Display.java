package org.example;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class Display {

    public Display() {
        System.out.println("Display initialized");
    }
    private final StringBuilder outputLog = new StringBuilder(); // To store messages for the frontend
    private final AtomicReference<String> userResponse = new AtomicReference<>(); // To capture user input

    // Clears the screen (simulated for frontend)
    public void clear() {
        outputLog.setLength(0); // Clear the output log
    }

    // Handles the 'Press Enter to continue' case
    public void clearScreen(boolean wait) {
        if (wait) {
            getMessage("Press Enter to continue");
        }
        clear();
    }

    // Simulates sending a message to the frontend
    public void sendMessage(String message) {
        outputLog.append(message).append("\n");
    }

    // Simulates getting a message from the frontend
    public String getMessage(String prompt) {
        sendMessage(prompt); // Display the prompt
        while (userResponse.get() == null) {
            try {
                Thread.sleep(100); // Wait for user response
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        String response = userResponse.get();
        userResponse.set(null); // Clear the response for the next prompt
        return response;
    }

    // Displays the current player's turn
    public void displayTurn(Player player) {
        clearScreen(false);
        sendMessage("Current Player: " + player.getName());
    }

    // Displays a player's hand
    public void displayHand(Player player) {
        if (player == null) return;

        if (player.id == -2) {
            sendMessage("Cards in attack: attack value: " + player.shields);
        } else if (player.id == -1) {
            sendMessage("Stage: Value: " + player.shields);
        } else {
            player.sortHand();
            sendMessage("Hand:");
        }

        ArrayList<AdventureCard> hand = player.getHand();
        for (int i = 0; i < player.getHandSize(); i++) {
            sendMessage((i + 1) + ": " + hand.get(i).GetCardName());
        }
    }

    // Retrieves the output log for the frontend
    public String getOutputLog() {
        return outputLog.toString();
    }

    // Clears the output log
    public void clearLog() {
        outputLog.setLength(0);
    }

    // Used by the controller to set user input from the frontend
    public void setUserResponse(String response) {
        userResponse.set(response);
    }
}
