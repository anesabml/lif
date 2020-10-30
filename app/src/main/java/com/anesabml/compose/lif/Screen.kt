package com.anesabml.compose.lif

import androidx.annotation.StringRes

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Plants : Screen("plants", R.string.your_plants)
    object PlantDetails : Screen("plant_details/{plant}", R.string.details)
}
