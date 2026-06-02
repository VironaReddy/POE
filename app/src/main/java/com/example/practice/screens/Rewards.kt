package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class RewardBadge(
    val title: String,
    val description: String,
    val requiredDays: Int? = null,
    val requiredSavings: Double? = null
)

@Composable
fun Rewards(navController: NavController) {

    //Hardcoded Ron's Bear
    val userLoginStreak = 12
    val userSavings = 6500.0

    //For login streak
    val rewardBadges = listOf(
        RewardBadge(
            title = "7 Day Streak",
            description = "Log in for 7 consecutive days",
            requiredDays = 7
        ),
        RewardBadge(
            title = "30 Day Streak",
            description = "Log in for 30 consecutive days",
            requiredDays = 30
        ),

        RewardBadge(
            title = "Penny Wise",
            description = "Log in for 60 days",
            requiredDays = 60
        ),

        //For money saved
        RewardBadge(
            title = "Small Moves First",
            description = "Save R500",
            requiredSavings = 500.0
        ),
        RewardBadge(
            title = "Now we're Talking",
            description = "Save R5000",
            requiredSavings = 5000.0
        ),
        RewardBadge(
            title = "High Roller",
            description = "Save R10000",
            requiredSavings = 10000.0
        )
    )

    val earnedCount = rewardBadges.count {
        hasEarnedBadge(
            badge = it,
            loginStreak = userLoginStreak,
            totalSaved = userSavings
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = earnedCount.toString(),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Badges Earned",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(rewardBadges) { badge ->

                val earned = hasEarnedBadge(
                    badge = badge,
                    loginStreak = userLoginStreak,
                    totalSaved = userSavings
                )

                BadgeCard(
                    badge = badge,
                    earned = earned
                )
            }
        }
    }
}

fun hasEarnedBadge(
    badge: RewardBadge,
    loginStreak: Int,
    totalSaved: Double
): Boolean {

    badge.requiredDays?.let {
        if (loginStreak >= it) return true
    }

    badge.requiredSavings?.let {
        if (totalSaved >= it) return true
    }

    return false
}

@Composable
fun BadgeCard(
    badge: RewardBadge,
    earned: Boolean
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                if (earned)
                    Color(0xFFFFF1F5)
                else
                    Color.LightGray.copy(alpha = 0.3f)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    if (earned)
                        Icons.Default.Face
                    else
                        Icons.Default.Lock,
                contentDescription = null,
                tint =
                    if (earned)
                        Color(0xFFFFB300)
                    else
                        Color.Gray,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text =
                        if (earned) "Unlocked"
                        else "Locked",
                    color =
                        if (earned) Color.Green
                        else Color.Red
                )
            }
        }
    }
}