package com.githukudenis.feature_user.data.remote

import com.githukudenis.feature_user.data.UserRepository
import com.githukudenis.feature_user.data.remote.model.Address
import com.githukudenis.feature_user.data.remote.model.Geolocation
import com.githukudenis.feature_user.data.remote.model.Name
import com.githukudenis.feature_user.data.remote.model.UsersDTO
import com.githukudenis.feature_user.data.remote.model.UsersDTOItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeUserRepository : UserRepository {

    val userDTOItems = arrayListOf(
        UsersDTOItem(
            address = Address(
                city = "",
                geolocation = Geolocation(lat = "", long = ""),
                number = 0,
                street = "",
                zipcode = ""
            ),
            __v = 1,
            email = "",
            id = 1,
            name = Name(firstname = "", lastname = ""),
            password = "",
            phone = "",
            username = ""
        ), UsersDTOItem(
            address = Address(
                city = "",
                geolocation = Geolocation(lat = "", long = ""),
                number = 0,
                street = "",
                zipcode = ""
            ),
            __v = 1,
            email = "",
            id = 2,
            name = Name(firstname = "", lastname = ""),
            password = "",
            phone = "",
            username = ""
        ), UsersDTOItem(
            address = Address(
                city = "",
                geolocation = Geolocation(lat = "", long = ""),
                number = 0,
                street = "",
                zipcode = ""
            ),
            __v = 1,
            email = "",
            id = 4,
            name = Name(firstname = "", lastname = ""),
            password = "",
            phone = "",
            username = ""
        ), UsersDTOItem(
            address = Address(
                city = "",
                geolocation = Geolocation(lat = "", long = ""),
                number = 0,
                street = "",
                zipcode = ""
            ),
            __v = 1,
            email = "",
            id = 3,
            name = Name(firstname = "", lastname = ""),
            password = "",
            phone = "",
            username = ""
        )
    )
    private val usersDTO = UsersDTO().apply { addAll(userDTOItems) }
    override val users: Flow<UsersDTO?>
        get() = flowOf(
            value = usersDTO
        )

    override suspend fun getUserById(userId: Int): Flow<UsersDTOItem?> = flow {
        userDTOItems.find { it.id == userId }
    }
}