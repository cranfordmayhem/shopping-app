package com.example.shopping_app.entity

import jakarta.persistence.*

@Entity
@Table(name="user_address")
data class UserAddress(
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable=false, length=255)
    var city: String,

    @Column(nullable=false, length=255)
    var state: String,

    @Column(nullable=false, length=20)
    var zipCode: String,

    @Column(nullable=false, length=255)
    var country: String,

    @Column(nullable=false, length=20)
    var contactNumber: String,

    @Column(name="is_default", nullable=false)
    var isDefault: Boolean = true,

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_account_id", nullable=false)
    var user: UserAccount
) : Auditor()