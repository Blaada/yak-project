package com.project.yak

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@EntityScan
@SpringBootApplication
class YakProjectApplication

fun main(args: Array<String>) {
	runApplication<YakProjectApplication>(*args)
}
