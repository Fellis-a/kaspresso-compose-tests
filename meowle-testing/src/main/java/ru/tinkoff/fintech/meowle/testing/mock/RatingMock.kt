package ru.tinkoff.fintech.meowle.testing.mock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching

/**
 * Mock для рейтинга котиков.
 */
class RatingMock : Mock() {
    override val matcher: MappingBuilder = get(urlPathMatching(".*/rating"))
}