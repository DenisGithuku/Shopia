package com.githukudenis.feature_cart.ui.views.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    onOpenProductDetails: (Int) -> Unit,
    cartViewModel: CartViewModel = hiltViewModel(),
) {
    val state by cartViewModel.uiState
    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        state = lazyColumnState
    ) {
        state.cartState?.let { cart ->
            items(cart.products) { product ->
                ProductInCartItem(
                    productInCart = product,
                    onOpenProductDetails = { productId ->
                        onOpenProductDetails(productId)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductInCartItem(
    productInCart: ProductInCart, modifier: Modifier = Modifier, onOpenProductDetails: (Int) -> Unit
) {
    productInCart.productDBO?.let { product ->
        Row(verticalAlignment = Alignment.Top, modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onOpenProductDetails(product.id)
            }
            .padding(12.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            GlideImage(
                model = product.image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = modifier.requiredSize(120.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = product.title, style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ), maxLines = 3, overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.description, maxLines = 4, overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${product.price}"
                )
                Text(
                    text = "Count: ${productInCart.quantity}"
                )
            }
        }
    }
}

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen(onOpenProductDetails = {})
}