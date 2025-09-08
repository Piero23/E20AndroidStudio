package com.example.e20frontendmobile.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.e20frontendmobile.R
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme

@Composable
fun eventCard(str: String, str2: String, navController: NavHostController) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .size(width = 320.dp, height = 320.dp),
            onClick = {
                navController.navigate("card")
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomStart
            ) {

                Image(
                    painter = painterResource(R.drawable.photomode_18072025_201346),
                    contentDescription = "Contact profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )

                Box(
                    Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.5f to Color.Transparent,
                                    0.9f to Color(0, 0, 0, 201)
                                )
                            )
                        ),
                )

                Column(modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp)) {
                    Text(
                        text = str,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = str,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
            }
        }
    }

@Preview
@Composable
fun PreviewEventCard(){

    E20FrontendMobileTheme {
        val navController = rememberNavController()
        eventCard("Evento Bello","A puttana e mammata", navController)
    }
}
