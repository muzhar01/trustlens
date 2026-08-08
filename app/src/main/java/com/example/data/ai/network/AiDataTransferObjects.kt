package com.example.data.ai.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Gemini API DTOs
@JsonClass(generateAdapter = true)
data class GeminiGenerateRequest(
    @field:Json(name = "contents") val contents: List<GeminiContent>
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @field:Json(name = "parts") val parts: List<GeminiPart>,
    @field:Json(name = "role") val role: String? = "user"
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @field:Json(name = "text") val text: String
)

@JsonClass(generateAdapter = true)
data class GeminiGenerateResponse(
    @field:Json(name = "candidates") val candidates: List<GeminiCandidate>? = null,
    @field:Json(name = "error") val error: GeminiError? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @field:Json(name = "content") val content: GeminiCandidateContent? = null,
    @field:Json(name = "finishReason") val finishReason: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidateContent(
    @field:Json(name = "parts") val parts: List<GeminiPart>? = null,
    @field:Json(name = "role") val role: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiError(
    @field:Json(name = "code") val code: Int? = null,
    @field:Json(name = "message") val message: String? = null,
    @field:Json(name = "status") val status: String? = null
)

// OpenCode.ai API DTOs (fallback endpoint: https://opencode.ai/zen/v1/chat/completions)
@JsonClass(generateAdapter = true)
data class OpenCodeChatRequest(
    @field:Json(name = "model") val model: String = "deepseek-v4-flash-free",
    @field:Json(name = "messages") val messages: List<OpenCodeChatMessage>,
    @field:Json(name = "temperature") val temperature: Double? = 0.7,
    @field:Json(name = "max_tokens") val maxTokens: Int? = 1000
)

@JsonClass(generateAdapter = true)
data class OpenCodeChatMessage(
    @field:Json(name = "role") val role: String,
    @field:Json(name = "content") val content: String
)

@JsonClass(generateAdapter = true)
data class OpenCodeChatResponse(
    @field:Json(name = "id") val id: String? = null,
    @field:Json(name = "choices") val choices: List<OpenCodeChoice>? = null,
    @field:Json(name = "error") val error: OpenCodeError? = null
)

@JsonClass(generateAdapter = true)
data class OpenCodeChoice(
    @field:Json(name = "index") val index: Int? = null,
    @field:Json(name = "message") val message: OpenCodeChatMessage? = null,
    @field:Json(name = "finish_reason") val finishReason: String? = null
)

@JsonClass(generateAdapter = true)
data class OpenCodeError(
    @field:Json(name = "message") val message: String? = null,
    @field:Json(name = "type") val type: String? = null,
    @field:Json(name = "code") val code: String? = null
)

