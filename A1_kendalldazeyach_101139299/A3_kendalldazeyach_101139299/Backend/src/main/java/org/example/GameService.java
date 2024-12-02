package org.example;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;

@Service
public class GameService {

    private Display display;
    Main game;
    @Autowired
    public GameService(Display display) {
        this.display = display;
        this.game = new Main(false, display);
        System.out.println("GameService initialized");
    }
    // Start the game
    public String startGame(boolean test) throws InterruptedException {
        game.begin(game,test);
        System.out.println(display.getOutputLog());
        String t = display.getOutputLog();
        //display.clear();
        return t;
    }

    public boolean getinputlocked() {
        return game.lockedInput;
    }
    public int getGameState() {
        return game.gameState;
    }

    public void reset() throws InterruptedException {
        System.out.println("Reset started");
        display.terminateExtraThreads(); // Set termination flag and wake up threads
        Thread.sleep(100); // Allow threads time to exit
        synchronized (display) {
            while (display.waitingThreads.get() > 0) {
                display.notifyAll(); // Ensure any remaining threads are notified
                Thread.sleep(50); // Small delay to allow threads to exit
            }
        }
        display.clearinput();
        game.reset();
        System.out.println("GameService initialized");
        display.resetTerminationFlag();
        Thread.sleep(100);
        this.display.sendMessage("Type start to start game", true);// Reset termination flag after all threads are confirmed exited
    }



    public void cont() throws InterruptedException {
        if (game.gameState == -3){
            game.reset();
            game.gameState = -2;
            return;
        }
        //do stuff based on game state
        switch(game.gameState){
            case -1://next turn
                System.out.println("Next Turn");
                game.nextTurn();
                break;
            case 0:
                System.out.println("Next Event");
                game.nextEvent();
                break;
            case 1:
                System.out.println("Setup Stage: " + game.sponsor + " | Quest of " + game.current_event.value + " stages");
                game.initializeStage(game.sponsor, game.current_event.value);
                break;
            case 2:
                System.out.println("Decide Players");
                if (game.eligble == null){
                    game.decidePlayers(game.current_q, null);
                }else{
                    game.decidePlayers(game.current_q, game.eligble);
                }
                break;
            case 3:
                System.out.println("Setup Attacks");
                if (game.setupPlayer == null){
                    game.setupPlayer = game.eligble.get(0);
                }else if (game.eligble.get(game.eligble.size()-1) == game.currentPlayer){
                    //should not be here
                    break;
                }else{
                    for (int i = 0; i < game.eligble.size(); i++) {
                        //find next
                        if (game.eligble.get(i) == game.setupPlayer){
                            game.setupPlayer = game.eligble.get(i+1);
                            break;
                        }
                    }
                }
                System.out.println("Setup: " + game.setupPlayer.name);
                game.setupAttack(game.setupPlayer, game.current_q.currentStage);
                if (game.setupPlayer != game.eligble.get(game.eligble.size()-1)){
                    display.getMessage("Continue to next player's setup?");
                }
                break;
            case 4:
                System.out.println("Attack results");
                game.eligble = game.attackResult(game.playAttack(game.current_q.currentStage),game.eligble);
                System.out.println("Next Stage");
                if (game.current_q == null){
                    return;
                }
                if (game.current_q.currentStage.id == game.current_q.stages.size()-1 || game.eligble == null){
                    //go to end quest
                    game.gameState = 5;
                    display.getMessage("End Quest?");
                }else {
                    game.current_q.nextStage();
                    game.current_q.attacks = new ArrayList<>();
                    game.setupPlayer = null;
                    game.gameState = 2;
                    display.getMessage("Next Stage?");
                }
                break;
            case 5:
                System.out.println("End of Quest");
                game.endQuest(game.current_q, game.sponsor, game.eligble);
                break;
        }
    }
}
