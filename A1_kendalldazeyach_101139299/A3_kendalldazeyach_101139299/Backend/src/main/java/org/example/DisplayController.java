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
    private boolean test_on;

    @Autowired
    public DisplayController(SimpMessagingTemplate messagingTemplate, Display display) {
        this.test_on = false;
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
        gameService.startGame(test_on);
        return ""; // HTTP 200
    }
    @PostMapping("/ENTER")
    public String next() throws InterruptedException {
        clearConsole();
        System.out.println("Next");
        Thread.sleep(5);
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
            Thread.sleep(5);
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
            test_on = false;
            gameService.reset();
            display.clear();
            return "";
        }else if (command.equals("display hands")) {
            gameService.game.displayallhands();
        }else{
            display.setUserResponse(command);
            System.out.println("continue");
        }
        System.out.println("Received command: " + command);
        return "Processed: " + command; // Return a response to the frontend
    }

    @MessageMapping("/test-setup")
    @SendTo("/topic/console")
    public String processTestSetup(String command) throws InterruptedException {
        test_on = true;
        System.out.println(command);
        //A1 scenario
        if (command.equals("setup_scenario1")){
            gameService.reset();
            gameService.game.distributeHands(12);
            //fix p1 hand
            Player p1 = new Player("p1",0, gameService.game.display);
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
            gameService.game.players.set(0,p1);
            gameService.game.currentPlayer =p1;
            //fix p2 hand
            Player p2 = new Player("p2",1, gameService.game.display);
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
            gameService.game.players.set(1,p2);
            //fix p3 hand
            Player p3 = new Player("p3",2, gameService.game.display);
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
            gameService.game.players.set(2,p3);
            //fix p4 hand
            Player p4 = new Player("p4",3, gameService.game.display);
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
            gameService.game.players.set(3,p4);
            gameService.game.game_on = true;
            //rig the deck
            gameService.game.main_deck.event_cards.set(0,new EventCard("Q4", "Q", 4));
            gameService.game.main_deck.adventure_cards.set(0,new AdventureCard("F30", "F", 30));
            gameService.game.main_deck.adventure_cards.set(1,new AdventureCard("S10", "S", 10));
            gameService.game.main_deck.adventure_cards.set(2,new AdventureCard("B15", "B", 15));
            gameService.game.main_deck.adventure_cards.set(3,new AdventureCard("F10", "F", 10));
            gameService.game.main_deck.adventure_cards.set(4,new AdventureCard("L20", "L", 20));
            gameService.game.main_deck.adventure_cards.set(5,new AdventureCard("L20", "L", 20));
            gameService.game.main_deck.adventure_cards.set(6,new AdventureCard("B15", "B", 15));
            gameService.game.main_deck.adventure_cards.set(7,new AdventureCard("S10", "S", 10));
            gameService.game.main_deck.adventure_cards.set(8,new AdventureCard("F30", "F", 30));
            gameService.game.main_deck.adventure_cards.set(9,new AdventureCard("L20", "L", 20));
        }else if (command.equals("setup_scenario2")){//2winner 2 quest scenario
            gameService.reset();
            gameService.game.distributeHands(1);
            //fix p1 hand
            Player p1 = new Player("p1",0, gameService.game.display);
            p1.addCardToHand(new AdventureCard("F5","F",5));
            p1.addCardToHand(new AdventureCard("F5","F",5));
            p1.addCardToHand(new AdventureCard("F10","F",10));
            p1.addCardToHand(new AdventureCard("F10","F",10));
            p1.addCardToHand(new AdventureCard("F15","F",15));
            p1.addCardToHand(new AdventureCard("F15","F",15));
            p1.addCardToHand(new AdventureCard("D5","D",5));
            p1.addCardToHand(new AdventureCard("H10","H",10));
            p1.addCardToHand(new AdventureCard("H10","H",10));
            p1.addCardToHand(new AdventureCard("B15","B",15));
            p1.addCardToHand(new AdventureCard("B15","B",15));
            p1.addCardToHand(new AdventureCard("L20","L",20),true);
            p1.sortHand();
            gameService.game.players.set(0,p1);
            gameService.game.currentPlayer =p1;
            //fix p2 hand
            Player p2 = new Player("p2",1, gameService.game.display);
            p2.addCardToHand(new AdventureCard("F40","F",40));
            p2.addCardToHand(new AdventureCard("F50","F",50));
            p2.addCardToHand(new AdventureCard("H10","H",10));
            p2.addCardToHand(new AdventureCard("H10","H",10));
            p2.addCardToHand(new AdventureCard("S10","S",10));
            p2.addCardToHand(new AdventureCard("S10","S",10));
            p2.addCardToHand(new AdventureCard("S10","S",10));
            p2.addCardToHand(new AdventureCard("B15","B",15));
            p2.addCardToHand(new AdventureCard("B15","B",15));
            p2.addCardToHand(new AdventureCard("L20","L",20));
            p2.addCardToHand(new AdventureCard("L20","L",20));
            p2.addCardToHand(new AdventureCard("E30","E",30),true);
            p2.sortHand();
            gameService.game.players.set(1,p2);
            //fix p3 hand
            Player p3 = new Player("p3",2, gameService.game.display);
            p3.addCardToHand(new AdventureCard("F5","F",5));
            p3.addCardToHand(new AdventureCard("F5","F",5));
            p3.addCardToHand(new AdventureCard("F5","F",5));
            p3.addCardToHand(new AdventureCard("F5","F",5));
            p3.addCardToHand(new AdventureCard("D5","D",5));
            p3.addCardToHand(new AdventureCard("D5","D",5));
            p3.addCardToHand(new AdventureCard("D5","D",5));
            p3.addCardToHand(new AdventureCard("H10","H",10));
            p3.addCardToHand(new AdventureCard("H10","H",10));
            p3.addCardToHand(new AdventureCard("H10","H",10));
            p3.addCardToHand(new AdventureCard("H10","H",10));
            p3.addCardToHand(new AdventureCard("H10","H",10),true);
            p3.sortHand();
            gameService.game.players.set(2,p3);
            //fix p4 hand
            Player p4 = new Player("p4",3, gameService.game.display);
            p4.addCardToHand(new AdventureCard("F50","F",50));
            p4.addCardToHand(new AdventureCard("F70","F",70));
            p4.addCardToHand(new AdventureCard("H10","H",10));
            p4.addCardToHand(new AdventureCard("H10","H",10));
            p4.addCardToHand(new AdventureCard("S10","S",10));
            p4.addCardToHand(new AdventureCard("S10","S",10));
            p4.addCardToHand(new AdventureCard("S10","S",10));
            p4.addCardToHand(new AdventureCard("B15","B",15));
            p4.addCardToHand(new AdventureCard("B15","B",15));
            p4.addCardToHand(new AdventureCard("L20","L",20));
            p4.addCardToHand(new AdventureCard("L20","L",20));
            p4.addCardToHand(new AdventureCard("E30","E",30),true);
            p4.sortHand();
            gameService.game.players.set(3,p4);
            //rig the deck
            gameService.game.main_deck.event_cards.set(0,new EventCard("Q4", "Q", 4));
            gameService.game.main_deck.adventure_cards.set(0,new AdventureCard("F5", "F", 5));
            gameService.game.main_deck.adventure_cards.set(1,new AdventureCard("F40", "F", 40));
            gameService.game.main_deck.adventure_cards.set(2,new AdventureCard("F10", "F", 10));
            gameService.game.main_deck.adventure_cards.set(3,new AdventureCard("F10", "F", 10));
            gameService.game.main_deck.adventure_cards.set(4,new AdventureCard("F30", "F", 30));
            gameService.game.main_deck.adventure_cards.set(5,new AdventureCard("F30", "F", 30));
            gameService.game.main_deck.adventure_cards.set(6,new AdventureCard("F15", "F", 15));
            gameService.game.main_deck.adventure_cards.set(7,new AdventureCard("F15", "F", 15));
            gameService.game.main_deck.adventure_cards.set(8,new AdventureCard("F20", "F", 20));

            gameService.game.main_deck.adventure_cards.set(9,new AdventureCard("F5", "F", 5));
            gameService.game.main_deck.adventure_cards.set(10,new AdventureCard("F10", "F", 10));
            gameService.game.main_deck.adventure_cards.set(11,new AdventureCard("F15", "F", 15));
            gameService.game.main_deck.adventure_cards.set(12,new AdventureCard("F15", "F", 15));
            gameService.game.main_deck.adventure_cards.set(13,new AdventureCard("F20", "F", 20));
            gameService.game.main_deck.adventure_cards.set(14,new AdventureCard("F20", "F", 20));
            gameService.game.main_deck.adventure_cards.set(15,new AdventureCard("F20", "F", 20));
            gameService.game.main_deck.adventure_cards.set(16,new AdventureCard("F20", "F", 20));
            gameService.game.main_deck.adventure_cards.set(17,new AdventureCard("F25", "F", 25));
            gameService.game.main_deck.adventure_cards.set(18,new AdventureCard("F25", "F", 25));
            gameService.game.main_deck.adventure_cards.set(19,new AdventureCard("F30", "F", 30));

            gameService.game.main_deck.event_cards.set(1,new EventCard("Q3", "Q", 3));
            gameService.game.main_deck.adventure_cards.set(20,new AdventureCard("D5", "D", 5));
            gameService.game.main_deck.adventure_cards.set(21,new AdventureCard("D5", "D", 5));

            gameService.game.main_deck.adventure_cards.set(22,new AdventureCard("F15", "F", 15));
            gameService.game.main_deck.adventure_cards.set(23,new AdventureCard("F15", "F", 15));

            gameService.game.main_deck.adventure_cards.set(24,new AdventureCard("F25", "F", 25));
            gameService.game.main_deck.adventure_cards.set(25,new AdventureCard("F25", "F", 25));

            gameService.game.main_deck.adventure_cards.set(26,new AdventureCard("F20", "F", 20));
            gameService.game.main_deck.adventure_cards.set(27,new AdventureCard("F20", "F", 20));
            gameService.game.main_deck.adventure_cards.set(28,new AdventureCard("F25", "F", 25));
            gameService.game.main_deck.adventure_cards.set(29,new AdventureCard("F30", "F", 30));
            gameService.game.main_deck.adventure_cards.set(30,new AdventureCard("S10", "S", 10));
            gameService.game.main_deck.adventure_cards.set(31,new AdventureCard("B15", "B", 15));
            gameService.game.main_deck.adventure_cards.set(32,new AdventureCard("B15", "B", 15));
            gameService.game.main_deck.adventure_cards.set(33,new AdventureCard("L20", "L", 20));

        }else if (command.equals("setup_scenario3")){
            //do stuff
            gameService.reset();
            gameService.game.distributeHands(1);
            //fix p1 hand
            Player p1 = new Player("p1",0, gameService.game.display);
            p1.addCardToHand(new AdventureCard("F5","F",5));
            p1.addCardToHand(new AdventureCard("F5","F",5));
            p1.addCardToHand(new AdventureCard("F10","F",10));
            p1.addCardToHand(new AdventureCard("F10","F",10));
            p1.addCardToHand(new AdventureCard("F15","F",15));
            p1.addCardToHand(new AdventureCard("F15","F",15));
            p1.addCardToHand(new AdventureCard("F20","F",20));
            p1.addCardToHand(new AdventureCard("F20","F",20));
            p1.addCardToHand(new AdventureCard("D5","D",5));
            p1.addCardToHand(new AdventureCard("D5","D",5));
            p1.addCardToHand(new AdventureCard("D5","D",5));
            p1.addCardToHand(new AdventureCard("D5","D",5),true);
            p1.sortHand();
            gameService.game.players.set(0,p1);
            gameService.game.currentPlayer =p1;
            //fix p2 hand
            Player p2 = new Player("p2",1, gameService.game.display);
            p2.addCardToHand(new AdventureCard("F25","F",25));
            p2.addCardToHand(new AdventureCard("F30","F",30));
            p2.addCardToHand(new AdventureCard("H10","H",10));
            p2.addCardToHand(new AdventureCard("H10","H",10));
            p2.addCardToHand(new AdventureCard("S10","S",10));
            p2.addCardToHand(new AdventureCard("S10","S",10));
            p2.addCardToHand(new AdventureCard("S10","S",10));
            p2.addCardToHand(new AdventureCard("B15","B",15));
            p2.addCardToHand(new AdventureCard("B15","B",15));
            p2.addCardToHand(new AdventureCard("L20","L",20));
            p2.addCardToHand(new AdventureCard("L20","L",20));
            p2.addCardToHand(new AdventureCard("E30","E",30),true);
            p2.sortHand();
            gameService.game.players.set(1,p2);
            //fix p3 hand
            Player p3 = new Player("p3",2, gameService.game.display);
            p3.addCardToHand(new AdventureCard("F25","F",25));
            p3.addCardToHand(new AdventureCard("F30","F",30));
            p3.addCardToHand(new AdventureCard("H10","H",10));
            p3.addCardToHand(new AdventureCard("H10","H",10));
            p3.addCardToHand(new AdventureCard("S10","S",10));
            p3.addCardToHand(new AdventureCard("S10","S",10));
            p3.addCardToHand(new AdventureCard("S10","S",10));
            p3.addCardToHand(new AdventureCard("B15","B",15));
            p3.addCardToHand(new AdventureCard("B15","B",15));
            p3.addCardToHand(new AdventureCard("L20","L",20));
            p3.addCardToHand(new AdventureCard("L20","L",20));
            p3.addCardToHand(new AdventureCard("E30","E",30),true);
            p3.sortHand();
            gameService.game.players.set(2,p3);
            //fix p4 hand
            Player p4 = new Player("p4",3, gameService.game.display);
            p4.addCardToHand(new AdventureCard("F25","F",25));
            p4.addCardToHand(new AdventureCard("F30","F",30));
            p4.addCardToHand(new AdventureCard("F70","F",70));
            p4.addCardToHand(new AdventureCard("H10","H",10));
            p4.addCardToHand(new AdventureCard("H10","H",10));
            p4.addCardToHand(new AdventureCard("S10","S",10));
            p4.addCardToHand(new AdventureCard("S10","S",10));
            p4.addCardToHand(new AdventureCard("S10","S",10));
            p4.addCardToHand(new AdventureCard("B15","B",15));
            p4.addCardToHand(new AdventureCard("B15","B",15));
            p4.addCardToHand(new AdventureCard("L20","L",20));
            p4.addCardToHand(new AdventureCard("L20","L",20), true);
            p4.sortHand();
            gameService.game.players.set(3,p4);
            //rig the deck
            gameService.game.main_deck.event_cards.set(0,new EventCard("Q4", "Q", 4));
            gameService.game.main_deck.adventure_cards.set(0,new AdventureCard("F5", "F", 5));
            gameService.game.main_deck.adventure_cards.set(1,new AdventureCard("F10", "F", 10));
            gameService.game.main_deck.adventure_cards.set(2,new AdventureCard("F20", "F", 20));
            gameService.game.main_deck.adventure_cards.set(3,new AdventureCard("F15", "F", 15));
            gameService.game.main_deck.adventure_cards.set(4,new AdventureCard("F5", "F", 5));
            gameService.game.main_deck.adventure_cards.set(5,new AdventureCard("F25", "F", 25));
            gameService.game.main_deck.adventure_cards.set(6,new AdventureCard("F5", "F", 5));
            gameService.game.main_deck.adventure_cards.set(7,new AdventureCard("F10", "F", 10));
            gameService.game.main_deck.adventure_cards.set(8,new AdventureCard("F20", "F", 20));
            gameService.game.main_deck.adventure_cards.set(9,new AdventureCard("F5", "F", 5));
            gameService.game.main_deck.adventure_cards.set(10,new AdventureCard("F10", "F", 10));
            gameService.game.main_deck.adventure_cards.set(11,new AdventureCard("F20", "F", 20));

            gameService.game.main_deck.adventure_cards.set(12,new AdventureCard("F5", "F", 5));
            gameService.game.main_deck.adventure_cards.set(13,new AdventureCard("F5", "F", 5));
            gameService.game.main_deck.adventure_cards.set(14,new AdventureCard("F10", "F", 10));
            gameService.game.main_deck.adventure_cards.set(15,new AdventureCard("F10", "F", 10));
            gameService.game.main_deck.adventure_cards.set(16,new AdventureCard("F15", "F", 15));
            gameService.game.main_deck.adventure_cards.set(17,new AdventureCard("F15", "F", 15));
            gameService.game.main_deck.adventure_cards.set(18,new AdventureCard("F15", "F", 15));
            gameService.game.main_deck.adventure_cards.set(19,new AdventureCard("F15", "F", 15));

            //earn shields
            //plague
            gameService.game.main_deck.event_cards.set(1,new EventCard("Plague","E",0)); //p2 loses shields


            gameService.game.main_deck.event_cards.set(2,new EventCard("Prosperity","E",0)); //p3 draws prosp
            gameService.game.main_deck.adventure_cards.set(20,new AdventureCard("F25", "F", 25));
            gameService.game.main_deck.adventure_cards.set(21,new AdventureCard("F25", "F", 25));
            gameService.game.main_deck.adventure_cards.set(22,new AdventureCard("H10", "H", 10));
            gameService.game.main_deck.adventure_cards.set(23,new AdventureCard("S10", "S", 10));
            gameService.game.main_deck.adventure_cards.set(24,new AdventureCard("B15", "B", 15));
            gameService.game.main_deck.adventure_cards.set(25,new AdventureCard("F40", "F", 40));
            gameService.game.main_deck.adventure_cards.set(26,new AdventureCard("D5", "D", 5));
            gameService.game.main_deck.adventure_cards.set(27,new AdventureCard("D5", "D", 5));



            gameService.game.main_deck.event_cards.set(3,new EventCard("Queen's Favor","E",0)); // p4 gets queens favor
            gameService.game.main_deck.adventure_cards.set(28,new AdventureCard("F30", "F", 30));
            gameService.game.main_deck.adventure_cards.set(29,new AdventureCard("F25", "F", 25));

            gameService.game.main_deck.event_cards.set(4,new EventCard("Q3", "Q", 3)); //p1 draws this q3

            gameService.game.main_deck.adventure_cards.set(30,new AdventureCard("B15", "B", 15));
            gameService.game.main_deck.adventure_cards.set(31,new AdventureCard("H10", "H", 10));
            gameService.game.main_deck.adventure_cards.set(32,new AdventureCard("F50", "F", 50));

            gameService.game.main_deck.adventure_cards.set(33,new AdventureCard("S10", "S", 10));
            gameService.game.main_deck.adventure_cards.set(34,new AdventureCard("S10", "S", 10));

            gameService.game.main_deck.adventure_cards.set(35,new AdventureCard("F40", "F", 40));
            gameService.game.main_deck.adventure_cards.set(36,new AdventureCard("F50", "F", 50));

            gameService.game.main_deck.adventure_cards.set(37,new AdventureCard("H10", "H", 10));
            gameService.game.main_deck.adventure_cards.set(38,new AdventureCard("H10", "H", 10));
            gameService.game.main_deck.adventure_cards.set(39,new AdventureCard("H10", "H", 10));
            gameService.game.main_deck.adventure_cards.set(40,new AdventureCard("S10", "S", 10));
            gameService.game.main_deck.adventure_cards.set(41,new AdventureCard("S10", "S", 10));
            gameService.game.main_deck.adventure_cards.set(42,new AdventureCard("S10", "S", 10));
            gameService.game.main_deck.adventure_cards.set(43,new AdventureCard("S10", "S", 10));
            gameService.game.main_deck.adventure_cards.set(44,new AdventureCard("F35", "F", 35));

        }else if (command.equals("setup_scenario4")){
            //do stuff
            //fix p1 hand
            gameService.reset();
            gameService.game.distributeHands(1);
            Player p1 = new Player("p1",0, gameService.game.display);
            p1.addCardToHand(new AdventureCard("F50","F",50));
            p1.addCardToHand(new AdventureCard("F70","F",70));
            p1.addCardToHand(new AdventureCard("D5","D",5));
            p1.addCardToHand(new AdventureCard("D5","D",5));
            p1.addCardToHand(new AdventureCard("S10","S",10));
            p1.addCardToHand(new AdventureCard("S10","S",10));
            p1.addCardToHand(new AdventureCard("H10","H",10));
            p1.addCardToHand(new AdventureCard("H10","H",10));
            p1.addCardToHand(new AdventureCard("B15","B",15));
            p1.addCardToHand(new AdventureCard("B15","B",15));
            p1.addCardToHand(new AdventureCard("L20","L",20));
            p1.addCardToHand(new AdventureCard("L20","L",20),true);
            p1.sortHand();
            gameService.game.players.set(0,p1);
            gameService.game.currentPlayer =p1;
            //fix p2 hand
            Player p2 = new Player("p2",1, gameService.game.display);
            p2.addCardToHand(new AdventureCard("F5","F",5));
            p2.addCardToHand(new AdventureCard("F5","F",5));
            p2.addCardToHand(new AdventureCard("F10","F",10));
            p2.addCardToHand(new AdventureCard("F15","F",15));
            p2.addCardToHand(new AdventureCard("F15","F",15));
            p2.addCardToHand(new AdventureCard("F20","F",20));
            p2.addCardToHand(new AdventureCard("F20","F",20));
            p2.addCardToHand(new AdventureCard("F25","F",25));
            p2.addCardToHand(new AdventureCard("F30","F",30));
            p2.addCardToHand(new AdventureCard("F30","F",30));
            p2.addCardToHand(new AdventureCard("F40","F",40));
            p2.addCardToHand(new AdventureCard("E30","E",30),true);
            p2.sortHand();
            gameService.game.players.set(1,p2);
            //fix p3 hand
            Player p3 = new Player("p3",2, gameService.game.display);
            p3.addCardToHand(new AdventureCard("F5","F",5));
            p3.addCardToHand(new AdventureCard("F5","F",5));
            p3.addCardToHand(new AdventureCard("F10","F",10));
            p3.addCardToHand(new AdventureCard("F15","F",15));
            p3.addCardToHand(new AdventureCard("F15","F",15));
            p3.addCardToHand(new AdventureCard("F20","F",20));
            p3.addCardToHand(new AdventureCard("F20","F",20));
            p3.addCardToHand(new AdventureCard("F25","F",25));
            p3.addCardToHand(new AdventureCard("F25","F",25));
            p3.addCardToHand(new AdventureCard("F30","F",30));
            p3.addCardToHand(new AdventureCard("F40","F",40));
            p3.addCardToHand(new AdventureCard("L20","L",20),true);
            p3.sortHand();
            gameService.game.players.set(2,p3);
            //fix p4 hand
            Player p4 = new Player("p4",3, gameService.game.display);
            p4.addCardToHand(new AdventureCard("F5","F",5));
            p4.addCardToHand(new AdventureCard("F5","F",5));
            p4.addCardToHand(new AdventureCard("F10","F",10));
            p4.addCardToHand(new AdventureCard("F15","F",15));
            p4.addCardToHand(new AdventureCard("F15","F",15));
            p4.addCardToHand(new AdventureCard("F20","F",20));
            p4.addCardToHand(new AdventureCard("F20","F",20));
            p4.addCardToHand(new AdventureCard("F25","F",25));
            p4.addCardToHand(new AdventureCard("F25","F",25));
            p4.addCardToHand(new AdventureCard("F30","F",30));
            p4.addCardToHand(new AdventureCard("F50","F",50));
            p4.addCardToHand(new AdventureCard("E30","E",30),true);
            p4.sortHand();
            gameService.game.players.set(3,p4);
            //rig the deck
            gameService.game.main_deck.event_cards.set(0,new EventCard("Q2", "Q", 2));


            gameService.game.main_deck.adventure_cards.set(0,new AdventureCard("F5", "F", 5));
            gameService.game.main_deck.adventure_cards.set(1,new AdventureCard("F15", "F", 15));
            gameService.game.main_deck.adventure_cards.set(2,new AdventureCard("F10", "F", 10));

            gameService.game.main_deck.adventure_cards.set(3,new AdventureCard("F5", "F", 5));
            gameService.game.main_deck.adventure_cards.set(4,new AdventureCard("F10", "F", 10));
            gameService.game.main_deck.adventure_cards.set(5,new AdventureCard("F15", "F", 15));
            gameService.game.main_deck.adventure_cards.set(6,new AdventureCard("D5", "D", 5));
            gameService.game.main_deck.adventure_cards.set(7,new AdventureCard("D5", "D", 5));
            gameService.game.main_deck.adventure_cards.set(8,new AdventureCard("D5", "D", 5));
            gameService.game.main_deck.adventure_cards.set(9,new AdventureCard("D5", "D", 5));
            gameService.game.main_deck.adventure_cards.set(10,new AdventureCard("H10", "H", 10));
            gameService.game.main_deck.adventure_cards.set(11,new AdventureCard("H10", "H", 10));
            gameService.game.main_deck.adventure_cards.set(12,new AdventureCard("H10", "H", 10));
            gameService.game.main_deck.adventure_cards.set(13,new AdventureCard("H10", "H", 10));
            gameService.game.main_deck.adventure_cards.set(14,new AdventureCard("S10", "S", 10));
            gameService.game.main_deck.adventure_cards.set(15,new AdventureCard("S10", "S", 10));
            gameService.game.main_deck.adventure_cards.set(16,new AdventureCard("S10", "S", 10));
        }
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
        return "Test Setup"; // Return a response to the frontend
    }
}
