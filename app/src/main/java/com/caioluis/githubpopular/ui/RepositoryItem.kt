package com.caioluis.githubpopular.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.extensions.openBrowserIntent
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.model.UiRepositoryOwner

@Composable
fun RepositoryItem(
    repository: UiGitHubRepository,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Card(
        onClick = { context.openBrowserIntent(repository.htmlUrl) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(12.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 12.dp),
            ) {
                AsyncImage(
                    model = repository.owner.avatarUrl,
                    contentDescription = "User Image",
                    placeholder = painterResource(id = R.drawable.ic_star),
                    error = painterResource(id = R.drawable.ic_star),
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = repository.owner.login,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                Text(
                    text = repository.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = repository.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Stars",
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = repository.stargazersCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 10.dp),
                        color = Color.Black,
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_forks),
                        contentDescription = "Forks",
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = repository.forksCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryItemPreview() {
    RepositoryItem(
        repository = UiGitHubRepository(
            name = "Example of title",
            description = "This is a example of a kotlin repository description. " +
                "It'll appear like this to the user! If the text pass 3 lines, ellipsis will shine",
            stargazersCount = 1234565,
            forksCount = 1234565,
            owner = UiRepositoryOwner(login = "User Name"),
        ),
    )
}
