package org.example;

import java.io.Serializable;

public class AdventureCard implements Comparable<AdventureCard> {
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

    public int compareTo(AdventureCard other) {
        if (this.type.equals("F") && !other.type.equals("F")) {
            return -1;
        }
        if (!this.type.equals("F") && other.type.equals("F")) {
            return 1;
        }

        if (this.type.equals("F") && other.type.equals("F")) {
            return Integer.compare(this.value, other.value);
        }

        int valueComparison = Integer.compare(this.value, other.value);
        if (valueComparison != 0) {
            return valueComparison;
        }
        if (this.value == 10 && other.value == 10) {
            if (this.type.equals("S") && other.type.equals("H")) {
                return -1;
            }
            if (this.type.equals("H") && other.type.equals("S")) {
                return 1;
            }
        }
        return this.type.compareTo(other.type);
    }

}
