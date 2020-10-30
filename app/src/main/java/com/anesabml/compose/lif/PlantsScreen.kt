package com.anesabml.compose.lif

import android.content.res.Configuration
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BaseTextField
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.contentColor
import androidx.compose.foundation.currentTextStyle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabConstants.defaultTabIndicatorOffset
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onActive
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focusObserver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
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
fun PlantsScreen(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val topLeftCornerSize = CornerSize(20)
    val otherCornerSize = CornerSize(0)
    var selectedIndex by mutableStateOf(0)
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(
            topLeft = topLeftCornerSize,
            topRight = otherCornerSize,
            bottomRight = otherCornerSize,
            bottomLeft = otherCornerSize
        ),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 8.dp,
    ) {
        Column(
            Modifier
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.your_plants),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(
                    top = 24.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                    start = 24.dp
                )
            )
            val searchState = rememberSearchState()
            SearchBar(
                query = searchState.query,
                onQueryChange = { searchState.query = it },
                searchFocused = searchState.focused,
                onSearchFocusChange = { searchState.focused = it },
                onClearQuery = { searchState.query = TextFieldValue("") },
                searching = searchState.searching,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.9f)
                    .preferredHeight(56.dp)
                    .padding(all = 8.dp)
            )
            BaseTabRow(
                selectedTabIndex = selectedIndex,
                onTabSelected = { selectedIndex = it }
            )
            val listState = rememberLazyListState()
            LazyRowFor(items = plants, state = listState) {
                PlantCardItem(
                    modifier = Modifier
                        .size(height = 350.dp, width = 250.dp)
                        .clickable {
//                            navController.navigate(Screen.PlantDetails.route)
                        },
                    plant = it,
                    paddingValues = PaddingValues(
                        start = 8.dp,
                        top = 16.dp,
                        end = 8.dp,
                        bottom = 16.dp
                    )
                )
            }
            Text(
                text = stringResource(id = R.string.upcoming_actions),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
            )
            LazyColumnFor(items = plants) {
                UpcomingActionItem(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                    plant = it
                )
            }
        }
    }
}

@Composable
fun UpcomingActionItem(modifier: Modifier = Modifier, plant: Plant) {
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

@Composable
fun SeekBar(
    modifier: Modifier = Modifier,
    progress: Float,
    maxProgress: Float,
    tint: Color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.4f),
    progressTint: Color = MaterialTheme.colors.secondaryVariant,
    shape: Shape = CircleShape,
) {
    WithConstraints(modifier) {
        val progressWidth = maxWidth * progress / maxProgress
        val preferredHeight = Modifier.preferredHeight(4.dp)
        Spacer(
            modifier = preferredHeight
                .width(maxWidth)
                .background(shape = shape, color = tint)
        )
        Spacer(
            modifier = preferredHeight
                .width(progressWidth)
                .background(shape = shape, color = progressTint)
        )
    }
}

@Composable
fun PlantCardItem(modifier: Modifier = Modifier, plant: Plant, paddingValues: PaddingValues) {
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colors
    val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    val remainingDays = abs(plant.nextDayWatering - today)
    Card(
        modifier = modifier.padding(paddingValues),
        shape = RoundedCornerShape(10.dp),
        elevation = 8.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                asset = imageResource(plant.image),
                modifier = Modifier
                    .padding(all = 8.dp)
                    .weight(1f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = MaterialTheme.colors.secondaryVariant,
                        shape = RoundedCornerShape(10.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    Modifier.padding(all = 16.dp)
                ) {
                    Text(
                        text = plant.name,
                        style = typography.h6,
                        color = colors.onSecondary,
                    )
                    Text(
                        text = plant.nickname,
                        style = typography.body1,
                        color = colors.onSecondary,
                    )
                    Text(
                        text = stringResource(
                            id = R.string.days_until_next_water,
                            remainingDays
                        ),
                        style = typography.body2,
                        color = colors.onSecondary,
                    )

                }
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        asset = Icons.Rounded.ArrowForward,
                        tint = colors.onSecondary,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayout::class)
@Composable
private fun BaseTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        TabIndicator(
            Modifier.defaultTabIndicatorOffset(tabPositions[selectedTabIndex])
        )
    }
    TabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        indicator = indicator
    ) {
        Tab(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            selected = selectedTabIndex == 0,
            onClick = { onTabSelected(0) },
            text = {
                Text(
                    text = stringResource(id = R.string.all),
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Normal,
                )
            }
        )
        Tab(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            selected = selectedTabIndex == 1,
            onClick = { onTabSelected(1) },
            text = {
                Text(
                    text = stringResource(id = R.string.indoor),
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Normal,
                )
            }
        )
        Tab(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            selected = selectedTabIndex == 2,
            onClick = { onTabSelected(2) },
            text = {
                Text(
                    text = stringResource(id = R.string.outdoor),
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Normal,
                )
            }
        )
        Tab(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            selected = selectedTabIndex == 3,
            onClick = { onTabSelected(3) },
            text = {
                Text(
                    text = stringResource(id = R.string.other),
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Normal,
                )
            }
        )
    }
}


@Composable
fun TabIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground
) {
    Spacer(
        modifier.fillMaxWidth()
            .preferredHeight(2.dp)
            .background(color, CircleShape)
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalFocus::class)
@Composable
private fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    searchFocused: Boolean,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    searching: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.secondaryVariant,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        if (query.text.isEmpty()) {
            SearchHint()
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
        ) {
            if (searchFocused) {
                IconButton(onClick = onClearQuery) {
                    Icon(
                        asset = Icons.Outlined.ArrowBack
                    )
                }
            }
            BaseTextField(
                value = query,
                onValueChange = onQueryChange,
                textStyle = currentTextStyle().copy(color = contentColor()),
                imeAction = ImeAction.Search,
                onImeActionPerformed = { /* TODO: 10/6/20 */ },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .focusObserver {
                        onSearchFocusChange(it.isFocused)
                    }
            )
            IconButton(
                onClick = {},
                modifier = Modifier.background(
                    color = MaterialTheme.colors.secondaryVariant,
                    shape = RoundedCornerShape(8.dp)
                )
            ) {
                Icon(
                    asset = Icons.Outlined.Search,
                    tint = MaterialTheme.colors.onSecondary
                )
            }
            if (searching) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .preferredSize(36.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchHint(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxSize().wrapContentSize()
    ) {
        Text(
            text = stringResource(R.string.search_lif),
        )
    }
}

@Composable
private fun rememberSearchState(
    query: TextFieldValue = TextFieldValue(""),
    focused: Boolean = false,
    searching: Boolean = false,
    searchResults: List<String> = emptyList()
): SearchState {
    return remember {
        SearchState(
            query = query,
            focused = focused,
            searching = searching,
            searchResults = searchResults
        )
    }
}

class SearchState(
    query: TextFieldValue,
    focused: Boolean,
    searching: Boolean,
    searchResults: List<String>
) {
    var query by mutableStateOf(query)
    var focused by mutableStateOf(focused)
    var searching by mutableStateOf(searching)
    var searchResults by mutableStateOf(searchResults)
}

@Preview
@Composable
fun PlantsScreenPreview() {
    LifTheme {
        PlantsScreen()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlantsScreenDarkPreview() {
    LifTheme {
        PlantsScreen()
    }
}