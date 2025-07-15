package ru.tinkoff.fintech.meowle

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.testing.mock.MeowleMock.meowleMock
import ru.tinkoff.fintech.meowle.testing.mock.response.SearchResponseFactory
import ru.tinkoff.fintech.meowle.testing.prefs.MeowlePrefs
import ru.tinkoff.fintech.meowle.testing.rule.MeowleTestRule
import ru.tinkoff.fintech.meowle.testing.screens.compose.SearchScreen

class ComposeSearchCatTest : TestCase() {

    @get:Rule
    val testRule = MeowleTestRule(isCompose = true, isLocalhost = true)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val mock = WireMockRule(5000)

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchCat() = run {
        val searchQuery = "Барсик"

        before {
            MeowlePrefs.authorize()
            MeowlePrefs.changeAppUrl()
            meowleMock {
                search.respondWith(SearchResponseFactory.cats())
            }
        }.after {
            MeowlePrefs.clear()
        }.run {
            step("Ищем котика по имени") {
                val searchScreen = SearchScreen(composeTestRule)
                searchScreen.searchBar.findCat(searchQuery)
            }

            step("Проверяем результаты поиска") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("catCard"), 3000)

                composeTestRule
                    .onAllNodesWithTag("catName", true)
                    .onFirst()
                    .assertTextContains(searchQuery, true)
            }
        }
    }
}
