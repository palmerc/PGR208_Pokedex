package no.kristiania.pokedex.data

import Pokemon
import android.util.Log
import no.kristiania.pokedex.network.PokemonNetwork

object PokemonRepository {
    suspend fun getPokemen(): List<Pokemon> {
        val response = PokemonNetwork.apiService.getAllPokemon()
        Log.d("PokemonRepository", "getPokemen()")

        if(response.isSuccessful) {
            // The "response body" might be NULL (the API response was successful, but didn't
            // return any data (NULL) -- If we don't handle this, we might also run into a crash!!
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Response was not successful")
        }
    }

    suspend fun getPokemonByName(pokemonName: String): Pokemon? {
        val response =  PokemonNetwork.apiService.getPokemon(pokemonName)
        Log.d("PokemonRepository", "getPokemonByName(${pokemonName})")

        if(response.isSuccessful) {
            // The "response body" might be NULL (the API response was successful, but didn't
            // return any data (NULL) -- If we don't handle this, we might also run into a crash!!
            return response.body()
        } else {
            throw Exception("Response was not successful")
        }
    }
}