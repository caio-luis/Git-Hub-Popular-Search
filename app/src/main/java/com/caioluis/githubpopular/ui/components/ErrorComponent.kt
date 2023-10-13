package com.caioluis.githubpopular.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.ui.theme.errorTextColor

@Composable
fun ErrorComponent(
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Teste de mensagem de erro do app!",
            color = errorTextColor,
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )

        IconButton(
            onClick = onRetryClick,
            modifier = Modifier.size(54.dp),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_retry_24),
                    contentDescription = null,
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    ErrorComponent {}
}
