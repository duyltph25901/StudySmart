package com.example.studysmart.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SessionTable")
data class Session(
    @PrimaryKey(autoGenerate = true)
    var sessionSubjectId: Int,
    var relatedToSubject: String,
    var date: Long,
    var duration: Long,
    var sessionId: Int
) {
}