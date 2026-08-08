package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessageEntity
import com.example.ui.theme.TrustLensOnBackground
import com.example.ui.theme.TrustLensOnPrimaryContainer
import com.example.ui.theme.TrustLensOnSurfaceVariant
import com.example.ui.theme.TrustLensOnTertiaryContainer
import com.example.ui.theme.TrustLensOutlineVariant
import com.example.ui.theme.TrustLensPrimary
import com.example.ui.theme.TrustLensPrimaryContainer
import com.example.ui.theme.TrustLensSecondaryContainer
import com.example.ui.theme.TrustLensTertiaryContainer

@Composable
fun ChatScreen(
    chatMessages: List<ChatMessageEntity>,
    inputText: String,
    onInputChanged: (String) -> Unit,
    onSendMessage: (String?) -> Unit,
    onChatActionClick: (String?) -> Unit,
    isThinking: Boolean,
    isRecording: Boolean,
    onToggleRecording: () -> Unit,
    currentLanguage: String,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val suggestedQueries = listOf(
        "How do I submit compliance docs?",
        "Why is my risk score 78?",
        "How to verify CNIC with NADRA?",
        "ٹیکس دستاویزات کیسے جمع کروائیں؟",
        "Secondary income proof requirements"
    )

    LaunchedEffect(chatMessages.size, isThinking) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Context Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Talk to TrustLens AI",
                style = MaterialTheme.typography.displaySmall.copy(
                    color = TrustLensOnBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = if (currentLanguage == "UR") "ٹرسٹ لینس اے آئی سے بات کریں | Ask any compliance question"
                else "Ask any compliance question | ٹرسٹ لینس اے آئی سے بات کریں",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TrustLensOnSurfaceVariant,
                    fontSize = 12.sp
                ),
                textAlign = TextAlign.Center
            )
        }

        // Suggested Chips (Pills in Lilac Container)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(suggestedQueries) { query ->
                FilterChip(
                    selected = false,
                    onClick = { onSendMessage(query) },
                    label = {
                        Text(
                            text = query,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = TrustLensSecondaryContainer,
                        labelColor = TrustLensOnPrimaryContainer
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = false,
                        borderColor = TrustLensOutlineVariant
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        // Chat Message History
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chatMessages) { message ->
                if (message.isUser) {
                    UserChatBubble(text = message.text)
                } else {
                    AiChatBubble(
                        message = message,
                        onActionClick = { onChatActionClick(message.actionType) }
                    )
                }
            }

            if (isThinking) {
                item {
                    AiThinkingBubble()
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Recording Audio Wave Indicator Banner
        AnimatedVisibility(visible = isRecording) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
                    .clip(RoundedCornerShape(16.dp)),
                color = TrustLensTertiaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            color = TrustLensOnTertiaryContainer,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Listening in Urdu / English...",
                            color = TrustLensOnTertiaryContainer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Tap mic to stop",
                        color = TrustLensOnTertiaryContainer.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Interactive Input Area (Sticky Bottom Dock, Professional Polish Pill)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(28.dp)),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mic Button
                IconButton(
                    onClick = onToggleRecording,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (isRecording) TrustLensTertiaryContainer else TrustLensSecondaryContainer)
                        .testTag("voice_input_mic_button")
                ) {
                    Icon(
                        imageVector = if (isRecording) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Voice Input",
                        tint = if (isRecording) TrustLensOnTertiaryContainer else TrustLensOnPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Text Input
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputChanged,
                    placeholder = {
                        Text(
                            text = if (currentLanguage == "UR") "ٹائپ کریں یا بولیں..." else "Type or speak...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TrustLensOnSurfaceVariant)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_text_input_field"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { onSendMessage(null) })
                )

                // Send Button in Primary container
                IconButton(
                    onClick = { onSendMessage(null) },
                    enabled = inputText.isNotBlank(),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (inputText.isNotBlank()) TrustLensPrimary else TrustLensSecondaryContainer)
                        .testTag("send_chat_message_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Message",
                        tint = if (inputText.isNotBlank()) Color.White else TrustLensOnSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun UserChatBubble(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 4.dp, bottomStart = 20.dp, bottomEnd = 20.dp)),
            color = TrustLensPrimaryContainer,
            shadowElevation = 1.dp
        ) {
            Text(
                text = text,
                color = TrustLensOnPrimaryContainer,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 21.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
private fun AiChatBubble(
    message: ChatMessageEntity,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 20.dp))
                .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(topStart = 4.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 20.dp)),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                // Corner psychology AI Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(TrustLensSecondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "AI",
                        tint = TrustLensOnPrimaryContainer,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = message.text,
                        color = TrustLensOnBackground,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 21.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.padding(end = 26.dp)
                    )

                    if (!message.textUrdu.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = message.textUrdu,
                            color = TrustLensOnSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Embedded Action button inside AI response bubble
                    if (message.actionButtonLabel != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(12.dp))
                                .clickable(onClick = onActionClick)
                                .testTag("ai_bubble_action_button"),
                            color = TrustLensSecondaryContainer,
                            shadowElevation = 1.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = TrustLensOnPrimaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = message.actionButtonLabel,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = TrustLensOnPrimaryContainer,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AiThinkingBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(18.dp)),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    color = TrustLensPrimary,
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Analyzing regulatory compliance...",
                    color = TrustLensOnBackground,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

