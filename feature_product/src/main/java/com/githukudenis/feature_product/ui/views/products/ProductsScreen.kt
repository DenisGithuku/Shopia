package com.githukudenis.feature_product.ui.views.products

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.githukudenis.feature_product.R


@Composable
fun ProductsRoute(
    snackbarHostState: SnackbarHostState,
    onOpenProfile: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenProductDetails: (Int) -> Unit,
    onOpenCart: () -> Unit
) {
    val context = LocalContext.current
    val productsViewModel: ProductsViewModel = hiltViewModel()
    val state by productsViewModel.state.collectAsStateWithLifecycle()
    val isRefreshing = state.isRefreshing
    val categories = state.categories.toList()
    var optionsMenuOpen by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = state.userMessages) {
        if (state.userMessages.isNotEmpty()) {
            val message = state.userMessages[0]
            snackbarHostState.showSnackbar(
                message = message.message ?: "An error occurred", duration = SnackbarDuration.Long
            )
            message.id?.let { ProductsScreenEvent.DismissUserMessage(it) }
                ?.let { productsViewModel.onEvent(it) }
        }
    }


    ProductsScreen(onOpenProfile = { onOpenProfile() },
        onOpenAbout = { onOpenAbout() },
        onOpenProductDetails = { onOpenProductDetails(it) },
        onOpenCart = { onOpenCart() },
        menuIsOpen = optionsMenuOpen,
        onToggleOptionsMenu = { optionsMenuOpen = !optionsMenuOpen },
        isRefreshing = isRefreshing,
        onRefresh = { productsViewModel.onEvent(ProductsScreenEvent.RefreshProducts) },
        state = state,
        onAddToCart = { productsViewModel.onEvent(ProductsScreenEvent.AddToCart(it)) },
        onChangeCategory = { productsViewModel.onEvent(ProductsScreenEvent.ChangeCategory(it)) },
        context = context
    )
}

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class
)
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    onOpenProfile: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenProductDetails: (Int) -> Unit,
    onOpenCart: () -> Unit,
    menuIsOpen: Boolean,
    onToggleOptionsMenu: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    state: ProductsScreenState,
    onAddToCart: (Int) -> Unit,
    onChangeCategory: (String) -> Unit,
    context: Context
) {

    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
            .pullRefresh(pullRefreshState)
    ) {

        LazyColumn(
            modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Shopia", style = TextStyle(
                            fontSize = 30.sp, fontWeight = FontWeight.Bold
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        state.cartState?.let { cart ->
                            cart.products.size.let { count ->
                                CartItem(
                                    itemCount = count,
                                    onOpenCart = onOpenCart,
                                    modifier = modifier,
                                    contentDescription = context.getString(R.string.cart)
                                )
                            }
                        }
                        Crossfade(
                            targetState = state.userState?.userLoading
                        ) { userLoading ->
                            userLoading?.let { loading ->
                                when (loading) {
                                    true -> {
                                        CircularProgressIndicator()
                                    }

                                    false -> {
                                        ProfileAvatar(username = "${state.userState?.currentUser?.username}",
                                            onClick = { onOpenProfile() })
                                    }
                                }

                            }
                        }

                        IconButton(onClick = {
                            onToggleOptionsMenu()
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More items"
                            )
                        }
                        DropdownMenu(expanded = menuIsOpen,
                            onDismissRequest = { onToggleOptionsMenu() }) {
                            DropdownMenuItem(onClick = {
                                onToggleOptionsMenu()
                                onOpenAbout()
                            }) {
                                Text(
                                    text = "About app"
                                )
                            }
                        }
                    }
                }
            }
            item {
                LazyRow(
                    modifier = modifier.padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = state.categories.toList()
                    ) { category ->
                        CategoryItem(category = category.value.replaceFirstChar { char -> char.uppercase() },
                            selected = state.selectedCategory == category.value,
                            onSelect = {
                                onChangeCategory(it)
                            })
                    }
                }
            }
            items(items = state.products) { productItem ->
                Row(verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            productItem.product?.id?.let { onOpenProductDetails(it) }
                        }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)

                ) {

                    GlideImage(
                        model = productItem.product?.image,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = modifier.requiredSize(120.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        productItem.product?.title?.let {
                            Text(
                                text = it, style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                ), maxLines = 3, overflow = TextOverflow.Ellipsis
                            )
                        }
                        productItem.product?.description?.let {
                            Text(
                                text = it, maxLines = 4, overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = "$ ${productItem.product?.price}"
                        )

                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {
                                    productItem.product?.id?.let {
                                        onAddToCart(it)
                                    }
                                },
                                shape = RoundedCornerShape(32.dp),
                                enabled = !productItem.productInCart,
                            ) {
                                Text(
                                    text = "Add to cart"
                                )
                            }
                            if (productItem.productInCart) {
                                Text(
                                    text = "In cart", style = TextStyle(
                                        fontStyle = FontStyle.Italic
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        if (state.error?.isNotEmpty() == true) {
            Text(
                text = state.error,
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

@Composable
fun CartItem(
    modifier: Modifier = Modifier,
    itemCount: Int,
    contentDescription: String,
    onOpenCart: () -> Unit
) {
    Box {
        IconButton(onClick = {
            onOpenCart()
        }) {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart, contentDescription = contentDescription
            )
        }
        Text(text = "$itemCount", style = TextStyle(
            color = MaterialTheme.colors.onPrimary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        ),

            modifier = modifier
                .align(Alignment.TopEnd)
                .drawBehind {
                    drawCircle(
                        color = Color.Red
                    )
                }
                .padding(4.dp))
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
            color = animateBgColor.value, shape = RoundedCornerShape(32.dp)
        )
        .border(
            width = 1.dp, color = animateBorderColor.value, shape = RoundedCornerShape(32.dp)
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

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier, username: String, onClick: (String) -> Unit
) {
    val initial by remember {
        mutableStateOf(username.first().uppercase())
    }
    Box(
        modifier = modifier
            .size(30.dp)
            .border(
                border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.primary),
                CircleShape
            )
            .padding(4.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colors.primary)
            .clickable { onClick(username) }, contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial, textAlign = TextAlign.Center, color = Color.White
        )
    }
}

@Preview
@Composable
fun ProfileAvatarPreview() {
    ProfileAvatar(username = "Allan", onClick = {})
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


@Preview
@Composable
fun CartItemPrev() {
    CartItem(itemCount = 12, contentDescription = "Cart", onOpenCart = {})
}