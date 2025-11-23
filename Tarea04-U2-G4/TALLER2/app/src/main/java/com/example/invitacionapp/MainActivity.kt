package com.example.invitacionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                ChristmasAppContent()
            }
        }
    }
}

@Composable
fun ChristmasAppContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.navidad),
            contentDescription = "Imagen de Navidad",
            modifier = Modifier
                .size(280.dp)
                .padding(bottom = 30.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfoRow(icon = Icons.Default.CalendarToday, text = "Lunes 25 de diciembre del 2025")
            Spacer(modifier = Modifier.height(15.dp))

            InfoRow(icon = Icons.Default.LocationOn, text = "Universidad Central Del Ecuador")
            Spacer(modifier = Modifier.height(15.dp))

            InfoRow(icon = Icons.Default.Email, text = "christmas@mail.com")
            Spacer(modifier = Modifier.height(15.dp))

            InfoRow(icon = Icons.Default.Phone, text = "09234567812")
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Compartir en:",
                fontSize = 20.sp, // ✅ TEXTO MÁS GRANDE
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                SocialIcon(icon = Icons.Default.Share, description = "Compartir", color = Color(0xFF666666))

                SocialIconCustom(
                    painter = painterResource(id = R.drawable.facebook),
                    description = "Facebook",
                    color = Color(0xFF1877F2)
                )

                SocialIconCustom(
                    painter = painterResource(id = R.drawable.twitter),
                    description = "Twitter",
                    color = Color(0xFF1DA1F2)
                )

                SocialIconCustom(
                    painter = painterResource(id = R.drawable.whatsapp),
                    description = "WhatsApp",
                    color = Color(0xFF25D366)
                )
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF2E7D32),
            modifier = Modifier
                .size(45.dp)
                .padding(end = 16.dp)
        )
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color(0xFF333333),
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun SocialIcon(icon: ImageVector, description: String, color: Color) {
    Icon(
        imageVector = icon,
        contentDescription = description,
        tint = color,
        modifier = Modifier.size(42.dp)
    )
}

@Composable
fun SocialIconCustom(
    painter: androidx.compose.ui.graphics.painter.Painter,
    description: String,
    color: Color
) {
    Icon(
        painter = painter,
        contentDescription = description,
        tint = color,
        modifier = Modifier.size(42.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChristmasAppContent()
}