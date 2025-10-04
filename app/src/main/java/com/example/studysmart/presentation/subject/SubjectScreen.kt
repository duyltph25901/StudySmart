package com.example.studysmart.presentation.subject

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studysmart.R
import com.example.studysmart.presentation.components.AddSubjectDialog
import com.example.studysmart.presentation.components.CountCard
import com.example.studysmart.presentation.components.DeleteDialog
import com.example.studysmart.presentation.components.studySessionList
import com.example.studysmart.presentation.components.taskList
import com.example.studysmart.presentation.destinations.TaskScreenRouteDestination
import com.example.studysmart.presentation.task.TaskScreenNavGraphsArgs
import com.example.studysmart.util.SnackBarEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

data class SubjectScreenNavGraphsArgs(
    val subjectId: Int
)

@Destination(
    navArgsDelegate = SubjectScreenNavGraphsArgs::class
)
@Composable
fun SubjectScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel = hiltViewModel<SubjectViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SubjectScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarEvent = viewModel.snackBarEventFlow,
        onBackEvent = {
            navigator.navigateUp()
        }, onAddTaskEvent = {
            val navArg = TaskScreenNavGraphsArgs(
                taskId = null,
                subjectId = null
            )
            navigator.navigate(TaskScreenRouteDestination(navArg))
        }, onTaskCardEvent = { taskId ->
            taskId?.let {
                val navArg = TaskScreenNavGraphsArgs(
                    taskId = it,
                    subjectId = null
                )
                navigator.navigate(TaskScreenRouteDestination(navArg))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(
    state: SubjectState,
    onEvent: (SubjectEvent) -> Unit,
    snackBarEvent: SharedFlow<SnackBarEvent>,
    onBackEvent: () -> Unit,
    onAddTaskEvent: () -> Unit,
    onTaskCardEvent: (Int?) -> Unit,
) {
    val sectionTitleTask = stringResource(R.string.upcoming_tasks).uppercase()
    val emptyTasks = stringResource(R.string.msg_empty_tasks)
    val sectionTitleSession = stringResource(R.string.recent_study_sessions).uppercase()
    val sectionTitleTaskComplete = stringResource(R.string.completed_task)
    val emptyTasksComplete = stringResource(R.string.msg_empty_tasks_complete)
    val emptySessions = stringResource(R.string.msg_empty_sessions)
    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val isFABExpanded by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        snackBarEvent.collectLatest { event ->
            when (event) {
                is SnackBarEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackBarEvent.NavigateUp -> {
                    onBackEvent.invoke()
                }
            }
        }
    }

    LaunchedEffect(
        key1 = state.studiedHours,
        key2 = state.goalStudyHours
    ) {
        onEvent(
            SubjectEvent.UpdateProgress
        )
    }

    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        selectedColors = state.subjectCardColor,
        subjectName = state.subjectName,
        goalHours = state.goalStudyHours,
        onSubjectNameChange = { newSubjectName ->
            onEvent(
                SubjectEvent.OnSubjectNameChange(newSubjectName)
            )
        }, onGoalHoursChange = { newGoalHours ->
            onEvent(
                SubjectEvent.OnGoalStudyHoursChange(newGoalHours)
            )
        }, onConfirmEvent = {
            isAddSubjectDialogOpen = false
            onEvent(
                SubjectEvent.UpdateSubject
            )
        }, onDismissRequestEvent = {
            isAddSubjectDialogOpen = false
        }, onColorChangeEvent = { colors ->
            onEvent(
                SubjectEvent.OnSubjectCardColorChange(
                    colors
                )
            )
        }
    )

    DeleteDialog(
        isOpen = isDeleteSubjectDialogOpen,
        title = "${stringResource(R.string.delete_subject)}?",
        bodyText = stringResource(R.string.msg_confirm_delete_subject),
        onDismissEvent = {
            isDeleteSubjectDialogOpen = false
        }, onConfirmEvent = {
            isDeleteSubjectDialogOpen = false
            onEvent(
                SubjectEvent.DeleteSubject
            )
        }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "${stringResource(R.string.delete_session)}?",
        bodyText = stringResource(R.string.msg_confirm_delete_session),
        onDismissEvent = {
            isDeleteSessionDialogOpen = false
        }, onConfirmEvent = {
            isDeleteSessionDialogOpen = false
            onEvent(
                SubjectEvent.DeleteSession
            )
        }
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }, topBar = {
            SubjectScreenTopBar(
                title = state.subjectName,
                onBackEvent = {
                    onBackEvent.invoke()
                }, onDeleteEvent = {
                    isDeleteSubjectDialogOpen = true
                }, onEditEvent = {
                    isAddSubjectDialogOpen = true
                }, scrollBehavior = scrollBehavior
            )
        }, floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onAddTaskEvent.invoke()
                }, icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                }, text = {
                    Text(
                        text = stringResource(R.string.add_task)
                    )
                }, expanded = isFABExpanded
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = listState
        ) {
            item {
                SubjectOverViewSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    studiedHours = state.studiedHours.toString(),
                    goalHours = state.goalStudyHours,
                    progress = state.progress
                )
            }

            taskList(
                sectionTitle = sectionTitleTask,
                emptyListText = emptyTasks,
                tasks = state.upComingTasks,
                onTaskClickEvent = { taskId ->
                    Log.d("duylt", "TaskId: $taskId")
                    onTaskCardEvent.invoke(taskId)
                }, onCheckboxTasClickEvent = { task ->
                    onEvent(
                        SubjectEvent.OnTaskIsCompleteChange(task)
                    )
                }
            )

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            taskList(
                sectionTitle = sectionTitleTaskComplete,
                emptyListText = emptyTasksComplete,
                tasks = state.completedTasks,
                onTaskClickEvent = { taskId ->
                    Log.d("duylt", "TaskId: $taskId")
                    onTaskCardEvent.invoke(taskId)
                }, onCheckboxTasClickEvent = { task ->
                    Log.d("duylt", "Task: $task")
                }
            )

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            studySessionList(
                sectionTitle = sectionTitleSession,
                emptyListText = emptySessions,
                sessions = state.recentSessions,
                onDeleteSessionEvent = { session ->
                    isDeleteSubjectDialogOpen = true
                    onEvent(
                        SubjectEvent.OnDeleteSessionButtonClick(session)
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreenTopBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
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
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
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
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun SubjectOverViewSection(
    modifier: Modifier = Modifier,
    studiedHours: String,
    goalHours: String,
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