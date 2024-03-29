package com.svkylmz.todoapp.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svkylmz.todoapp.data.models.Priority
import com.svkylmz.todoapp.data.models.TodoTask
import com.svkylmz.todoapp.data.repositories.DataStoreRepository
import com.svkylmz.todoapp.data.repositories.TodoRepository
import com.svkylmz.todoapp.util.Action
import com.svkylmz.todoapp.util.Constants.MAX_TITLE_LENGTH
import com.svkylmz.todoapp.util.RequestState
import com.svkylmz.todoapp.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
        private val repository: TodoRepository,
        private val dataStoreRepository: DataStoreRepository
): ViewModel() {
    val searchAppBarState: MutableState<SearchAppBarState> = mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")
    private val _allTasks = MutableStateFlow<RequestState<List<TodoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<TodoTask>>> = _allTasks

    fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    private val _selectedTask: MutableStateFlow<TodoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<TodoTask?> = _selectedTask

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId = taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)
    val description: MutableState<String> = mutableStateOf("")

    fun updateSelectedTaskFields(selectedTask: TodoTask?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            priority.value = selectedTask.priority
            description.value = selectedTask.description
        } else {
            id.value = 0
            title.value = ""
            priority.value = Priority.LOW
            description.value = ""
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title.value = newTitle
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = TodoTask(
                title = title.value,
                priority = priority.value,
                description = description.value
            )
            repository.addTask(todoTask = todoTask)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = TodoTask(
                id = id.value,
                title = title.value,
                priority = priority.value,
                description = description.value
            )
            repository.updateTask(todoTask = todoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = TodoTask(
                id = id.value,
                title = title.value,
                priority = priority.value,
                description = description.value
            )
            repository.deleteTask(todoTask = todoTask)
        }
    }

    private val _searchedTasks = MutableStateFlow<RequestState<List<TodoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<TodoTask>>> = _searchedTasks

    fun searchDatabase(searchQuery: String) {
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%") //Finds any values that have written letters in any position
                    .collect { searchedTasks ->
                        _searchedTasks.value = RequestState.Success(searchedTasks)
                }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e)
        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    val lowPriorityTasks: StateFlow<List<TodoTask>> =
            repository.sortByLowPriority.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    emptyList()
            )
    val highPriorityTasks: StateFlow<List<TodoTask>> =
            repository.sortByHighPriority.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    emptyList()
            )

    fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                        .map {
                            Priority.valueOf(it)
                        }
                        .collect {
                            _sortState.value = RequestState.Success(it)
                        }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e)
        }
    }

    fun persistSortState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority = priority)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    fun handleDatabaseAction(action: Action) {
        when(action) {
            Action.ADD -> { addTask() }
            Action.UPDATE -> { updateTask() }
            Action.DELETE -> { deleteTask() }
            Action.DELETE_ALL -> { deleteAllTasks() }
            Action.UNDO -> { addTask() }
            else -> {  }
        }
        this.action.value = Action.NO_ACTION
    }
}