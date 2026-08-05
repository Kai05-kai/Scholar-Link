package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.matcher.MatchResult
import com.example.ui.theme.GreenMatchBg
import com.example.ui.theme.GreenMatchText

@Composable
fun MatchPercentageChip(
    percentage: Int,
    isEligible: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (percentage >= 90) {
        GreenMatchBg
    } else if (percentage >= 75) {
        Color(0xFFFEF3C7) // Amber
    } else {
        Color(0xFFF1F5F9)
    }

    val textColor = if (percentage >= 90) {
        GreenMatchText
    } else if (percentage >= 75) {
        Color(0xFFB45309)
    } else {
        Color(0xFF475569)
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.testTag("match_percentage_chip")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = if (isEligible) Icons.Default.CheckCircle else Icons.Default.Info,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$percentage% Match",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = textColor
            )
        }
    }
}

@Composable
fun MatchReasonsBreakdown(
    matchResult: MatchResult,
    modifier: Modifier = Modifier,
    maxReasonsToShow: Int = 3
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        val displayReasons = matchResult.matchReasons.take(maxReasonsToShow)
        displayReasons.forEach { reason ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = GreenMatchText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = reason,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = Color(0xFF166534)
                    )
                )
            }
        }

        if (matchResult.matchReasons.size > maxReasonsToShow) {
            Text(
                text = "+ ${matchResult.matchReasons.size - maxReasonsToShow} more profile matches",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 20.dp, top = 2.dp)
            )
        }

        matchResult.warningReasons.take(1).forEach { warning ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = warning,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = Color(0xFFB45309)
                    )
                )
            }
        }
    }
}
