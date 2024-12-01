package org.example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class Display {

    private final SimpMessagingTemplate messagingTemplate;
    private final StringBuilder outputLog = new StringBuilder(); // To store messages for the frontend
    private final AtomicReference<String> userResponse = new AtomicReference<>();
    final AtomicInteger waitingThreads = new AtomicInteger(0); // Track waiting threads
    private boolean terminateExtraThreads = false; // Flag to terminate extra threads

    @Autowired
    public Display(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        System.out.println("Display initialized");
    }

    public void clearinput(){
        userResponse.set(null);
    }
    public synchronized String getMessage(String prompt) {
        waitingThreads.incrementAndGet(); // Increment thread count
        try {
            clearinput(); // Ensure any existing input is cleared
            sendMessage(prompt, true); // Send the prompt to the frontend
            System.out.println("getMessage: Waiting for user response... Prompt: " + prompt);

            // Block until a response is received or termination is flagged
            while (userResponse.get() == null) {
                // Exit if termination is flagged
                if (terminateExtraThreads) {
                    System.out.println("getMessage: Termination flag set. Exiting thread.");
                    return null;
                }
                try {
                    wait(100); // Use a timeout to periodically recheck conditions
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                    System.err.println("getMessage: Thread interrupted!");
                    return null;
                }
            }

            // Retrieve and clear the response
            String response = userResponse.getAndSet(null);
            System.out.println("getMessage: Received user response: " + response);
            return response;

        } finally {
            int remaining = waitingThreads.decrementAndGet(); // Decrement the thread count after processing
            System.out.println("getMessage: Exiting thread. Remaining threads: " + remaining);
            notifyAll(); // Notify other waiting threads if necessary
        }
    }



    public synchronized void setUserResponse(String response) {
        if (userResponse.get() != null) {
            System.out.println("setUserResponse: Ignoring redundant response: " + response);
            return; // Ignore if a response is already set
        }
        userResponse.set(response); // Set the user response
        System.out.println("setUserResponse: Setting user response and notifying waiting threads.");
        notifyAll(); // Resume the thread waiting in getMessage
    }


    // Send a message to the frontend
    public void sendMessage(String message, boolean send) {
        if (send) {
            outputLog.append(message).append("\n"); // Append the message to the log
            messagingTemplate.convertAndSend("/topic/console", outputLog.toString());
            clear();
        } else {
            outputLog.append(message).append("\n"); // Accumulate messages in the log
        }
    }

    public synchronized void terminateExtraThreads() {
        System.out.println("terminateExtraThreads: Terminating all extra threads.");
        terminateExtraThreads = true;
        while (waitingThreads.get() > 0) {
            notifyAll(); // Notify all waiting threads
            try {
                wait(50); // Give threads time to wake up and exit
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("terminateExtraThreads: Interrupted while waiting for threads to exit.");
                break;
            }
        }
    }


    // Resets the termination flag
    public void resetTerminationFlag() {
        terminateExtraThreads = false;
        System.out.println("resetTerminationFlag: Threads termination flag reset.");
    }

    // Clears the console log
    public void clear() {
        outputLog.setLength(0);
    }

    // Handles the 'Press Enter to continue' case
    public void clearScreen(boolean wait) {
        if (wait) {
            getMessage("Press Enter to continue");
        }
        clear();
        sendMessage("clear-console", true);
    }

    // Displays a player's hand
    public void displayHand(Player player) throws InterruptedException {
        if (player == null) return;

        if (player.id == -2) {
            sendMessage("Cards in attack: attack value: " + player.shields ,true);
        } else if (player.id == -1) {
            sendMessage("Stage: Value: " + player.shields, true);
        } else {
            player.sortHand();
            sendMessage("Hand:", false);
        }

        ArrayList<AdventureCard> hand = player.getHand();
        for (int i = 0; i < player.getHandSize(); i++) {
            sendMessage((i + 1) + ": " + hand.get(i).GetCardName(),false);
        }
        sendMessage("",true);
        Thread.sleep(150);
    }

    // Displays the current player's turn
    public void displayTurn(Player player) {
        clearScreen(false);
        sendMessage("Current Player: " + player.getName(),true);
    }

    public String getOutputLog() {
        return outputLog.toString();
    }
}


