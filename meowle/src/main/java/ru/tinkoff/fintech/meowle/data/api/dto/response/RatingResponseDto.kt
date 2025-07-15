package ru.tinkoff.fintech.meowle.data.api.dto.response

import ru.tinkoff.fintech.meowle.data.api.dto.CatDto
import kotlinx.serialization.Serializable


/**
 * @author Ruslan Ganeev
 */
@Serializable
data class RatingResponseDto(
    val likes: List<CatDto>,
    val dislikes: List<CatDto>
)
