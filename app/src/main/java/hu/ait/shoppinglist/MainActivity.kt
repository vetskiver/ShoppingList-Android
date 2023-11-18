package hu.ait.shoppinglist

import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import hu.ait.shoppinglist.ui.screen.ShoppingListScreen
import hu.ait.shoppinglist.ui.screen.ShoppingSummaryScreen
import hu.ait.shoppinglist.ui.screen.SplashScreen
import hu.ait.shoppinglist.ui.theme.ShoppingAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = stringResource(R.string.splash)
                    ) {
                        composable("splash") {
                            SplashScreen {
                                Handler().postDelayed({
                                    navController.navigate(getString(R.string.shoppinglist))
                                }, 3000)
                            }
                        }

                        composable("shoppinglist") {
                            ShoppingListScreen(
                                onNavigateToSummary = { all, bought ->
                                    navController.navigate("shoppingsummary/$all/$bought")
                                }
                            )
                        }

                        composable(
                            "shoppingsummary/{numallshopping}/{numbought}",
                            arguments = listOf(
                                navArgument(getString(R.string.numallshopping)) { type = NavType.IntType },
                                navArgument(getString(R.string.numbought)) { type = NavType.IntType }
                            )
                        ) {
                            val numallshopping = it.arguments?.getInt("numallshopping")
                            val numbought = it.arguments?.getInt("numbought")
                            if (numallshopping != null && numbought != null) {
                                ShoppingSummaryScreen(
                                    numallshopping = numallshopping,
                                    numboughtshopping = numbought
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = stringResource(R.string.splash)
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable("splash") {
            SplashScreen {
                navController.navigate(R.string.shoppinglist)
            }
        }

        composable("shoppinglist") {
            ShoppingListScreen(
                onNavigateToSummary = { all, bought ->
                    navController.navigate("shoppingsummary/$all/$bought")
                }
            )
        }

        composable("shoppingsummary/{numallshopping}/{numbought}",
            arguments = listOf(
                navArgument("numallshopping") { type = NavType.IntType },
                navArgument("numbought") { type = NavType.IntType }
            )
        ) {
            val numallshopping = it.arguments?.getInt("numallshopping")
            val numbought = it.arguments?.getInt("numbought")
            if (numallshopping != null && numbought != null) {
                ShoppingSummaryScreen(
                    numallshopping = numallshopping,
                    numboughtshopping = numbought
                )
            }
        }
    }
}
