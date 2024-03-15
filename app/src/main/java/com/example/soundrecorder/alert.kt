package com.example.soundrecorder

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.soundrecorder.database.Sound
import com.example.soundrecorder.database.SoundViewModel
import com.example.soundrecorder.navigation.Marshroutes
import java.io.File

@Composable
fun alert(navController: NavController, sound: Sound, mSoundViewModel: SoundViewModel, path:String, context: Context){
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)){
        Column{
            Box(modifier = Modifier.weight(2f))
            Text(text="Вы уверенвы,что хотите перезаписать файл?",color=Color.White, fontSize = 19.sp ,modifier = Modifier.weight(3f))
            Row(modifier = Modifier.weight(1f)){
                Button(onClick = {
                    refactorSound(sound,sound.audio,mSoundViewModel,path,context);
                    navController.navigate(Marshroutes.route1) {
                        popUpTo(Marshroutes.route1)
                        { inclusive = true }
                    } }, modifier = Modifier.height(200.dp)
                    .width(85.dp)) {
                    Text("Yes",color=Color.White)
                }
                Button(onClick = {
                    navController.navigate(Marshroutes.route1) {
                        popUpTo(Marshroutes.route1)
                        { inclusive = true }
                    } }, modifier = Modifier
                    .height(200.dp)
                        .width(85.dp)
                    .offset(LocalConfiguration.current.screenWidthDp.dp-170.dp,0.dp)) {
                    Text("No",color=Color.White)
                }
            }
        }
    }
}

fun refactorSound(sound:Sound,audio:String,mSoundViewModel: SoundViewModel,path:String,context: Context){
    mSoundViewModel.updateSound(sound)
    val file = File(path)
    val path1 =
        Environment.getExternalStorageDirectory().toString() + "/" + audio + ".3gp"
    val newfile = File(path1)
    file.renameTo(newfile)
    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
}