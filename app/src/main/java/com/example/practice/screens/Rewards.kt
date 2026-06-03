package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.practice.ui.components.ScreenWrapper


data class RewardBadge(
    val title: String,
    val icon: String,
    val description: String,
    val isEarned: Boolean
)

@Composable
fun Rewards(navController: NavController) {

    // Mock data - In a real app, these would come from a database or ViewModel
    // Using some sample values to demonstrate the earned/locked states
    val userSavings = 6500.0
    val userLoginStreak = 12
    val goalsCompleted = 1
    val monthsWithinBudget = 1
    val noSpendDays = 3
    val savingsGrowthMonths = 2

    val rewardBadges = listOf(
        RewardBadge("First Saver", "🥉", "Saved your first R500.", userSavings >= 500),
        RewardBadge("Smart Saver", "🥈", "Saved a total of R2 500.", userSavings >= 2500),
        RewardBadge("Super Saver", "🥇", "Saved a total of R5 000.", userSavings >= 5000),
        RewardBadge("Savings Master", "💎", "Saved a total of R10 000.", userSavings >= 10000),
        RewardBadge("7-Day Streak", "🔥", "Logged in for 7 consecutive days.", userLoginStreak >= 7),
        RewardBadge("30-Day Streak", "⭐", "Logged in for 30 consecutive days.", userLoginStreak >= 30),
        RewardBadge("Goal Achiever", "📈", "Successfully completed a savings goal.", goalsCompleted >= 1),
        RewardBadge("Budget Champion", "🎯", "Stayed within budget for an entire month.", monthsWithinBudget >= 1),
        RewardBadge("No-Spend Hero", "💰", "Completed 5 days without unnecessary spending.", noSpendDays >= 5),
        RewardBadge("Financial Guru", "🚀", "Maintained savings growth for 6 consecutive months.", savingsGrowthMonths >= 6)
    )

    val earnedCount = rewardBadges.count { it.isEarned }

    ScreenWrapper(
        title = "Rewards",
        navController = navController
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Badges earned",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$earnedCount / ${rewardBadges.size} unlocked",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(rewardBadges) { badge ->
                    BadgeCard(badge = badge)
                }
            }
        }
    }
}

@Composable
fun BadgeCard(badge: RewardBadge) {
    val contentColor = if (badge.isEarned) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray
    val containerColor = if (badge.isEarned) 
        MaterialTheme.colorScheme.surfaceVariant 
    else 
        Color.LightGray.copy(alpha = 0.3f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(56.dp)
            ) {
                // Icon / Emoji
                Text(
                    text = badge.icon,
                    fontSize = 36.sp,
                    modifier = Modifier.graphicsLayer(alpha = if (badge.isEarned) 1f else 0.3f)
                )
                
                // Grey lock if not earned
                if (!badge.isEarned) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor
                )
            }
        }
    }
}
