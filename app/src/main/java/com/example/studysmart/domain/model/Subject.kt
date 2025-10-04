package com.example.studysmart.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studysmart.presentation.theme.gradient1
import com.example.studysmart.presentation.theme.gradient2
import com.example.studysmart.presentation.theme.gradient3
import com.example.studysmart.presentation.theme.gradient4
import com.example.studysmart.presentation.theme.gradient5

@Entity(tableName = "SubjectTable")
data class Subject(
    @PrimaryKey(autoGenerate = true)
    var subjectId: Int = 0,
    var name: String,
    var goalHours: Float,
    var colors: List<Int>
) {
    companion object {
        val subjectCardColors =
            listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}