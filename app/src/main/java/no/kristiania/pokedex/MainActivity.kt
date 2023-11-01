package no.kristiania.pokedex

import PokemenListViewModel
import PokemonDetailScreen
import PokemonDetailsViewModel
import PokemonListScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    private val _pokemonListViewModel: PokemenListViewModel by viewModels()
    private val _pokemonDetailsViewModel: PokemonDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "pokemon_list_screen") {

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
                    PokemonListScreen(
                        viewModel = _pokemonListViewModel,
                        onPokemonClick = { pokemonName ->
                            Log.d("MainActivity", "Click - ${pokemonName}")
                            navController.navigate("pokemon_detail_screen/$pokemonName")
                        }
                    )
                }
                composable(
                    route = "pokemon_detail_screen/{pokemonName}",
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
                    }
                ) {backStackEntry ->
                    val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: ""

                    println("pokemon_detail_screen - ${pokemonName}")

                    LaunchedEffect(pokemonName) {
                        _pokemonDetailsViewModel.setSelectedPokemon(pokemonName)
                    }

                    PokemonDetailScreen(
                        viewModel = _pokemonDetailsViewModel,
                        onBackButtonClick = { navController.popBackStack() })
                }
            }
        }
    }
}
