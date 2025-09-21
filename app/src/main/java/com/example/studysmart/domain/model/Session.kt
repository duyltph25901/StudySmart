package com.example.studysmart.domain.model

data class Session(
    var sessionSubjectId: Int,
    var relatedToSubject: String,
    var date: Long,
    var duration: Long,
    var sessionId: Int
) {
}