package no.kristiania.pokedex

import com.istudio.pokedex.data.remote.responses.Result

class PokemonRepository(private val api: PokeApi) {
    suspend fun getPokemen(): List<Result> {
        return api.getPokemonList(limit = 60, offset = 0).results
    }
}