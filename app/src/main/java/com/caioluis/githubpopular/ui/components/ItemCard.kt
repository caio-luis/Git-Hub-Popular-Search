package com.caioluis.githubpopular.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.caioluis.githubpopular.extensions.openBrowserIntent
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.model.UiRepositoryOwner
import com.caioluis.githubpopular.R as AppR

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemCard(
    model: UiGitHubRepository,
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .clickable {
                context.openBrowserIntent(model.htmlUrl)
            }
            .fillMaxWidth()
            .padding(
                start = 8.dp,
                top = 5.dp,
                end = 8.dp,
                bottom = 5.dp
            ),
        elevation = CardDefaults.cardElevation(7.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GlideImage(
                    model = model.owner.avatarUrl,
                    contentDescription = "",
                    modifier = Modifier.size(80.dp)
                )

                Text(
                    text = model.owner.login,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = model.fullName,
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = model.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = AppR.drawable.ic_star),
                        contentDescription = "Star Icon",
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = model.stargazersCount.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 4.dp, end = 10.dp)
                    )

                    Image(
                        painter = painterResource(id = AppR.drawable.ic_forks),
                        contentDescription = "Forks Icon",
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = model.forksCount.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemCardPreview() {
    ItemCard(
        UiGitHubRepository(
            name = "Sample",
            stargazersCount = 1337,
            forksCount = 12,
            description = "This is the description",
            owner = UiRepositoryOwner(
                login = "Test",
            )
        )
    )
}
