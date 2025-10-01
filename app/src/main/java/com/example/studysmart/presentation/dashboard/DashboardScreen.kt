package com.example.studysmart.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studysmart.R
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.domain.model.Task
import com.example.studysmart.presentation.components.AddSubjectDialog
import com.example.studysmart.presentation.components.CountCard
import com.example.studysmart.presentation.components.DeleteDialog
import com.example.studysmart.presentation.components.SubjectCard
import com.example.studysmart.presentation.components.studySessionList
import com.example.studysmart.presentation.components.taskList
import com.example.studysmart.presentation.destinations.SessionScreenRouteDestination
import com.example.studysmart.presentation.destinations.SubjectScreenRouteDestination
import com.example.studysmart.presentation.destinations.TaskScreenRouteDestination
import com.example.studysmart.presentation.subject.SubjectScreenNavGraphsArgs
import com.example.studysmart.presentation.task.TaskScreenNavGraphsArgs
import com.example.studysmart.util.SnackBarEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Destination(start = true)
@Composable
fun DashboardScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel = hiltViewModel<DashboardViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val task by viewModel.tasks.collectAsStateWithLifecycle()
    val recentSessions by viewModel.recentSessions.collectAsStateWithLifecycle()

    DashboardScreen(
        state = state,
        task = task,
        recentSessions = recentSessions,
        snackBarEvent = viewModel.snackBarEventFlow,
        onEvent = viewModel::onEvent,
        onSubjectCardEvent = { subjectCardId ->
            subjectCardId?.let {
                val navArg = SubjectScreenNavGraphsArgs(it)
                navigator.navigate(SubjectScreenRouteDestination(navArg))
            }
        }, onTaskCardEvent = { taskId ->
            taskId?.let {
                val navArg = TaskScreenNavGraphsArgs(
                    taskId = it,
                    subjectId = null
                )
                navigator.navigate(TaskScreenRouteDestination(navArg))
            }
        }, onStartSessionEvent = {
            navigator.navigate(SessionScreenRouteDestination())
        }
    )
}

@Composable
fun DashboardScreen(
    state: DashboardState,
    task: List<Task>,
    recentSessions: List<Session>,
    snackBarEvent: SharedFlow<SnackBarEvent>,
    onEvent: (DashboardEvent) -> Unit,
    onSubjectCardEvent: (Int?) -> Unit,
    onTaskCardEvent: (Int?) -> Unit,
    onStartSessionEvent: () -> Unit
) {
    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }
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
            }
        }
    }

    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        selectedColors = state.subjectCardColors,
        subjectName = state.subjectName,
        goalHours = state.goalStudyHours,
        onSubjectNameChange = { newSubjectName ->
            onEvent(
                DashboardEvent.OnSubjectNameChange(newSubjectName)
            )
        }, onGoalHoursChange = { newGoalHours ->
            onEvent(
                DashboardEvent.OnGoalStudyHoursChange(newGoalHours)
            )
        }, onConfirmEvent = {
            isAddSubjectDialogOpen = false
            onEvent(
                DashboardEvent.SaveSubject
            )
        }, onDismissRequestEvent = {
            isAddSubjectDialogOpen = false
        }, onColorChangeEvent = { newSelectedColor ->
            onEvent(
                DashboardEvent.OnSubjectCardColorChange(newSelectedColor)
            )
        }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = stringResource(R.string.delete_session),
        bodyText = stringResource(R.string.msg_confirm_delete_session),
        onDismissEvent = {
            isDeleteSessionDialogOpen = false
        }, onConfirmEvent = {
            onEvent(
                DashboardEvent.DeleteSession
            )
            isDeleteSessionDialogOpen = false
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DashboardScreenTopBar()
        }, snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        }
    ) { paddingValues ->
        val sectionTitleTask = stringResource(R.string.upcoming_tasks).uppercase()
        val emptyTasks = stringResource(R.string.msg_empty_tasks)
        val sectionTitleSession = stringResource(R.string.recent_study_sessions).uppercase()
        val emptySessions = stringResource(R.string.msg_empty_sessions)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CountCardsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount = state.totalSubjectCount.toString(),
                    studiedHours = state.totalStudiedHours.toString(),
                    goalStudyHours = state.totalGoalStudyHours.toString()
                )
            }

            item {
                SubjectCardSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectsList = state.subjects,
                    onAddSubjectEvent = {
                        isAddSubjectDialogOpen = true
                    }, onSubjectCardEvent = {
                        onSubjectCardEvent.invoke(it)
                    }
                )
            }

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 48.dp,
                            vertical = 20.dp
                        ),
                    onClick = {
                        onStartSessionEvent.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.start_study_session)
                    )
                }
            }

            taskList(
                sectionTitle = sectionTitleTask,
                emptyListText = emptyTasks,
                tasks = task,
                onTaskClickEvent = { taskId ->

                    onTaskCardEvent.invoke(taskId)
                }, onCheckboxTasClickEvent = { task ->
                    onEvent(
                        DashboardEvent.OnTaskIsCompleteChange(task)
                    )
                }
            )

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            studySessionList(
                sectionTitle = sectionTitleSession,
                emptyListText = emptySessions,
                sessions = recentSessions,
                onDeleteSessionEvent = { session ->
                    onEvent(
                        DashboardEvent.OnDeleteSessionEvent(session)
                    )
                    isDeleteSessionDialogOpen = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Composable
private fun CountCardsSection(
    modifier: Modifier = Modifier,
    subjectCount: String,
    studiedHours: String,
    goalStudyHours: String
) {
    Row(
        modifier = modifier
    ) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = stringResource(R.string.subject_count),
            count = subjectCount
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = stringResource(R.string.studied_hours),
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = stringResource(R.string.goal_study_hours),
            count = goalStudyHours
        )
    }
}

@Composable
private fun SubjectCardSection(
    modifier: Modifier = Modifier,
    subjectsList: List<Subject>,
    emptyListTextRes: Int = R.string.msg_empty_subjects,
    onAddSubjectEvent: () -> Unit,
    onSubjectCardEvent: (Int?) -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(
                    start = 12.dp
                ),
                text = stringResource(R.string.subjects).uppercase(),
                style = MaterialTheme.typography.bodySmall
            )

            IconButton(
                onClick = {
                    onAddSubjectEvent.invoke()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_subject)
                )
            }
        }

        if (subjectsList.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.img_books),
                contentDescription = stringResource(emptyListTextRes)
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(emptyListTextRes),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            items(subjectsList) { subject ->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColors = subject.colors.map { Color(it) },
                    onClick = {
                        onSubjectCardEvent.invoke(subject.subjectId)
                    }
                )
            }
        }
    }
}