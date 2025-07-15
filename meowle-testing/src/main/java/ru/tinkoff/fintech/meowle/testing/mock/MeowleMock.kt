package ru.tinkoff.fintech.meowle.testing.mock

import io.qameta.allure.kotlin.Allure.step

/**
 * @author Ruslan Ganeev
 */
object MeowleMock {

    //endpoints
    val search = SearchMock()
    val details = DetailsMock()
    val photos = PhotosMock()
    val rating = RatingMock()
    val likes = LikesMock()
    val addCat = AddCatMock()
    val editCat = EditCatMock()

    //scenarios
    val meowleScenario = MockScenario()

    inline fun meowleMock(crossinline block: MeowleMock.() -> Unit) {
        step("[MOCK]") {
            block(this@MeowleMock)
        }
    }
}
