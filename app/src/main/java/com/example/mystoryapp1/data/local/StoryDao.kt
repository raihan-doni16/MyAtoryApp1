package com.example.mystoryapp1.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mystoryapp1.data.response.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
      suspend fun insertStory(story: List<ListStoryItem>?)

    @Query("SELECT * FROM storyDatabase")
    fun getAllStory(): PagingSource<Int,ListStoryItem>

    @Query("DELETE FROM storyDatabase")
    suspend fun deleteAllStory()
}