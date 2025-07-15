package ru.tinkoff.fintech.meowle

import androidx.test.core.app.ActivityScenario
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.containing
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.presentation.view.AuthActivity
import ru.tinkoff.fintech.meowle.testing.mock.MeowleMock.meowleMock
import ru.tinkoff.fintech.meowle.testing.mock.response.EditCatResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.RatingResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.DetailsResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.PhotosResponseFactory
import ru.tinkoff.fintech.meowle.testing.prefs.MeowlePrefs
import ru.tinkoff.fintech.meowle.testing.rule.MeowleTestRule
import ru.tinkoff.fintech.meowle.testing.screens.RatingScreen
import ru.tinkoff.fintech.meowle.testing.screens.CatDetailsScreen

class EditCatTest : TestCase() {

    @get:Rule
    val testRule = MeowleTestRule(isLocalhost = true)

    @get:Rule
    val mock = WireMockRule(5000)

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
            step("Запускаем приложение и переходим на экран рейтинга") {
                ActivityScenario.launch(AuthActivity::class.java)
                RatingScreen(this) {
                    openRatingScreen()
                    checkScreenOpened()
                    selectCat(5)
                }
            }

            step("Редактируем описание кота") {
                CatDetailsScreen(this) {
                    checkScreenOpened()
                    clickEditButton()
                    changeDescription(newDescription)
                    clickSaveButton()
                    checkDescription(newDescription)
                }
            }

            step("Проверяем корректный запрос на редактирование кота") {
                mock.verify(
                    postRequestedFor(urlPathMatching(".*/cats/save-description"))
                        .withRequestBody(containing("\"catDescription\":\"$newDescription\""))
                        .withRequestBody(containing("\"catId\":5"))
                )
            }
        }
    }
}