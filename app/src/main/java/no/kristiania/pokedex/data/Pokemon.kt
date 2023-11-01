data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int?,
    val weight: Int?,
    val url: String)

data class PokemonListResponse(
    val results: List<Pokemon>
)