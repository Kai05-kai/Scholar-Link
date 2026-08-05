package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.OpportunityEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.MainViewModel
import com.example.ui.OpportunityWithMatch
import com.example.ui.components.OpportunityDetailDialog
import com.example.ui.screens.AdminOpportunityFormDialog
import com.example.ui.screens.AdminPortalScreen
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuestionnaireScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.ScholarLinkTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ScholarLinkTheme {
                ScholarLinkApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ScholarLinkApp(viewModel: MainViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    val allOpportunities by viewModel.allOpportunities.collectAsState()
    val savedOpportunities by viewModel.savedOpportunities.collectAsState()
    val filteredOpportunities by viewModel.filteredOpportunities.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val showOnlyEligible by viewModel.showOnlyEligible.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()

    var currentScreen by remember { mutableStateOf("welcome") }
    var selectedOpportunityDetail by remember { mutableStateOf<OpportunityWithMatch?>(null) }
    var opportunityToEditAdmin by remember { mutableStateOf<OpportunityEntity?>(null) }
    var showAdminFormDialog by remember { mutableStateOf(false) }

    // Auto navigate from welcome if profile exists
    if (userProfile?.isCompleted == true && currentScreen == "welcome") {
        currentScreen = "discover"
    }

    val showBottomBar = currentScreen in listOf("discover", "saved", "ai", "profile", "admin")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen == "discover",
                        onClick = { currentScreen = "discover" },
                        icon = { Icon(Icons.Default.Explore, contentDescription = "Discover") },
                        label = { Text("Discover") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2563EB),
                            selectedTextColor = Color(0xFF2563EB),
                            indicatorColor = Color(0xFFEFF6FF),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_discover")
                    )

                    NavigationBarItem(
                        selected = currentScreen == "saved",
                        onClick = { currentScreen = "saved" },
                        icon = { Icon(Icons.Default.Bookmark, contentDescription = "Saved") },
                        label = { Text("Saved") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2563EB),
                            selectedTextColor = Color(0xFF2563EB),
                            indicatorColor = Color(0xFFEFF6FF),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_saved")
                    )

                    NavigationBarItem(
                        selected = currentScreen == "ai",
                        onClick = { currentScreen = "ai" },
                        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "ScholarBot") },
                        label = { Text("ScholarBot") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2563EB),
                            selectedTextColor = Color(0xFF2563EB),
                            indicatorColor = Color(0xFFEFF6FF),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_ai")
                    )

                    NavigationBarItem(
                        selected = currentScreen == "profile",
                        onClick = { currentScreen = "profile" },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2563EB),
                            selectedTextColor = Color(0xFF2563EB),
                            indicatorColor = Color(0xFFEFF6FF),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_profile")
                    )

                    if (isAdminMode) {
                        NavigationBarItem(
                            selected = currentScreen == "admin",
                            onClick = { currentScreen = "admin" },
                            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                            label = { Text("Admin") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFDC2626),
                                selectedTextColor = Color(0xFFDC2626)
                            ),
                            modifier = Modifier.testTag("nav_admin")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                "welcome" -> {
                    WelcomeScreen(
                        onStartQuestionnaire = { currentScreen = "questionnaire" },
                        onContinueAsGuest = {
                            val guestProfile = UserProfileEntity(isGuest = true, isCompleted = true)
                            viewModel.saveProfile(guestProfile)
                            currentScreen = "discover"
                        }
                    )
                }

                "questionnaire" -> {
                    QuestionnaireScreen(
                        initialProfile = userProfile,
                        onComplete = { profile ->
                            viewModel.saveProfile(profile)
                            currentScreen = "discover"
                        },
                        onBackToWelcome = { currentScreen = "welcome" }
                    )
                }

                "discover" -> {
                    DiscoverScreen(
                        userProfile = userProfile,
                        filteredList = filteredOpportunities,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.searchQuery.value = it },
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.selectedCategory.value = it },
                        showOnlyEligible = showOnlyEligible,
                        onToggleOnlyEligible = { viewModel.showOnlyEligible.value = it },
                        onOpportunityClick = { selectedOpportunityDetail = it },
                        onBookmarkToggle = { viewModel.toggleSaved(it.opportunity) },
                        onEditProfileClick = { currentScreen = "questionnaire" }
                    )
                }

                "saved" -> {
                    SavedScreen(
                        savedList = savedOpportunities,
                        userProfile = userProfile,
                        onOpportunityClick = { selectedOpportunityDetail = it },
                        onBookmarkToggle = { viewModel.toggleSaved(it.opportunity) }
                    )
                }

                "ai" -> {
                    AiAssistantScreen(
                        messages = chatMessages,
                        isThinking = isAiThinking,
                        onSendMessage = { viewModel.sendAiQuestion(it) }
                    )
                }

                "profile" -> {
                    ProfileScreen(
                        userProfile = userProfile,
                        isAdminMode = isAdminMode,
                        onToggleAdminMode = { enabled ->
                            viewModel.isAdminMode.value = enabled
                            if (enabled) currentScreen = "admin"
                        },
                        onEditProfileClick = { currentScreen = "questionnaire" },
                        onResetProfileClick = {
                            viewModel.resetProfile()
                            currentScreen = "welcome"
                        }
                    )
                }

                "admin" -> {
                    AdminPortalScreen(
                        opportunities = allOpportunities,
                        onAddClick = {
                            opportunityToEditAdmin = null
                            showAdminFormDialog = true
                        },
                        onEditClick = { opp ->
                            opportunityToEditAdmin = opp
                            showAdminFormDialog = true
                        },
                        onDeleteClick = { viewModel.deleteOpportunity(it) },
                        onToggleExpired = { viewModel.toggleExpired(it) },
                        onToggleVerified = { viewModel.toggleVerified(it) }
                    )
                }
            }

            // Opportunity Detail Modal
            selectedOpportunityDetail?.let { item ->
                OpportunityDetailDialog(
                    item = item,
                    onDismiss = { selectedOpportunityDetail = null },
                    onBookmarkToggle = {
                        viewModel.toggleSaved(item.opportunity)
                        selectedOpportunityDetail = item.copy(
                            opportunity = item.opportunity.copy(isSaved = !item.opportunity.isSaved)
                        )
                    }
                )
            }

            // Admin Opportunity Form Dialog
            if (showAdminFormDialog) {
                AdminOpportunityFormDialog(
                    opportunityToEdit = opportunityToEditAdmin,
                    onDismiss = { showAdminFormDialog = false },
                    onSave = { opp ->
                        viewModel.saveOpportunity(opp)
                        showAdminFormDialog = false
                    }
                )
            }
        }
    }
}
