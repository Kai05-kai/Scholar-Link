package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiAssistantHelper
import com.example.data.database.ScholarLinkDatabase
import com.example.data.matcher.OpportunityMatcher
import com.example.data.matcher.MatchResult
import com.example.data.model.OpportunityEntity
import com.example.data.model.UserProfileEntity
import com.example.data.repository.ScholarLinkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false
)

data class OpportunityWithMatch(
    val opportunity: OpportunityEntity,
    val matchResult: MatchResult
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ScholarLinkRepository

    val userProfile: StateFlow<UserProfileEntity?>
    val allOpportunities: StateFlow<List<OpportunityEntity>>
    val savedOpportunities: StateFlow<List<OpportunityEntity>>

    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")
    val showOnlyEligible = MutableStateFlow(false)
    val isAdminMode = MutableStateFlow(false)

    val chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                isUser = false,
                text = "👋 Welcome to **ScholarLink Assistant**!\n\nI can help you discover scholarships, internships, schemes, and guide you through required documents.\n\n*Ask me anything like:* 'What scholarships am I eligible for?' or 'What documents are required for engineering grants?'"
            )
        )
    )

    val isAiThinking = MutableStateFlow(false)

    val filteredOpportunities: StateFlow<List<OpportunityWithMatch>>

    init {
        val database = ScholarLinkDatabase.getDatabase(application, viewModelScope)
        repository = ScholarLinkRepository(
            opportunityDao = database.opportunityDao(),
            userProfileDao = database.userProfileDao()
        )

        userProfile = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        allOpportunities = repository.allOpportunities.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        savedOpportunities = repository.savedOpportunities.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        filteredOpportunities = combine(
            allOpportunities,
            userProfile,
            searchQuery,
            selectedCategory,
            showOnlyEligible
        ) { opps, profile, query, category, onlyEligible ->
            val defaultProfile = profile ?: UserProfileEntity()
            opps.map { opp ->
                val match = OpportunityMatcher.evaluateMatch(opp, defaultProfile)
                OpportunityWithMatch(opp, match)
            }
                .filter { item ->
                    val opp = item.opportunity
                    val matchesSearch = query.isBlank() ||
                            opp.title.contains(query, ignoreCase = true) ||
                            opp.organization.contains(query, ignoreCase = true) ||
                            opp.category.contains(query, ignoreCase = true) ||
                            opp.benefits.contains(query, ignoreCase = true) ||
                            opp.shortDescription.contains(query, ignoreCase = true)

                    val matchesCategory = category == "All" || opp.category.equals(category, ignoreCase = true)

                    val matchesEligible = !onlyEligible || item.matchResult.isEligible

                    matchesSearch && matchesCategory && matchesEligible
                }
                .sortedByDescending { it.matchResult.matchPercentage }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun saveProfile(profile: UserProfileEntity) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }

    fun toggleSaved(opportunity: OpportunityEntity) {
        viewModelScope.launch {
            repository.toggleSavedStatus(opportunity.id, opportunity.isSaved)
        }
    }

    fun toggleExpired(opportunity: OpportunityEntity) {
        viewModelScope.launch {
            repository.toggleExpiredStatus(opportunity.id, opportunity.isExpired)
        }
    }

    fun toggleVerified(opportunity: OpportunityEntity) {
        viewModelScope.launch {
            repository.toggleVerifiedStatus(opportunity.id, opportunity.isVerified)
        }
    }

    fun deleteOpportunity(opportunity: OpportunityEntity) {
        viewModelScope.launch {
            repository.deleteOpportunity(opportunity)
        }
    }

    fun saveOpportunity(opportunity: OpportunityEntity) {
        viewModelScope.launch {
            if (opportunity.id == 0) {
                repository.insertOpportunity(opportunity)
            } else {
                repository.updateOpportunity(opportunity)
            }
        }
    }

    fun sendAiQuestion(userQuestion: String) {
        if (userQuestion.isBlank()) return

        val userMsg = ChatMessage(isUser = true, text = userQuestion.trim())
        val loadingMsg = ChatMessage(isUser = false, text = "Analyzing database...", isLoading = true)

        chatMessages.value = chatMessages.value + userMsg + loadingMsg
        isAiThinking.value = true

        viewModelScope.launch {
            val reply = GeminiAssistantHelper.generateAnswer(
                question = userQuestion,
                profile = userProfile.value,
                opportunities = allOpportunities.value
            )

            isAiThinking.value = false
            val currentList = chatMessages.value.filter { !it.isLoading }
            chatMessages.value = currentList + ChatMessage(isUser = false, text = reply)
        }
    }

    fun resetProfile() {
        viewModelScope.launch {
            repository.clearProfile()
        }
    }
}
