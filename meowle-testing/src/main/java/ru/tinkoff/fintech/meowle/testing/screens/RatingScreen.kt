package ru.tinkoff.fintech.meowle.testing.screens

import android.view.View
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.tabs.KTabLayout
import io.github.kakaocup.kakao.common.views.KView
import ru.tinkoff.fintech.meowle.R
import org.hamcrest.Matcher

class RatingScreen(testContext: TestContext<*>) : BaseScreen(testContext) {

    private val ratingTabBtn = KView { withId(R.id.tab_btn_rating) }

    private val tabLayout = KTabLayout { withId(R.id.tab_layout) }

    private val ratingList = KRecyclerView(
        builder = { withId(R.id.rv_cats_list) },
        itemTypeBuilder = { itemType(::RatingItem) }
    )

    fun openRatingScreen() {
        step("Переходим на экран рейтинга через нижнюю навигацию") {
            ratingTabBtn.click()
        }
    }

    fun checkScreenOpened() {
        step("Проверяем, что экран рейтинга открыт") {
            tabLayout.isDisplayed()
        }
    }

    fun switchToDislikesTab() {
        step("Переключаемся на вкладку 'Дизлайки'") {
            tabLayout.selectTab(1)
        }
    }

    fun checkDislikedCatsDisplayed() {
        step("Проверяем, что отображается список дизлайкнутых котиков") {
            ratingList.isDisplayed()
            ratingList.hasSize(4)
        }
    }

    fun clickFirstCat() {
        step("Кликаем на первого котика в рейтинге дизлайков") {
            ratingList.childAt<RatingItem>(0) {
                click()
            }
        }
    }

    fun selectCat(position: Int) {
        step("Выбираем кота номер $position в списке") {
            ratingList.childAt<RatingItem>(position - 1) {
                click()
            }
        }
    }

    private class RatingItem(matcher: Matcher<View>) : KRecyclerItem<RatingItem>(matcher) {
        val name = KTextView(matcher) { withId(R.id.cat_name) }
        val dislikes = KTextView(matcher) { withId(R.id.cat_dislikes) }
    }

    companion object {
        const val SCREEN_NAME = "Экран рейтинга котиков"

        inline operator fun invoke(testContext: TestContext<*>, crossinline block: RatingScreen.() -> Unit) {
            testContext.step(SCREEN_NAME) {
                RatingScreen(testContext).apply {
                    block()
                }
            }
        }
    }
}