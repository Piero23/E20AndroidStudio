package com.example.e20frontendmobile.activities.user

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.e20frontendmobile.data.apiService.Utente.UtenteService
import com.example.e20frontendmobile.data.auth.AuthStateStorage
import com.example.e20frontendmobile.model.Utente
import com.example.e20frontendmobile.viewModels.UserViewModel

@Composable
fun userCard(
    utente: Utente,
    navController: NavHostController,
    userViewModel: UserViewModel = viewModel()
) {

    val context: Context = LocalContext.current
    var toggledHeart by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        toggledHeart = userViewModel.checkIfFollowed(context,utente.username)
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .height(100.dp),
        onClick = {
            userViewModel.setDisplayableUser(utente)
            navController.navigate("userCard")
        }
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1.0F)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(255, 157, 71, 255),
                                Color(199, 79, 0, 255)
                            )
                        ), shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    utente.username[0].toString().uppercase(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displayLarge,
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "@${utente.username}",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            if (AuthStateStorage(context).getUserInfo()?.sub != utente.username) {
                IconButton(onClick = {
                    // SE L'UTENTE È LOGGATO
                    if (AuthStateStorage(context).getUserInfo()?.roles != null) {
                        toggledHeart = !toggledHeart
                        if (toggledHeart) {
                            userViewModel.followUser(
                                context,
                                utente.username
                            )
                            Toast.makeText(
                                context,
                                "Hai iniziato a seguire " + utente.username,
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            userViewModel.unfollowUser(context, utente.username)
                            Toast.makeText(
                                context,
                                "Hai smesso di seguire " + utente.username,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Devi essere registrato per seguire un utente",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }) {
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        contentDescription = "Salva",
                        modifier = Modifier.size(50.dp),
                        tint = if (toggledHeart) Color.Red else Color.Black
                    )

                }
            }
        }
    }
}