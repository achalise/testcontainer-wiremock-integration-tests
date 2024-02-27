package dev.integrationtestdemo

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.core.publisher.Mono


@SpringBootTest
@AutoConfigureWebTestClient(timeout = "10000")
@AutoConfigureWireMock(stubs = ["classpath:/mappings/stub.json"])
@Testcontainers
class DemoControllerTest( @Autowired val client: WebTestClient, @Autowired val customerRepository: CustomerRepository) {
    companion object {
        private val db = PostgreSQLContainer("postgres")

        @JvmStatic
        @BeforeAll
        fun startDBContainer() {
            db.start()
        }

        @JvmStatic
        @AfterAll
        fun stopDBContainer() {
            db.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerDBContainer(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", db::getJdbcUrl)
            registry.add("spring.datasource.username", db::getUsername)
            registry.add("spring.datasource.password", db::getPassword)
        }
    }

    @LocalServerPort
    private val port: Int? = null

    @Test
    fun `test creating a customer is successful with image retrieved from external api`() {
        val customerRecord = CustomerRecord("testUser", "testLastName", "", "" )
        client.post().uri("/customer")
            .body(Mono.just(customerRecord), CustomerRecord::class.java)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.profileImageUrl")
            .isNotEmpty
        val customers = customerRepository.findAll()
        assertTrue(customers.count() > 0)
    }
}