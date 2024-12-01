package org.example;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "http://127.0.0.1:8081")
public class DisplayController {

    private final SimpMessagingTemplate messagingTemplate;
    private GameService gameService;
    private Display display;


    @Autowired
    public DisplayController(SimpMessagingTemplate messagingTemplate, Display display) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = new GameService(display);
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
    public String startGame() throws InterruptedException {
        clearConsole();
        System.out.println("Game started");
        gameService.startGame();
        return ""; // HTTP 200
    }
    @PostMapping("/ENTER")
    public String next() throws InterruptedException {
        clearConsole();
        System.out.println("Next");
        Thread.sleep(200);
        gameService.cont();
        return ""; // HTTP 200
    }
    @MessageMapping("/send-response")
    public void receiveResponse(String response) throws InterruptedException {
        System.out.println("Received response: " + response);
        if (gameService.getGameState() == -2){
            return;
        }
        //check locked input,. do nothing if locked
        if (gameService.getinputlocked()){
            System.out.println("Locked");
            display.setUserResponse(response);
        }else{
            System.out.println("Unlocked");
            display.setUserResponse(response);
            Thread.sleep(200);
            next();
        }
    }

    @MessageMapping("/send-command")
    @SendTo("/topic/console")
    public String processCommand(String command) throws InterruptedException {
        // Process the command here
        if (command.equals("start")) {
            return startGame();
        }else if (command.equals("reset")) {
            gameService.reset();
            display.clear();
        }else{
            display.setUserResponse(command);
            System.out.println("continue");
        }
        System.out.println("Received command: " + command);
        return "Processed: " + command; // Return a response to the frontend
    }
}
