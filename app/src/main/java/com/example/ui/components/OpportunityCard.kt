package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.OpportunityWithMatch
import com.example.ui.theme.ExpiredRed
import com.example.ui.theme.ExpiredRedContainer
import com.example.ui.theme.OrangeBadgeBg
import com.example.ui.theme.OrangeBadgeText

@Composable
fun OpportunityCard(
    item: OpportunityWithMatch,
    onCardClick: () -> Unit,
    onBookmarkToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val opp = item.opportunity
    val match = item.matchResult

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = if (match.matchPercentage >= 90) {
            BorderStroke(1.5.dp, Color(0xFF86EFAC))
        } else {
            BorderStroke(1.dp, Color(0xFFE2E8F0))
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("opportunity_card_${opp.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row: Category, Match Chip, Verified, Bookmark
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CategoryChip(category = opp.category)

                    if (opp.isVerified) {
                        Surface(
                            color = Color(0xFFEFF6FF),
                            shape = CircleShape,
                            modifier = Modifier.size(22.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified Opportunity",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MatchPercentageChip(
                        percentage = match.matchPercentage,
                        isEligible = match.isEligible
                    )

                    IconButton(
                        onClick = onBookmarkToggle,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("bookmark_button_${opp.id}")
                    ) {
                        Icon(
                            imageVector = if (opp.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (opp.isSaved) "Remove Bookmark" else "Save Bookmark",
                            tint = if (opp.isSaved) MaterialTheme.colorScheme.primary else Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = opp.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Organization
            Text(
                text = opp.organization,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Benefits Banner
            Surface(
                color = OrangeBadgeBg,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = OrangeBadgeText,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = opp.benefits,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        ),
                        color = OrangeBadgeText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Match Reasons Breakdown (WHY this matches your profile)
            Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Why this matches your profile:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    MatchReasonsBreakdown(matchResult = match, maxReasonsToShow = 2)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer: Deadline
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Deadline: ${opp.deadline}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = Color(0xFF64748B)
                    )
                }

                if (opp.isExpired) {
                    Surface(
                        color = ExpiredRedContainer,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Expired",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = ExpiredRed,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                } else if (opp.daysRemaining in 1..14) {
                    Surface(
                        color = Color(0xFFFEF2F2),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "⚡ ${opp.daysRemaining} days left",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = Color(0xFF991B1B),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (category) {
        "Scholarship" -> Pair(Color(0xFFFFEDD5), Color(0xFFC2410C))
        "Government Scheme" -> Pair(Color(0xFFDCFCE7), Color(0xFF15803D))
        "Internship" -> Pair(Color(0xFFDBEAFE), Color(0xFF1D4ED8))
        "Fellowship" -> Pair(Color(0xFFE0E7FF), Color(0xFF4338CA))
        "Grant" -> Pair(Color(0xFFFEF3C7), Color(0xFFB45309))
        "Skill Program" -> Pair(Color(0xFFF3E8FF), Color(0xFF6B21A8))
        "Competition" -> Pair(Color(0xFFFCE7F3), Color(0xFF9D174D))
        else -> Pair(Color(0xFFF1F5F9), Color(0xFF475569))
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(100.dp),
        modifier = modifier
    ) {
        Text(
            text = category.uppercase(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.5.sp
            ),
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
