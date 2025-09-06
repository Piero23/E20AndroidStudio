package com.example.e20frontendmobile


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme
import com.example.e20frontendmobile.ui.theme.bungeeInLineFontFamily
import com.example.e20frontendmobile.ui.theme.museoModernoFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val list1  = listOf(R.drawable.ce21765b01256e5c454799156979c8ab,
                R.drawable.color_festival_powder_partyevent_poster_template_de51e43782d312549d6a8021e848a257_screen,
                R.drawable.d385c8ed5c1a2d71d7298f8693aabda2, R.drawable.download,)
            E20FrontendMobileTheme {
                CarouselExample(true,list1)
            }
        }
    }
}

fun timer(scrollState : ScrollState, scope: CoroutineScope){
    scope.launch { scrollState.scrollTo(scrollState.maxValue) }
}

@Composable
fun CarouselExample(farward: Boolean, list: List<Int>) {

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var value = if (farward) 1  else -1

    if(!farward)
        timer(scrollState,scope)

    Row (
        modifier = Modifier
            .background(Color.LightGray)
            .horizontalScroll(scrollState,false)
    ){
        for (i : Int in list) {
            Image(
                painter = painterResource(i),
                contentDescription = "",

            )
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
        onSearch = { /* Handle search submission */ },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizableSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    // Customization options
    placeholder: @Composable () -> Unit = { Text("Search") },
    leadingIcon: @Composable (() -> Unit)? = { null},
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingContent: (@Composable (String) -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Track expanded state of search bar
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
                // Customizable input field implementation
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onSearch(query)
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon
                )
            },
            expanded = false,
            onExpandedChange = { expanded = it },
        ) {
            // Show search results in a lazy column for better performance
            LazyColumn {
                items(count = searchResults.size) { index ->
                    val resultText = searchResults[index]
                    ListItem(
                        headlineContent = { Text(resultText) },
                        supportingContent = supportingContent?.let { { it(resultText) } },
                        leadingContent = leadingContent,
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                onResultClick(resultText)
                                expanded = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
// [END android_compose_components_customizable_searchbar]

@Preview(showBackground = true)
@Composable
fun CustomizableSearchBarExample() {
    // Manage query state
    var query by rememberSaveable { mutableStateOf("") }
    val items = listOf(
        "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb",
        "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow",
        "Nougat", "Oreo", "Pie"
    )

    // Filter items based on query
    val filteredItems by remember {
        derivedStateOf {
            if (query.isEmpty()) {
                items
            } else {
                items.filter { it.contains(query, ignoreCase = true) }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CustomizableSearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { /* Handle search submission */ },
            searchResults = filteredItems,
            onResultClick = { query = it },
            // Customize appearance with optional parameters
            placeholder = { Text("Cerca un evento...") },
            trailingIcon = {Icon(Icons.Default.Search, contentDescription = "Search")  },
            supportingContent = { Text("Android dessert") },
            leadingContent = { Icon(Icons.Filled.Star, contentDescription = "Starred item") }
        )

        // Display the filtered list below the search bar
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 72.dp, // Provides space for the search bar
                end = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.semantics {
                traversalIndex = 1f
            },
        ) {
            items(count = filteredItems.size) {
                Text(text = filteredItems[it])
            }
        }
    }
}
/////////////////////////////////////
@Composable
fun MultipleStylesInText() {
    val brush = Brush.linearGradient(colors = listOf(Color(248,185,50,255), Color(176,88,15,255)),
        start = Offset(900f,0f) ,
        end = Offset(900f,100f))


    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(
                color = Color.White,
                fontSize = 100.sp,
                fontFamily = bungeeInLineFontFamily,)) {
                append("E")
            }
            withStyle(style = SpanStyle(
                brush = brush , alpha = 1f,
                fontFamily = bungeeInLineFontFamily,
                fontSize = 100.sp
            )
            ) {
                append("20")
            }
        }
    )
}

@Composable
fun mainLogo(){

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@PreviewScreenSizes
@Composable
fun wallpaperPreview(){

    val list1  = listOf(R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a,
        R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a, R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a,
        R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a, R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a,
    )


    E20FrontendMobileTheme {
        Column (
            modifier = Modifier.verticalScroll(
                enabled = true,
                state = ScrollState(0),
            )
        ) {
            Box {

                //Carosello
                Column {
                    CarouselExample(false, list1)
                    CarouselExample(true, list1)
                }

                //Overlay
                Box(
                    Modifier
                        .matchParentSize()
                )

                //Testo Centrale
                Box(modifier = Modifier
                    .align(Alignment.Center)
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        MultipleStylesInText()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Lorem ipsum dolor sic amet non so cosa scrivere",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontFamily = museoModernoFontFamily,
                            textAlign = TextAlign.Center
                        )
                        val textFieldState = rememberTextFieldState()
                        Spacer(modifier = Modifier.height(44.dp))
                        SimpleSearchBar(
                            textFieldState = textFieldState,
                            onSearch = { /* Handle search submission */ },
                        )
                    }
                }
            }
        }
    }
}