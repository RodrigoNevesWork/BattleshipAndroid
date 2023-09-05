package isel.leic.pdm.battleship

import isel.leic.pdm.battleship.preferences.UserInfo
import isel.leic.pdm.battleship.preferences.validateUserInfoParts
import org.junit.Test
import org.junit.Assert.*

class UserInfoTests {
    @Test(expected = IllegalArgumentException::class)
    fun `create instance with blank nick throws`() {
        UserInfo(username = "\n  \t ", password = "password")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create instance with blank moto throws`() {
        UserInfo(username = "nick", password = "\n  \t ")
    }

    @Test
    fun `create instance with non empty nick and moto succeeds`() {
        UserInfo(username = "nick", password = "password")
    }

    @Test
    fun `validateUserInfoParts returns false when nick is blank`() {
        assertFalse(validateUserInfoParts("  \n", "password"))
    }

    @Test
    fun `validateUserInfoParts returns false when password is blank`() {
        assertFalse(validateUserInfoParts("nick", " "))
    }

    @Test
    fun `validateUserInfoParts returns true when password and nick are non empty`() {
        assertTrue(validateUserInfoParts("nick", "password"))
    }
}