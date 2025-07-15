package ru.tinkoff.fintech.meowle.testing.screens

import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import org.hamcrest.Matchers.allOf
import ru.tinkoff.fintech.meowle.R

class AddCatScreen(testContext: TestContext<*>) : BaseScreen(testContext) {

    private val addTabBtn = KView { withId(R.id.tab_btn_add) }
    private val nameField = KEditText { withId(R.id.et_name) }
    private val descField = KEditText { withId(R.id.til_desc) }
    private val addButton = KButton { withId(R.id.confirm_button) }

    fun openRatingScreen() {
        step("Переходим на экран рейтинга через нижнюю навигацию") {
            addTabBtn.click()
        }
    }

    fun checkScreenOpened() {
        step("Проверяем, что экран рейтинга открыт") {
            nameField.isDisplayed()
        }
    }

    fun enterCatName(name: String) {
        step("Вводим имя кота: $name") {
            nameField.replaceText(name)
        }
    }

    fun enterCatGender(gender: String) {
        step("Выбираем пол кота: $gender") {
            androidx.test.espresso.Espresso.onView(
                allOf(
                    androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom(android.widget.AutoCompleteTextView::class.java),
                    isDescendantOfA(withId(R.id.til_gender))
                )
            ).perform(androidx.test.espresso.action.ViewActions.click())

            androidx.test.espresso.Espresso.onView(withText(gender))
                .inRoot(isPlatformPopup())
                .perform(androidx.test.espresso.action.ViewActions.click())
        }
    }

    fun enterCatDescription(desc: String) {
        step("Вводим описание кота: $desc") {
            descField.replaceText(desc)
        }
    }

    fun clickAddButton() {
        step("Нажимаем кнопку 'Добавить'") {
            addButton.click()
        }
    }

    companion object {
        const val SCREEN_NAME = "Экран добавления кота"

        inline operator fun invoke(testContext: TestContext<*>, crossinline block: AddCatScreen.() -> Unit) {
            testContext.step(SCREEN_NAME) {
                AddCatScreen(testContext).apply {
                    block()
                }
            }
        }
    }
}