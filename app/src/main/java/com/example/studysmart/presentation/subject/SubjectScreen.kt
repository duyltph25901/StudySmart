package com.example.studysmart.presentation.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studysmart.R
import com.example.studysmart.presentation.components.CountCard

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
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
        ){
            item {
                SubjectOverViewSection(
                    modifier = Modifier.fillMaxWidth()
                        .padding(12.dp ),
                    studiedHours = 10,
                    goalHours = 10,
                    progress = 0.75f
                )
            }
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

@Composable
fun SubjectOverViewSection(
    modifier: Modifier = Modifier,
    studiedHours: Int ,
    goalHours: Int,
    progress: Float,
) {
    val percentageProgress = remember(progress) {
        (progress * 100).toInt().coerceIn(0, 100)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = stringResource(R.string.goal_study_hours),
            count = goalHours
        )

        Spacer(modifier = Modifier.width(10.dp))

        CountCard(
            modifier = Modifier.weight(1f),
            headingText = stringResource(R.string.study_hours),
            count = studiedHours
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = 1f,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )

            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = progress,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )

            Text(
                text = "$percentageProgress%"
            )
        }
    }
}