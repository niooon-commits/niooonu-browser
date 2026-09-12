package com.niooon.browser.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Custom Canvas painter rendering 3D liquid waves, translucent refractive curves,
 * and specular glass water droplets matching the screenshot aesthetic.
 */
@Composable
fun LiquidGlassBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // 1. Base Liquid Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFE1F5FE),
                    Color(0xFFB3E5FC),
                    Color(0xFF81D4FA),
                    Color(0xFFB3E5FC),
                    Color(0xFFE0F7FA),
                    Color(0xFFFFFFFF)
                )
            ),
            size = size
        )

        // 2. Large Organic Liquid Wave Curves (Left & Top Right)
        val wavePath1 = Path().apply {
            moveTo(0f, height * 0.1f)
            cubicTo(
                width * 0.4f, height * 0.05f,
                width * 0.2f, height * 0.35f,
                0f, height * 0.45f
            )
            close()
        }
        drawPath(
            path = wavePath1,
            brush = Brush.radialGradient(
                colors = listOf(Color(0x66FFFFFF), Color(0x1A81D4FA), Color.Transparent),
                center = Offset(width * 0.1f, height * 0.25f),
                radius = width * 0.4f
            )
        )

        val wavePath2 = Path().apply {
            moveTo(width, height * 0.2f)
            cubicTo(
                width * 0.6f, height * 0.3f,
                width * 0.7f, height * 0.6f,
                width, height * 0.7f
            )
            close()
        }
        drawPath(
            path = wavePath2,
            brush = Brush.radialGradient(
                colors = listOf(Color(0x66FFFFFF), Color(0x224FC3F7), Color.Transparent),
                center = Offset(width * 0.85f, height * 0.45f),
                radius = width * 0.5f
            )
        )

        // 3. Floating 3D Specular Glass Droplets / Bubbles
        fun drawGlassBubble(center: Offset, radius: Float) {
            // Shadow behind bubble
            drawCircle(
                color = Color(0x1F0288D1),
                radius = radius,
                center = center + Offset(radius * 0.1f, radius * 0.15f)
            )
            // Bubble base body
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x33FFFFFF),
                        Color(0x2281D4FA),
                        Color(0x664FC3F7),
                        Color(0x990288D1)
                    ),
                    center = center + Offset(-radius * 0.2f, -radius * 0.2f),
                    radius = radius
                ),
                radius = radius,
                center = center
            )
            // Rim highlight
            drawCircle(
                color = Color(0xB3FFFFFF),
                radius = radius,
                center = center,
                style = Stroke(width = radius * 0.08f)
            )
            // Specular Reflection glint
            drawCircle(
                color = Color.White,
                radius = radius * 0.25f,
                center = center - Offset(radius * 0.35f, radius * 0.35f)
            )
            // Secondary small bounce light
            drawCircle(
                color = Color(0x99FFFFFF),
                radius = radius * 0.12f,
                center = center + Offset(radius * 0.3f, radius * 0.3f)
            )
        }

        // Bubbles positioned around borders like the design screenshot
        drawGlassBubble(Offset(width * 0.08f, height * 0.28f), width * 0.065f)
        drawGlassBubble(Offset(width * 0.05f, height * 0.52f), width * 0.085f)
        drawGlassBubble(Offset(width * 0.92f, height * 0.12f), width * 0.055f)
        drawGlassBubble(Offset(width * 0.94f, height * 0.32f), width * 0.095f)
        drawGlassBubble(Offset(width * 0.9f, height * 0.88f), width * 0.07f)
    }
}
