package com.example.studysmart.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studysmart.R
import com.example.studysmart.presentation.components.DeleteDialog
import com.example.studysmart.presentation.components.FeatureOrPresentSelectableDates
import com.example.studysmart.presentation.components.SubjectsBottomSheet
import com.example.studysmart.presentation.components.TaskCheckBox
import com.example.studysmart.presentation.components.TaskDatePicker
import com.example.studysmart.presentation.theme.Red
import com.example.studysmart.util.Priority
import com.example.studysmart.util.changeMillisToDateString
import com.example.studysmart.util.dummySubjectData
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.time.Instant

data class TaskScreenNavGraphsArgs(
    val taskId: Int?,
    val subjectId: Int?
)

@Destination(
    navArgsDelegate = TaskScreenNavGraphsArgs::class
)
@Composable
fun TaskScreenRoute(
    navigator: DestinationsNavigator
) {
    TaskScreen(
        onBackEvent = {
            navigator.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    onBackEvent: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var taskTitleError by rememberSaveable { mutableStateOf<String?>(null) }
    taskTitleError = when {
        title.isBlank() -> stringResource(R.string.msg_title_blank)
        title.length < 4 -> stringResource(R.string.msg_title_under_4_chars)
        title.length > 20 -> stringResource(R.string.msg_title_over_20_chars)
        else -> null
    }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDatePickerDialogOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
        selectableDates = FeatureOrPresentSelectableDates
    )
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = "${stringResource(R.string.delete_task)}?",
        bodyText = stringResource(R.string.msg_confirm_delete_task),
        onDismissEvent = {
            isDeleteDialogOpen = false
        }, onConfirmEvent = {
            isDeleteDialogOpen = false
        }
    )

    TaskDatePicker(
        state = datePickerState,
        isOpen = isDatePickerDialogOpen,
        confirmText = stringResource(R.string.ok),
        dismissText = stringResource(R.string.cancel),
        onDismissEvent = {
            isDatePickerDialogOpen = false
        }, onConfirmEvent = {
            isDatePickerDialogOpen = false
        }
    )

    SubjectsBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = dummySubjectData,
        onClickSubjectEvent = { subject ->
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isBottomSheetOpen = false
            }
        }, onDismissEvent = {
            isBottomSheetOpen = false
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TaskScreenTopBar(
                isTaskExist = true,
                isComplete = false,
                checkboxBorderColor = Red,
                onBackEvent = {
                    onBackEvent.invoke()
                }, onDeleteEvent = {
                    isDeleteDialogOpen = true
                }, onCheckboxEvent = {

                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { newTitle ->
                    title = newTitle
                }, label = {
                    Text(
                        text = stringResource(R.string.title)
                    )
                }, singleLine = true,
                maxLines = 1,
                isError = taskTitleError != null && title.isNotBlank(),
                supportingText = {
                    Text(
                        text = taskTitleError.orEmpty()
                    )
                }
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = { newDes ->
                    description = newDes
                }, label = {
                    Text(
                        text = stringResource(R.string.desciption)
                    )
                }
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = stringResource(R.string.due_date),
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = datePickerState.selectedDateMillis.changeMillisToDateString(),
                    style = MaterialTheme.typography.bodyLarge
                )

                IconButton(
                    onClick = {
                        isDatePickerDialogOpen = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.select_due_date)
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = stringResource(R.string.priority),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Priority.entries.forEach { entry ->
                    PriorityButton(
                        modifier = Modifier.weight(1f),
                        label = entry.title,
                        backgroundColor = entry.color,
                        borderColor = if (entry == Priority.MEDIUM) Color.White else Color.Transparent,
                        labelColor = if (entry == Priority.MEDIUM) Color.White else Color.White.copy(
                            alpha = 0.7f
                        ),
                        onClickEvent = {

                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.related_to_subject),
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "English",
                    style = MaterialTheme.typography.bodyLarge
                )

                IconButton(
                    onClick = {
                        isBottomSheetOpen = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = stringResource(R.string.select_subject)
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                onClick = {

                },
                enabled = taskTitleError == null,
            ) {
                Text(
                    text = stringResource(R.string.save)
                )
            }
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
        },
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
        actions = {
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

@Composable
private fun PriorityButton(
    modifier: Modifier = Modifier,
    label: String,
    backgroundColor: Color,
    borderColor: Color,
    labelColor: Color,
    onClickEvent: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable {
                onClickEvent.invoke()
            }
            .background(backgroundColor)
            .padding(5.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = labelColor
        )
    }
}