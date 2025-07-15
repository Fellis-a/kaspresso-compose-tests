package ru.tinkoff.fintech.meowle.testing.mock.response

import ru.tinkoff.fintech.meowle.data.api.dto.CatDto
import ru.tinkoff.fintech.meowle.data.api.dto.response.LetterGroup
import ru.tinkoff.fintech.meowle.data.api.dto.response.SearchResponseDto

/**
 * @author Ruslan Ganeev
 */
object SearchResponseFactory {

    fun cats(): SearchResponseDto {
        return SearchResponseDto(
            listOf(
                LetterGroup(
                    title = "Б",
                    cats = listOf(
                        CatDto(
                            id = 1,
                            name = "Барсик",
                            description = "Рыжий кот",
                            tags = "",
                            gender = "male",
                            likes = 32,
                            dislikes = 2
                        ),
                        CatDto(
                            id = 2,
                            name = "Борис",
                            description = "Белый пушистый кот",
                            tags = "",
                            gender = "male",
                            likes = 1,
                            dislikes = 2
                        ),
                        CatDto(
                            id = 3,
                            name = "Бегемот",
                            description = "Рыжий пушистый кот",
                            tags = "",
                            gender = "male",
                            likes = 1,
                            dislikes = 2
                        ),
                        CatDto(
                            id = 4,
                            name = "Бобик",
                            description = "Черный пухлый кот",
                            tags = "",
                            gender = "male",
                            likes = 32,
                            dislikes = 2
                        ),

                    ),
                    count = 4
                )
            )
        )
    }
}
