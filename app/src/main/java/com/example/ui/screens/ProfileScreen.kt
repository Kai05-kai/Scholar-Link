package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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

@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    isAdminMode: Boolean,
    onToggleAdminMode: (Boolean) -> Unit,
    onEditProfileClick: () -> Unit,
    onResetProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile = userProfile ?: UserProfileEntity()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Profile & Settings",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Student Profile Overview Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile.name.ifBlank { "Guest Student" },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "${profile.age} years • ${profile.gender} • ${profile.state}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                            color = Color(0xFF64748B)
                        )
                    }

                    IconButton(
                        onClick = onEditProfileClick,
                        modifier = Modifier.testTag("profile_edit_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(14.dp))

                // Profile Details Grid
                ProfileDetailRow(label = "Education Level", value = profile.educationLevel)
                ProfileDetailRow(label = "Course / Field", value = profile.course)
                ProfileDetailRow(label = "Current Year", value = profile.currentYear)
                ProfileDetailRow(label = "Family Income", value = profile.familyIncome)
                ProfileDetailRow(label = "Social Category", value = profile.socialCategory)
                ProfileDetailRow(label = "PwD Quota", value = if (profile.isDisability) "Yes" else "No")
                ProfileDetailRow(label = "Minority Quota", value = if (profile.isMinority) "Yes" else "No")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onEditProfileClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Re-take Profile Questionnaire")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Administrator Portal Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isAdminMode) Color(0xFFEFF6FF) else MaterialTheme.colorScheme.surface
            ),
            border = if (isAdminMode) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = if (isAdminMode) MaterialTheme.colorScheme.primary else Color(0xFFF1F5F9),
                            shape = CircleShape,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = if (isAdminMode) Color.White else Color(0xFF475569),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Administrator Portal",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Add, edit, delete & verify opportunities",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Switch(
                        checked = isAdminMode,
                        onCheckedChange = onToggleAdminMode,
                        modifier = Modifier.testTag("admin_mode_switch")
                    )
                }

                if (isAdminMode) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFFDBEAFE),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "⚡ Admin Portal Mode Active: You can manage opportunities directly in real-time from the Admin tab below.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            ),
                            color = Color(0xFF1E40AF),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Reset Profile Option
        OutlinedButton(
            onClick = onResetProfileClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Reset All Profile Data", color = Color(0xFFDC2626))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mission Statement
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.School, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ScholarLink Mission",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Built to ensure no student ever misses a scholarship, internship or grant they were fully eligible for. Powered by automatic eligibility matching and transparent grounds.",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

@Composable
private fun ProfileDetailRow(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF64748B)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0F172A)
        )
    }
}
