package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

    }

    public void GenerateDeck(){

    }

    public int GetEventDeckSize(){
        return 0;
    }

    public int GetAdventureDeckSize(){
        return 0;
    }

    public AdventureCard DrawAdventureCard(){
        return new AdventureCard();
    }

    public EventCard DrawEventCard(){
        return new EventCard();
    }

}