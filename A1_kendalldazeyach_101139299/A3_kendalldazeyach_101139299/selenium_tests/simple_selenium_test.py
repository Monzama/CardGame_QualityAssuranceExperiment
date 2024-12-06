# Document owned and created by Omar Syed
import unittest
import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.chrome import ChromeDriverManager

sleep_time = 1.2  # this is your test delay

class BlackjackGameTest(unittest.TestCase):
    
    def validate_player_hands(self):
        # Locate all console lines
        console_lines = self.driver.find_elements(By.CSS_SELECTOR, "#console .console-line")
        # Dictionary to store actual hands
        actual_hands = {}
        wins = ""
        count = 0
        for line in console_lines:
            if count == 0:
                if "Win" in line.text:
                    wins = line.text
                    count += 2
                    continue    
            if "Hand:" in line.text and count != 0:
                actual_hands[f"p{count}"] = line.text
                count += 1
                continue
            
        print(wins)
        print(actual_hands)
        return wins, actual_hands

    def lines_changed(self, driver, old_lines_text):
        # First, find the console by ID
        console = driver.find_element(By.ID, "console")

        # Check if there are any console-line children
        console_lines = console.find_elements(By.CSS_SELECTOR, ".console-line")

        # If there are no lines, compare empty text state
        if not console_lines:
            current_console_text = []
        else:
            # Extract text from each console line
            current_console_text = console.text.split('\n')

        return current_console_text != old_lines_text

    @classmethod
    def setUpClass(cls):
        cls.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))
        cls.driver.get("http://127.0.0.1:8081/")  # Open the application
        cls.driver.maximize_window()
        time.sleep(3)  # Allow time for page to load

    @classmethod
    def tearDownClass(cls):
        cls.driver.quit()

    def test_send_input_command_and_list_scenario1(self):
        input_bar = self.driver.find_element(By.ID, "input-bar")

        # Send the initial setup command
        setup_command = "setup_scenario1"
        input_bar.send_keys(setup_command)
        input_bar.send_keys("\n")  # Simulate pressing Enter
        time.sleep(0.5)  # Wait for the setup to complete
        # Validate the setup command was processed
        console_lines = self.driver.find_elements(By.CSS_SELECTOR, "#console .console-line")
        self.assertTrue(
            any(setup_command in line.text for line in console_lines),
            "The setup command should appear in the console output."
        )

        # List of strings to parse and send
        commands_list = ["start","", "no","yes", "", "1", "8", "Quit", "", "3", "7",
                        "Quit", "", "4", "6","10", "Quit", "", "5",
                        "11", "Quit", "","", "t", "1", "",
                        "t", "1", "", "t", "1", "", "",
                        "5","6", "Quit", "","6","4", "Quit", "", "4","7", "Quit",
                        "", "", "t", "", "t","","t","","","7","6", "Quit", "", "9","5", "Quit", "", "6", "7", "Quit", "",
                        "", "t", "", "t", "", "", "9", "7","4", "Quit", "", "7", "6","9", "Quit", "", "",
                        "t","","t","","", "7", "6","8", "Quit", "","4","5","7", "8","Quit","", "1", "1", "1", "1","","display hands"]

        for command in commands_list:
            # Record old console text
            console = self.driver.find_element(By.CSS_SELECTOR, "#console")
            old_console_text = console.text.split('\n')

            # Send command if not empty
            if command:
                input_bar.send_keys(command)
            input_bar.send_keys("\n")

            # Wait for console to change
            WebDriverWait(self.driver, 10).until(lambda d: self.lines_changed(d, old_console_text))
            time.sleep(sleep_time+0.1)
            
        #assert card count, shields, display final hand
        time.sleep(5)
        # Assert player shield and card counts
        player1_status_div = self.driver.find_element(By.ID, "status-p1")
        player2_status_div = self.driver.find_element(By.ID, "status-p2")
        player3_status_div = self.driver.find_element(By.ID, "status-p3")
        player4_status_div = self.driver.find_element(By.ID, "status-p4")
        self.assertEqual("p1: 0 Shields | 9 Cards", player1_status_div.text)
        self.assertEqual("p2: 0 Shields | 12 Cards", player2_status_div.text)
        self.assertEqual("p3: 0 Shields | 5 Cards", player3_status_div.text)
        self.assertEqual("p4: 4 Shields | 4 Cards", player4_status_div.text)
        p1_hand = ['1: F5', '2: F10', '3: F15', '4: F15', '5: F30', '6: H10', '7: B15', '8: B15', '9: L20']
        p3_hand = ['1: F5', '2: F5', '3: F15', '4: F30', '5: S10']
        p4_hand = ['1: F15', '2: F15', '3: F40', '4: L20']
        win, actualhands = self.validate_player_hands()
        
        self.assertTrue("No Winners" in win)
        
        for card in p1_hand:
            if card not in win:
                print("P1 hand fail")
                self.assertTrue(False)
                break
        for card in p3_hand:
            if card not in actualhands["p3"]:
                print("P3 hand fail")
                self.assertTrue(False)
                break
        for card in p4_hand:
            if card not in actualhands["p4"]:
                print("P4 hand fail")
                self.assertTrue(False)
                break
        print("\n\nA1 Scenario: TEST PASS\n\n")
        
    def test_send_input_command_and_list_scenario2(self):
        self.driver.refresh()
        time.sleep(2)
        input_bar = self.driver.find_element(By.ID, "input-bar")

        # Send the initial setup command
        setup_command = "setup_scenario2"
        input_bar.send_keys(setup_command)
        input_bar.send_keys("\n")  # Simulate pressing Enter
        time.sleep(0.5)  # Wait for the setup to complete
        # Validate the setup command was processed
        console_lines = self.driver.find_elements(By.CSS_SELECTOR, "#console .console-line")
        self.assertTrue(
            any(setup_command in line.text for line in console_lines),
            "The setup command should appear in the console output."
        )

        commands_list = ["start","", "yes", "", "1", "Quit", "", "2", "7",
                        "Quit", "", "4", "8", "Quit", "", "3",
                        "10", "Quit", "","", "t", "1", "",
                        "t", "1", "", "t", "1", "", "",
                        "6", "Quit", "", "Quit", "", "7", "Quit",
                        "", "", "t", "", "t","","","5", "Quit", "", "5", "Quit", "",
                        "", "t", "", "t", "", "", "7", "6", "Quit", "", "7", "6", "Quit", "", "",
                        "t","","t","","", "6", "8", "Quit", "","6","7","Quit","","",
                        "1", "1", "1", "1", "", "","", "no","yes","","1", "Quit", "",
                        "2", "6", "Quit", "", "3", "8", "Quit", "", "", "w", "", "t","", "t","","",
                        "6", "Quit", "", "6", "Quit", "","", "t", "", "t","","", "7", "Quit", "",
                        "7", "Quit", "", "", "t","","t","", "", "10", "Quit", "", "10", "Quit", "", "1","2","2",""]

        for command in commands_list:
            console = self.driver.find_element(By.CSS_SELECTOR, "#console")
            old_console_text = console.text.split('\n')

            if command != "":
                input_bar.send_keys(command)
            input_bar.send_keys("\n")  # Press Enter

            WebDriverWait(self.driver, 10).until(lambda d: self.lines_changed(d, old_console_text))
            time.sleep(sleep_time+0.1)
            
        time.sleep(5)
            
        # Assert player shield and card counts
        player1_status_div = self.driver.find_element(By.ID, "status-p1")
        player2_status_div = self.driver.find_element(By.ID, "status-p2")
        player3_status_div = self.driver.find_element(By.ID, "status-p3")
        player4_status_div = self.driver.find_element(By.ID, "status-p4")
        self.assertEqual("p1: 0 Shields | 12 Cards", player1_status_div.text)
        self.assertEqual("p2: 7 Shields | 9 Cards", player2_status_div.text)
        self.assertEqual("p3: 0 Shields | 12 Cards", player3_status_div.text)
        self.assertEqual("p4: 7 Shields | 9 Cards", player4_status_div.text)
        p1_hand = ['1: F15', '2: F15', '3: F20', '4: F20', '5: F20', '6: F20', '7: F25', '8: F25', '9: F30', '10: H10', '11: B15', '12: L20']
        p2_hand =  ['1: F10', '2: F15', '3: F15', '4: F25', '5: F30', '6: F40', '7: F50', '8: L20', '9: L20']
        p3_hand = ['1: F20', '2: F40', '3: D5', '4: D5', '5: S10', '6: H10', '7: H10', '8: H10', '9: H10', '10: B15', '11: B15', '12: L20']
        p4_hand = ['1: F15', '2: F15', '3: F20', '4: F25', '5: F30', '6: F50', '7: F70', '8: L20', '9: L20']
        win, actualhands = self.validate_player_hands()

        self.assertTrue("p2 & p4 Win The Game!" in win)

        for card in p1_hand:
            if card not in win:
                print("P1 hand fail")
                self.assertTrue(False)
                break
        for card in p2_hand:
            if card not in actualhands["p2"]:
                print("P2 hand fail")
                self.assertTrue(False)
                break
        for card in p3_hand:
            if card not in actualhands["p3"]:
                print("P3 hand fail")
                self.assertTrue(False)
                break
        for card in p4_hand:
            if card not in actualhands["p4"]:
                print("P4 hand fail")
                self.assertTrue(False)
                break   
        print("\n\n2winner_game_2winner_quest: TEST PASS\n\n")    

    def test_send_input_command_and_list_scenario3(self):
        self.driver.refresh()
        time.sleep(2)
        input_bar = self.driver.find_element(By.ID, "input-bar")

        # Send the initial setup command
        setup_command = "setup_scenario3"
        input_bar.send_keys(setup_command)
        input_bar.send_keys("\n")  # Simulate pressing Enter
        time.sleep(0.5)
        console_lines = self.driver.find_elements(By.CSS_SELECTOR, "#console .console-line")
        self.assertTrue(
            any(setup_command in line.text for line in console_lines),
            "The setup command should appear in the console output."
        )

        commands_list = ["start","", "yes", "", "1", "Quit", "", "3",
                        "Quit", "", "5", "Quit", "", "7",
                        "Quit", "","", "t", "1", "",
                        "t", "1", "", "t", "1", "", "",
                        "3", "Quit", "","3", "Quit", "", "4", "Quit",
                        "", "", "t", "", "t","", "t","","","6", "Quit", "", "7", "Quit", "", "7", "Quit", "",
                        "", "t", "", "t","", "t","", "", "8", "Quit", "", "9", "Quit", "", "9" , "Quit", "", "",
                        "t","","t","","t","","", "11", "Quit", "","11","Quit","","11", "Quit", "",
                        "1", "1", "2", "2", "", "", "","", "", "1", "", "1","", "","1", "", "","1", "", "","1", "", "", "", "3", "", "4", "", "",
                        "","yes","","1", "Quit", "", "2", "9", "Quit", "","6","10", "Quit", "" ,"", "t", "1", "", "t", "1", "", "t", "1", "", "",
                        "9", "Quit", "", "9", "Quit", "", "10", "Quit", "","",
                        "t","","t","", "","10", "8", "Quit", "", "10", "6", "Quit", "", "",
                        "t","","t","", "", "10", "6", "Quit", "","11", "Quit", "", "1", "1", "1",""]

        for command in commands_list:
            console = self.driver.find_element(By.CSS_SELECTOR, "#console")
            old_console_text = console.text.split('\n')

            if command != "":
                input_bar.send_keys(command)
            input_bar.send_keys("\n")

            WebDriverWait(self.driver, 10).until(lambda d: self.lines_changed(d, old_console_text))
            time.sleep(sleep_time+0.1)

        time.sleep(5)

        # Assert player shield and card counts
        player1_status_div = self.driver.find_element(By.ID, "status-p1")
        player2_status_div = self.driver.find_element(By.ID, "status-p2")
        player3_status_div = self.driver.find_element(By.ID, "status-p3")
        player4_status_div = self.driver.find_element(By.ID, "status-p4")
        self.assertEqual("p1: 0 Shields | 12 Cards", player1_status_div.text)
        self.assertEqual("p2: 5 Shields | 9 Cards", player2_status_div.text)
        self.assertEqual("p3: 7 Shields | 10 Cards", player3_status_div.text)
        self.assertEqual("p4: 4 Shields | 11 Cards", player4_status_div.text)

        p1_hand = ['1: F25', '2: F25', '3: F35', '4: D5', '5: D5', '6: S10', '7: S10', '8: S10', '9: S10', '10: H10', '11: H10', '12: H10']
        p2_hand = ['1: F15', '2: F25', '3: F30', '4: F40', '5: S10', '6: S10', '7: S10', '8: H10', '9: E30']
        p3_hand = ['1: F10', '2: F25', '3: F30', '4: F40', '5: F50', '6: S10', '7: S10', '8: H10', '9: H10', '10: L20']
        p4_hand = ['1: F25', '2: F25', '3: F30', '4: F50', '5: F70', '6: D5', '7: D5', '8: S10', '9: S10', '10: B15', '11: L20']

        win, actualhands = self.validate_player_hands()

        self.assertTrue("p3 Wins The Game!" in win)

        for card in p1_hand:
            if card not in win:
                print("P1 hand fail")
                print("C: " + card)
                self.assertTrue(False)
                break
        for card in p2_hand:
            if card not in actualhands["p2"]:
                print("P2 hand fail")
                print("C: " + card)
                self.assertTrue(False)
                break
        for card in p3_hand:
            if card not in actualhands["p3"]:
                print("P3 hand fail")
                print("C: " + card)
                self.assertTrue(False)
                break
        for card in p4_hand:
            if card not in actualhands["p4"]:
                print("P4 hand fail\n\n")
                print("C: " + card)
                self.assertTrue(False)
                break 
        print("\n\n1winner_game_with_events: TEST PASS\n\n")

    def test_send_input_command_and_list_scenario4(self):
        self.driver.refresh()
        time.sleep(2)
        input_bar = self.driver.find_element(By.ID, "input-bar")

        # Send the initial setup command
        setup_command = "setup_scenario4"
        input_bar.send_keys(setup_command)
        input_bar.send_keys("\n")
        time.sleep(0.5)
        console_lines = self.driver.find_elements(By.CSS_SELECTOR, "#console .console-line")
        self.assertTrue(
            any(setup_command in line.text for line in console_lines),
            "The setup command should appear in the console output."
        )

        commands_list = [
            "start","", "yes", "", "1","3","5","7","9","11", "Quit", "", "2","4","6","8","10","12",
            "Quit","","","", "t", "1", "", "t", "4", "", "t", "3","","", "12", "Quit", "", "Quit", "", "Quit", "", "","1", "1" ,"","display hands"
        ]

        for command in commands_list:
            console = self.driver.find_element(By.CSS_SELECTOR, "#console")
            old_console_text = console.text.split('\n')

            if command != "":
                input_bar.send_keys(command)
            input_bar.send_keys("\n")

            WebDriverWait(self.driver, 10).until(lambda d: self.lines_changed(d, old_console_text))
            time.sleep(sleep_time+0.1)

        time.sleep(5)

        # Assert player shield and card counts
        player1_status_div = self.driver.find_element(By.ID, "status-p1")
        player2_status_div = self.driver.find_element(By.ID, "status-p2")
        player3_status_div = self.driver.find_element(By.ID, "status-p3")
        player4_status_div = self.driver.find_element(By.ID, "status-p4")
        self.assertEqual("p1: 0 Shields | 12 Cards", player1_status_div.text)
        self.assertEqual("p2: 0 Shields | 11 Cards", player2_status_div.text)
        self.assertEqual("p3: 0 Shields | 12 Cards", player3_status_div.text)
        self.assertEqual("p4: 0 Shields | 12 Cards", player4_status_div.text)
        
        p1_hand = ['1: F15', '2: D5', '3: D5', '4: D5', '5: D5', '6: S10', '7: S10', '8: S10', '9: H10', '10: H10', '11: H10', '12: H10']
        p2_hand = ['1: F5', '2: F5', '3: F10', '4: F15', '5: F15', '6: F20', '7: F20', '8: F25', '9: F30', '10: F30', '11: F40']
        p3_hand = ['1: F5', '2: F5', '3: F10', '4: F15', '5: F15', '6: F20', '7: F20', '8: F25', '9: F25', '10: F30', '11: F40', '12: L20']
        p4_hand = ['1: F5', '2: F5', '3: F10', '4: F15', '5: F15', '6: F20', '7: F20', '8: F25', '9: F25', '10: F30', '11: F50', '12: E30']

        win, actualhands = self.validate_player_hands()

        self.assertTrue("No Winners" in win)

        for card in p1_hand:
            if card not in win:
                print("P1 hand fail")
                print("C: " + card)
                self.assertTrue(False)
                break
        for card in p2_hand:
            if card not in actualhands["p2"]:
                print("P2 hand fail")
                print("C: " + card)
                self.assertTrue(False)
                break
        for card in p3_hand:
            if card not in actualhands["p3"]:
                print("P3 hand fail")
                print("C: " + card)
                self.assertTrue(False)
                break
        for card in p4_hand:
            if card not in actualhands["p4"]:
                print("P4 hand fail\n\n")
                print("C: " + card)
                self.assertTrue(False)
                break 
        print("\n\n0_winner_quest: TEST PASS\n\n")

if __name__ == "__main__":
    unittest.main()
