package ru.tinkoff.fintech.meowle

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.testing.mock.MeowleMock.meowleMock
import ru.tinkoff.fintech.meowle.testing.mock.response.DetailsResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.PhotosResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.SearchResponseFactory
import ru.tinkoff.fintech.meowle.testing.prefs.MeowlePrefs
import ru.tinkoff.fintech.meowle.testing.rule.MeowleTestRule
import ru.tinkoff.fintech.meowle.testing.screens.compose.SearchScreen

class ComposeSearchCatToDetailTest : TestCase() {

    @get:Rule
    val testRule = MeowleTestRule(isCompose = true, isLocalhost = true)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val mock = WireMockRule(5000)

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchCatToDetail() = run {
        val searchQuery = "Барсик"
        val expectedCatName = "Барсик"
        val expectedCatDescription = "Рыжий кот"

        before {
            MeowlePrefs.authorize()
            MeowlePrefs.changeAppUrl()
            meowleMock {
                search.respondWith(SearchResponseFactory.cats())
                details.respondWith(DetailsResponseFactory.catDetails())
                photos.respondWith(PhotosResponseFactory.photos())
            }
        }.after {
            MeowlePrefs.clear()
        }.run {
            step("Запускаем приложение") {
                composeTestRule.waitForIdle()
            }

            step("Ищем котика по имени") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("search"), 5000)

                val searchScreen = SearchScreen(composeTestRule)

                searchScreen.searchBar.findCat(searchQuery)
            }

            step("Ждем появления результатов поиска") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("catCard"), 5000)

                composeTestRule
                    .onAllNodesWithTag("catName", true)
                    .onFirst()
                    .assertTextContains(searchQuery, true)
            }

            step("Кликаем на первого найденного котика") {
                composeTestRule
                    .onAllNodesWithTag("catCard", true)
                    .onFirst()
                    .performClick()
            }

            step("Проверяем открытие экрана деталей") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("catName"), 5000)

                composeTestRule
                    .onNodeWithTag("catName")
                    .assertTextContains(expectedCatName, true)

                composeTestRule
                    .onNodeWithTag("catDescription")
                    .assertTextContains(expectedCatDescription, true)
            }
        }
    }
}
