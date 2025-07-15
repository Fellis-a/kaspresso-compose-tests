package ru.tinkoff.fintech.meowle

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.github.tomakehurst.wiremock.client.WireMock.containing
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.testing.mock.MeowleMock.meowleMock
import ru.tinkoff.fintech.meowle.testing.mock.response.DetailsResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.EditCatResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.PhotosResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.RatingResponseFactory
import ru.tinkoff.fintech.meowle.testing.prefs.MeowlePrefs
import ru.tinkoff.fintech.meowle.testing.rule.MeowleTestRule

class ComposeEditCatTest : TestCase() {

    @get:Rule
    val testRule = MeowleTestRule(isCompose = true, isLocalhost = true)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val mock = WireMockRule(5000)

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun editCatDescription() = run {
        val newDescription = "Обновленное описание котика"

        before {
            MeowlePrefs.authorize()
            MeowlePrefs.changeAppUrl()
            meowleMock {
                rating.respondWith(RatingResponseFactory.success())
                details.respondWith(DetailsResponseFactory.success(newDescription))
                photos.respondWith(PhotosResponseFactory.photos())
                editCat.respondWith(EditCatResponseFactory.success())
            }
        }.after {
            MeowlePrefs.clear()
        }.run {
            step("Запускаем приложение") {
                composeTestRule.waitForIdle()
            }

            step("Переходим на экран рейтинга") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("bottomNavigation"), 5000)

                composeTestRule.onNodeWithTag("ratingTab").performClick()

                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("ratingScreen"), 5000)
            }

            step("Кликаем на 5 кота в списке") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("catCard"), 5000)

                composeTestRule
                    .onAllNodesWithTag("catCard", true)[4]
                    .performClick()
            }

            step("На открывшемся экране деталей изменяем описание кота") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("catName"), 5000)

                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("editButton"), 5000)

                composeTestRule.onNodeWithTag("editButton").performClick()

                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("descriptionField"), 5000)

                composeTestRule.onNodeWithTag("descriptionField").performTextClearance()
                composeTestRule.onNodeWithTag("descriptionField").performTextInput(newDescription)

                composeTestRule.onNodeWithTag("saveButton").performClick()

                Thread.sleep(2000)
            }

            step("Проверяем что описание изменилось") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("catDescription"), 5000)

                composeTestRule
                    .onNodeWithTag("catDescription")
                    .assertTextContains(newDescription, true)
            }

            step("Проверяем что вызвался корректный метод с запросом") {
                val fifthCatId = RatingResponseFactory.success().likes[4].id

                mock.verify(
                    postRequestedFor(urlPathMatching(".*/cats/save-description"))
                        .withRequestBody(containing("\"catDescription\":\"$newDescription\""))
                        .withRequestBody(containing("\"catId\":$fifthCatId"))
                )
            }
        }
    }
}
