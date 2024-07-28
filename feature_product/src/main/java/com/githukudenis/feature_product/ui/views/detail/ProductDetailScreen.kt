package com.githukudenis.feature_product.ui.views.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
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


@Composable
fun ProductDetailRoute(
    snackbarHostState: SnackbarHostState, modifier: Modifier = Modifier, onNavigateUp: () -> Unit
) {
    val productsDetailViewModel: ProductsDetailViewModel = hiltViewModel()
    val state by productsDetailViewModel.state
    val scope = rememberCoroutineScope()

    if (state.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Center
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

    ProductDetailScreen(
        modifier = modifier,
        state = state,
        onAddToCart = { productsDetailViewModel.onEvent(ProductDetailEvent.AddToCart(it)) },
        onRemoveFromCart = { productsDetailViewModel.onEvent(ProductDetailEvent.RemoveFromCart) },
        onNavigateUp = onNavigateUp,
        onToggleFavourite = { productsDetailViewModel.onEvent(ProductDetailEvent.ToggleFavourite)}
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    state: ProductDetailScreenState,
    onAddToCart: (Int) -> Unit,
    onRemoveFromCart: () -> Unit,
    onNavigateUp: () -> Unit,
    onToggleFavourite: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val (id, category, title, description, price, image, rating, isFavourite) = state.product

        Box {
            GlideImage(
                model = image,
                contentDescription = "Product image",
                modifier = modifier.sizeIn(maxHeight = 200.dp),
                contentScale = ContentScale.Fit
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = "Back"
                    )
                }
                Row {
                    IconButton(onClick = {
                        if (id != null) {
                            onToggleFavourite()
                        }
                    }) {
                        Icon(
                            imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                            tint = if (isFavourite) Color(0xFFF7980C) else MaterialTheme.colors.onBackground,
                            contentDescription = if (isFavourite) "Remove from favourites" else "Add to favourites"
                        )
                    }
                }
            }
        }

        Text(modifier = modifier,
            text = title?.replaceFirstChar { char -> char.uppercase() } ?: "",
            style = TextStyle(
                fontWeight = FontWeight.Bold, fontSize = 20.sp
            ))
        Text(
            text = description ?: "", textAlign = TextAlign.Justify
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            InfoPill {
                Icon(
                    imageVector = Icons.Default.Star,
                    tint = Color(0xFFF7980C),
                    contentDescription = "Product rating",
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "${state.product.image}",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f)
                )
            }
        }

        AddToCartSection(
            onAddToCart = { quantity ->
                onAddToCart(quantity)
            },
            productInCart = state.product.productInCart,
            price = "$${state.product.price}",
            id = state.product.id ?: return,
            removeFromCart = onRemoveFromCart
        )
    }
}

@Composable
fun InfoPill(
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = Modifier.border(
            width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(32.dp)
        ), contentAlignment = Center
    ) {
        content

    }
}

@Composable
fun AddToCartSection(
    modifier: Modifier = Modifier,
    onAddToCart: (Int) -> Unit,
    price: String,
    id: Int,
    productInCart: Boolean,
    removeFromCart: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = price,
            style = MaterialTheme.typography.h5,
        )
        if (!productInCart) {
            Button(
                onClick = { onAddToCart(id) },
                elevation = ButtonDefaults.elevation(0.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            ) {
                Text(
                    text = "Add to cart",
                )
            }
        }
    }
}

@Preview
@Composable
fun CartSectionPreview() {
    AddToCartSection(
        onAddToCart = {},
        productInCart = true,
        price = "$518",
        id = 3,
        removeFromCart = {})
}