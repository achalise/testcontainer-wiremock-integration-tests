package dev.integrationtestdemo

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Entity
@Table(name = "customer")
open class Customer (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    open var id: Long? = null,
    open var firstName: String? = null,
    open var lastName: String? = null,
    open var profileImageUrl: String? = null
)

@Repository
interface CustomerRepository: CrudRepository<Customer, Long>