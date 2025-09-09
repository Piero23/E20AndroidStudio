package com.example.e20frontendmobile.activities

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.e20frontendmobile.R
import com.example.e20frontendmobile.composables.CustomTextField
import com.example.e20frontendmobile.composables.IconButtonType1
import com.example.e20frontendmobile.composables.IconTextButtonType1
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme

@Preview
@Composable
fun createEventPreview(){
    E20FrontendMobileTheme {
        createEvent()
    }
}


@Composable
fun createEvent(){
    val scrollState = rememberScrollState()
    var toggledBell by rememberSaveable { mutableStateOf(false) }
    var toggledHeart by rememberSaveable { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ){
        Box(contentAlignment = Alignment.BottomEnd){
            Image(
                /*bitmap = event.image?.asImageBitmap() ?: ,*/
                painter = painterResource(id = R.drawable.images),
                contentDescription = "Image of ${"event.id"}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
            )
            IconButtonType1(
                onClick = { },
                icon = Icons.Default.Edit,
                iconDescription = "",
                iconSize = 20.dp,
                modifier = Modifier.padding(10.dp)
            )
        }
        Column(
            modifier = Modifier.padding(15.dp, 10.dp, 15.dp, 0.dp)
        ){
            Text(
                "Titolo",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )

            CustomTextField(
                modifier = Modifier.fillMaxWidth()
            )



            Spacer(modifier = Modifier.height(40.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,

            ) {

                Column {
                    Text(
                        "Data",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row {
                        CustomTextField(
                            modifier = Modifier.width(120.dp)
                        )

                        IconButtonType1(
                            onClick = { },
                            icon = Icons.Default.DateRange,
                            iconDescription = "",
                            iconSize = 20.dp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Column {
                    Text(
                        "Orario",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row {
                        CustomTextField(
                            modifier = Modifier.width(120.dp)
                        )

                        IconButtonType1(
                            onClick = { },
                            icon = Icons.Default.ShoppingCart,
                            iconDescription = "",
                            iconSize = 20.dp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }



            }

            Column {
                Text(
                    "Location",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall
                )

                Row {
                    CustomTextField(modifier = Modifier.width(300.dp))

                    IconButtonType1(
                        onClick = { },
                        icon = Icons.Default.Place,
                        iconDescription = "",
                        iconSize = 20.dp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier
                .height(30.dp)
                .padding(vertical = 18.dp))


            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,

                ) {

                Column (modifier = Modifier.fillMaxWidth(0.5f)){
                    Text(
                        "Posti",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row {
                        CustomTextField()
                    }
                }
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                Column {
                    Text(
                        "Prezzo",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row {
                        CustomTextField()
                    }
                }



            }

            Spacer(modifier = Modifier
                .height(30.dp)
                .padding(vertical = 18.dp))

            var checked by remember { mutableStateOf(true) }

            Row (verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Vietato ai minori",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .width(250.dp)
                )
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                    },
                )
            }

            Row (verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Biglietto Nominativo",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .width(250.dp)
                )
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                    }
                )
            }


            Row (verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Biglietto Riutilizzabile",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .width(250.dp)
                )
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                    }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Column {
                Text(
                    "Descrizione",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall
                )

                Row {
                    CustomTextField(modifier = Modifier.fillMaxWidth())
                }
            }



            Spacer(modifier = Modifier.height(60.dp))



            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(80.dp),
                shape = RoundedCornerShape(20)
            ) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(text = "Crea Evento",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }

        }
    }
}





@Composable
@PreviewScreenSizes
fun previewButton(){
    E20FrontendMobileTheme {
        CustomTextField()
    }
}