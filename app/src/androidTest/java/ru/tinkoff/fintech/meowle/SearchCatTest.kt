package ru.tinkoff.fintech.meowle

import androidx.test.core.app.ActivityScenario
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.presentation.view.AuthActivity
import ru.tinkoff.fintech.meowle.testing.mock.MeowleMock.meowleMock
import ru.tinkoff.fintech.meowle.testing.mock.response.SearchResponseFactory
import ru.tinkoff.fintech.meowle.testing.prefs.MeowlePrefs
import ru.tinkoff.fintech.meowle.testing.rule.MeowleTestRule
import ru.tinkoff.fintech.meowle.testing.screens.SearchScreen

class SearchCatTest : TestCase() {

    @get:Rule
    val testRule = MeowleTestRule(isLocalhost = true)

    @get: Rule
    val mock = WireMockRule(5000)

    @Test
    fun successfulCatSearch() = run {
        before {
            MeowlePrefs.authorize()
            MeowlePrefs.changeAppUrl()

            meowleMock {
                search.respondWith(
                    SearchResponseFactory.cats()
                )
            }
        }.after {
            MeowlePrefs.clear()
        }.run {
            step("Запускаем приложение и ищем кота") {
                ActivityScenario.launch(AuthActivity::class.java)

                SearchScreen(this) {
                    checkScreenOpened()
                    findCat("Барсик")
                    checkCatName("Барсик", 0)
                }
            }
        }
    }
}