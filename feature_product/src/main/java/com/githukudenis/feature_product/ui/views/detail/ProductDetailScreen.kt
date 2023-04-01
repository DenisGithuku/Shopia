package com.githukudenis.feature_product.ui.views.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(key1 = state) {
        if (state.userMessages.isNotEmpty()) {
            val (id, userMessage) = state.userMessages[0]
            userMessage?.let{ message ->
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message  = message,
                        duration = SnackbarDuration.Long
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

        Text(
            modifier = modifier,
            text = title?.replaceFirstChar { char -> char.uppercase() } ?: "",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
        Text(
            text = description ?: "",
            textAlign = TextAlign.Justify
        )
        Text(
            text = price ?: "0",
            textAlign = TextAlign.End,
            color = Color.White,
            modifier = modifier
                .drawBehind {
                    drawRoundRect(
                        color = Color.Blue,
                        cornerRadius = CornerRadius(x = 32.dp.toPx(), y = 32.dp.toPx())
                    )
                }
                .padding(12.dp)
        )

        Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Rated: "
            )
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
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
    }

}