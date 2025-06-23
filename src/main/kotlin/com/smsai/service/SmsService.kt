package com.smsai.service

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class SmsService {
    private val accountSid = System.getenv("TWILIO_ACCOUNT_SID")
    private val authToken = System.getenv("TWILIO_AUTH_TOKEN")
    private val twilioPhoneNumber = System.getenv("TWILIO_PHONE_NUMBER")

    fun init() {
        Twilio.init(accountSid, authToken)
        logger.info { "Twilio initialized with account SID: $accountSid" }
    }

    fun sendSms(toPhoneNumber: String, messageBody: String) {
        try {
            logger.info { "Sending SMS to $toPhoneNumber: $messageBody" }
            
            val message = Message.creator(
                PhoneNumber(toPhoneNumber),
                PhoneNumber(twilioPhoneNumber),
                messageBody
            ).create()

            logger.info { "SMS sent successfully. Message SID: ${message.sid}" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to send SMS to $toPhoneNumber" }
            throw e
        }
    }
}