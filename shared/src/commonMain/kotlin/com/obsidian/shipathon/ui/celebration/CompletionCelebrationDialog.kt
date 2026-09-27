package com.obsidian.shipathon.ui.celebration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.obsidian.shipathon.domain.model.Game
import com.obsidian.shipathon.ui.theme.GamingBackground
import com.obsidian.shipathon.ui.theme.GamingBlue
import com.obsidian.shipathon.ui.theme.GamingCard
import com.obsidian.shipathon.ui.theme.GamingGold
import com.obsidian.shipathon.ui.theme.GamingStat
import com.obsidian.shipathon.ui.theme.GamingTextPrimary
import com.obsidian.shipathon.ui.theme.GamingTextSecondary
import com.obsidian.shipathon.ui.theme.GlassBorderBrush
import com.obsidian.shipathon.ui.theme.GoldGradient
import com.obsidian.shipathon.ui.theme.ObsidianIcons

@Composable
fun CompletionCelebrationDialog(
    game: Game,
    playtimeHours: Double,
    onDismiss: () -> Unit,
    onRateGame: (Double) -> Unit = {},
    onShareTrophy: (Game) -> Unit = {},
) {
    var userRating by remember { mutableStateOf(5) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, Color(0x24FFFFFF), RoundedCornerShape(24.dp)),
            color = GamingBackground,
            shadowElevation = 24.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Trophy icon & celebration halo
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(GoldGradient),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = ObsidianIcons.Trophy,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = GamingTextPrimary,
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "GAME CONQUERED!",
                        style = MaterialTheme.typography.titleMedium,
                        color = GamingGold,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GamingBlue.copy(alpha = 0.15f))
                            .border(1.dp, GamingBlue.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 3.dp),
                    ) {
                        Text(
                            text = "+500 Gamer XP Gained",
                            style = MaterialTheme.typography.labelSmall,
                            color = GamingBlue,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                // Game Card preview
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = GamingCard),
                    border = BorderStroke(1.dp, Color(0x18FFFFFF)),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        AsyncImage(
                            model = game.coverUrl,
                            contentDescription = game.name,
                            modifier = Modifier
                                .size(width = 48.dp, height = 64.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                text = game.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = GamingTextPrimary,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = "Logged: ${playtimeHours.toInt()} hours completed",
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingTextSecondary,
                            )
                        }
                    }
                }

                // Star Rating Bar (1 to 5) with Vector Icons
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = "YOUR RATING",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                        color = GamingTextSecondary,
                        fontWeight = FontWeight.Bold,
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..5).forEach { star ->
                            val isSelected = star <= userRating
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { userRating = star }
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = if (isSelected) ObsidianIcons.Star else ObsidianIcons.StarOutline,
                                    contentDescription = "$star stars",
                                    modifier = Modifier.size(28.dp),
                                    tint = if (isSelected) GamingGold else GamingTextSecondary.copy(alpha = 0.5f),
                                )
                            }
                        }
                    }
                }

                // Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Button(
                        onClick = {
                            onRateGame(userRating.toDouble())
                            onShareTrophy(game)
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(
                                imageVector = ObsidianIcons.Link,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = GamingTextPrimary,
                            )
                            Text(
                                text = "Share Trophy Card",
                                color = GamingTextPrimary,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                    Button(
                        onClick = {
                            onRateGame(userRating.toDouble())
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GamingStat),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0x18FFFFFF)),
                        contentPadding = PaddingValues(vertical = 11.dp),
                    ) {
                        Text(
                            text = "Done",
                            color = GamingTextSecondary,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
        }
    }
}
