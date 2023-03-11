package com.githukudenis.coroutinesindustrialbuild.ui.views.products

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    onOpenProductDetails: (Int) -> Unit
) {
    val productsViewModel: ProductsViewModel = hiltViewModel()
    val state by productsViewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val isRefreshing = state.isRefreshing
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        productsViewModel.onEvent(ProductsScreenEvent.RefreshProducts)
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

        }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)) {

            LazyColumn {
                items(items = state.products) { productItem ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
//                            productsViewModel
//                                .onEvent(
//                                    ProductsScreenEvent.OpenProductDetails(
//                                        productItem.id
//                                    )
//                                )
//                                .also {
//                                    coroutineScope.launch {
//                                        if (!modalBottomSheetState.isVisible) {
//                                            modalBottomSheetState.show()
//                                        } else {
//                                            modalBottomSheetState.hide()
//                                        }
//                                    }
//                                }
                            onOpenProductDetails(productItem.id)
                        }
                        .padding(12.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)

                    ) {

                        GlideImage(
                            model = productItem.image,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = modifier.requiredSize(150.dp)
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = productItem.title
                            )
                            Text(
                                text = productItem.description,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${productItem.price}"
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

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
