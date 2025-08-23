package com.leonardlau.gymmonitor.gymmonitorlite

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class GymmonitorLiteApplication

fun main(args: Array<String>) {
	runApplication<GymmonitorLiteApplication>(*args)
}
