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
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.presentation.MainActivity
import ru.tinkoff.fintech.meowle.testing.mock.MeowleMock.meowleMock
import ru.tinkoff.fintech.meowle.testing.mock.response.DetailsResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.LikesResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.PhotosResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.RatingResponseFactory
import ru.tinkoff.fintech.meowle.testing.prefs.MeowlePrefs
import ru.tinkoff.fintech.meowle.testing.rule.MeowleTestRule

class ComposeLikeTopDislikedCatTest : TestCase() {

    @get:Rule
    val testRule = MeowleTestRule(isCompose = true, isLocalhost = true)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val mock = WireMockRule(5000)

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun likeTopDislikedCat() = run {
        before {
            MeowlePrefs.authorize()
            MeowlePrefs.changeAppUrl()

            meowleMock {
                rating.respondWith(RatingResponseFactory.dislikedCats())
                details.respondWith(DetailsResponseFactory.catDetails())
                likes.respondWith(LikesResponseFactory.success())
                photos.respondWith(PhotosResponseFactory.photos())
            }
        }.after {
            MeowlePrefs.clear()
        }.run {
            step("Запускаем приложение") {
                composeTestRule.waitForIdle()
            }

            step("Переходим на экран рейтинга (топ по дизлайкам)") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("bottomNavigation"), 5000)

                composeTestRule.onNodeWithTag("ratingTab").performClick()

                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("ratingScreen"), 5000)

                composeTestRule.onNodeWithTag("dislikesTab").performClick()
            }

            step("Проверяем что вывелись замоканные данные") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("catCard"), 5000)

                composeTestRule
                    .onAllNodesWithTag("catName", true)
                    .onFirst()
                    .assertTextContains("Барсик", true)
            }

            step("Кликаем на самого первого котика по дизлайкам") {
                composeTestRule
                    .onAllNodesWithTag("catCard", true)
                    .onFirst()
                    .performClick()
            }

            step("На экране деталей котика ставим ему лайк") {
                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("catName"), 5000)

                composeTestRule
                    .onNodeWithTag("catName")
                    .assertIsDisplayed()

                composeTestRule
                    .onNodeWithTag("catName")
                    .assertTextContains("Барсик", true)

                composeTestRule.waitUntilAtLeastOneExists(hasTestTag("likeButton"), 10000)

                composeTestRule.onNodeWithTag("likeButton").assertIsDisplayed()

                composeTestRule.onNodeWithTag("likeButton").performClick()

                Thread.sleep(2000)
            }

            step("Проверяем что вызвался корректный метод лайка") {
                val firstCatId = RatingResponseFactory.dislikedCats().dislikes.first().id

                mock.verify(
                    postRequestedFor(urlPathMatching(".*/likes/cats/$firstCatId/likes"))
                )
            }
        }
    }
}
