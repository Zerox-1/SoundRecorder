package com.example.soundrecorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soundrecorder.database.Sound
import com.example.soundrecorder.database.SoundViewModel
import com.example.soundrecorder.navigation.Marshroutes
import com.example.soundrecorder.ui.theme.SoundRecorderTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
    private lateinit var mSoundViewModel: SoundViewModel
    private var Recording=false;
    lateinit var mMr: MediaRecorder
    var mPath= Environment.getExternalStorageDirectory().toString()+"/myrec.3gp"
    lateinit var mSound: Sound
    var mSound1:Sound=Sound(null,"","","")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        mMr= MediaRecorder()
        mSoundViewModel= ViewModelProvider(this).get(SoundViewModel::class.java)
        super.onCreate(savedInstanceState)
        if (Environment.isExternalStorageManager()) {
            // The app has been granted the MANAGE_EXTERNAL_STORAGE permission
        } else {
            // The app has not been granted the MANAGE_EXTERNAL_STORAGE permission
            // Request the permission from the user
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:" + packageName)
            startActivity(intent)
        }
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE),111)
        }
        setContent {
                SoundRecorderTheme {
                    // A surface container using the 'background' color from the theme
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Marshroutes.route1) {
                        composable(route = Marshroutes.route1) {
                            Greeting(
                                navController = navController,
                                this@MainActivity
                            )
                        }
                        composable(route = Marshroutes.route2) { Screen2(navController = navController) }
                        composable(route = Marshroutes.route3) {
                            alert(
                                navController,
                                mSound,
                                mSoundViewModel,
                                mPath,
                                navController.context
                            )
                        }
                        composable(route = Marshroutes.route4) {
                            redactorScreen(
                                navController = navController,
                                sound = mSound1,
                                mSoundViewModel,
                                this@MainActivity
                            )
                        }
                    }
                }
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Greeting(navController: NavController, viewLifecycleOwner: LifecycleOwner) {
        var message by remember {
            mutableStateOf("")
        }
        var message1 by remember {
            mutableStateOf("")
        }
        Box(modifier = Modifier.background(Color.Black)) {
            Button(
                onClick = { navController.navigate(Marshroutes.route2) },
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp),
                contentPadding = PaddingValues(0.dp),
            ) {
                Text(text = ">", fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f), contentAlignment = Alignment.Center
                ) {
                    Column {

                        Column {
                            Text(text = "Введите свое имя", color = Color.White)
                            TextField(
                                value = message,
                                onValueChange = { newText -> message = newText },
                                colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
                            )
                        }
                        Column {
                            Text(text = "Введите имя файла", color = Color.White)
                            TextField(
                                value = message1,
                                onValueChange = { newText -> message1 = newText },
                                colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .offset(0.dp, 50.dp)
                ) {
                    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(
                            onClick = {
                                Recording=true
                                recordSound()
                            },
                            modifier = Modifier
                                .height(200.dp)
                                .width(85.dp)

                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.microphone_1),
                                contentDescription = "Record"
                            )
                        }
                        Button(
                            onClick = {
                                Recording=false
                                stopRecording()
                            },
                            modifier = Modifier
                                .height(200.dp)
                                .width(85.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.black_square),
                                contentDescription = "Stop record"
                            )
                        }
                        Button(
                            onClick = {
                                if(!Recording) {
                                    saveSound(
                                        message,
                                        message1,
                                        navController
                                    )
                                }
                            },
                            modifier = Modifier
                                .height(200.dp)
                                .width(85.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.save),
                                contentDescription = "Save"
                            )
                        }
                    }
                }
            }
        }
    }

    fun recordSound() {
        try{
            mMr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mMr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mMr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mMr.setOutputFile(mPath)
            mMr.prepare()
            mMr.start()}
        catch (e:IllegalStateException ) {
            Toast.makeText(this,"Already recording", Toast.LENGTH_LONG).show()
        }

    }
    fun stopRecording(){
        try{
            mMr.stop()}
        catch (e:IllegalStateException ) {
            Toast.makeText(this,"Nothing to stop", Toast.LENGTH_LONG).show()
        }
    }
    fun saveSound(author:String, audio:String, navController: NavController) {
        if (audio.isEmpty() || author.isEmpty()) {
            Toast.makeText(this, "Write all data!", Toast.LENGTH_SHORT).show()
        }
        else {
            mSound =
                Sound(null, author, audio, SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
            mSoundViewModel.readSound(audio)
            mSoundViewModel.sound.observe(this, Observer { sound2 ->
                if (sound2 != null) {
                    mSound.id=sound2.id
                    navController.navigate(Marshroutes.route3)
                }
                else {
                    addInDb(mSound,audio)
                }
            })
        }
    }
    fun addInDb(sound: Sound,audio: String){
        mSoundViewModel.addSound(sound)
        val file = File(mPath)
        val path1 =
            Environment.getExternalStorageDirectory().toString() + "/" + audio + ".3gp"
        val newfile = File(path1)
        file.renameTo(newfile)
        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen2(navController: NavController){
        Scaffold (
            content={
                soundContent(mSoundViewModel,navController,mSound1)
            }
        )
    }
}