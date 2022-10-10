package com.example

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ExampleConfig {

    @Bean
    fun userDetailsService(passwordEncoder: PasswordEncoder): MapReactiveUserDetailsService {

        val admin: UserDetails = User
            .withUsername("admin")
            .password(passwordEncoder.encode("pass"))
            .roles("ADMIN")
            .build()

        val john: UserDetails = User
            .withUsername("john")
            .password(passwordEncoder.encode("pass"))
            .roles("USER")
            .build()

        val ann: UserDetails = User
            .withUsername("ann")
            .password(passwordEncoder.encode("pass"))
            .roles("USER")
            .build()

        return MapReactiveUserDetailsService(admin, john, ann)
    }

}