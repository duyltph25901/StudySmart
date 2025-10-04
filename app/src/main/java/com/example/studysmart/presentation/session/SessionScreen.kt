package com.example.studysmart.presentation.session

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studysmart.R
import com.example.studysmart.presentation.components.DeleteDialog
import com.example.studysmart.presentation.components.SubjectsBottomSheet
import com.example.studysmart.presentation.components.studySessionList
import com.example.studysmart.util.dummySubjectData
import com.example.studysmart.util.dummyTaskSession
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun SessionScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel = hiltViewModel<SessionViewModel>()

    SessionScreen(
        onBackEvent = {
            navigator.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    onBackEvent: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }

    SubjectsBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = dummySubjectData,
        onClickSubjectEvent = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isBottomSheetOpen = false
            }
        }, onDismissEvent = {
            isBottomSheetOpen = false
        }
    )

    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = stringResource(R.string.delete_session),
        bodyText = stringResource(R.string.msg_confirm_delete_session),
        onDismissEvent = {
            isDeleteDialogOpen = false
        }, onConfirmEvent = {
            isDeleteDialogOpen = false
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SessionScreenTopBar(
                onBackEvent = {
                    onBackEvent.invoke()
                }
            )
        }
    ) { innerPadding ->
        val sectionTitle = stringResource(R.string.recent_study_sessions).uppercase()
        val emptyListText = stringResource(R.string.msg_empty_sessions)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                TimeSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }

            item {
                RelatedToSubjetSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    relatedToSubject = "English",
                    onRelatedToSubjectEvent = {
                        isBottomSheetOpen = true
                    }
                )
            }

            item {
                ButtonSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    onStartEvent = {

                    }, onCancelEvent = {

                    }, onFinishEvent = {

                    }
                )
            }

            studySessionList(
                sectionTitle = sectionTitle,
                emptyListText = emptyListText,
                sessions = dummyTaskSession,
                onDeleteSessionEvent = { session ->
                    isDeleteDialogOpen = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreenTopBar(
    onBackEvent: () -> Unit
) {
    TopAppBar(
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
                text = stringResource(R.string.study_sessions),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    )
}

@Composable
private fun TimeSection(
    modifier: Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(
                    width = 5.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                )
        )
        Text(
            text = "00:05:32",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 45.sp
            )
        )
    }
}

@Composable
private fun RelatedToSubjetSection(
    modifier: Modifier,
    relatedToSubject: String,
    onRelatedToSubjectEvent: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.related_to_subject),
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = relatedToSubject,
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(
                onClick = {
                    onRelatedToSubjectEvent.invoke()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.select_subject)
                )
            }
        }
    }
}

@Composable
private fun ButtonSection(
    modifier: Modifier,
    onStartEvent: () -> Unit,
    onCancelEvent: () -> Unit,
    onFinishEvent: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                onCancelEvent.invoke()
            }
        ) {
            Text(
                text = stringResource(R.string.cancel),
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp
                )
            )
        }

        Button(
            onClick = {
                onStartEvent.invoke()
            }
        ) {
            Text(
                text = stringResource(R.string.start),
                modifier = Modifier.padding(
                    vertical = 5.dp,
                    horizontal = 10.dp
                )
            )
        }

        Button(
            onClick = {
                onFinishEvent.invoke()
            }
        ) {
            Text(
                text = stringResource(R.string.finish),
                modifier = Modifier.padding(
                    horizontal = 5.dp,
                    vertical = 10.dp
                )
            )
        }
    }
}