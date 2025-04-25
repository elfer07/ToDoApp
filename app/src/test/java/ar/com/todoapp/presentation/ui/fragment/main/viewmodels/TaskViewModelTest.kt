package ar.com.todoapp.presentation.ui.fragment.main.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.model.Task
import ar.com.todoapp.repository.TaskRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class TaskViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var taskRepository: TaskRepository
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        taskRepository = mock()
        viewModel = TaskViewModel(taskRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch task list returns success`() = runTest(testDispatcher) {
        val taskList = listOf(Task(1, "title", "desc"))
        whenever(taskRepository.getTaskList()).thenReturn(Resource.Success(taskList))

        val result = mutableListOf<Resource<List<Task>>>()
        val liveData = viewModel.fetchTaskList()

        liveData.observeForever {
            result.add(it)
        }

        advanceUntilIdle()

        assert(result.isNotEmpty())
        assert(result.first() is Resource.Success)
        assertEquals(taskList, (result.first() as Resource.Success).data)
    }

    @Test
    fun `fetch task list returns failure`() = runTest {
        val exception = RuntimeException("DB Error")
        whenever(taskRepository.getTaskList()).thenThrow(exception)

        val result = mutableListOf<Resource<List<Task>>>()
        viewModel.fetchTaskList().observeForever {
            result.add(it)
        }

        assert(result.first() is Resource.Failure)
        assertEquals(exception, (result.first() as Resource.Failure).exception)
    }

    @Test
    fun `save task calls task repository`() = runTest {
        val task = Task(1, "Save", "Task")
        viewModel.saveTask(task)
        advanceUntilIdle()
        verify(taskRepository).saveTask(task)
    }

    @Test
    fun `delete task calls task repository`() = runTest {
        val task = Task(2, "Delete", "Task")
        viewModel.deleteTask(task)
        advanceUntilIdle()
        verify(taskRepository).deleteTask(task)
    }

    @Test
    fun `update task calls task repository`() = runTest {
        val task = Task(3, "Update", "Task")
        viewModel.updateTask(task)
        advanceUntilIdle()
        verify(taskRepository).update(task)
    }
}