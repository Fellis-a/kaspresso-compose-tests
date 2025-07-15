package ru.tinkoff.fintech.meowle.testing.mock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching

class LikesMock : Mock() {
    override val matcher: MappingBuilder = post(urlPathMatching(".*/likes/cats/.*/likes"))
}