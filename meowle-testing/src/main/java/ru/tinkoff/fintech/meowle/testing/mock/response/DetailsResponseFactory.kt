package ru.tinkoff.fintech.meowle.testing.mock.response

import ru.tinkoff.fintech.meowle.data.api.dto.CatDto
import ru.tinkoff.fintech.meowle.data.api.dto.response.CatByIdResponseDto

/**
 * @author Ruslan Ganeev
 */
object DetailsResponseFactory {

    fun catDetails(): CatByIdResponseDto {
        return CatByIdResponseDto(
            cat = CatDto(
                id = 1,
                name = "Барсик",
                description = "Рыжий кот",
                tags = "",
                gender = "male",
                likes = 32,
                dislikes = 2
            )
        )
    }

    fun success(description: String = "Серый кот с зелёными глазами"): CatByIdResponseDto {
        return CatByIdResponseDto(
            cat = CatDto(
                id = 5,
                name = "Мурзик",
                description = description,
                tags = "",
                gender = "male",
                likes = 15,
                dislikes = 1
            )
        )
    }
}
