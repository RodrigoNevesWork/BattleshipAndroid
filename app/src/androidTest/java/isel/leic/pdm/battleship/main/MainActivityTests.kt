package isel.leic.pdm.battleship.main

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import isel.leic.pdm.battleship.activities.about.AboutScreenTag
import isel.leic.pdm.battleship.activities.main.BattleshipActivity
import isel.leic.pdm.battleship.activities.main.*
import isel.leic.pdm.battleship.activities.user.NicknameInputTag
import isel.leic.pdm.battleship.activities.user.PasswordInputTag
import isel.leic.pdm.battleship.activities.user.UserScreenTag
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTests {

    @get:Rule
    val testRule = createAndroidComposeRule<BattleshipActivity>()

    @Test
    fun screen_contains_start_button() {
        testRule.onNodeWithTag(BattleshipScreenTag).assertExists()
        testRule.onNodeWithTag(PlayButtonTag).assertExists()
        testRule.onNodeWithTag(RankingButtonTag).assertExists()
        testRule.onNodeWithTag(GameRulesButtonTag).assertExists()
        testRule.onNodeWithTag(UserInfoButtonTag).assertExists()
        testRule.onNodeWithTag(AboutButtonTag).assertExists()
    }

    @Test
    fun pressing_about_button_navigates_to_about() {

        // Act
        testRule.onNodeWithTag(AboutButtonTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag(AboutScreenTag).assertExists()
    }

    @Test
    fun pressing_user_button_navigates_to_user_info() {

        // Act
        testRule.onNodeWithTag(UserInfoButtonTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag(UserScreenTag).assertExists()
        testRule.onNodeWithTag(NicknameInputTag).assertExists()
        testRule.onNodeWithTag(PasswordInputTag).assertExists()
    }

}