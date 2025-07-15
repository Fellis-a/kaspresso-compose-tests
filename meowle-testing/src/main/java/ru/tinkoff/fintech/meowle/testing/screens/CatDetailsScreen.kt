package ru.tinkoff.fintech.meowle.testing.screens

import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import ru.tinkoff.fintech.meowle.R
import ru.tinkoff.fintech.meowle.presentation.view.fragments.DetailsFragment

/**
 * @author Ruslan Ganeev
 */
class CatDetailsScreen(testContext: TestContext<*>) : BaseScreen(testContext) {
    override val layoutId: Int = R.layout.details_fragment_redesign
    override val viewClass: Class<*> = DetailsFragment::class.java

    private val name = KTextView { withId(R.id.cat_name) }
    private val likeButton = KButton { withId(R.id.ib_like) }
    private val description = KTextView { withId(R.id.cat_description) }
    private val editButton = KButton { withId(R.id.btn_edit) }
    private val descField = KEditText { withId(R.id.til_desc) }
    private val confirmBtn = KButton { withId(R.id.confirm_button) }

    fun checkCatName(catName: String) {
        step("Проверяем имя котика") {
            name.hasText(catName)
        }
    }

    fun checkCatDescription(catDescription: String) {
        step("Проверяем описание котика") {
            description.hasText(catDescription)
        }
    }

    fun checkScreenOpened() {
        step("Проверяем, что экран деталей кота открыт") {
            name.isDisplayed()
        }
    }

    fun likeCat() {
        step("Ставим лайк котику") {
            likeButton.click()
        }
    }

    fun clickEditButton() {
        step("Нажимаем кнопку редактирования") {
            editButton.click()
        }
    }

    fun changeDescription(newDescription: String) {
        step("Меняем описание кота") {
            Thread.sleep(1000)

            androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withId(R.id.til_desc))
                .perform(androidx.test.espresso.action.ViewActions.click())

            androidx.test.espresso.Espresso.onView(
                org.hamcrest.Matchers.allOf(
                    androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom(com.google.android.material.textfield.TextInputEditText::class.java),
                    androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA(androidx.test.espresso.matcher.ViewMatchers.withId(R.id.til_desc))
                )
            ).perform(
                androidx.test.espresso.action.ViewActions.replaceText(newDescription),
                androidx.test.espresso.action.ViewActions.closeSoftKeyboard()
            )
        }
    }

    fun clickSaveButton() {
        step("Нажимаем кнопку сохранения") {
            confirmBtn.click()
        }
    }

    fun checkDescription(expectedDescription: String) {
        step("Проверяем, что описание изменилось") {
            description.hasText(expectedDescription)
        }
    }

    companion object {
        const val SCREEN_NAME = "Экран деталей котика"

        inline operator fun invoke(testContext: TestContext<*>, crossinline block: CatDetailsScreen.() -> Unit) {
            testContext.step(SCREEN_NAME) {
                CatDetailsScreen(testContext).apply {
                    block()
                }
            }
        }
    }
}
