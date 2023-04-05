package com.githukudenis.feature_user.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.githukudenis.feature_user.R

@Composable
fun AboutRoute(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = com.githukudenis.core_design.R.drawable.blue_cart),
            contentDescription = context.getString(R.string.app_icon),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(width = 2.dp, shape = CircleShape, color = MaterialTheme.colors.primary)
        )
        Spacer(
            modifier = modifier.height(20.dp)
        )
        Box(
            modifier = modifier.fillMaxWidth(.8f)
                .background(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = context.getString(R.string.app_name),
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(modifier = modifier.height(12.dp))
                    Divider(modifier = modifier.fillMaxWidth(.8f))
                }
                Text(
                    text = "Version: ${com.githukudenis.core_data.BuildConfig.BUILD_TYPE}"
                )
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Connect: "
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutRoutePrev() {
    AboutRoute()
}