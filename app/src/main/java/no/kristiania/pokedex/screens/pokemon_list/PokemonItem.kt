package no.kristiania.pokedex.screens.pokemon_list

import Pokemon
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import no.kristiania.pokedex.PokemonUtils

@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onClick: () -> Unit = {}
) {
    val imageURL = PokemonUtils.formatPokemonUrl(pokemon.url)
    Log.d("PokemonItem","${imageURL}")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
            .shadow( // shadow includes clipping of content to shape (like '.clip()'-modifier)
                elevation = 4.dp,
                shape = RoundedCornerShape(10)
            )
            .background(color = Color.White) // Background needs to come after shadow
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageURL,
            contentDescription = pokemon.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(250.dp)
        )
        Text(fontSize = 24.sp, text = PokemonUtils.capitalizeText(pokemon.name))
    }
}
