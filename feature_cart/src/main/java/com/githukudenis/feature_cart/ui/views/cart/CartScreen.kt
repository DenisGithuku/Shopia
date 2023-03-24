package com.githukudenis.feature_cart.ui.views.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = hiltViewModel(),
) {
    val state by cartViewModel.uiState
    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        state = lazyColumnState
    ) {
        state.cartState?.let { cart ->
            items(cart.products) { product ->
                Card(
                    modifier = modifier,
                    onClick = {}
                ) {

                    Column(modifier = modifier.fillParentMaxWidth().padding(12.dp)) {
                        Text(
                            text = product.productDBO?.title ?: return@Card,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(text = product.productDBO.category)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen()
}