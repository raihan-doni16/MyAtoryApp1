package com.example.mystoryapp1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insert(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun  getRemoteKeysId(id:String):RemoteKeys?

    @Query("DELETE FROM REMOTE_KEYS")
    suspend fun deleteRemoteKeys()
}