package org.example;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "http://127.0.0.1:8081")
public class DisplayController {

    private final SimpMessagingTemplate messagingTemplate;
    private GameService gameService;
    private Display display;


    @Autowired
    public DisplayController(SimpMessagingTemplate messagingTemplate, GameService gameService, Display display) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
        this.display = display;
        System.out.println("DisplayController initialized");
    }

    @PostMapping("/clear-console")
    public void clearConsole() {
        messagingTemplate.convertAndSend("/topic/console", "clear-console");
        System.out.println("Clear console message sent to clients.");
    }

    // Start the game
    @PostMapping("/start")
    public String startGame() {
        clearConsole();
        System.out.println("Game started");
        String result = gameService.startGame();
        return result; // HTTP 200
    }
    @PostMapping("/command")
    public void test(){
        System.out.println("soemthing happened");
    }
    // Advance to the next turn
    @PostMapping("/next-turn")
    public String nextTurn() {
        return gameService.nextTurn();
    }

    // Get the current game state
    @GetMapping("/state")
    public String getGameState() {
        return gameService.getGameState();
    }

    // Send a response to the game
    @PostMapping("/response")
    public String sendResponse(@RequestBody String response) {
        System.out.println("received");
        return gameService.handleResponse(response);
    }

    // Get the display messages (for frontend polling)
    @GetMapping("/messages")
    public String getMessages() {
        return display.getOutputLog();
    }
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/send-command")
    @SendTo("/topic/console")
    public String processCommand(String command) {
        // Process the command here
        if (command.equals("start")) {
            return startGame();
        }else{

        }
        System.out.println("Received command: " + command);
        return "Processed: " + command; // Return a response to the frontend
    }





}
