package com.githukudenis.feature_user.ui.profile

import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.feature_user.data.UserRepository
import com.githukudenis.feature_user.data.remote.FakeUserRepositoryImpl
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userPrefsRepository: UserPreferencesRepository
    private lateinit var profileViewModel: ProfileViewModel

    @get:Rule
    val mainCoroutineRule by lazy { MainCoroutineRule() }

    @Before
    fun setUp() {
        userRepository = FakeUserRepositoryImpl()
        userPrefsRepository = FakeUserPrefsRepository()
        profileViewModel = ProfileViewModel(userRepository, userPrefsRepository)
    }

    @Test
    fun getUserProfile() = runTest {
        profileViewModel.getUserProfile(1)
        val state = profileViewModel.uiState.value
        assertThat(state).isNotNull()
    }
}