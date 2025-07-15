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
import ru.tinkoff.fintech.meowle.testing.mock.response.AddCatResponseFactory
import ru.tinkoff.fintech.meowle.testing.prefs.MeowlePrefs
import ru.tinkoff.fintech.meowle.testing.rule.MeowleTestRule
import ru.tinkoff.fintech.meowle.testing.screens.AddCatScreen


class AddCatTest : TestCase() {

    @get:Rule
    val testRule = MeowleTestRule(isLocalhost = true)

    @get:Rule
    val mock = WireMockRule(5000)

    @Test
    fun addCat() = run {
        before {
            MeowlePrefs.authorize()
            MeowlePrefs.changeAppUrl()
            meowleMock {
                addCat.respondWith(AddCatResponseFactory.success())
            }
        }.after {
            MeowlePrefs.clear()
        }.run {
            step("Запускаем приложение и переходим на экран добавления кота") {
                ActivityScenario.launch(AuthActivity::class.java)

                AddCatScreen(this) {
                    openRatingScreen()
                    checkScreenOpened()
                    enterCatName("Барсик")
                    enterCatGender("Муж.")
                    enterCatDescription("Британец, 2 года")
                    clickAddButton()
                }
            }

            step("Проверяем успешное добавление и корректный запрос") {
                mock.verify(
                    postRequestedFor(urlPathMatching(".*/cats/add"))
                        .withRequestBody(containing("\"name\":\"Барсик\""))
                )
            }
        }
    }
}