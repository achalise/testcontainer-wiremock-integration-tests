package dev.integrationtestdemo

import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@RestController
class DemoController(private val webClient: WebClient, private val customerRepository: CustomerRepository) {
    @GetMapping("/customer/{id}")
    fun getCustomer(@PathVariable id: Long): Mono<Customer> {
       val customer = customerRepository.findById(id)
       return Mono.just(customer.get())
    }

    @PostMapping("/customer")
    fun createCustomer(@RequestBody customerRecord: CustomerRecord): Mono<Customer> {
        return webClient.get().uri("/photos/1").retrieve().bodyToMono(Photo::class.java).flatMap {
            val customer = customerRepository.save(Customer(firstName = customerRecord.firstName,
                lastName = customerRecord.lastName,
                profileImageUrl = it.url))
            Mono.just(customer)
        }
    }
}

data class CustomerRecord(var firstName: String, var lastName: String, var applicationStatus: String, var imageUrl: String)
data class Photo(var albumId: Int, var id: Int, var title: String,
                 var url: String, var thumbnailUrl: String);