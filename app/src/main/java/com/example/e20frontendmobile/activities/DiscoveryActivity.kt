package com.example.e20frontendmobile.activities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.e20frontendmobile.activities.evento.eventCard
import com.example.e20frontendmobile.apiService.EventoLocation.EventService
import com.example.e20frontendmobile.composables.CustomizableSearchBar
import com.example.e20frontendmobile.model.Event
import com.example.e20frontendmobile.viewModels.EventViewModel

@Composable
fun ShowDiscovery(
    navController: NavHostController,
    inputQuery: String = "",
    eventViewModel: EventViewModel
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Eventi", "Profili")

    var query by remember { mutableStateOf(inputQuery) }
    val items = eventViewModel.items

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        eventViewModel.search(context, query, )
    }

    Column {
        CustomizableSearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { eventViewModel.search(context, query) },
            searchResults = listOf(),
            onResultClick = { query = it },
            placeholder = { Text("Cerca un evento...") },
            trailingIcon = { Icon(Icons.Default.Search, null) },
            leadingContent = { Icon(Icons.Filled.Star, null) },
            modifier = Modifier.padding(horizontal = 15.dp)
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(2.dp),
                    color = Color.Blue
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                NoRippleTab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = title,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = items,
                key = { it.id }
            ) { resultItem ->
                Box(Modifier.padding(top = 25.dp)) {
                    eventCard(resultItem, navController, eventViewModel)
                }
            }
        }
    }
}



@Composable
fun NoRippleTab(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(vertical = 15.dp) //modifica altezza tab
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.Black else Color.Gray, //colore e font tab selezionata
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview
@Composable
fun previ(){
    val nav=rememberNavController()
    //ShowDiscovery(nav)
}