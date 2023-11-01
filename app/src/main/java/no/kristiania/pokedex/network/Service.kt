package no.kristiania.pokedex.network

import Pokemon
import PokemonListResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {
    @GET("pokemon/?limit=60&offset=0")
    suspend fun getAllPokemon(): Response<PokemonListResponse>

    @GET("pokemon/{name}")
    suspend fun getPokemon(
        @Path("name") name: String
    ): Response<Pokemon>
}

object PokemonNetwork {
    private fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService = getClient().create(PokemonService::class.java)
}