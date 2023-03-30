package com.githukudenis.feature_product.ui.views.products

import com.githukudenis.feature_user.data.UserRepository
import com.githukudenis.feature_user.data.remote.model.Address
import com.githukudenis.feature_user.data.remote.model.Geolocation
import com.githukudenis.feature_user.data.remote.model.Name
import com.githukudenis.feature_user.data.remote.model.UsersDTO
import com.githukudenis.feature_user.data.remote.model.UsersDTOItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeUserRepositoryImpl : UserRepository {

    private val userDTOItems = listOf(
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
            name = Name(firstname = "alex", lastname = "sam"),
            password = "14234231",
            phone = "0789234",
            username = "alexsam"
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
            name = Name(firstname = "peter", lastname = "man"),
            password = "",
            phone = "",
            username = "peterman"
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
            name = Name(firstname = "jack", lastname = "odhiambo"),
            password = "",
            phone = "",
            username = "jackodhiambo"
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
            name = Name(firstname = "Rita", lastname = "Awuor"),
            password = "",
            phone = "",
            username = "ritaawuor"
        )
    )

    private val usersDTO = UsersDTO().apply {
        addAll(userDTOItems)
    }

    override val users: Flow<UsersDTO?>
        get() = flowOf(
            value = usersDTO
        )

    override suspend fun getUserByUserName(username: String): Flow<UsersDTOItem?> = flow {
        userDTOItems.find { it.username == username }
    }
}