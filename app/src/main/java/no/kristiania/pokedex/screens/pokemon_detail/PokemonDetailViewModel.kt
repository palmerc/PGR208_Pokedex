import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.kristiania.pokedex.data.PokemonRepository

class PokemonDetailsViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _selectedPokemon = MutableStateFlow<Pokemon?>(null)
    val selectedPokemon = _selectedPokemon.asStateFlow()

    fun setSelectedPokemon(pokemonName: String) {
        viewModelScope.launch {
            _loading.value = true
            _selectedPokemon.value = PokemonRepository.getPokemonByName(pokemonName)
            _loading.value = false
        }
    }
}