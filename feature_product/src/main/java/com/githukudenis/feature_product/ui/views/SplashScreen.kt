package com.githukudenis.feature_product.ui.views

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.githukudenis.feature_product.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onNavigateToProducts: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        val animateSplashImage = remember {
            Animatable(0f)
        }

        LaunchedEffect(key1 = Unit) {
            animateSplashImage.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = EaseOut
                )
            )
            delay(timeMillis = 3000)
            onNavigateToProducts()
        }

        Image(
            modifier = modifier.offset {
                IntOffset(
                    x = 0.dp.toPx().toInt(),
                    y = -10.dp.toPx().toInt() * animateSplashImage.value.toInt()
                )
            },
            painter = painterResource(id = R.drawable.shopping_cart),
            contentDescription = null
        )

    }
}