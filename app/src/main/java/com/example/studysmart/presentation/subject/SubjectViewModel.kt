package com.example.studysmart.presentation.subject

import androidx.lifecycle.ViewModel
import com.example.studysmart.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
): ViewModel() {
}