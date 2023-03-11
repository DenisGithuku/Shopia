package com.githukudenis.coroutinesindustrialbuild.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun CountriesScreen(modifier: Modifier = Modifier) {
    val countriesViewmodel: CountriesViewModel = viewModel()
    val state by countriesViewmodel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val isRefreshing = state.isRefreshing
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        countriesViewmodel.onEvent(CountriesScreenEvent.RefreshCountries)
    })

    var modalBottomSheetState = remember {
        ModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden
        )
    }

    ModalBottomSheetLayout(
        sheetShape = MaterialTheme.shapes.large.copy(
            CornerSize(16.dp)
        ),
        sheetState = modalBottomSheetState,
        sheetElevation = 16.dp,
        scrimColor = Color.LightGray.copy(alpha = 0.1f),
        sheetContent = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = state.selectedCountry?.name?.official ?: "No country selected"
                )
            }
        }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)) {

            LazyColumn {
                items(items = state.countries?.toList() ?: return@LazyColumn) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            countriesViewmodel
                                .onEvent(
                                    CountriesScreenEvent.OpenCountriesDetails(
                                        it.flag ?: return@clickable
                                    )
                                )
                                .also {
                                    coroutineScope.launch {
                                        if (!modalBottomSheetState.isVisible) {
                                            modalBottomSheetState.show()
                                        } else {
                                            modalBottomSheetState.hide()
                                        }
                                    }
                                }
                        }
                        .padding(12.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)

                    ) {

                        GlideImage(
                            model = it.flag, contentDescription = null
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = it.name?.official ?: return@Row
                            )
                            Text(
                                text = it.capital.first()
                            )
                            Text(
                                text = it.population.toString()
                            )
                        }
                    }
                }
            }


            if (state.error?.isNotEmpty() == true) {
                    Text(
                        text = state.error ?: "An unknown error occurred",
                        modifier = modifier.align(Alignment.Center)
                    )
            }


            if (state.isLoading) {
                CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
