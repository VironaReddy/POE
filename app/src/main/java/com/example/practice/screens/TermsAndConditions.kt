package com.example.practice.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TermsAndConditions (navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Terms and Conditions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Last Updated: May 2026",
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,

            )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "1. Acceptance of Terms",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "By creating an account or using the app," +
                            " you acknowledge that you have read, understood, " +
                            "and agreed to these Terms and Conditions.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "2. User Accounts",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Users are responsible for maintaining the confidentiality " +
                            "of their account credentials and for all activities that occur" +
                            " under their account.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )


                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "3. Privacy",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Your use of the app is also governed by our Privacy Policy. " +
                            "We take measures to protect your personal information " +
                            "and ensure it remains confidential.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "4. Termination",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "We reserve the right to suspend or terminate access to the" +
                            " app if a user violates these Terms and Conditions.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "5. Contact us",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "If you have any questions regarding these Terms and Conditions, " +
                            "please contact us through the support section of the app.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}