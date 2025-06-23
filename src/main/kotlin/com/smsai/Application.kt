package com.smsai

import com.smsai.controller.webhookRoutes
import com.smsai.service.ClaudeService
import com.smsai.service.SmsService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.jackson.*
import io.ktor.server.plugins.calllogging.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main() {
    val smsService = SmsService()
    smsService.init()
    
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting(ClaudeService(), smsService)
        configureSerialization()
        configureLogging()
        configureStatusPages()
    }.start(wait = true)
}

fun Application.configureRouting(claudeService: ClaudeService, smsService: SmsService) {
    routing {
        webhookRoutes(claudeService, smsService)
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson()
    }
}

fun Application.configureLogging() {
    install(CallLogging)
}

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            logger.error(cause) { "Unhandled exception" }
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
        }
    }
}