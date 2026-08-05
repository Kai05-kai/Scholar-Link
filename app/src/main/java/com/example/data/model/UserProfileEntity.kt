package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val age: Int = 20,
    val gender: String = "Female", // Female, Male, Non-binary, All
    val state: String = "Maharashtra",
    val nationality: String = "Indian",
    val isStudying: Boolean = true,
    val educationLevel: String = "Undergraduate", // High School, Undergraduate, Postgraduate, Doctorate, Diploma, Other
    val course: String = "Engineering / Technology", // Engineering / Technology, Medicine, Science, Arts & Humanities, Commerce, Law, All
    val currentYear: String = "2nd Year", // 1st Year, 2nd Year, 3rd Year, 4th Year, Final Year, Graduated
    val familyIncome: String = "₹1.0L - ₹2.5L", // Below ₹1.0L, ₹1.0L - ₹2.5L, ₹2.5L - ₹5.0L, ₹5.0L - ₹8.0L, Above ₹8.0L
    val socialCategory: String = "OBC", // General, EWS, OBC, SC, ST
    val isDisability: Boolean = false, // PwD
    val isMinority: Boolean = false,
    val isCompleted: Boolean = false,
    val isGuest: Boolean = false
)
