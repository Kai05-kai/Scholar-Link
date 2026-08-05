package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "opportunities")
data class OpportunityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // Scholarship, Government Scheme, Internship, Fellowship, Grant, Skill Program, Competition, Research
    val organization: String,
    val shortDescription: String,
    val fullDetails: String,
    val deadline: String, // e.g. "30 Sep 2026"
    val daysRemaining: Int = 30,
    val benefits: String, // e.g. "₹50,000 / year stipend + Book Allowance"
    val eligibilitySummary: String,
    val requiredDocuments: String, // Comma separated string
    val applicationLink: String,
    val officialWebsite: String,
    val isVerified: Boolean = true,
    val isExpired: Boolean = false,
    val isSaved: Boolean = false,

    // Matching rules
    val minAge: Int = 0,
    val maxAge: Int = 100,
    val allowedGenders: String = "All", // All, Female, Male
    val allowedStates: String = "All", // All, or comma separated states
    val allowedNationalities: String = "All",
    val allowedEducationLevels: String = "All", // All, or "Undergraduate,Postgraduate"
    val allowedCourses: String = "All",
    val maxIncomeLimit: String = "Above ₹8.0L", // Below ₹1.0L, ₹1.0L - ₹2.5L, ₹2.5L - ₹5.0L, ₹5.0L - ₹8.0L, Above ₹8.0L
    val allowedCategories: String = "All", // All, or "OBC,SC,ST,EWS"
    val requiresDisability: Boolean = false,
    val requiresMinority: Boolean = false
)
