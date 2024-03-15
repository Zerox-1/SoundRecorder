package com.example.soundrecorder

import android.media.MediaPlayer
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
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
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun redactorScreen(navController: NavController, sound: Sound, mSoundViewModel: SoundViewModel, lifecycleOwner: LifecycleOwner) {
    val mMp = MediaPlayer()
    var mMessage by remember {
        mutableStateOf("")
    }
    var mMessage1 by remember {
        mutableStateOf("")
    }
    var mPath = Environment.getExternalStorageDirectory().toString() + "/" + sound.audio + ".3gp"
    if (File(mPath).isFile) {
        mMp.setDataSource(mPath)
    }
    mMp.setOnCompletionListener {
        mMp.stop()
    }
    val darkTheme: Boolean = isSystemInDarkTheme()
    SoundRecorderTheme{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Button(
                    onClick = {
                        navController.navigate(Marshroutes.route2) {
                            popUpTo(Marshroutes.route2) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp),
                    contentPadding = PaddingValues(0.dp),
                ) { Text(text = "<", fontSize = 20.sp, fontWeight = FontWeight.Black) }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Id", color = Color.White, modifier = Modifier.weight(1f))
                        Text(text = "Автор", color = Color.White, modifier = Modifier.weight(2f))
                        Text(text = "Название", color = Color.White, modifier = Modifier.weight(2f))
                        Text(text = "Дата", color = Color.White, modifier = Modifier.weight(2f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${sound.id}", color = Color.White)
                        Text(text = sound.author, color = Color.White)
                        Text(text = sound.audio, color = Color.White)
                        Text(text = sound.date_cr, color = Color.White)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f)
                    .offset(0.dp, -80.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {

                    Column {
                        Text(text = "Введите свое имя", color = Color.White)
                        TextField(
                            value = mMessage,
                            onValueChange = { newText -> mMessage = newText },
                            colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
                        )
                    }
                    Column {
                        Text(text = "Введите имя файла", color = Color.White)
                        TextField(
                            value = mMessage1,
                            onValueChange = { newText -> mMessage1 = newText },
                            colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(5f)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Button(
                        onClick = {
                            if (!mMp.isPlaying) {
                                mMp.prepare()
                                mMp.start()
                            }
                        },
                        modifier = Modifier
                            .height(200.dp)
                            .width(85.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.play_button),
                            contentDescription = "Play audio"
                        )
                    }
                    Button(
                        onClick = {
                            if (mMp.isPlaying) {
                                mMp.stop()
                            }
                        },
                        modifier = Modifier
                            .height(200.dp)
                            .width(85.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.black_square),
                            contentDescription = "Stop audio"
                        )
                    }
                    Button(
                        onClick = {
                            mSoundViewModel.readSound(mMessage1)
                            mSoundViewModel.sound.observe(lifecycleOwner, Observer { sound1 ->
                                if (sound1 != null) {
                                    Toast.makeText(
                                        navController.context,
                                        "Can`t refactor, already exist file!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    if (mMessage != "") {
                                        sound.author = mMessage
                                    }
                                    if (mMessage1 != "") {
                                        sound.audio = mMessage1
                                        val file = File(mPath)
                                        mPath = Environment.getExternalStorageDirectory()
                                            .toString() + "/" + sound.audio + ".3gp"
                                        file.renameTo(File(mPath))
                                    }
                                    mSoundViewModel.updateSound(sound)
                                    navController.navigate(Marshroutes.route2)
                                }
                            })

                        },
                        modifier = Modifier
                            .height(200.dp)
                            .width(85.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pencil),
                            contentDescription = "Refactor"
                        )
                    }
                    Button(
                        onClick = {
                            mMp.reset(); deleteSound(
                            navController,
                            sound,
                            mSoundViewModel
                        )
                        },
                        modifier = Modifier
                            .height(200.dp)
                            .width(85.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.trash),
                            contentDescription = "Delete"
                        )
                    }
                }
            }
        }
    }
}
fun deleteSound(navController: NavController,sound: Sound,mSoundViewModel: SoundViewModel) {
    mSoundViewModel.deleteSound(sound)
    val path = Environment.getExternalStorageDirectory().toString() + "/" + sound.audio + ".3gp"
    val file = File(path)
    if (file.exists()) {
        file.delete()
        navController.navigate(Marshroutes.route2) {
            popUpTo(Marshroutes.route2) { inclusive = true }
        }
    } else {
        println("File does not exist: $path")
    }
}