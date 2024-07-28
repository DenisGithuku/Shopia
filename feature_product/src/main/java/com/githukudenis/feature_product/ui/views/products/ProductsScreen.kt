package com.githukudenis.feature_product.ui.views.products

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.githukudenis.core_data.data.local.db.model.product.ProductDBO
import com.githukudenis.core_design.theme.DefaultWhite
import com.githukudenis.core_design.theme.Green200
import com.githukudenis.core_design.theme.Green500
import com.githukudenis.core_design.theme.NeutralDark
import com.githukudenis.feature_product.R


@Composable
fun ProductsRoute(
    snackbarHostState: SnackbarHostState,
    onOpenProfile: () -> Unit,
    onOpenProductDetails: (Int) -> Unit,
    onOpenCart: () -> Unit
) {
    val context = LocalContext.current
    val productsViewModel: ProductsViewModel = hiltViewModel()
    val state by productsViewModel.state.collectAsStateWithLifecycle()
    val isRefreshing = state.isRefreshing
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


    ProductsScreen(
        onOpenProfile = { onOpenProfile() },
        onOpenProductDetails = { onOpenProductDetails(it) },
        onOpenCart = { onOpenCart() },
        menuIsOpen = optionsMenuOpen,
        onToggleOptionsMenu = { optionsMenuOpen = !optionsMenuOpen },
        isRefreshing = isRefreshing,
        onRefresh = { productsViewModel.onEvent(ProductsScreenEvent.RefreshProducts) },
        state = state,
//        onAddToCart = { productsViewModel.onEvent(ProductsScreenEvent.AddToCart(it)) },
        onChangeCategory = { productsViewModel.onEvent(ProductsScreenEvent.ChangeCategory(it)) },
        context = context,
        onSearch = {
            productsViewModel.onEvent(ProductsScreenEvent.Search)
        },
        onQueryChange = {
            productsViewModel.onEvent(ProductsScreenEvent.OnSearchQueryChange(it))
        })
}

@OptIn(
    ExperimentalMaterialApi::class
)
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    onOpenProductDetails: (Int) -> Unit,
    onOpenCart: () -> Unit,
    menuIsOpen: Boolean,
    onToggleOptionsMenu: () -> Unit,
    onOpenProfile: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    state: ProductsScreenState,
    onChangeCategory: (String) -> Unit,
    onSearch: () -> Unit,
    onQueryChange: (String) -> Unit,
    context: Context
) {

    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {

        LazyVerticalGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(span = {
                GridItemSpan(2)
            }) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Discover", style = TextStyle(
                            fontSize = 30.sp, fontWeight = FontWeight.Bold
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        state.cartState.let { cart ->
                            cart.products.size.let { count ->
                                CartItem(
                                    itemCount = count,
                                    onOpenCart = onOpenCart,
                                    modifier = modifier,
                                    contentDescription = context.getString(R.string.cart)
                                )
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
                                onOpenProfile()
                            }) {
                                Text(
                                    text = "Settings"
                                )
                            }
                        }
                    }
                }
            }

            item(span = {
                GridItemSpan(2)
            }) {
                SearchBar(
                    state = state.searchState, onQueryChange = onQueryChange, onSearch = onSearch
                )
            }

            item(span = {
                GridItemSpan(2)
            }) {
                ItemOnOffer()
            }

            item(span = {
                GridItemSpan(2)
            }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Categories",
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(
                                text = "See all",
                                style = MaterialTheme.typography.caption,
                                color = Green200
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.categories.toList()
                        ) { category ->
                            CategoryItem(category = category.value,
                                selected = state.selectedCategory == category.value,
                                onSelect = {
                                    onChangeCategory(it)
                                })
                        }
                    }
                }
            }

            items(
                items = state.products
            ) { productItem ->
                ProductItem(
                    product = productItem, onOpenProductDetails = onOpenProductDetails
                )
            }
        }

        if (state.error?.isNotEmpty() == true) {
            Text(
                text = state.error, modifier = modifier.align(Alignment.Center)
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
fun SearchBar(
    state: SearchState, onQueryChange: (String) -> Unit, onSearch: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            placeholder = {
                Text(
                    text = "Search name or description",
                    style = MaterialTheme.typography.body1,
                )
            },
            value = state.query,
            onValueChange = onQueryChange,
            colors = TextFieldDefaults.textFieldColors(
                textColor = NeutralDark,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = MaterialTheme.colors.background,
            ),
            trailingIcon = {
                Icon(imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NeutralDark,
                    modifier = Modifier.clickable(enabled = state.isActive) { onSearch() })
            },
            modifier = Modifier.weight(0.8f),
            shape = RoundedCornerShape(12.dp)
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductItem(
    product: ProductDBO, onOpenProductDetails: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp), backgroundColor = DefaultWhite, border = BorderStroke(
            width = 0.6.dp, color = Color.LightGray
        ), elevation = 0.dp
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                product.id.let { onOpenProductDetails(it) }
            }
            .padding(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)

        ) {
            GlideImage(
                model = product.image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .requiredSize(60.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.body2,
                    color = NeutralDark.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(2f)
                )

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        tint = Color(0xFFF7980C),
                        contentDescription = "Product rating",
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "${product.rating.rate}", style = MaterialTheme.typography.caption
                    )
                }
            }
            Text(
                style = MaterialTheme.typography.body1,
                text = "$ ${product.price}",
                fontWeight = FontWeight.Bold
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
            color = animateBgColor.value, shape = RoundedCornerShape(32.dp)
        )
        .border(
            width = 1.dp, color = animateBorderColor.value, shape = RoundedCornerShape(32.dp)
        )
        .clickable(indication = null, interactionSource = interactionSource) {
            onSelect(category)
        }) {
        Text(
            text = category.replaceFirstChar { char -> char.uppercase() },
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

@Composable
fun ItemOnOffer() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Green500,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Clearance sale",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onPrimary
                )

                Button(
                    shape = RoundedCornerShape(32.dp),
                    elevation = ButtonDefaults.elevation(0.dp),
                    colors = ButtonDefaults.buttonColors(
                    backgroundColor = DefaultWhite,
                ), onClick = {}) {
                    Text(
                        text = "Upto 50% off", style = MaterialTheme.typography.body2
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.fridge),
                contentDescription = null,
                modifier = Modifier.size(100.dp)

            )
        }
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