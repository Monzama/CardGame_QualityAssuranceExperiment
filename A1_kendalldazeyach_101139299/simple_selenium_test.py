# Document owned and created by Omar Syed
import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import time

class BlackjackGameTest(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        cls.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))
        # CHANGES MUST BE MADE HERE FOR RIGHT DIRECTORY
        cls.driver.get("file:///C:/Users/irock/Documents/Selenium-Tutorial/blackjack-game/index.html")
        cls.driver.maximize_window()
        time.sleep(2)

    @classmethod
    def tearDownClass(cls):
        cls.driver.quit()

    def test_hit_button(self):
        hit_button = self.driver.find_element(By.ID, "hit-button")
        hit_button.click()
        time.sleep(1)
        player_cards = self.driver.find_elements(By.CSS_SELECTOR, "#player-cards .card")
        self.assertGreaterEqual(len(player_cards), 3, "Player should have at least 3 cards after hitting.")

    def test_stand_button(self):
        stand_button = self.driver.find_element(By.ID, "stand-button")
        stand_button.click()
        time.sleep(2)
        game_status = self.driver.find_element(By.ID, "game-status").text
        self.assertNotEqual(game_status, "", "Game status should be updated after standing.")

    def test_restart_button(self):
        restart_button = self.driver.find_element(By.ID, "restart-button")
        restart_button.click()
        time.sleep(1)
        game_status_after_restart = self.driver.find_element(By.ID, "game-status").text
        self.assertEqual(game_status_after_restart, "", "Game should be reset after clicking Restart.")

if __name__ == "__main__":
    unittest.main()