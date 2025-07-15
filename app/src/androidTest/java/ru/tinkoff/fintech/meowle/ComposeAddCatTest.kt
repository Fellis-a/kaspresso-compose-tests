package ru.tinkoff.fintech.meowle

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
import ru.tinkoff.fintech.meowle.testing.mock.response.AddCatResponseFactory
import ru.tinkoff.fintech.meowle.testing.prefs.MeowlePrefs
import ru.tinkoff.fintech.meowle.testing.rule.MeowleTestRule

class ComposeAddCatTest : TestCase() {

    @get:Rule
    val testRule = MeowleTestRule(isCompose = true, isLocalhost = true)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val mock = WireMockRule(5000)

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun addCat() = run {
        val catName = "Мурзик"
        val catDescription = "Британец, 2 года"

        before {
            MeowlePrefs.authorize()
            MeowlePrefs.changeAppUrl()

            meowleMock {
                addCat.respondWith(AddCatResponseFactory.success())
            }
        }.after {
            MeowlePrefs.clear()
        }.run {
            step("Запускаем приложение") {
                composeTestRule.waitForIdle()
            }

            step("Переходим на экран добавления кота") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("bottomNavigation"), 5000)

                composeTestRule.onNodeWithTag("addCatTab").performClick()

                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("addCatScreen"), 5000)
            }

            step("Вводим данные кота") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("catNameField"), 5000)

                composeTestRule.onNodeWithTag("catNameField").performTextInput(catName)

                composeTestRule.onNodeWithTag("catDescriptionField").performTextInput(catDescription)
            }

            step("Кликаем на кнопку добавить") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("addCatButton"), 5000)

                composeTestRule.onNodeWithTag("addCatButton").performClick()

                Thread.sleep(2000)
            }

            step("Проверяем успешное добавление") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("bottomNavigation"), 5000)
            }

            step("Проверяем что вызвался корректный метод добавления кота") {
                mock.verify(
                    postRequestedFor(urlPathMatching(".*/cats/add"))
                        .withRequestBody(containing("\"name\":\"$catName\""))
                        .withRequestBody(containing("\"description\":\"$catDescription\""))
                        .withRequestBody(containing("\"gender\":\"unisex\""))
                )
            }
        }
    }
}
