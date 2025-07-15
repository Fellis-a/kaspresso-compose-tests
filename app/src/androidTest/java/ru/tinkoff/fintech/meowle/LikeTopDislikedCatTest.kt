package ru.tinkoff.fintech.meowle

import androidx.test.core.app.ActivityScenario
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.presentation.view.AuthActivity
import ru.tinkoff.fintech.meowle.testing.mock.MeowleMock.meowleMock
import ru.tinkoff.fintech.meowle.testing.mock.response.DetailsResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.LikesResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.PhotosResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.RatingResponseFactory
import ru.tinkoff.fintech.meowle.testing.prefs.MeowlePrefs
import ru.tinkoff.fintech.meowle.testing.rule.MeowleTestRule
import ru.tinkoff.fintech.meowle.testing.screens.RatingScreen
import ru.tinkoff.fintech.meowle.testing.screens.CatDetailsScreen

class LikeTopDislikedCatTest : TestCase() {

    @get:Rule
    val testRule = MeowleTestRule(isLocalhost = true)

    @get:Rule
    val mock = WireMockRule(5000)

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
                ActivityScenario.launch(AuthActivity::class.java)
                RatingScreen(this) {
                    openRatingScreen()
                    checkScreenOpened()
                    switchToDislikesTab()
                    checkDislikedCatsDisplayed()
                }
            }

            step("Кликаем на первого котика по дизлайкам") {
                RatingScreen(this) {
                    clickFirstCat()
                }
            }

            step("Ставим лайк котику на экране деталей") {
                CatDetailsScreen(this) {
                    checkScreenOpened()
                    likeCat()
                }
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