package com.example.joao.yamda.utils

import android.app.Activity
import android.app.Service
import com.example.joao.yamda.MovieApp

val Activity.MovieApp: MovieApp
    get() = this.application as MovieApp

val Service.MovieApp: MovieApp
    get() = this.application as MovieApp

