package com.smsai.service

import com.anthropic.client.AnthropicClient
import com.anthropic.client.okhttp.AnthropicOkHttpClient
import com.anthropic.models.messages.MessageCreateParams
import com.anthropic.models.messages.Model
import kotlinx.coroutines.future.await
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class ClaudeService {
    // Configures using the `CLAUDE_API_KEY`, environment variable
    private val client: AnthropicClient = AnthropicOkHttpClient.fromEnv()

    suspend fun processMessage(message: String): String {
        logger.info { "Processing message with Claude: $message" }

        val params = MessageCreateParams.builder()
            .maxTokens(1024L)
            .addUserMessage(message)
            .addUserMessage("Please reply with a short, concise message under 140 characters long.")
            .model(Model.CLAUDE_3_7_SONNET_LATEST)
            .build()

        val messageResponse = client.async().messages().create(params).await()

        return messageResponse.content().map {
            it.asText().text()
        }.joinToString("")
    }
}