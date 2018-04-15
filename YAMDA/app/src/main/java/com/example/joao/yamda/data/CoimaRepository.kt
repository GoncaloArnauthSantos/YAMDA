package com.example.joao.yamda.data

import com.example.joao.yamda.data.dtos.MovieDto
import com.example.joao.yamda.domain.entities.Movie
import com.example.joao.yamda.domain.entities.Operation

interface CoimaRepository{
    fun searchMovies(searchStr: Operation, cb: (List<MovieDto>) -> Unit)
    fun getMovies(action: Operation, cb: (List<MovieDto>) -> Unit)
    fun updateTables(lst : List<Operation>)
    fun addFavourite(movie : Movie)
    fun removeFavourite(movie: Movie)
    fun searchFavourite(cb: (List<MovieDto>) -> Unit)
}