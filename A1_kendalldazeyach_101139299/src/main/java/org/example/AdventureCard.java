package org.example;

import java.io.Serializable;

public class AdventureCard {
    String type;
    String name;
    int value;
    public AdventureCard(String n, String t, int v) {
        name = n;
        type = t;
        value = v;
    }
    public String GetCardType(){
        return type;
    }
    public int GetCardValue(){
        return value;
    }
    public String GetCardName(){
        return name;
    }
}
