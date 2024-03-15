package com.example.soundrecorder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.soundrecorder.database.Sound
import com.example.soundrecorder.database.SoundViewModel
import com.example.soundrecorder.navigation.Marshroutes
import com.example.soundrecorder.ui.theme.Pink40
import com.example.soundrecorder.ui.theme.Pink80
import com.example.soundrecorder.ui.theme.Purple40
import com.example.soundrecorder.ui.theme.Purple80
import com.example.soundrecorder.ui.theme.PurpleGrey40
import com.example.soundrecorder.ui.theme.PurpleGrey80
import com.example.soundrecorder.ui.theme.SoundRecorderTheme

@Composable
fun soundContent(mSoundViewModel: SoundViewModel, navController: NavController, sound: Sound) {

    val mSounds by mSoundViewModel.readAllData.observeAsState(emptyList())
    SoundRecorderTheme {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                Column {
                    Button(
                        onClick = {
                            navController.navigate(Marshroutes.route1) {
                                popUpTo(Marshroutes.route1) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Text(text = "<", fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Id", modifier = Modifier.weight(1f), color = Color.White)
                        Text(text = "Название", modifier = Modifier.weight(2f), color = Color.White)
                        Text(text = "Автор", modifier = Modifier.weight(2f), color = Color.White)
                        Text(text = "Дата", modifier = Modifier.weight(2f), color = Color.White)
                    }
                    LazyColumn(Modifier.padding(start = 6.dp, end = 6.dp)) {
                        items(mSounds) { sound1 ->
                            EventListItem(sound = sound1, navController, sound)
                        }
                    }
                }
            }
    }
}

@Composable
fun EventListItem(sound:Sound,navController: NavController,sound1: Sound){
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            sound1.id = sound.id
            sound1.audio = sound.audio
            sound1.author = sound.author
            sound1.date_cr = sound.date_cr
            navController.navigate(Marshroutes.route4)
        }) {
        Text(text = "${sound.id}", modifier = Modifier.weight(1f), color = Color.White)
        Text(text = sound.audio, modifier = Modifier.weight(2f), color = Color.White)
        Text(text = sound.author, modifier = Modifier.weight(2f), color = Color.White)
        Text(text = sound.date_cr, modifier = Modifier.weight(2f), color = Color.White)
    }
}