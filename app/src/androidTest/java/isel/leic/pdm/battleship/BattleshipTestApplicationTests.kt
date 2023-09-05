package isel.leic.pdm.battleship

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class BattleshipTestApplicationTests {

    @Test
    fun instrumented_tests_use_application_test_context() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("isel.leic.pdm.battleship", context.packageName)
        assertTrue(
            "Make sure the tests runner is correctly configured in build.gradle\n" +
                    "defaultConfig { testInstrumentationRunner <test runner class name> }",
            context.applicationContext is BattleshipApplication
        )
    }

    @Test
    fun application_context_contains_dependencies() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertTrue(context.applicationContext is DependenciesContainer)
    }
}