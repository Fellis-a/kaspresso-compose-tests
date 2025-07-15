package ru.tinkoff.fintech.meowle

import androidx.test.core.app.ActivityScenario
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.fintech.meowle.presentation.view.AuthActivity
import ru.tinkoff.fintech.meowle.testing.mock.MeowleMock.meowleMock
import ru.tinkoff.fintech.meowle.testing.mock.response.DetailsResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.PhotosResponseFactory
import ru.tinkoff.fintech.meowle.testing.mock.response.SearchResponseFactory
import ru.tinkoff.fintech.meowle.testing.prefs.MeowlePrefs
import ru.tinkoff.fintech.meowle.testing.rule.MeowleTestRule
import ru.tinkoff.fintech.meowle.testing.screens.SearchScreen
import ru.tinkoff.fintech.meowle.testing.screens.CatDetailsScreen

class SearchCatToDetailTest : TestCase() {

    @get:Rule
    val testRule = MeowleTestRule(isLocalhost = true)

    @get:Rule
    val mock = WireMockRule(5000)

    @Test
    fun openCatDetailFromSearch() = run {
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
            step("Запускаем приложение и ищем кота") {
                ActivityScenario.launch(AuthActivity::class.java)

                SearchScreen(this) {
                    checkScreenOpened()
                    findCat("Барсик")
                    openFirstCatDetails()
                }
            }
            step("Проверяем экран деталей кота") {
                CatDetailsScreen(this) {
                    checkScreenOpened()
                    checkCatName("Барсик")
                    checkCatDescription("Рыжий кот")
                }
            }
        }
    }
}