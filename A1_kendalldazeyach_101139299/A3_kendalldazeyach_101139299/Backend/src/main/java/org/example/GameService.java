package org.example;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class GameService {

    private final Display display;
    private final Main game;



    @Autowired
    public GameService(Display display) {
        this.display = display;
        this.game = new Main(false, display);
        System.out.println("GameService initialized");
    }

    // Start the game
    public String startGame() {
        game.begin(game);
        System.out.println(display.getOutputLog());
        String t = display.getOutputLog();
        display.clear();
        return t;
    }

    // Advance the game loop
    public String nextTurn() {
        if (!game.game_on) {
            return "Game is not running!";
        }
        game.nextTurn();
        return "Turn advanced!";
    }

    // Get current game state
    public String getGameState() {
        return "Current Player: " + game.currentPlayer.name;
    }

    // Handle player response
    public String handleResponse(String response) {
        display.setUserResponse(response);
        return "Response received: " + response;
    }
}
