package org.example;

public class EventCard {
    String type;
    String name;
    int value;
    public EventCard(String n, String t, int v) {
        type = t;
        name = n;
        value = v;
    }
    public String GetCardType(){
        return type;
    }
    public String GetCardName(){
        return name;
    }
    public int GetCardValue(){
        return value;
    }
}
