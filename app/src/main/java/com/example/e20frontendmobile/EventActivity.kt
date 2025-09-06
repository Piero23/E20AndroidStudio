package com.example.e20frontendmobile

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun ShowEvent(){

    val scrollState = rememberScrollState()
    var toggledBell by rememberSaveable { mutableStateOf(false) }
    var toggledHeart by rememberSaveable { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .verticalScroll(scrollState)
    ){
        Image(
            /*bitmap = event.image?.asImageBitmap() ?: ,*/
            painter = painterResource(id = R.drawable.images) ,
            contentDescription = "Image of ${"event.id"}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth(),
        )
        Column(
            modifier = Modifier.padding(15.dp, 10.dp, 15.dp, 0.dp)
        ){
            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column (
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Text(
                        "Vieni a vedere le mucche",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(255, 157, 71, 255),
                                        Color(199, 79, 0, 255)
                                    ),
                                )
                            )
                            .height(40.dp)
                            .width(150.dp),
                        Alignment.Center
                    ){
                        TextWithShadow(
                            modifier = Modifier,
                            text = "9-11-2001 11.00"
                        )
                    }
                }
                Column (
                    Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp)
                ){
                    IconButton(
                        onClick = { toggledBell = !toggledBell }
                    ){
                        Icon(
                            Icons.Filled.Notifications,
                            contentDescription = "Ricordamelo",
                            modifier = Modifier
                                .size(50.dp),
                            tint = if (toggledBell) Color.Yellow else Color.Black
                        )
                    }
                    IconButton(
                        onClick = { toggledHeart = !toggledHeart }
                    ) {
                        Icon(
                            Icons.Filled.FavoriteBorder,
                            contentDescription = "Salva",
                            modifier = Modifier
                                .size(50.dp),
                            tint = if (toggledHeart) Color.Red else Color.Black
                        )
                    }
                }
            }
            Column (
                modifier = Modifier.padding(0.dp, 80.dp, 0.dp, 80.dp)
            ) {
                Text(
                    "Descrizione",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    modifier = Modifier.padding(2.dp, 5.dp, 0.dp, 0.dp),
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec elementum, erat ut aliquam vehicula, dolor nisi elementum nulla, in sagittis odio odio non sem. Curabitur dolor dolor, faucibus eleifend enim ac, blandit condimentum elit. Donec placerat tortor quis orci vehicula rhoncus. Etiam eget ligula lobortis, tempor risus id, scelerisque enim. Cras aliquet sollicitudin est, sed vehicula leo vestibulum facilisis. Praesent turpis massa, ultrices vel orci at, congue gravida arcu.",
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    modifier = Modifier.alignBy(LastBaseline),
                    text = "Posti disponibili: ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    modifier = Modifier.alignBy(LastBaseline),
                    text = "2",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(106, 51, 0, 255)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Box (
                    modifier = Modifier.width(280.dp)
                ){
                    Image(
                        modifier = Modifier.matchParentSize(),
                        painter = painterResource(id = R.drawable.image) ,
                        contentDescription = "Ticket",
                        contentScale = ContentScale.FillWidth
                    )
                    Column(
                        modifier = Modifier.padding(80.dp, 20.dp, 0.dp, 0.dp)
                    ){
                        Text(
                            text = "1 Biglietto",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.offset(0.dp, (-8).dp)
                        ){
                            Text(
                                modifier = Modifier.alignBy(LastBaseline),
                                text = "200.00",
                                style = MaterialTheme.typography.headlineLarge,
                                fontSize = 40.sp,
                                color = Color(106, 51, 0, 255)
                            )
                            Text(
                                modifier = Modifier.alignBy(LastBaseline),
                                text = "€",
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 20.sp,
                                color = Color(182, 97, 17, 255)
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(0.dp, 15.dp, 22.dp, 80.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                TextButton(
                    onClick = {},
                    colors = ButtonColors(Color.Transparent, Color.Black, Color.Transparent, Color.Transparent)

                ) {
                    Text(
                        text = "Compra Ora",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .graphicsLayer(alpha = 0.99f)
                            .drawWithCache {
                                val brush = Brush.horizontalGradient(listOf(
                                    Color(255, 157, 71, 255),
                                    Color(199, 79, 0, 255)))
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(brush, blendMode = BlendMode.SrcAtop)
                                }
                            }
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Location",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier.weight(1f),
            ){
                Text(
                    text = "Via di tua mamma",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp
                )
                Text(
                    text = "118",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ){
                Text(
                    text = "Napoli (NA)",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp
                )
                Text(
                    text = "88888",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(700.dp)
                .padding(15.dp, 10.dp, 15.dp, 50.dp)
        ){
            WebViewScreen(45.0755969,7.638332)
        }
    }
}

@Composable
fun TextWithShadow(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        color = Color.DarkGray,
        modifier = modifier
            .offset(
                x = 1.dp,
                y = 1.dp
            )
            .alpha(0.75f)
    )
    Text(
        text = text,
        color = Color.White,
        modifier = modifier
    )
}

@Composable
fun WebViewScreen(latitude: Double, longitude: Double) {
    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
            }
        },
        update = { web ->
            web.loadUrl("https://maps.google.com/?q=${latitude},${longitude}")
            //TODO scegliere che tipo di mappa usare
        }
    )
}



@Composable
@PreviewScreenSizes
fun prev(){
    ShowEvent()
}