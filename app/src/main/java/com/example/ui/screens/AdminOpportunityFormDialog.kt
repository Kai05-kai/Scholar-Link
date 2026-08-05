package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.OpportunityEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOpportunityFormDialog(
    opportunityToEdit: OpportunityEntity?,
    onDismiss: () -> Unit,
    onSave: (OpportunityEntity) -> Unit
) {
    var title by remember { mutableStateOf(opportunityToEdit?.title ?: "") }
    var category by remember { mutableStateOf(opportunityToEdit?.category ?: "Scholarship") }
    var organization by remember { mutableStateOf(opportunityToEdit?.organization ?: "") }
    var shortDescription by remember { mutableStateOf(opportunityToEdit?.shortDescription ?: "") }
    var fullDetails by remember { mutableStateOf(opportunityToEdit?.fullDetails ?: "") }
    var deadline by remember { mutableStateOf(opportunityToEdit?.deadline ?: "30 Nov 2026") }
    var daysRemainingStr by remember { mutableStateOf(opportunityToEdit?.daysRemaining?.toString() ?: "30") }
    var benefits by remember { mutableStateOf(opportunityToEdit?.benefits ?: "") }
    var eligibilitySummary by remember { mutableStateOf(opportunityToEdit?.eligibilitySummary ?: "") }
    var requiredDocuments by remember { mutableStateOf(opportunityToEdit?.requiredDocuments ?: "Aadhaar Card, Marksheet, Income Certificate") }
    var applicationLink by remember { mutableStateOf(opportunityToEdit?.applicationLink ?: "https://scholarships.gov.in/") }
    var officialWebsite by remember { mutableStateOf(opportunityToEdit?.officialWebsite ?: "https://education.gov.in/") }
    var isVerified by remember { mutableStateOf(opportunityToEdit?.isVerified ?: true) }
    var isExpired by remember { mutableStateOf(opportunityToEdit?.isExpired ?: false) }

    var minAgeStr by remember { mutableStateOf(opportunityToEdit?.minAge?.toString() ?: "16") }
    var maxAgeStr by remember { mutableStateOf(opportunityToEdit?.maxAge?.toString() ?: "30") }
    var allowedGenders by remember { mutableStateOf(opportunityToEdit?.allowedGenders ?: "All") }
    var allowedStates by remember { mutableStateOf(opportunityToEdit?.allowedStates ?: "All") }
    var maxIncomeLimit by remember { mutableStateOf(opportunityToEdit?.maxIncomeLimit ?: "₹2.5L - ₹5.0L") }
    var allowedCategories by remember { mutableStateOf(opportunityToEdit?.allowedCategories ?: "All") }
    var requiresDisability by remember { mutableStateOf(opportunityToEdit?.requiresDisability ?: false) }
    var requiresMinority by remember { mutableStateOf(opportunityToEdit?.requiresMinority ?: false) }

    val categoryOptions = listOf(
        "Scholarship", "Government Scheme", "Internship", "Fellowship",
        "Grant", "Skill Program", "Competition", "Research"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.92f)
                .testTag("admin_opportunity_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (opportunityToEdit == null) "Add New Opportunity" else "Edit Opportunity",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Form
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Opportunity Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("admin_input_title")
                    )

                    // Category Dropdown
                    var expandedCat by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedCat,
                        onExpandedChange = { expandedCat = !expandedCat },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCat,
                            onDismissRequest = { expandedCat = false }
                        ) {
                            categoryOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        category = option
                                        expandedCat = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = organization,
                        onValueChange = { organization = it },
                        label = { Text("Organization / Department") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = shortDescription,
                        onValueChange = { shortDescription = it },
                        label = { Text("Short Description") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = fullDetails,
                        onValueChange = { fullDetails = it },
                        label = { Text("Full Details / Description") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = deadline,
                            onValueChange = { deadline = it },
                            label = { Text("Deadline (e.g. 30 Nov 2026)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = daysRemainingStr,
                            onValueChange = { daysRemainingStr = it.filter { c -> c.isDigit() } },
                            label = { Text("Days Left") },
                            modifier = Modifier.weight(0.7f)
                        )
                    }

                    OutlinedTextField(
                        value = benefits,
                        onValueChange = { benefits = it },
                        label = { Text("Benefits / Stipend Amount") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = eligibilitySummary,
                        onValueChange = { eligibilitySummary = it },
                        label = { Text("Eligibility Requirements Summary") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = requiredDocuments,
                        onValueChange = { requiredDocuments = it },
                        label = { Text("Required Documents (comma separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = applicationLink,
                        onValueChange = { applicationLink = it },
                        label = { Text("Official Application Link") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = officialWebsite,
                        onValueChange = { officialWebsite = it },
                        label = { Text("Official Website URL") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Match Criteria Settings:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = minAgeStr,
                            onValueChange = { minAgeStr = it.filter { c -> c.isDigit() } },
                            label = { Text("Min Age") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = maxAgeStr,
                            onValueChange = { maxAgeStr = it.filter { c -> c.isDigit() } },
                            label = { Text("Max Age") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = allowedGenders,
                        onValueChange = { allowedGenders = it },
                        label = { Text("Target Gender (All / Female / Male)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = allowedStates,
                        onValueChange = { allowedStates = it },
                        label = { Text("Target States (All or Maharashtra,Delhi...)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = maxIncomeLimit,
                        onValueChange = { maxIncomeLimit = it },
                        label = { Text("Income Upper Limit (e.g. ₹2.5L - ₹5.0L)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = allowedCategories,
                        onValueChange = { allowedCategories = it },
                        label = { Text("Social Categories (All or OBC,SC,ST,EWS)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Switches for Verified, Expired, Disability, Minority
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mark Information as Verified", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = isVerified, onCheckedChange = { isVerified = it })
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mark as Expired", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = isExpired, onCheckedChange = { isExpired = it })
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Requires PwD (Disability)", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = requiresDisability, onCheckedChange = { requiresDisability = it })
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Requires Minority Status", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = requiresMinority, onCheckedChange = { requiresMinority = it })
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val newOpp = OpportunityEntity(
                                id = opportunityToEdit?.id ?: 0,
                                title = title.ifBlank { "Untitled Opportunity" },
                                category = category,
                                organization = organization.ifBlank { "Government / Official Dept" },
                                shortDescription = shortDescription,
                                fullDetails = fullDetails.ifBlank { shortDescription },
                                deadline = deadline,
                                daysRemaining = daysRemainingStr.toIntOrNull() ?: 30,
                                benefits = benefits.ifBlank { "Financial Assistance & Support" },
                                eligibilitySummary = eligibilitySummary.ifBlank { "Open to eligible candidates" },
                                requiredDocuments = requiredDocuments,
                                applicationLink = applicationLink,
                                officialWebsite = officialWebsite,
                                isVerified = isVerified,
                                isExpired = isExpired,
                                isSaved = opportunityToEdit?.isSaved ?: false,
                                minAge = minAgeStr.toIntOrNull() ?: 0,
                                maxAge = maxAgeStr.toIntOrNull() ?: 100,
                                allowedGenders = allowedGenders,
                                allowedStates = allowedStates,
                                maxIncomeLimit = maxIncomeLimit,
                                allowedCategories = allowedCategories,
                                requiresDisability = requiresDisability,
                                requiresMinority = requiresMinority
                            )
                            onSave(newOpp)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1.3f).testTag("admin_save_opportunity_button")
                    ) {
                        Text("Save Opportunity", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
