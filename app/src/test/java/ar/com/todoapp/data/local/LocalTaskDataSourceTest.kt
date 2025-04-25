package ar.com.todoapp.data.local

import app.cash.turbine.test
import ar.com.todoapp.data.model.Task
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class LocalTaskDataSourceTest {
    private val taskDao: TaskDao = mock()
    private lateinit var localDataSource: LocalTaskDataSource

    @Before
    fun setup() {
        localDataSource = LocalTaskDataSource(taskDao)
    }

    @Test
    fun `get task list returns success`() = runTest {
        val tasks = listOf(Task(1, "Test", "Description"))
        whenever(taskDao.getTaskList()).thenReturn(flowOf(tasks))

        val resultFlow = localDataSource.getTaskList()

        resultFlow.test {
            val result = awaitItem()
            assertEquals(tasks, result)
            awaitComplete()
        }
    }

    @Test
    fun `save task calls dao`() = runTest {
        val task = Task(2, "Save", "Task")
        localDataSource.saveTask(task)

        verify(taskDao).saveTask(task)
    }

    @Test
    fun `delete task calls dao`() = runTest {
        val task = Task(3, "Delete", "Task")
        localDataSource.deleteTask(task)

        verify(taskDao).deleteTask(task)
    }

    @Test
    fun `update task calls dao`() = runTest {
        val task = Task(4, "Update", "Task")
        localDataSource.updateTask(task)

        verify(taskDao).update(task)
    }
}