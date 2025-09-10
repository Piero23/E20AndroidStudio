package com.example.e20frontendmobile


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.composeuitemplates.presentation.bottomNavigationScreen.BottomNavigationScreen
import com.example.e20frontendmobile.activities.home.EventCarousel
import com.example.e20frontendmobile.ui.theme.BungeeInline
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme

import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()
        setContent {
            E20FrontendMobileTheme {
                BottomNavigationScreen()
            }
        }
    }
}



//////////////////////////////////////

// [START android_compose_components_simple_searchbar]
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Controls expansion state of the search bar
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())},
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Cerca Eventi...") }
                )
            },
            expanded = false,
            onExpandedChange = {},
        ) {
            // Ricerca da implementare
            Column(Modifier.verticalScroll(rememberScrollState())) {
//                searchResults.forEach { result ->
//                    ListItem(
//                        headlineContent = { Text(result) },
//                        modifier = Modifier
//                            .clickable {
//                                textFieldState.edit { replace(0, length, result) }
//                                expanded = false
//                            }
//                            .fillMaxWidth()
//                    )
//                }
            }
        }
    }
}
// [END android_compose_components_simple_searchbar]

@Preview(showBackground = true)
@Composable
private fun SimpleSearchBarExample() {
    // Create and remember the text field state

//    val items = listOf(
//        "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb",
//        "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop"
//    )
//
//    // Filter items based on the current search text
//    val filteredItems by remember {
//        derivedStateOf {
//            val searchText = textFieldState.text.toString()
//            if (searchText.isEmpty()) {
//                emptyList()
//            } else {
//                items.filter { it.contains(searchText, ignoreCase = true) }
//            }
//        }
//    }

    val textFieldState = rememberTextFieldState()

    SimpleSearchBar(
        textFieldState = textFieldState,
        onSearch = {  },
    )
}
/////////////////////////////////////
@Composable
fun MainLogo() {
    val brush = Brush.linearGradient(colors = listOf(Color(248,185,50,255), Color(176,88,15,255)),
        start = Offset(900f,0f) ,
        end = Offset(900f,100f))

    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(
                color = Color.White,
                fontSize = 100.sp,
                fontFamily = BungeeInline,)) {
                append("E")
            }
            withStyle(style = SpanStyle(
                brush = brush , alpha = 1f,
                fontFamily = BungeeInline,
                fontSize = 100.sp
            )
            ) {
                append("20")
            }
        }
    )
}


@Composable
fun CaroselloInfinito() {
    val listState = rememberLazyListState()

    val infiniteTransition = rememberInfiniteTransition()
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f, // normalizziamo a 0..1
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 30000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val scope = rememberCoroutineScope()
    val itemCount = 5
    val itemWidthPx = with(LocalDensity.current) {300.dp.toPx()} // larghezza + padding

    // Aggiorna lo scroll in base all'animazione
    LaunchedEffect(offsetX) {
        val scrollPosition = (offsetX * (itemWidthPx * (itemCount - 1))).toInt()
        scope.launch {
            listState.scrollToItem(0, scrollPosition) // scrollToItem(itemIndex, offset)
        }
    }

    LazyRow(
        state = listState,
        userScrollEnabled = false
    ) {
        items(itemCount) { index ->
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.photomode_18072025_201346),
                    contentDescription = "Contact profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

}



@Composable
@Preview
fun cia(){
    CaroselloInfinito()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@PreviewScreenSizes
@Composable
fun wallpaperPreview(){
    val navController = rememberNavController()

    E20FrontendMobileTheme {
        mainFun(navController)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainFun(navController: NavHostController){

    val list1  = listOf(R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a,
        R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a, R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a,
        R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a, R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a,
    )

    val colorStops = arrayOf(
        0.7f to Color(0, 0, 0, 158),
        0.9f to MaterialTheme.colorScheme.background
    )

    Column (
        modifier = Modifier.verticalScroll(
            enabled = true,
            state = ScrollState(0),
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        E20FrontendMobileTheme {
            Box {
                //Carosello
                Column {
                    CaroselloInfinito()
                    CaroselloInfinito()
                }

                //Overlay
                Box(
                    Modifier
                        .matchParentSize()
                        .background(Brush.verticalGradient(colorStops = colorStops))
                )

                //Testo Centrale
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MainLogo()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Lorem ipsum dolor sic amet non so cosa scrivere",
//                            color = Color.White,
//                            fontSize = 15.sp,
//                            fontFamily = MuseoModerno,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Light
                            )
                        )
                        val textFieldState = rememberTextFieldState()
                        Spacer(modifier = Modifier.height(44.dp))
                        SimpleSearchBar(
                            textFieldState = textFieldState,
                            onSearch = { navController.navigate("discovery/${textFieldState.text}") },
                        )
                    }
                }

                //Sotto
            }
            Spacer(modifier = Modifier.height(30.dp))
            Column {
                EventCarousel(28.sp, "Partecipano i tuoi amici",navController)
                EventCarousel(28.sp, "Partecipano i tuoi amici",navController)
                EventCarousel(28.sp, "Partecipano i tuoi amici",navController)
                EventCarousel(28.sp, "Partecipano i tuoi amici",navController)
            }
        }

    }
}