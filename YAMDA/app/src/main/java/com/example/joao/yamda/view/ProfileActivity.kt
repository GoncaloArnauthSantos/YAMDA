package com.example.joao.yamda.view

import android.app.Activity
import android.os.Bundle
import com.example.joao.yamda.R
import kotlinx.android.synthetic.main.profile.*
import android.content.Intent
import android.net.Uri

class ProfileActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        git1.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/GoncaloArnauthSantos"))
            startActivity(browserIntent)
        }

        git2.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JoaoLeitao"))
            startActivity(browserIntent)
        }

        git3.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JoseRicardoMoreira"))
            startActivity(browserIntent)
        }

        apiLogo.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/"))
            startActivity(browserIntent)
        }
    }
}
