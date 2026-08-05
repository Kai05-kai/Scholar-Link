package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.matcher.OpportunityMatcher
import com.example.data.model.OpportunityEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.OpportunityWithMatch
import com.example.ui.components.OpportunityCard

@Composable
fun SavedScreen(
    savedList: List<OpportunityEntity>,
    userProfile: UserProfileEntity?,
    onOpportunityClick: (OpportunityWithMatch) -> Unit,
    onBookmarkToggle: (OpportunityWithMatch) -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultProfile = userProfile ?: UserProfileEntity()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp)
        ) {
            Text(
                text = "Saved Bookmarks (${savedList.size})",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
            )

            if (savedList.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No saved opportunities yet",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF475569)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Bookmark scholarships, internships or schemes in the Discover tab to track deadlines easily.",
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
                        items = savedList,
                        key = { it.id }
                    ) { opp ->
                        val match = OpportunityMatcher.evaluateMatch(opp, defaultProfile)
                        val item = OpportunityWithMatch(opp, match)
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
}
