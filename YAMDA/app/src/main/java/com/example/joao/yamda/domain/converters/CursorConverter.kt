package com.example.joao.yamda.domain.converters

import android.database.Cursor
import com.example.joao.yamda.data.dtos.MovieDto

fun Cursor.toDomain() : MovieDto =
        MovieDto(this.getInt(1), this.getString(2), this.getString(3), this.getDouble(4),
                this.getString(5), this.getString(6))