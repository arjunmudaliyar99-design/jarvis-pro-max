package com.example.jarvspromax.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jarvspromax.command.Command
import com.example.jarvspromax.command.CommandRouter

// HUD palette, inspired by the look of a dark holographic interface.
private val HudBackground = Color(0xFF05080D)
private val HudCyan = Color(0xFF28E0FF)
private val HudCyanDim = Color(0x6628E0FF)
private val HudText = Color(0xFFBFF6FF)

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Systems online. Type a command.") }

    fun submit() {
        val result = when (val command = CommandRouter.parse(input)) {
            is Command.OpenApp -> "Opening ${command.appId}. (Launch action arrives in step 3.)"
            Command.Unknown -> "Command not recognised or not allowed."
        }
        status = result
        input = ""
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HudBackground)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "J.A.R.V.I.S.",
            color = HudCyan,
            fontFamily = FontFamily.Monospace,
            fontSize = 22.sp,
        )

        ArcReactorButton(
            onClick = { status = "Voice input arrives in step 5. Use text for now." },
            modifier = Modifier.size(220.dp),
        )

        Text(
            text = status,
            color = HudText,
            fontFamily = FontFamily.Monospace,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                singleLine = true,
                label = { Text("Command", color = HudCyanDim, fontFamily = FontFamily.Monospace) },
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = HudText,
                    fontFamily = FontFamily.Monospace,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { submit() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HudCyan,
                    unfocusedBorderColor = HudCyanDim,
                    cursorColor = HudCyan,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick = { submit() },
                colors = ButtonDefaults.buttonColors(containerColor = HudCyanDim),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            ) {
                Text("Send", color = HudBackground, fontFamily = FontFamily.Monospace)
            }
        }
    }
}

@Composable
private fun ArcReactorButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .clickable(onClick = onClick),
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension / 2f

        // Outer glow ring.
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(HudCyan.copy(alpha = 0.35f), Color.Transparent),
                center = center,
                radius = radius,
            ),
            radius = radius,
            center = center,
        )
        // Outer ring and inner core.
        drawCircle(color = HudCyan, radius = radius * 0.92f, center = center, style = Stroke(width = 3.dp.toPx()))
        drawCircle(color = HudCyanDim, radius = radius * 0.7f, center = center, style = Stroke(width = 2.dp.toPx()))
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(HudText, HudCyan, Color.Transparent),
                center = center,
                radius = radius * 0.5f,
            ),
            radius = radius * 0.5f,
            center = center,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF05080D)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
