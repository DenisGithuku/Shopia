package com.githukudenis.feature_cart.ui.views.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    onOpenProductDetails: (Int) -> Unit,
    cartViewModel: CartViewModel = hiltViewModel(),
) {
    val state by cartViewModel.uiState
    val totalPrice = state.cartState?.let { cart ->
        cart.products.map { productInCart ->
            productInCart.productDBO?.price ?: 0.0
        }
    }?.let { prices ->
        prices.sumOf { price -> price }
    }

    val lazyColumnState = rememberLazyListState()
    val cartStateIsVisible = remember {
        derivedStateOf {
            lazyColumnState.firstVisibleItemIndex >= 1
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyColumnState,
            modifier = modifier.fillMaxWidth()
        ) {
            item {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total bill: $ $totalPrice",
                        style = MaterialTheme.typography.h6.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
            state.cartState?.let { cart ->
                items(cart.products) { product ->
                    ProductInCartItem(productInCart = product, onOpenProductDetails = { productId ->
                        onOpenProductDetails(productId)
                    })
                }
            }
        }
        AnimatedVisibility(
            visible = cartStateIsVisible.value,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Box(
                modifier = modifier
                    .padding(12.dp)
                    .align(Alignment.TopCenter)
                    .clip(
                        shape = RoundedCornerShape(32.dp)
                    )
                    .background(MaterialTheme.colors.primaryVariant),
                contentAlignment = Alignment.Center


            ) {
                Text(
                    text = "Total bill: $ $totalPrice",
                    style = MaterialTheme.typography.caption.copy(
                        color = MaterialTheme.colors.onPrimary
                    ),
                    modifier = modifier.padding(12.dp)
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