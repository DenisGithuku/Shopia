package com.githukudenis.feature_user.ui.profile

import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.feature_cart.data.repo.CartRepository
import com.githukudenis.feature_user.data.UserRepository
import com.githukudenis.feature_user.data.remote.FakeUserRepositoryImpl
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userPrefsRepository: UserPreferencesRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var profileViewModel: ProfileViewModel

    @get:Rule
    val mainCoroutineRule by lazy { MainCoroutineRule() }

    @Before
    fun setUp() {
        userRepository = FakeUserRepositoryImpl()
        userPrefsRepository = FakeUserPrefsRepository()
        cartRepository = FakeCartRepository()
        profileViewModel = ProfileViewModel(userRepository, cartRepository, userPrefsRepository)
    }

    @Test
    fun `get user profile returns user object`() = runTest {
        profileViewModel.getUserProfile(1)
        val state = profileViewModel.uiState.value
        assertThat(state).isNotNull()
    }

    @Test
    fun `test logout sets logged in pref to false`() = runTest {
        profileViewModel.onEvent(ProfileUiEvent.Logout)
        val isLoggedIn = userPrefsRepository.userPreferencesFlow.first().userLoggedIn
        assertThat(isLoggedIn).isFalse()
    }

    @Test
    fun `test on logout updates logout ui state to true`() = runTest {
        profileViewModel.onEvent(ProfileUiEvent.Logout)
        val logoutSuccess = profileViewModel.uiState.value.signedOut
        assertThat(logoutSuccess).isTrue()
    }

    @Test
    fun `logout clears user cart`() = runTest {
        profileViewModel.onEvent(ProfileUiEvent.Logout)
        val cart = cartRepository.getProductsInCart(12).first()
    }
}