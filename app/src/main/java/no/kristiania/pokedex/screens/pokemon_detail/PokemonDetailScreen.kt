import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import no.kristiania.pokedex.PokemonUtils

@Composable
fun PokemonDetailScreen(
    viewModel: PokemonDetailsViewModel,
    onBackButtonClick: () -> Unit = {})
{
    val loading = viewModel.loading.collectAsState()
    val pokemonState = viewModel.selectedPokemon.collectAsState()

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

    val pokemon = pokemonState.value
    if (pokemon == null) {
        Text(text = "Failed to get Pokémon details. Selected Pokémon object is NULL!")
        return
    }

    val imageURL = PokemonUtils.formatPokemonUrl("${pokemon.id}")
    Column {
        IconButton(
            onClick = { onBackButtonClick() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Button"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
                .background(color = Color.White), // Background needs to come after shadow
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageURL,
                contentDescription = pokemon.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(250.dp)
            )
            Text(text = "Name: " + pokemon.name, fontSize = 24.sp)
            Text(text = "Weight: " + pokemon?.weight + "kg", fontSize = 24.sp)
        }
    }
}
