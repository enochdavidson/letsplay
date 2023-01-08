package com.letsplay.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Controller

@EnableJpaRepositories("com.letsplay")
@EntityScan("com.letsplay")
@Controller
class DatabaseConfig {
}