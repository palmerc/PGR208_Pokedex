import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.kristiania.pokedex.data.PokemonRepository

class PokemenListViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _pokemon = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemon = _pokemon.asStateFlow()

    init {
        getPokemen()
    }
    fun getPokemen() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            _pokemon.value = PokemonRepository.getPokemen()
            _loading.value = false
        }
    }
}