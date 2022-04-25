package com.grigorevmp.catwidget

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grigorevmp.catwidget.databinding.ActivityAboutBinding

const val authorTelegramLink = "https://t.me/grigorevmp"
const val authorGitHubLink = "https://github.com/grigorevmp/QuickPass-Mobile-Password-manager"
const val quickPass =
    "https://play.google.com/store/apps/details?id=com.mikhailgrigorev.quickpassword"

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.ivTelegram.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(authorTelegramLink))
            startActivity(i)
        }

        binding.cvGitHub.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(authorGitHubLink))
            startActivity(i)
        }

        binding.quickPass.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(quickPass))
            startActivity(i)
        }
    }
}