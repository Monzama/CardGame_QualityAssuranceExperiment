package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Display {
    Scanner sc = new Scanner(System.in);
    public void clearScreen (){
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }


    public String getMessage(String message){
        String m = "";
        System.out.print(message+":");
        m = sc.nextLine();
        return m;
    }

    public void displayTurn(Player p){
        clearScreen();
        System.out.println("Current Player: " + p.getName());
    }
    public void displayHand(Player p){
        System.out.println("Hand:");
        ArrayList<AdventureCard> hand = p.getHand();
        for (int i = 0; i < p.getHandSize(); i++) {
            System.out.println(hand.get(i).GetCardName());
        }
    }
}
