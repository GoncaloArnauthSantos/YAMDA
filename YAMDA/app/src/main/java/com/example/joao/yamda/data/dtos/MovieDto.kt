package com.example.joao.yamda.data.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown=true)
data class MovieDto(
        val id: Int,
        val title: String,
        val poster_path: String?,
        val vote_average : Double?,
        val overview : String?,
        val release_date : String?
)