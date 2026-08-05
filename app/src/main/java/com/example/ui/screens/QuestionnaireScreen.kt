package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnaireScreen(
    initialProfile: UserProfileEntity?,
    onComplete: (UserProfileEntity) -> Unit,
    onBackToWelcome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableStateOf(1) }

    var name by remember { mutableStateOf(initialProfile?.name ?: "") }
    var ageStr by remember { mutableStateOf(initialProfile?.age?.toString() ?: "20") }
    var gender by remember { mutableStateOf(initialProfile?.gender ?: "Female") }
    var state by remember { mutableStateOf(initialProfile?.state ?: "Maharashtra") }
    var nationality by remember { mutableStateOf(initialProfile?.nationality ?: "Indian") }

    var isStudying by remember { mutableStateOf(initialProfile?.isStudying ?: true) }
    var educationLevel by remember { mutableStateOf(initialProfile?.educationLevel ?: "Undergraduate") }
    var course by remember { mutableStateOf(initialProfile?.course ?: "Engineering / Technology") }
    var currentYear by remember { mutableStateOf(initialProfile?.currentYear ?: "2nd Year") }

    var familyIncome by remember { mutableStateOf(initialProfile?.familyIncome ?: "₹1.0L - ₹2.5L") }
    var socialCategory by remember { mutableStateOf(initialProfile?.socialCategory ?: "OBC") }
    var isDisability by remember { mutableStateOf(initialProfile?.isDisability ?: false) }
    var isMinority by remember { mutableStateOf(initialProfile?.isMinority ?: false) }

    val genderOptions = listOf("Female", "Male", "Non-binary", "Prefer not to say")
    val stateOptions = listOf(
        "Andhra Pradesh", "Assam", "Bihar", "Delhi", "Gujarat", "Haryana",
        "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Punjab",
        "Rajasthan", "Tamil Nadu", "Telangana", "Uttar Pradesh", "West Bengal", "Other State"
    )
    val educationOptions = listOf(
        "High School (Class 9-12)", "Diploma", "Undergraduate", "Postgraduate", "Doctorate (Ph.D)", "Other"
    )
    val courseOptions = listOf(
        "Engineering / Technology", "Medicine / Healthcare", "Science & Research",
        "Arts & Humanities", "Commerce & Management", "Law", "All Courses"
    )
    val yearOptions = listOf("1st Year", "2nd Year", "3rd Year", "4th Year", "Final Year", "Graduated")
    val incomeOptions = listOf(
        "Below ₹1.0L", "₹1.0L - ₹2.5L", "₹2.5L - ₹5.0L", "₹5.0L - ₹8.0L", "Above ₹8.0L"
    )
    val categoryOptions = listOf("General", "EWS", "OBC", "SC", "ST")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        // Top Header Navigation Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    if (step > 1) step-- else onBackToWelcome()
                }
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "Step $step of 3",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress Bar
        LinearProgressIndicator(
            progress = step / 3f,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color(0xFFE2E8F0)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Scrollable Form Body
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            when (step) {
                1 -> {
                    Text(
                        text = "Personal Profile",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Required to match age, gender & location specific schemes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        placeholder = { Text("e.g. Ananya Sharma") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_name")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Age
                    OutlinedTextField(
                        value = ageStr,
                        onValueChange = { ageStr = it.filter { char -> char.isDigit() } },
                        label = { Text("Age (in Years)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_age")
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Gender Selection (Crucial for gender-specific scholarships)
                    Text(
                        text = "Gender (Required for gender-specific schemes)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        genderOptions.take(3).forEach { option ->
                            val selected = gender.equals(option, ignoreCase = true)
                            Surface(
                                color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(10.dp),
                                border = if (selected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { gender = option }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 12.dp)
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                        ),
                                        color = if (selected) MaterialTheme.colorScheme.primary else Color(0xFF334155)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // State Dropdown
                    CustomDropdown(
                        label = "Home State / Residence",
                        options = stateOptions,
                        selectedOption = state,
                        onOptionSelected = { state = it }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Nationality
                    OutlinedTextField(
                        value = nationality,
                        onValueChange = { nationality = it },
                        label = { Text("Nationality") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                2 -> {
                    Text(
                        text = "Academic Profile",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Matches degree level, stream & research opportunities.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Currently studying switch
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Are you currently studying?",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Enrolled in school, college, or university",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                        }
                        Switch(
                            checked = isStudying,
                            onCheckedChange = { isStudying = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Education Level
                    CustomDropdown(
                        label = "Current Education Level",
                        options = educationOptions,
                        selectedOption = educationLevel,
                        onOptionSelected = { educationLevel = it }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Course / Stream
                    CustomDropdown(
                        label = "Course / Field of Study",
                        options = courseOptions,
                        selectedOption = course,
                        onOptionSelected = { course = it }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Current Year
                    CustomDropdown(
                        label = "Current Academic Year",
                        options = yearOptions,
                        selectedOption = currentYear,
                        onOptionSelected = { currentYear = it }
                    )
                }

                3 -> {
                    Text(
                        text = "Financial & Background",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Unlocks government income caps, reservation & minority quotas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Annual Family Income
                    CustomDropdown(
                        label = "Annual Family Income",
                        options = incomeOptions,
                        selectedOption = familyIncome,
                        onOptionSelected = { familyIncome = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Social Category
                    Text(
                        text = "Social Category",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categoryOptions.forEach { cat ->
                            val selected = socialCategory.equals(cat, ignoreCase = true)
                            Surface(
                                color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(8.dp),
                                border = if (selected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { socialCategory = cat }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = cat,
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                        ),
                                        color = if (selected) MaterialTheme.colorScheme.primary else Color(0xFF334155)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Disability Status (PwD)
                    ToggleRow(
                        title = "Persons with Disability (PwD)",
                        subtitle = "Qualifies for dedicated PwD government quotas & grants",
                        isChecked = isDisability,
                        onCheckedChange = { isDisability = it }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Minority Status
                    ToggleRow(
                        title = "Minority Community Status",
                        subtitle = "Muslim, Christian, Sikh, Buddhist, Jain, Parsi students",
                        isChecked = isMinority,
                        onCheckedChange = { isMinority = it }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Navigation Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (step > 1) {
                OutlinedButton(
                    onClick = { step-- },
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Text("Back")
                }
            }

            Button(
                onClick = {
                    if (step < 3) {
                        step++
                    } else {
                        val parsedAge = ageStr.toIntOrNull() ?: 20
                        val finalProfile = UserProfileEntity(
                            id = 1,
                            name = name.ifBlank { "Student" },
                            age = parsedAge,
                            gender = gender,
                            state = state,
                            nationality = nationality,
                            isStudying = isStudying,
                            educationLevel = educationLevel,
                            course = course,
                            currentYear = currentYear,
                            familyIncome = familyIncome,
                            socialCategory = socialCategory,
                            isDisability = isDisability,
                            isMinority = isMinority,
                            isCompleted = true,
                            isGuest = false
                        )
                        onComplete(finalProfile)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .weight(1.5f)
                    .height(50.dp)
                    .testTag("questionnaire_next_button")
            ) {
                Text(
                    text = if (step < 3) "Next Step" else "Discover Opportunities",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = if (step < 3) Icons.Default.ArrowForward else Icons.Default.CheckCircle,
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B)
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}
