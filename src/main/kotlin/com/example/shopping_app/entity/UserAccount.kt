package com.example.shopping_app.entity

import com.example.shopping_app.entity.enums.Role
import jakarta.persistence.*

@Entity
@Table(name="user_account")
data class UserAccount(
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable=false, unique=true, length=255)
    var email: String,

    @Column(nullable=false, length=255)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    var role: Role,

    @OneToOne(mappedBy = "user", fetch=FetchType.LAZY, cascade = [CascadeType.ALL])
    var profile: UserProfile? = null,

    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY, cascade = [CascadeType.ALL])
    var addresses: MutableList<UserAddress> = mutableListOf()
): Auditor() {
    override fun toString() = "UserAccount(id=$id, email=$email)"
    override fun equals(other: Any?) = other is UserAccount && other.id == id
    override fun hashCode() = id.hashCode()
}