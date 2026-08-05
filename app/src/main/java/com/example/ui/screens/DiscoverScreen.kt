package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity
import com.example.ui.OpportunityWithMatch
import com.example.ui.components.OpportunityCard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Brush

@Composable
fun DiscoverScreen(
    userProfile: UserProfileEntity?,
    filteredList: List<OpportunityWithMatch>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    showOnlyEligible: Boolean,
    onToggleOnlyEligible: (Boolean) -> Unit,
    onOpportunityClick: (OpportunityWithMatch) -> Unit,
    onBookmarkToggle: (OpportunityWithMatch) -> Unit,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "All", "Scholarship", "Government Scheme", "Internship", "Fellowship",
        "Grant", "Skill Program", "Competition", "Research"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FB))
    ) {
        // Top App Bar
        Surface(
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Column {
                    Text(
                        text = "ScholarLink",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        color = Color(0xFF1D4ED8)
                    )
                    Text(
                        text = "PERSONALIZED FOR YOU",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.5.sp
                        ),
                        color = Color(0xFF94A3B8)
                    )
                }

                // Profile Avatar Initials
                val initials = if (userProfile != null && userProfile.name.isNotBlank()) {
                    userProfile.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("").uppercase()
                } else "SL"

                Surface(
                    color = Color(0xFFEFF6FF),
                    border = BorderStroke(2.dp, Color(0xFFDBEAFE)),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(42.dp)
                        .clickable { onEditProfileClick() }
                        .testTag("edit_profile_avatar")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = initials,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = Color(0xFF1D4ED8)
                        )
                    }
                }
            }
        }

        // Profile Match Banner with Blue-Indigo Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF2563EB), Color(0xFF4338CA))
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (userProfile != null && userProfile.isCompleted) {
                            "Your Profile: 100% Ready"
                        } else {
                            "Your Profile: Incomplete"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        ),
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${filteredList.size} Matches Found",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = Color.White
                    )
                }

                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .clickable { onEditProfileClick() }
                        .testTag("ai_sync_badge")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "✨ AI Sync",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Search Bar
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search scholarships, schemes, internships...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_input")
                )
            }

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val selected = selectedCategory.equals(category, ignoreCase = true)
                FilterChip(
                    selected = selected,
                    onClick = { onCategorySelected(category) },
                    label = {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFF1F5F9),
                        labelColor = Color(0xFF475569)
                    )
                )
            }
        }

        // Feed Area Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
        ) {
            Text(
                text = "Top Recommendations",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = Color(0xFF1E293B)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Eligible Only",
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Switch(
                    checked = showOnlyEligible,
                    onCheckedChange = onToggleOnlyEligible,
                    modifier = Modifier.testTag("eligible_toggle_switch")
                )
            }
        }

        // Opportunities List
        if (filteredList.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No matching opportunities found",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Try clearing filters, searching for a broader term, or updating your questionnaire profile.",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = filteredList,
                    key = { it.opportunity.id }
                ) { item ->
                    OpportunityCard(
                        item = item,
                        onCardClick = { onOpportunityClick(item) },
                        onBookmarkToggle = { onBookmarkToggle(item) }
                    )
                }
            }
        }
    }
}
