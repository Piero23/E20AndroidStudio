package com.example.e20frontendmobile

import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.nio.file.WatchEvent


@Preview
@Composable
fun ShowEvent(){
    val museoModerno = FontFamily(
        Font(R.font.museomoderno_bold, FontWeight.Bold),
        Font(R.font.museomoderno_thin, FontWeight.Thin),
        Font(R.font.museomoderno_black, FontWeight.Black),
        Font(R.font.museomoderno_light, FontWeight.Light),
        Font(R.font.museomoderno_medium, FontWeight.Medium),
        Font(R.font.museomoderno_extrabold, FontWeight.ExtraBold),
        Font(R.font.museomoderno_extralight, FontWeight.ExtraLight),
        Font(R.font.museomoderno_regular, FontWeight.Normal),
        Font(R.font.museomoderno_semibold, FontWeight.SemiBold),
    )

    Column {
        Image(
            /*bitmap = event.image?.asImageBitmap() ?: ,*/
            painter = painterResource(id = R.drawable.images) ,
            contentDescription = "Image of ${"event.id"}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth(),
        )
        Row (modifier = Modifier.fillMaxWidth()
            .padding(10.dp,10.dp, 10.dp, 0.dp ),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    "Vieni a vedere le mucche",
                    fontWeight = FontWeight.Bold,
                    fontFamily = museoModerno,
                    fontSize = 30.sp
                )
                Box(
                    Modifier.clip(RoundedCornerShape(15.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFFF9D47), Color(0xFFC74F00)),
                                start = Offset(0f, 500f),
                                end = Offset(700f, 500f)
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
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Ricordamelo",
                    modifier = Modifier
                        .size(50.dp)
                )
                Icon(
                    Icons.Filled.FavoriteBorder,
                    contentDescription = "Salva",
                    modifier = Modifier
                        .size(50.dp)
                )
            }
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