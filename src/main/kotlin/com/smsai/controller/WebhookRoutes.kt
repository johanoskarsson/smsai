package com.smsai.controller

import com.smsai.service.ClaudeService
import com.smsai.service.SmsService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun Route.webhookRoutes(claudeService: ClaudeService, smsService: SmsService) {
    route("/webhook") {
        post("/sms") {
            try {
                val parameters = call.receiveParameters()
                val from = parameters["From"] ?: throw IllegalArgumentException("Missing 'From' parameter")
                val to = parameters["To"] ?: throw IllegalArgumentException("Missing 'To' parameter")
                val body = parameters["Body"] ?: throw IllegalArgumentException("Missing 'Body' parameter")
                val messageSid = parameters["MessageSid"] ?: throw IllegalArgumentException("Missing 'MessageSid' parameter")
                
                logger.info { "Received SMS from $from to $to: $body" }
                
                val claudeResponse = claudeService.processMessage(body)
                logger.info { "Claude response: $claudeResponse" }
                
                smsService.sendSms(from, claudeResponse)
                logger.info { "SMS response sent to $from" }
                
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                logger.error(e) { "Error processing SMS webhook" }
                
                try {
                    val parameters = call.receiveParameters()
                    val from = parameters["From"]
                    if (from != null) {
                        smsService.sendSms(from, "Sorry, I encountered an error processing your message. Please try again later.")
                    }
                } catch (sendError: Exception) {
                    logger.error(sendError) { "Failed to send error message" }
                }
                
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
        
        get("/health") {
            call.respond(mapOf("status" to "healthy", "timestamp" to System.currentTimeMillis().toString()))
        }
    }
}