package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.OpportunityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OpportunityDao {

    @Query("SELECT * FROM opportunities ORDER BY id DESC")
    fun getAllOpportunities(): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities WHERE id = :id")
    fun getOpportunityById(id: Int): Flow<OpportunityEntity?>

    @Query("SELECT * FROM opportunities WHERE isSaved = 1 ORDER BY id DESC")
    fun getSavedOpportunities(): Flow<List<OpportunityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunity(opportunity: OpportunityEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunities(opportunities: List<OpportunityEntity>)

    @Update
    suspend fun updateOpportunity(opportunity: OpportunityEntity)

    @Delete
    suspend fun deleteOpportunity(opportunity: OpportunityEntity)

    @Query("UPDATE opportunities SET isSaved = :isSaved WHERE id = :id")
    suspend fun updateSavedStatus(id: Int, isSaved: Boolean)

    @Query("UPDATE opportunities SET isExpired = :isExpired WHERE id = :id")
    suspend fun updateExpiredStatus(id: Int, isExpired: Boolean)

    @Query("UPDATE opportunities SET isVerified = :isVerified WHERE id = :id")
    suspend fun updateVerifiedStatus(id: Int, isVerified: Boolean)

    @Query("UPDATE opportunities SET deadline = :deadline, daysRemaining = :daysRemaining WHERE id = :id")
    suspend fun updateDeadline(id: Int, deadline: String, daysRemaining: Int)
}
