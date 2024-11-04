# Created by Kendall at 10/31/2024
Feature: 4004 card game
  Test all scenarios given in pdf

  Scenario: A1_scenario
    Given a new card game starts
    When the 4 starting hands are as posted
    And P1 draws a quest of 4 stages
    Then p1 is asked to sponsor
    And p2 chooses to sponsor
    And  p2 builds the 4 stages of the quest as posted
    And Other players decide to participate and draw a card