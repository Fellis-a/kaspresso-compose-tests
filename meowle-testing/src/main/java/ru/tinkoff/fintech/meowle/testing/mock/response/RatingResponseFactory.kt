package ru.tinkoff.fintech.meowle.testing.mock.response

import ru.tinkoff.fintech.meowle.data.api.dto.CatDto
import ru.tinkoff.fintech.meowle.data.api.dto.response.RatingResponseDto

object RatingResponseFactory {

    fun dislikedCats(): RatingResponseDto {
        return RatingResponseDto(
            likes = emptyList(),
            dislikes = listOf(
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
                    name = "Пушок",
                    description = "Белый пушистый кот",
                    tags = "",
                    gender = "male",
                    likes = 1,
                    dislikes = 2
                ),
                CatDto(
                    id = 3,
                    name = "Пушистик",
                    description = "Рыжий пушистый кот",
                    tags = "",
                    gender = "male",
                    likes = 1,
                    dislikes = 2
                ),
                CatDto(
                    id = 4,
                    name = "Пухляш",
                    description = "Черный пухлый кот",
                    tags = "",
                    gender = "male",
                    likes = 32,
                    dislikes = 2
                ),
            )
        )
    }

    fun success(): RatingResponseDto {
        return RatingResponseDto(
            likes = listOf(
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
                    name = "Пушок",
                    description = "Белый пушистый кот",
                    tags = "",
                    gender = "male",
                    likes = 1,
                    dislikes = 2
                ),
                CatDto(
                    id = 3,
                    name = "Пушистик",
                    description = "Рыжий пушистый кот",
                    tags = "",
                    gender = "male",
                    likes = 1,
                    dislikes = 2
                ),
                CatDto(
                    id = 4,
                    name = "Пухляш",
                    description = "Черный пухлый кот",
                    tags = "",
                    gender = "male",
                    likes = 32,
                    dislikes = 2
                ),
                CatDto(
                    id = 5,
                    name = "Мурзик",
                    description = "Серый кот с зелёными глазами",
                    tags = "",
                    gender = "male",
                    likes = 15,
                    dislikes = 1
                )
            ),
            dislikes = emptyList()
        )
    }
}