package com.jesando.zphererent.activity.mainActivity

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.jesando.zphererent.R
import com.jesando.zphererent.activity.defaultActivity.DashboardActivity
import com.jesando.zphererent.activity.guestActivity.LoginActivity
import com.jesando.zphererent.activity.guestActivity.RegisterActivity
import com.jesando.zphererent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isVideoPrepared = false


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // Cek apakah token ada di SharedPreferences
        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token != null) {
            // Jika token ada, arahkan ke DashboardActivity
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish() // Mengakhiri MainActivity agar tidak kembali ke halaman ini
        } else {
            // Jika tidak ada token, lanjutkan untuk menginisialisasi tampilan MainActivity
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            btnLoginListener()
            btnRegisterListener()
        }

        val videoView: VideoView = binding.videoIntro
        val uri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.car_intro)
        videoView.setVideoURI(uri)

        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            isVideoPrepared = true
            videoView.start()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isVideoPrepared) {
            binding.videoIntro.start() // Melanjutkan pemutaran video ketika kembali ke aplikasi
        }
    }

    override fun onPause() {
        super.onPause()
        if (isVideoPrepared) {
            binding.videoIntro.pause() // Menghentikan pemutaran video ketika aplikasi di latar belakang
        }
    }

    override fun onStart() {
        super.onStart()
        if (isVideoPrepared) {
            binding.videoIntro.start() // Memulai kembali video ketika aktivitas terlihat
        }
    }

    override fun onStop() {
        super.onStop()
        if (isVideoPrepared) {
            binding.videoIntro.stopPlayback() // Menghentikan video sepenuhnya untuk menghemat sumber daya
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoIntro.stopPlayback() // Menghentikan pemutaran video dan membersihkan sumber daya
    }

    private fun btnLoginListener() {
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun btnRegisterListener() {
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
