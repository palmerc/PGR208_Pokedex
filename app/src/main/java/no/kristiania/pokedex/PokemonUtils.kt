package no.kristiania.pokedex

import Pokemon
import java.util.Locale
import kotlin.math.roundToInt

object PokemonUtils {
    fun formatPokemonUrl(pokemonUrl: String): String {
        val number = if (pokemonUrl.endsWith("/")) {
            pokemonUrl.dropLast(1).takeLastWhile { it.isDigit() }
        } else {
            pokemonUrl.takeLastWhile { it.isDigit() }
        }
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${number}.png"
    }

    fun formatPokemonDetailUrl(pokemonUrl: String): String {
        return pokemonUrl.replace("pokemon","pokemon/other/official-artwork")
    }

    fun getPokemonNumber(pokemon: Pokemon): String {
        if (pokemon.url != null) {
            val number = if(pokemon.url.endsWith("/")) {
                pokemon.url.dropLast(1).takeLastWhile { it.isDigit() }
            } else {
                pokemon.url.takeLastWhile { it.isDigit() }
            }
            return number
        }

        return "0";
    }

    fun capitalizeText(text :String): String {
        return text.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    fun weightHeightMeasurement(value :Int): String {
        return ((value * 100f).roundToInt() / 1000f).toInt().toString()
    }

}