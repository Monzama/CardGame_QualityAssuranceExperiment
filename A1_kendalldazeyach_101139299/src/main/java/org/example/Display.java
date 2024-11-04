package org.example;

import java.io.InputStream;
import java.io.PipedInputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Scanner;

public class Display {
    Main m;
    Scanner sc;
    public void clear(){
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    public Display(){
        this.m = m;
        sc = new Scanner(System.in);
    }
    public Display(InputStream pipedIn){
        this.m = m;
        sc = new Scanner(pipedIn);
    }

    public void clearScreen(boolean Wait){
        if (!Wait){
            clear();
        }else{
            getMessage("Press Enter to continue");
            clear();
        }
    }
    public String getMessage(String message){
        String m = null;
        System.out.print(message);
        if (sc.hasNextLine()){
            m = sc.nextLine();
        }
        return m;
    }

    public void displayTurn(Player p){
        clearScreen(false);
        System.out.println("Current Player: " + p.getName());
    }
    public void displayHand(Player p){
        if (p == null){return;}
        if (p.id ==-2){
            System.out.println("Cards in attack: attack value: " + p.shields);
        }else if (p.id ==-1){
            System.out.println("Stage: Value: " + p.shields);
        }else{
            p.sortHand();
            System.out.println("Hand:");
        }
        ArrayList<AdventureCard> hand = p.getHand();
        for (int i = 0; i < p.getHandSize(); i++) {
            System.out.println((i+1) + ": " + hand.get(i).GetCardName());
        }
    }
}
