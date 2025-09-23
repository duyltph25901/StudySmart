package com.example.studysmart.presentation.subject

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.studysmart.R

@Composable
fun SubjectScreen() {
    Scaffold(
        topBar = {
            SubjectScreenTopBar(
                title = "English",
                onBackEvent = {

                }, onDeleteEvent = {

                }, onEditEvent = {

                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .padding(innerPadding)
        ){

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreenTopBar(
    title: String,
    onBackEvent: () -> Unit,
    onDeleteEvent: () -> Unit,
    onEditEvent: () -> Unit
) {
    LargeTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackEvent.invoke()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_back)
                )
            }
        }, title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        }, actions = {
            IconButton(
                onClick = {
                    onDeleteEvent.invoke()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_subject)
                )
            }

            IconButton(
                onClick = {
                    onEditEvent.invoke()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_subject)
                )
            }
        }
    )
}