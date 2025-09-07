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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

fun search(items : List<String>, query: String): List<String> {
    return if (query.isEmpty()) {
        listOf()
    } else {
        items.filter { it.contains(query, ignoreCase = true) }
    }
}


@Composable
fun ShowDiscovery(navController: NavHostController){
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Eventi", "Profili") //nomi tabs

    var query by rememberSaveable { mutableStateOf("") }
    var items = listOf<String>("pinga", "pino", "sbingus", "sboxi") //TODO rimpiazzare con Evento
    var filteredItems by remember { mutableStateOf<List<String>>(emptyList()) }


    Column(
    ){
        CustomizableSearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { filteredItems = search(items, query) },
            searchResults = listOf(), onResultClick = { query = it },
            // Customize appearance with optional parameters
            placeholder = { Text("Cerca un evento...") },
            trailingIcon = {Icon(Icons.Default.Search,
                contentDescription = "Search") },
            supportingContent = { Text("Android dessert") },
            leadingContent = { Icon(Icons.Filled.Star,
                contentDescription = "Starred item")},
            modifier = Modifier
                .padding(15.dp, 0.dp, 15.dp, 0.dp )
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
            items(count = filteredItems.size) { index ->
                val resultText = filteredItems[index]
                Box(
                    modifier = Modifier.padding(top = 25.dp)
                ){
                    eventCard(resultText, "aaaaaaaaaaaaaaaaaaaaa", navController)
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
    ShowDiscovery(nav)
}