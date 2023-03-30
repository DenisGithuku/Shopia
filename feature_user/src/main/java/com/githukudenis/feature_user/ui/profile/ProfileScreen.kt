package com.githukudenis.feature_user.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.githukudenis.feature_user.R
import com.githukudenis.feature_user.data.remote.model.Address
import com.githukudenis.feature_user.data.remote.model.Geolocation
import com.githukudenis.feature_user.data.remote.model.Name
import com.githukudenis.feature_user.data.remote.model.UsersDTOItem
import kotlinx.coroutines.launch


@Composable
fun ProfileRoute(snackbarHostState: SnackbarHostState, onSignOut: () -> Unit) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val uiState = profileViewModel.uiState.value

    val signOut by rememberUpdatedState(onSignOut)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = uiState.signedOut) {
        if (uiState.signedOut) {
            signOut()
        }

        if (uiState.userMessages.isNotEmpty()) {
            val userMessage = uiState.userMessages.first()
            coroutineScope.launch {
                userMessage.message?.let {
                    snackbarHostState.showSnackbar(
                        message = it, duration = SnackbarDuration.Long
                    )
                    userMessage.id?.let { id ->
                        ProfileUiEvent.DismissUserMessage(
                            id
                        )
                    }?.let { event -> profileViewModel.onEvent(event) }
                }
            }
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    ProfileScreen(uiState = uiState, onSignOut = {
        profileViewModel.onEvent(ProfileUiEvent.Logout)
    })

}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier, uiState: ProfileUiState, onSignOut: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        uiState.profile?.let { profile ->
            Box(
                modifier = modifier
                    .padding(16.dp)
                    .size(size = 100.dp)
                    .border(
                        border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.primary),
                        shape = CircleShape
                    )
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "${
                        profile.name.firstname.first().uppercase()
                    }${profile.name.lastname.first().uppercase()}", style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onPrimary
                    )
                )

            }
            ProfileItem(icon = Icons.Default.Person,
                iconDescription = R.string.person_avatar,
                title = "Name",
                value = "${profile.name.firstname.replaceFirstChar { it.uppercase() }} ${profile.name.lastname.replaceFirstChar { it.uppercase() }}"
            )
            ProfileItem(
                icon = Icons.Default.Email,
                iconDescription = R.string.email,
                title = "Email",
                value = profile.email
            )
            ProfileItem(
                icon = Icons.Default.Phone,
                iconDescription = R.string.phone,
                title = "Phone",
                value = profile.phone
            )
        }
        Button(
            onClick = {
                onSignOut()
            }, shape = RoundedCornerShape(32.dp)
        ) {
            Text(
                text = "Sign out"
            )
        }
    }
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    iconDescription: Int,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = context.getString(iconDescription),
            tint = MaterialTheme.colors.onBackground.copy(alpha = .8f)
        )
        Column(
            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title, style = TextStyle(
                    color = MaterialTheme.colors.onBackground.copy(
                        alpha = .8f
                    )
                )
            )

            Text(
                text = value, style = TextStyle(
                    fontSize = 20.sp, textAlign = TextAlign.Center
                )
            )
            Divider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreePreview() {
    ProfileScreen(uiState = ProfileUiState(
        profile = UsersDTOItem(
            address = Address(
                city = "Nairobi",
                geolocation = Geolocation(lat = "12.345", long = "4.5432423"),
                number = 0,
                street = "3rd",
                zipcode = "00100"
            ),
            __v = 1,
            email = "alan@gmail.com",
            id = 1,
            name = Name(firstname = "Alan", lastname = "Omondi"),
            password = "12345",
            phone = "0712345678",
            username = "alanmosh"
        )
    ), onSignOut = {})
}