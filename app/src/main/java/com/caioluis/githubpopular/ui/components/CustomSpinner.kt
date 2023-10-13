package com.caioluis.githubpopular.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.caioluis.githubpopular.R.drawable

@Composable
fun CustomSpinner(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(selectedOption)) }

    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
    ) {
        TextField(
            value = textFieldValue,
            onValueChange = { value ->
                textFieldValue = value
                onOptionSelected(value.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            enabled = false
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.widthIn(min = 280.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        textFieldValue = TextFieldValue(option)
                        onOptionSelected(option)
                        expanded = false
                    },
                    trailingIcon = {
                        Image(
                            painter = painterResource(id = drawable.ic_star),
                            contentDescription = "Star Icon",
                            modifier = Modifier.size(12.dp)
                        )
                    },
                    text = {
                        Text(option)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomSpinnerPreview() {
    CustomSpinner(
        options = listOf("Kotlin, Java"),
        selectedOption = "Kotlin",
        onOptionSelected = {}
    )
}
