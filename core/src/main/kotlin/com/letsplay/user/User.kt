package com.letsplay.user

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
open class User {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    open var id: UUID? = null

    @Column(name = "name")
    open var name: String? = null

    @Column(name = "avatar_url")
    open var avatarUrl: String? = null

    @Column(name = "data", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    open var data: MutableMap<String, String> = mutableMapOf()

    @Enumerated
    @Column(name = "role")
    open var role: Role? = null
}
