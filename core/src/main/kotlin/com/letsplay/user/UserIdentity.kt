package com.letsplay.user

import javax.persistence.*

@Entity
@Table(name = "user_identity")
open class UserIdentity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "provider")
    open var provider: String? = null

    @Column(name = "identity")
    open var identity: String? = null

    @Column(name = "credential")
    open var credential: String? = null

    @ManyToOne
    @JoinColumn(name = "user_id")
    open var user: User? = null
}