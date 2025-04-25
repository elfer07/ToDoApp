package ar.com.todoapp.presentation.ui.fragment.main.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.model.Task
import ar.com.todoapp.repository.TaskRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
        whenever(taskRepository.getTaskList()).thenReturn(flowOf(Resource.Success(emptyList())))
        viewModel = TaskViewModel(taskRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch task list returns success`() = runTest(testDispatcher) {
        val expectedTasks = listOf(Task(1, "title", "desc"))
        val flow = flowOf(Resource.Success(expectedTasks))

        whenever(taskRepository.getTaskList()).thenReturn(flow)

        viewModel = TaskViewModel(taskRepository)

        viewModel.tasks.test {
            val tasksResource = awaitItem()
            assertTrue(tasksResource is Resource.Success)
            assertEquals(expectedTasks, (tasksResource as Resource.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
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