package ar.com.todoapp.repository

import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.local.LocalTaskDataSource
import ar.com.todoapp.data.model.Task
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class TaskRepositoryImplTest {
    private val localDataSource: LocalTaskDataSource = mock()
    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setup() {
        repository = TaskRepositoryImpl(localDataSource)
    }

    @Test
    fun `get task list returns Resource from dataSource`() = runTest {
        val tasks = listOf(Task(1, "Repo Task", "Test"))
        val expected = Resource.Success(tasks)

        whenever(localDataSource.getTaskList()).thenReturn(expected)

        val result = repository.getTaskList()

        assertTrue(result is Resource.Success)
        assertEquals(tasks, (result as Resource.Success).data)
    }

    @Test
    fun `save task calls dataSource`() = runTest {
        val task = Task(2, "Save", "Repo")

        repository.saveTask(task)

        verify(localDataSource).saveTask(task)
    }

    @Test
    fun `save task and it appears in the task list`() = runTest {
        val task = Task(2, "Save", "Repo")
        val tasks = mutableListOf<Task>()

        // Mock dataSource save n get tasks
        whenever(localDataSource.saveTask(any())).thenAnswer {
            val savedTask = it.getArgument<Task>(0)
            tasks.add(savedTask)
            Unit
        }

        whenever(localDataSource.getTaskList()).thenReturn(Resource.Success(tasks))

        // Act: save task
        repository.saveTask(task)

        // Assert: check request list success
        verify(localDataSource).saveTask(task)

        // Check task added
        val result = repository.getTaskList()
        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data.contains(task))
    }

    @Test
    fun `delete task calls dataSource`() = runTest {
        val task = Task(3, "Delete", "Repo")

        repository.deleteTask(task)

        verify(localDataSource).deleteTask(task)
    }

    @Test
    fun `update task calls dataSource`() = runTest {
        val task = Task(4, "Update", "Repo")

        repository.update(task)

        verify(localDataSource).updateTask(task)
    }
}