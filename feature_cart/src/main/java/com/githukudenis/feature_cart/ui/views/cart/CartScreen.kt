package com.githukudenis.feature_cart.ui.views.cart

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Maximize
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import kotlinx.coroutines.launch

@Composable
fun CartRoute(
    onOpenProductDetails: (Int) -> Unit, onNavigateUp: () -> Unit
) {
    val cartViewModel: CartViewModel = hiltViewModel()
    val state by cartViewModel.uiState


    CartScreen(onOpenProductDetails = onOpenProductDetails,
        cartState = state.cartState,
        onNavigateUp = onNavigateUp,
        onRemoveFromCart = {
            cartViewModel.removeItemFromCart(it)
        },
        onChangeCount = { id, count ->
            cartViewModel.changeProductCount(id, count)
        })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartState: CartState?,
    onOpenProductDetails: (Int) -> Unit,
    onRemoveFromCart: (Int) -> Unit,
    onChangeCount: (Int, Int) -> Unit,
    onNavigateUp: () -> Unit
) {
    val totalPrice = cartState?.let { cart ->
        cart.products.map { productInCart ->
            productInCart.productDBO?.price ?: 0.0
        }
    }?.let { prices ->
        prices.sumOf { price -> price }
    }?.toFloat()

    val lazyColumnState = rememberLazyListState()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


    ModalBottomSheetLayout(sheetState = sheetState,
        sheetShape = RoundedCornerShape(32.dp),
        sheetBackgroundColor = MaterialTheme.colors.surface,
        sheetContent = {
            Column(
                modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Payment Details",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "No payment methods have been added. Would you like" + " to add a payment method for a seamless checkout process?",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f)
                )

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Text(
                        text = "Add payment method",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }) {

        val interactionSource = remember {
            MutableInteractionSource()
        }

        val coroutineScope = rememberCoroutineScope()

        LazyColumn(
            state = lazyColumnState, modifier = modifier.fillMaxWidth()
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, contentDescription = "Back"
                        )
                    }
                    Text(
                        text = "My cart", style = MaterialTheme.typography.body1
                    )
                    Box(
                        modifier = Modifier
                            .border(
                                width = 0.5.dp, color = Color.LightGray, shape = CircleShape
                            )
                            .clickable(interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    coroutineScope.launch {
                                        if (sheetState.isVisible) {
                                            sheetState.hide()
                                        } else {
                                            sheetState.show()
                                        }
                                    }
                                })
                    ) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            imageVector = Icons.Default.MoreHoriz,
                            tint = Color.LightGray,
                            contentDescription = "More options"
                        )
                    }
                }
            }
            cartState?.let { cart ->
                items(cart.products) { product ->
                    ProductInCartItem(
                        productInCart = product, onOpenProductDetails = { productId ->
                            onOpenProductDetails(productId)
                        }, onRemoveFromCart = onRemoveFromCart, onChangeCount = onChangeCount
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Subtotal:",
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "$totalPrice",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Delivery fee:",
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "$5",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Discount:",
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "$45",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                        )
                    }
                    Divider(
                        color = Color.LightGray, thickness = 0.6.dp
                    )
                }
            }
            item {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "Checkout for $$totalPrice",
                        style = MaterialTheme.typography.body1,
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductInCartItem(
    productInCart: ProductInCart,
    modifier: Modifier = Modifier,
    onOpenProductDetails: (Int) -> Unit,
    onRemoveFromCart: (Int) -> Unit,
    onChangeCount: (Int, Int) -> Unit
) {
    productInCart.productDBO?.let { product ->
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onOpenProductDetails(product.id)
                }
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            GlideImage(
                model = product.image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = modifier.requiredSize(60.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(4f), text = product.title, style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    IconButton(modifier = Modifier.weight(1f),
                        onClick = { onRemoveFromCart(product.id) }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove from cart",
                            tint = Color.LightGray
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "$${product.price}",
                        style = MaterialTheme.typography.body2,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CountButton(imageVector = Icons.Default.Maximize,
                            contentDescription = "Minus",
                            onClick = {
                                productInCart.quantity?.let {
                                    if (productInCart.quantity <= 0) return@CountButton

                                    onChangeCount(
                                        productInCart.productDBO.id, productInCart.quantity.minus(1)
                                    )
                                }
                            })
                        Text(
                            text = "${productInCart.quantity}"
                        )
                        CountButton(imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            onClick = {
                                productInCart.quantity?.let {
                                    onChangeCount(
                                        productInCart.productDBO.id,
                                        productInCart.quantity.plus(1)
                                    )
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun CountButton(
    imageVector: ImageVector, contentDescription: String, onClick: () -> Unit
) {
    Box(modifier = Modifier
        .clickable { onClick() }
        .border(
            width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp)
        )
        .padding(4.dp), contentAlignment = Alignment.Center) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen(onOpenProductDetails = {},
        cartState = CartState(),
        onNavigateUp = {},
        onRemoveFromCart = {},
        onChangeCount = { _, _ ->})
}