package com.niooon.browser.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niooon.browser.ui.theme.GoogleBlue
import com.niooon.browser.ui.theme.GoogleGreen
import com.niooon.browser.ui.theme.GoogleRed
import com.niooon.browser.ui.theme.GoogleYellow

@Composable
fun GoogleBrandHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val fontSize = 42.sp
        val fontWeight = FontWeight.SemiBold

        Text(text = "G", color = GoogleBlue, fontSize = fontSize, fontWeight = fontWeight, fontFamily = FontFamily.SansSerif)
        Text(text = "o", color = GoogleRed, fontSize = fontSize, fontWeight = fontWeight, fontFamily = FontFamily.SansSerif)
        Text(text = "o", color = GoogleYellow, fontSize = fontSize, fontWeight = fontWeight, fontFamily = FontFamily.SansSerif)
        Text(text = "g", color = GoogleBlue, fontSize = fontSize, fontWeight = fontWeight, fontFamily = FontFamily.SansSerif)
        Text(text = "l", color = GoogleGreen, fontSize = fontSize, fontWeight = fontWeight, fontFamily = FontFamily.SansSerif)
        Text(text = "e", color = GoogleRed, fontSize = fontSize, fontWeight = fontWeight, fontFamily = FontFamily.SansSerif)
    }
}
