package dev.integrationtestdemo

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class Config {

    @Value("\${photo.server.url}")
    private var photoServerUrl: String = ""

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder().baseUrl(photoServerUrl).build()
    }
}