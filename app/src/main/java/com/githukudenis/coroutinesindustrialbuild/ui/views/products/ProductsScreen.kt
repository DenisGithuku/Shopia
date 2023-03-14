package com.githukudenis.coroutinesindustrialbuild.ui.views.products

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier, onOpenProductDetails: (Int) -> Unit
) {
    val productsViewModel: ProductsViewModel = hiltViewModel()
    val state by productsViewModel.state.collectAsState()
    val isRefreshing = state.isRefreshing
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        productsViewModel.onEvent(ProductsScreenEvent.RefreshProducts)
    })
    val categories = state.categories.toList()

    val modalBottomSheetState = remember {
        ModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden
        )
    }

    ModalBottomSheetLayout(sheetShape = MaterialTheme.shapes.large.copy(
        CornerSize(16.dp)
    ),
        sheetState = modalBottomSheetState,
        sheetElevation = 16.dp,
        scrimColor = Color.LightGray.copy(alpha = 0.1f),
        sheetContent = {
            Column {
                Text("some text")
            }
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
                .pullRefresh(pullRefreshState)
        ) {

            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    LazyRow(
                        modifier = modifier.padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = categories
                        ) { category ->
                            CategoryItem(
                                category = category.value.replaceFirstChar { char -> char.uppercase() },
                                selected = state.selectedCategory == category.value,
                                onSelect = {
                                    productsViewModel.onEvent(
                                        ProductsScreenEvent.ChangeCategory(
                                            category.value
                                        )
                                    )
                                })
                        }
                    }
                }
                items(items = state.products) { productItem ->
                    Row(verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOpenProductDetails(productItem.id)
                            }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)

                    ) {

                        GlideImage(
                            model = productItem.image,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = modifier.requiredSize(120.dp)
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = productItem.title,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                ),
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
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

@Composable
fun CategoryItem(
    category: String, selected: Boolean, modifier: Modifier = Modifier, onSelect: (String) -> Unit
) {
    val interactionSource = MutableInteractionSource()
    val animateBorderColor =
        animateColorAsState(targetValue = if (!selected) MaterialTheme.colors.primary else Color.Transparent)
    val animateBgColor =
        animateColorAsState(targetValue = if (selected) MaterialTheme.colors.primary else Color.Transparent)
    Box(modifier = modifier
        .background(
            color = animateBgColor.value, shape = RoundedCornerShape(16.dp)
        )
        .border(
            width = 1.dp, color = animateBorderColor.value, shape = RoundedCornerShape(24.dp)
        )
        .clickable(indication = null, interactionSource = interactionSource) {
            onSelect(category)
        }) {
        Text(
            text = category,
            modifier = modifier.padding(8.dp),
            color = if (selected) Color.White else Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPrev() {
    CategoryItem(category = "Jewelery", selected = true, onSelect = {})
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SelectedCategoryItemPrev() {
    CategoryItem(category = "Jewelery", selected = false, onSelect = {})
}
