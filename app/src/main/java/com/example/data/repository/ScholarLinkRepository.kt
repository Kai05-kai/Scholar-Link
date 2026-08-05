package com.example.data.repository

import com.example.data.dao.OpportunityDao
import com.example.data.dao.UserProfileDao
import com.example.data.model.OpportunityEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow

class ScholarLinkRepository(
    private val opportunityDao: OpportunityDao,
    private val userProfileDao: UserProfileDao
) {
    val allOpportunities: Flow<List<OpportunityEntity>> = opportunityDao.getAllOpportunities()
    val savedOpportunities: Flow<List<OpportunityEntity>> = opportunityDao.getSavedOpportunities()
    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()

    suspend fun saveUserProfile(profile: UserProfileEntity) {
        userProfileDao.saveUserProfile(profile)
    }

    suspend fun toggleSavedStatus(id: Int, currentSaved: Boolean) {
        opportunityDao.updateSavedStatus(id, !currentSaved)
    }

    suspend fun toggleExpiredStatus(id: Int, currentExpired: Boolean) {
        opportunityDao.updateExpiredStatus(id, !currentExpired)
    }

    suspend fun toggleVerifiedStatus(id: Int, currentVerified: Boolean) {
        opportunityDao.updateVerifiedStatus(id, !currentVerified)
    }

    suspend fun insertOpportunity(opportunity: OpportunityEntity): Long {
        return opportunityDao.insertOpportunity(opportunity)
    }

    suspend fun updateOpportunity(opportunity: OpportunityEntity) {
        opportunityDao.updateOpportunity(opportunity)
    }

    suspend fun deleteOpportunity(opportunity: OpportunityEntity) {
        opportunityDao.deleteOpportunity(opportunity)
    }

    suspend fun clearProfile() {
        userProfileDao.clearProfile()
    }
}
