import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import no.kristiania.pokedex.PokemonUtils
import no.kristiania.pokedex.screens.pokemon_list.PokemonItem

@Composable
fun PokemonListScreen(
    viewModel: PokemenListViewModel,
    onPokemonClick:(pokemonName: String)-> Unit = {})
{
    val loading = viewModel.loading.collectAsState()
    val pokemen = viewModel.pokemon.collectAsState()

    if (loading.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(pokemen.value) {pokemon ->
            val pokemonName = PokemonUtils.capitalizeText(pokemon.name)
            Log.d("PokemonList", "${pokemonName}, ${pokemon.url}")
            PokemonItem(
                pokemon = pokemon,
                onClick = {
                    Log.d("PokemonItem","Click - ${pokemon.name}")
                    onPokemonClick(pokemon.name)
                }
            )
        }
    }
}