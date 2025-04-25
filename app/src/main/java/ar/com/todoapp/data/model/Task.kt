package ar.com.todoapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
@Entity(tableName = "Tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = -1,
    @ColumnInfo(name = "title")
    var title: String = "NO_TITLE",
    @ColumnInfo(name = "description")
    var description: String = "NO_DESCRIPTION"
)
