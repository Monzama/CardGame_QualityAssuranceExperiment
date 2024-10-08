package org.example;

import java.util.ArrayList;

public class Quest {

    ArrayList<Player> stages;
    int value;
    int stageCount;
    Player currentStage, previousStage = null;
    public Quest(int n) {
        value = n;
        stageCount = 0;
        stages = new ArrayList<>(0);
    }
    public void addStage(Player s){
        previousStage = s;
        stages.add(s);
        stageCount++;
    }



}
