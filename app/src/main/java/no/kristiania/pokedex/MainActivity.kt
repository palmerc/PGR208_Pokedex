package no.kristiania.pokedex

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import com.istudio.pokedex.data.remote.responses.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            MainView()
        }
    }
}

@Composable
fun MainView() {
    val pokemenRepository = PokemonRepository(RetrofitClient.apiService)
    val getPokemenUseCase = GetPokemenUseCase(pokemenRepository)
    val viewModel = PokemenViewModel(getPokemenUseCase)

    PokemenScreen(viewModel = viewModel)
}

// Presentation Layer
@Composable
fun PokemenScreen(viewModel: PokemenViewModel) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.getPokemen()
        }
    }

    val pokemen by viewModel.pokemen.collectAsState()

    PokemonList(pokemen)
}

@Composable
fun PokemonList(pokemen: List<Result>) {
    LazyColumn {
        items(pokemen) { pokemon ->
            Log.d("PokemonList", "${pokemon.name}, ${pokemon.url}")
            Text(text = pokemon.name)
        }
    }
}

// Domain Layer
class GetPokemenUseCase(private val pokemonRepository: PokemonRepository) {
    suspend operator fun invoke(): List<Result> {
        return pokemonRepository.getPokemen()
    }
}

class PokemenViewModel(private val getPokemenUseCase: GetPokemenUseCase) : ViewModel() {
    private val _pokemen = MutableStateFlow(emptyList<Result>())
    val pokemen: StateFlow<List<Result>> = _pokemen

    suspend fun getPokemen() {
        _pokemen.value = getPokemenUseCase()
    }
}
