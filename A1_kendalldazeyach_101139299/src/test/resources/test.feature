# Created by Kendall at 10/31/2024
Feature: 4004 card game
  Test all scenarios given in pdf

  Scenario: A1_scenario
    Given a new card game starts
    When the 4 starting hands are as posted
    And P1 draws a quest of 4 stages
    Then p2 chooses to sponsor
    And  p2 builds the stages of the quest as posted
      |F5|
      |H10|
      |Quit|
      |F15|
      |S10|
      |Quit|
      |F15|
      |D5|
      |B15|
      |Quit|
      |F40|
      |B15|
      |Quit|
    #stage 1
    And Other players decide to participate and draw a card
      |P1:F30:1|
      |P3:S10:1|
      |P4:B15:1|
    And p1 sets up their attack for stage 1
      |D5|
      |S10|
      |Quit|
    And p3 sets up their attack for stage 1
      |S10|
      |D5|
      |Quit|
    And p4 sets up their attack for stage 1
      |D5|
      |H10|
      |Quit|
    And Players are told if their attack for stage 1 is sufficient
      |p1:Wins|
      |p3:Wins|
      |p4:Wins|
    #stage 2
    And Other players decide to participate and draw a card
      |P1:F10|
      |P3:L20|
      |P4:L20|
    And p1 sets up their attack for stage 2
      |H10|
      |S10|
      |Quit|
    And p3 sets up their attack for stage 2
      |B15|
      |S10|
      |Quit|
    And p4 sets up their attack for stage 2
      |H10|
      |B15|
      |Quit|
    And Players are told if their attack for stage 2 is sufficient
      |p1:Loses|
      |p3:Wins|
      |p4:Wins|
    And p1 has 0 shields and hand is below
      |F5|
      |F10|
      |F15|
      |F15|
      |F30|
      |H10|
      |B15|
      |B15|
      |L20|
        #stage 3
    And Other players decide to participate and draw a card
      |P3:B15|
      |P4:S10|
    And p3 sets up their attack for stage 3
      |L20|
      |H10|
      |S10|
      |Quit|
    And p4 sets up their attack for stage 3
      |B15|
      |S10|
      |L20|
      |Quit|
    And Players are told if their attack for stage 3 is sufficient
      |p3:Wins|
      |p4:Wins|
              #stage 4
    And Other players decide to participate and draw a card
      |P3:F30|
      |P4:L20|
    And p3 sets up their attack for stage 4
      |B15|
      |H10|
      |L20|
      |Quit|
    And p4 sets up their attack for stage 4
      |D5|
      |S10|
      |L20|
      |E30|
      |Quit|
    And Players are told if their attack for stage 4 is sufficient
      |p3:Loses|
      |p4:Wins|
    And The quest ends
    And p3 has 0 shields and hand is below
      |F5|
      |F5|
      |F15|
      |F30|
      |S10|
    And p4 has 4 shields and hand is below
      |F15|
      |F15|
      |F40|
      |L20|
    And  p2 has 12 cards

  Scenario: 2winner_game_2winner_quest
    Given a new card game starts
    When the 4 starting hands are as posted for scenario 2
    And P1 draws a quest of 4 stages
    Then p1 chooses to sponsor
    And  p1 builds the stages of the quest as posted
      |F15|
      |Quit|
      |F15|
      |S10|
      |Quit|
      |F15|
      |S10|
      |D5|
      |Quit|
      |F40|
      |Quit|
    #stage 1
    And Other players decide to participate and draw a card
      |P2:S10:1|
      |P3:S10:1|
      |P4:B15:1|
    And p1 sets up their attack for stage 1
      |D5|
      |S10|
      |Quit|
    And p3 sets up their attack for stage 1
      |D5|
      |Quit|
    And p4 sets up their attack for stage 1
      |D5|
      |H10|
      |Quit|
    And Players are told if their attack for stage 1 is sufficient
      |p1:Wins|
      |p3:Loses|
      |p4:Wins|

    #assert p3 hand here


    #stage 2
    And Other players decide to participate and draw a card
      |P2:L20|
      |P4:L20|
    And p2 sets up their attack for stage 2
      |L20|
      |S10|
      |Quit|
    And p4 sets up their attack for stage 2
      |H10|
      |B15|
      |Quit|
    And Players are told if their attack for stage 2 is sufficient
      |p2:Wins|
      |p4:Wins|
        #stage 3
    And Other players decide to participate and draw a card
      |P2:E30|
      |P4:E30|
    And p2 sets up their attack for stage 3
      |E30|
      |Quit|
    And p4 sets up their attack for stage 3
      |E30|
      |Quit|
    And Players are told if their attack for stage 3 is sufficient
      |p2:Wins|
      |p4:Wins|
              #stage 4
    And Other players decide to participate and draw a card
      |P2:L20|
      |P4:L20|
    And p2 sets up their attack for stage 4
      |L20|
      |E30|
      |Quit|
    And p4 sets up their attack for stage 4
      |L20|
      |E30|
      |Quit|
    And Players are told if their attack for stage 4 is sufficient
      |p2:Wins|
      |p4:Wins|
    And The quest ends
    And p2 has 4 shields and hand is below
      |F15|
      |F15|
      |F40|
      |D5|
      |S10|
      |H10|
      |H10|
      |B15|
      |B15|
    And p4 has 4 shields and hand is below
      |F15|
      |F15|
      |F40|
      |D5|
      |S10|
      |B15|
      |L20|
      |L20|
    And  p1 has 12 cards
    And next turn
  #quest 2
    And P2 draws a quest of 3 stages
    And p3 chooses to sponsor
    And p3 builds the stages of the quest as posted
      |F5|
      |Quit|
      |F15|
      |S10|
      |Quit|
      |F5|
      |B15|
      |S10|
      |Quit|
    #stage 1
    And Other players decide to participate and draw a card
      |P1:|
      |P2:L20|
      |P4:L20|
    And p2 sets up their attack for stage 1
      |D5|
      |Quit|
    And p4 sets up their attack for stage 1
      |D5|
      |Quit|
    And Players are told if their attack for stage 1 is sufficient
      |p2:Wins|
      |p4:Wins|
    #stage 2
    And Other players decide to participate and draw a card
      |P2:L20|
      |P4:L20|
    And p2 sets up their attack for stage 2
      |L20|
      |S10|
      |Quit|
    And p4 sets up their attack for stage 2
      |L20|
      |S10|
      |Quit|
    And Players are told if their attack for stage 2 is sufficient
      |p2:Wins|
      |p4:Wins|
    #stage 3
    And Other players decide to participate and draw a card
      |P2:L20|
      |P4:L20|
    And p2 sets up their attack for stage 3
      |L20|
      |B15|
      |Quit|
    And p4 sets up their attack for stage 3
      |L20|
      |B15|
      |Quit|
    And Players are told if their attack for stage 3 is sufficient
      |p2:Wins|
      |p4:Wins|
    And The quest ends
    And p2 has 7 shields
    And p4 has 7 shields
    And End Turn
      |p2|
      |p4|

  Scenario: 1winner_game_with_events
    Given a new card game starts
    When the 4 starting hands are as posted for scenario 3
    And P1 draws a quest of 4 stages
    Then p1 chooses to sponsor
    And  p1 builds the stages of the quest as posted
      |F5|
      |Quit|
      |F15|
      |Quit|
      |F15|
      |D5|
      |Quit|
      |F40|
      |Quit|
    #stage 1
    And Other players decide to participate and draw a card
      |P2:F30:1|
      |P3:S10:1|
      |P4:B15:1|
    And p2 sets up their attack for stage 1
      |D5|
      |Quit|
    And p3 sets up their attack for stage 1
      |D5|
      |Quit|
    And p4 sets up their attack for stage 1
      |D5|
      |Quit|
    And Players are told if their attack for stage 1 is sufficient
      |p2:Wins|
      |p3:Wins|
      |p4:Wins|
    #stage 2
    And Other players decide to participate and draw a card
      |P2:S10|
      |P3:L20|
      |P4:L20|
    And p2 sets up their attack for stage 2
      |B15|
      |Quit|
    And p3 sets up their attack for stage 2
      |L20|
      |Quit|
    And p4 sets up their attack for stage 2
      |L20|
      |Quit|
    And Players are told if their attack for stage 2 is sufficient
      |p2:Wins|
      |p3:Wins|
      |p4:Wins|
        #stage 3
    And Other players decide to participate and draw a card
      |P2:L20|
      |P3:L20|
      |P4:L20|
    And p2 sets up their attack for stage 3
      |L20|
      |Quit|
    And p3 sets up their attack for stage 3
      |H10|
      |S10|
      |Quit|
    And p4 sets up their attack for stage 3
      |L20|
      |Quit|
    And Players are told if their attack for stage 3 is sufficient
      |p2:Wins|
      |p3:Wins|
      |p4:Wins|
              #stage 4
    And Other players decide to participate and draw a card
      |P2:E30|
      |P3:F30|
      |P4:L20|
    And p2 sets up their attack for stage 4
      |E30|
      |H10|
      |Quit|
    And p3 sets up their attack for stage 4
      |L20|
      |B15|
      |S10|
      |Quit|
    And p4 sets up their attack for stage 4
      |L20|
      |E30|
      |Quit|
    And Players are told if their attack for stage 4 is sufficient
      |p2:Wins|
      |p3:Wins|
      |p4:Wins|
    And The quest ends
    And p2 has 4 shields
    And p3 has 4 shields
    And p4 has 4 shields
    And p1 has 12 cards

    And next turn
    And p2 draws plaque
    And p2 has 2 shields
    And next turn
    And p3 draws prosperity
    And p1 has 12 cards
    And p2 has 12 cards
    And p3 has 10 cards
    And p4 has 12 cards
    And next turn
    And p4 draws queens favor
    And p4 has 12 cards
    And next turn
    And P1 draws a quest of 3 stages
    And p1 chooses to sponsor
    And p1 builds the stages of the quest as posted
      |F5|
      |S10|
      |Quit|
      |F5|
      |B15|
      |Quit|
      |F15|
      |S10|
      |Quit|

    #stage 1
    And Other players decide to participate and draw a card
      |P2:L20|
      |P3:B15|
      |P4:L20|
    And p2 sets up their attack for stage 1
      |B15|
      |Quit|
    And p3 sets up their attack for stage 1
      |L20|
      |Quit|
    And p4 sets up their attack for stage 1
      |D5|
      |Quit|
    And Players are told if their attack for stage 1 is sufficient
      |p2:Wins|
      |p3:Wins|
      |p4:Loses|

        #stage 2
    And Other players decide to participate and draw a card
      |P2:E30|
      |P3:L20|
    And p2 sets up their attack for stage 2
      |L20|
      |Quit|
    And p3 sets up their attack for stage 2
      |L20|
      |Quit|
    And Players are told if their attack for stage 2 is sufficient
      |p2:Wins|
      |p3:Wins|

        #stage 3
    And Other players decide to participate and draw a card
      |P2:E30|
      |P3:L20|
    And p2 sets up their attack for stage 3
      |E30|
      |Quit|
    And p3 sets up their attack for stage 3
      |L20|
      |B15|
      |Quit|
    And p4 sets up their attack for stage 3
      |L20|
      |B15|
      |Quit|
    And Players are told if their attack for stage 3 is sufficient
      |p2:Wins|
      |p3:Wins|
    And The quest ends
    And p2 has 5 shields
    And p3 has 7 shields
    And p4 has 4 shields
    And End Turn
      |p3|

  Scenario: 0_winner_quest
    Given a new card game starts
    When the 4 starting hands are as posted
    And P1 draws a quest of 2 stages
    Then p1 chooses to sponsor
    And  p1 builds the stages of the quest as posted
      |F5|
      |H10|
      |Quit|
      |F15|
      |S10|
      |Quit|
    #stage 1
    And Other players decide to participate and draw a card
      |P1:D5:1|
      |P3:D5:1|
      |P4:D5:1|
    And p2 sets up their attack for stage 1
      |D5|
      |Quit|
    And p3 sets up their attack for stage 1
      |D5|
      |Quit|
    And p4 sets up their attack for stage 1
      |D5|
      |Quit|
    And Players are told if their attack for stage 1 is sufficient
      |p2:Loses|
      |p3:Loses|
      |p4:Loses|
    And The quest ends
    And p1 has 0 shields and hand is below
      |F15|
      |F30|
      |D5|
      |S10|
      |S10|
      |H10|
      |B15|
      |B15|
      |B15|
      |L20|
      |L20|
      |L20|
    And p2 has 0 shields and hand is below
      |F5|
      |F15|
      |F15|
      |F40|
      |D5|
      |S10|
      |H10|
      |H10|
      |B15|
      |B15|
      |E30|
    And p3 has 0 shields and hand is below
      |F5|
      |F5|
      |F15|
      |D5|
      |S10|
      |S10|
      |S10|
      |H10|
      |H10|
      |B15|
      |L20|
    And p4 has 0 shields and hand is below
      |F15|
      |F15|
      |F40|
      |D5|
      |D5|
      |S10|
      |H10|
      |H10|
      |B15|
      |L20|
      |E30|