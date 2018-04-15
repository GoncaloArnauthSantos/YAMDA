package com.example.joao.yamda.domain.converters

import com.example.joao.yamda.data.dtos.MovieDto
import com.example.joao.yamda.domain.entities.Movie

fun MovieDto.toDomain() : Movie =
        Movie(this.id, this.title, this.poster_path, this.vote_average, this.overview, this.release_date, null )