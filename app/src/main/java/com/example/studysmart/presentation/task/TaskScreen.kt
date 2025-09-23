package com.example.studysmart.presentation.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studysmart.R
import com.example.studysmart.presentation.components.TaskCheckBox
import com.example.studysmart.presentation.theme.Red

@Composable
fun TaskScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TaskScreenTopBar(
                isTaskExist = true,
                isComplete = false,
                checkboxBorderColor = Red,
                onBackEvent = {

                }, onDeleteEvent = {

                }, onCheckboxEvent = {

                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenTopBar(
    isTaskExist: Boolean,
    isComplete: Boolean,
    checkboxBorderColor: Color,
    onBackEvent: () -> Unit,
    onDeleteEvent: () -> Unit,
    onCheckboxEvent: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.task),
                style = MaterialTheme.typography.headlineSmall
            )
        }, navigationIcon = {
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
        }, actions = {
            if (isTaskExist) {
                TaskCheckBox(
                    isCompleted = isComplete,
                    borderColor = checkboxBorderColor,
                    onCheckboxEvent = {
                        onCheckboxEvent.invoke()
                    }
                )

                IconButton(
                    onClick = {
                        onDeleteEvent.invoke()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_task)
                    )
                }
            }
        },
    )
}