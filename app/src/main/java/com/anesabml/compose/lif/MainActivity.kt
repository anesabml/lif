package com.anesabml.compose.lif

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.platform.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.ui.tooling.preview.Preview
import com.anesabml.compose.lif.module.Plant
import com.anesabml.compose.lif.ui.LifTheme
import com.anesabml.compose.lif.ui.pink500

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifTheme(darkTheme = false /* TODO: remove this */) {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            Header(
                Modifier.fillMaxWidth()
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Plants.route
        ) {
            composable(Screen.Plants.route) {
                PlantsScreen(navController)
            }
            composable(
                Screen.PlantDetails.route,
                listOf(
                    navArgument("plant") {
                        type = NavType.ParcelableType(Plant::class.java)
                    })
            ) { backStackEntry ->
                PlantDetails(
                    navController,
                    plant = backStackEntry.arguments?.getParcelable("plant")!!
                )
            }
        }
    }
}

@Composable
fun Header(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colors
    Box(
        modifier
            .background(
                LinearGradient(
                    0f to colors.primaryVariant,
                    0.5f to pink500,
                    1f to colors.primary,
                    startX = 0f,
                    endX = 1000f,
                    startY = 0f,
                    endY = 100f
                )
            ),
        alignment = Alignment.Center
    ) {
        IconButton(
            onClick = {}, modifier
                .wrapContentWidth(Alignment.End)
        ) {
            Icon(asset = Icons.Rounded.Menu)
        }
    }
}

@Preview
@Composable
fun PreviewAppContent() {
    LifTheme {
        AppContent()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppContentDark() {
    LifTheme {
        AppContent()
    }
}

@Preview
@Composable
fun PreviewHeader() {
    LifTheme {
        Header()
    }
}
