package no.kristiania.pokedex

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.istudio.pokedex.data.remote.responses.Pokemon
import com.istudio.pokedex.data.remote.responses.Result
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

// The Clean Architecture Pattern?!

// Data Layer
object RetrofitClient {
    private fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService = getClient().create(PokeApi::class.java)
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "pokemon_list_screen") {
                composable(route = "pokemon_list_screen",
                    enterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    }) {
                    println("pokemon_list_screen")
                    PokemonListScreen(navController = navController)
                }
                composable(route = "pokemon_detail_screen/{pokemonName}",
                    arguments = listOf(navArgument("pokemonName") {
                        type = NavType.StringType
                    }),
                    enterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    }) {
                    val pokemonName = remember {
                        it.arguments?.getString("pokemonName")
                    }
                    println("pokemon_detail_screen - ${pokemonName}")

                    PokemonDetailScreen(navController = navController,
                        pokemonName = pokemonName?.lowercase(Locale.ROOT) ?: "")
                }
            }
        }
    }
}

// Presentation Layer
@Composable
fun PokemonListScreen(navController: NavController) {
    PokemonLazyList(onItemClick = { pokemon ->
        navController.navigate(
            "pokemon_detail_screen/${pokemon.name}"
        )
    })
}

@Composable 
fun PokemonDetailScreen(navController: NavController,
                        pokemonName: String) {
    val vm: PokemonDetailViewModel = viewModel()
    vm.getPokemonInfo(pokemonName)

    Box(modifier = Modifier
        .fillMaxSize()) {
        Column {
            Button(onClick = { navController.popBackStack() }) {
                Text("Back Button")
            }
            vm.pokemon.value?.let {
                AsyncImage(model = PokemonUtils.formatPokemonUrl("${it.id}"),
                    contentDescription = pokemonName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(250.dp))
                Text(text = "Name: " + it.name, fontSize = 24.sp)
                Text(text = "Weight: " + it.weight + "kg", fontSize = 24.sp)
            }
        }

    }

}

@Composable
fun PokemonLazyList(onItemClick:(Result)-> Unit) {
    val vm: PokemenViewModel = viewModel()

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        val pokemen = vm.pokemen
        items(pokemen) { pokemon ->
            val imageURL = PokemonUtils.formatPokemonUrl(pokemon.url)
            val pokemonName = PokemonUtils.capitalizeText(pokemon.name)
            Log.d("PokemonList", "${pokemonName}, ${pokemon.url}")
            Box(modifier = Modifier.clickable {
                onItemClick(pokemon)
            }) {
                AsyncImage(model = imageURL,
                    contentDescription = pokemonName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(250.dp))
                Text(fontSize = 24.sp, text = pokemonName)
            }
        }
    }
}

class PokemenViewModel : ViewModel() {
    val pokemen = mutableStateListOf<Result>()

    init {
        getPokemen()
    }
    fun getPokemen() {
        viewModelScope.launch {
            val list = RetrofitClient.apiService.getPokemonList(limit = 60, offset = 0)
            pokemen.addAll(list.results)
        }
    }
}

class PokemonDetailViewModel : ViewModel() {
    private val _pokemon: MutableState<Pokemon?> = mutableStateOf(null)
    val pokemon: State<Pokemon?> = _pokemon

    fun getPokemonInfo(pokemonName: String) {
        viewModelScope.launch {
            _pokemon.value = RetrofitClient.apiService.getPokemonInfo(pokemonName)
        }
    }
}
