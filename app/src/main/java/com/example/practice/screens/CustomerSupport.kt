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
fun CustomerSupport (navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Customer Support",
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
                    text = "Contact Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Call or WhatsApp us at:",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "062 672 4200",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Alternatively, you can email at:",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Email:",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "SmartSpender@gmail.com",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )


            }
        }
    }
}
