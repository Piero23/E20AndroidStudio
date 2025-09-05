package com.example.e20frontendmobile


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.rememberInfiniteTransition

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme
import com.example.e20frontendmobile.ui.theme.bungeeInLineFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer
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
    //Usa questo
    val infiniteTransition = rememberInfiniteTransition(label = "carousel")

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

        //elimina questo inefficente , schifo
        fixedRateTimer(
            daemon = false,
            period = 50
        ) {
            scope.launch {
                if(scrollState.value==scrollState.maxValue || scrollState.value == 0)
                    value*=-1
                scrollState.animateScrollBy(10f*value)
            }
        }
    }
}

@Composable
fun MultipleStylesInText(modifier: Modifier) {
    val brush = Brush.linearGradient(colors = listOf(Color(248,185,50,255), Color(176,88,15,255)))

    Text(
        modifier = modifier,
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

@Preview
@PreviewScreenSizes
@Composable
fun wallpaperPreview(){

    val list1  = listOf(R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a,
        R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a, R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a,
        R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a, R.drawable._c16eafedcecc5b7dcc7cab70aaf1a3a,
    )

    val list2  = listOf(R.drawable.images, R.drawable.photomode_18072025_201346,
        R.drawable.stock_vector_night_dance_party_music_night_poster_template_electro_style_concert_disco_club_party_event_flyer_741157993
    )
    E20FrontendMobileTheme{
        Box() {

            Column {
                CarouselExample(false,list1)
                CarouselExample(true,list1)
            }
            Box(
                Modifier
                    .matchParentSize()
                    .background(Color(0, 0, 0, 190))
            )
            MultipleStylesInText(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}