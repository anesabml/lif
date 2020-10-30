package com.anesabml.compose.lif

import android.content.res.Configuration
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.ui.tooling.preview.Preview
import com.anesabml.compose.lif.module.Plant
import com.anesabml.compose.lif.module.plants
import com.anesabml.compose.lif.ui.LifTheme
import java.util.Calendar
import kotlin.math.abs

@Composable
fun PlantDetails(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier,
    plant: Plant
) {
    val topLeftCornerSize = CornerSize(20)
    val otherCornerSize = CornerSize(0)
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colors
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(
            topLeft = topLeftCornerSize,
            topRight = otherCornerSize,
            bottomRight = otherCornerSize,
            bottomLeft = otherCornerSize
        ),
        backgroundColor = colors.background,
        elevation = 8.dp,
    ) {
        Column(modifier) {
            Row {
                Image(
                    asset = imageResource(plant.image),
                    modifier = Modifier
                        .weight(1f)
                        .padding(all = 8.dp)
                        .height(300.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(0.2f)
                        .height(300.dp)
                ) {
                    Icon(
                        asset = vectorResource(id = R.drawable.sun),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        asset = vectorResource(id = R.drawable.drop),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Text(
                text = plant.name,
                style = typography.h5,
                color = colors.secondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            )
            Text(
                text = plant.nickname,
                style = typography.body1,
                color = colors.secondary,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            )
            UpcomingWaterDay(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                plant = plant
            )
            Text(
                text = stringResource(id = R.string.log_activity),
                style = typography.h6,
                color = colors.onSecondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            )
            Row {
                WaterInterval(
                    Modifier
                        .padding(8.dp)
                        .height(200.dp)
                        .weight(1f)
                )
                FertilizerInterval(
                    Modifier
                        .padding(8.dp)
                        .height(200.dp)
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun WaterInterval(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(
        modifier = modifier
            .background(MaterialTheme.colors.primary),
        onClick = { onClick() },
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            text = stringResource(id = R.string.water),
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,

            )
    }
}

@Composable
fun FertilizerInterval(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(
        modifier = modifier
            .background(MaterialTheme.colors.secondary),
        onClick = { onClick() },
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            text = stringResource(id = R.string.fertilizer),
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun UpcomingWaterDay(modifier: Modifier = Modifier, plant: Plant) {
    val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    val remainingDays = abs(plant.nextDayWatering - today)
    val passedDays = abs(today - plant.lastDayWatering)
    val animatedProgress = animatedFloat(initVal = 0f)
    onActive {
        animatedProgress.animateTo(
            targetValue = passedDays.toFloat(),
            anim = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        )
    }
    val text =
        if (passedDays >= plant.wateringIntervalDay)
            stringResource(
                R.string.water_today,
                plant.name
            )
        else
            stringResource(
                id = R.string.water_in_days,
                formatArgs = arrayOf(plant.name, remainingDays)
            )
    Column(modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1
        )
        SeekBar(
            modifier = Modifier.padding(top = 8.dp),
            progress = animatedProgress.value,
            maxProgress = plant.wateringIntervalDay.toFloat()
        )
    }
}

@Preview
@Composable
fun PreviewPlantDetails() {
    LifTheme {
        PlantDetails(plant = plants.first())
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPlantDetailsDark() {
    LifTheme {
        PlantDetails(plant = plants.first())
    }
}