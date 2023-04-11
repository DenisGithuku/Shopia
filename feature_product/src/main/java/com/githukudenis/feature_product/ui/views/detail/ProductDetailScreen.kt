package com.githukudenis.feature_product.ui.views.detail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
) {
    val productsDetailViewModel: ProductsDetailViewModel = hiltViewModel()
    val state by productsDetailViewModel.state
    val scope = rememberCoroutineScope()

    if (state.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(key1 = state) {
        if (state.userMessages.isNotEmpty()) {
            val (id, userMessage) = state.userMessages[0]
            userMessage?.let { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = message, duration = SnackbarDuration.Long
                    )
                    id?.let {
                        productsDetailViewModel.onEvent(ProductDetailEvent.DismissUserMessage(it))
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val (id, category, title, description, price, image, rating) = state.product

        GlideImage(
            model = image,
            contentDescription = "Product image",
            modifier = modifier
                .sizeIn(maxHeight = 200.dp)
                .align(CenterHorizontally),
            contentScale = ContentScale.Fit
        )

        Text(modifier = modifier,
            text = title?.replaceFirstChar { char -> char.uppercase() } ?: "",
            style = TextStyle(
                fontWeight = FontWeight.Bold, fontSize = 20.sp
            ))
        Text(
            text = description ?: "", textAlign = TextAlign.Justify
        )
        Text(text = "$ $price",
            textAlign = TextAlign.End,
            color = Color.White,
            modifier = modifier
                .drawBehind {
                    drawRoundRect(
                        color = Color.Blue,
                        cornerRadius = CornerRadius(x = 32.dp.toPx(), y = 32.dp.toPx())
                    )
                }
                .padding(12.dp))

        Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Rated: "
            )
            Row(
                modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                rating?.let { rating ->
                    for (i in 0..rating.roundToInt()) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            tint = Color(0xFFF7980C),
                            contentDescription = "Product rating"
                        )
                    }
                }
            }
        }
        AddToCartSection(onAddToCart = { quantity ->
            productsDetailViewModel.onEvent(
                ProductDetailEvent.AddToCart(quantity)
            )
        }, productInCart = state.product.productInCart, removeFromCart = { productsDetailViewModel.onEvent(ProductDetailEvent.RemoveFromCart) })
    }
}

@Composable
fun AddToCartSection(
    modifier: Modifier = Modifier,
    onAddToCart: (Int) -> Unit,
    productInCart: Boolean,
    removeFromCart: () -> Unit
) {
    var productCount by rememberSaveable {
        mutableStateOf(0)
    }

    val buttonEnabled = remember {
        derivedStateOf {
            productCount >= 1
        }
    }
    Crossfade(targetState = productInCart) { status ->
        when (status) {
            true -> {
                Row(
                    modifier = modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = removeFromCart, shape = RoundedCornerShape(32.dp)) {
                        Text(
                            text = "Remove from cart"
                        )
                    }
                }
            }

            false -> {
                Row(
                    modifier = modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    OutlinedButton(
                        modifier = modifier.weight(2f), onClick = {
                            onAddToCart(productCount)
                        }, shape = RoundedCornerShape(32.dp), enabled = buttonEnabled.value

                    ) {
                        Text(
                            "Add to cart",
                        )
                    }
                    Row(
                        modifier = modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(color = MaterialTheme.colors.primary)
                            .clickable {
                                productCount += 1
                            }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add one",
                                tint = MaterialTheme.colors.onPrimary,
                                modifier = modifier.padding(8.dp)
                            )
                        }
                        Text(
                            text = "$productCount", style = TextStyle(
                                fontSize = 16.sp
                            )
                        )
                        Box(modifier = modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(color = MaterialTheme.colors.primary)
                            .clickable {
                                if (productCount == 0) {
                                    return@clickable
                                }
                                productCount -= 1
                            }) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Minus one",
                                tint = MaterialTheme.colors.onPrimary,
                                modifier = modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CartSectionPreview() {
    AddToCartSection(onAddToCart = {}, productInCart = true, removeFromCart = {})
}