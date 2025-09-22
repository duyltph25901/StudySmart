package com.example.studysmart.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.studysmart.R

@Composable
fun DeleteDialog(
    isOpen: Boolean,
    title: String,
    bodyText: String,
    onDismissEvent: () -> Unit,
    onConfirmEvent: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = {
                onDismissEvent.invoke()
            }, title = {
                Text(
                    text = title
                )
            }, text = {
                Text(
                    text = bodyText
                )
            }, dismissButton = {
                TextButton(
                    onClick = {
                        onDismissEvent.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            }, confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmEvent.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete)
                    )
                }
            }
        )
    }
}