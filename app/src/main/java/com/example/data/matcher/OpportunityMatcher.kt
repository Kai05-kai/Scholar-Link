package com.example.data.matcher

import com.example.data.model.OpportunityEntity
import com.example.data.model.UserProfileEntity

data class MatchResult(
    val matchPercentage: Int,
    val isEligible: Boolean,
    val matchReasons: List<String>,
    val warningReasons: List<String>
)

object OpportunityMatcher {

    fun evaluateMatch(opportunity: OpportunityEntity, profile: UserProfileEntity): MatchResult {
        var score = 0
        var totalCriteria = 0
        val matchReasons = mutableListOf<String>()
        val warningReasons = mutableListOf<String>()

        // 1. Age check
        totalCriteria += 1
        if (profile.age in opportunity.minAge..opportunity.maxAge) {
            score += 1
            if (opportunity.minAge > 0 || opportunity.maxAge < 100) {
                matchReasons.add("✓ Your age (${profile.age}) matches (${opportunity.minAge}-${opportunity.maxAge} yrs)")
            } else {
                matchReasons.add("✓ Age criteria satisfied (Open to all ages)")
            }
        } else {
            warningReasons.add("⚠ Requires age between ${opportunity.minAge} and ${opportunity.maxAge} years")
        }

        // 2. Gender check
        totalCriteria += 1
        val genderMatched = opportunity.allowedGenders.contains("All", ignoreCase = true) ||
                opportunity.allowedGenders.contains(profile.gender, ignoreCase = true)
        if (genderMatched) {
            score += 1
            if (!opportunity.allowedGenders.contains("All", ignoreCase = true)) {
                matchReasons.add("✓ Your gender (${profile.gender}) matches target demographic (${opportunity.allowedGenders})")
            } else {
                matchReasons.add("✓ Gender criteria matched (Open to all genders)")
            }
        } else {
            warningReasons.add("⚠ Specific to ${opportunity.allowedGenders} candidates")
        }

        // 3. Education Level check
        totalCriteria += 1
        val eduMatched = opportunity.allowedEducationLevels.contains("All", ignoreCase = true) ||
                opportunity.allowedEducationLevels.contains(profile.educationLevel, ignoreCase = true)
        if (eduMatched) {
            score += 1
            if (!opportunity.allowedEducationLevels.contains("All", ignoreCase = true)) {
                matchReasons.add("✓ Your education level (${profile.educationLevel}) matches requirement")
            } else {
                matchReasons.add("✓ Education level criteria matched (Open to all levels)")
            }
        } else {
            warningReasons.add("⚠ Requires ${opportunity.allowedEducationLevels} status")
        }

        // 4. Course check
        totalCriteria += 1
        val courseMatched = opportunity.allowedCourses.contains("All", ignoreCase = true) ||
                opportunity.allowedCourses.contains(profile.course, ignoreCase = true) ||
                profile.course.contains("All", ignoreCase = true)
        if (courseMatched) {
            score += 1
            if (!opportunity.allowedCourses.contains("All", ignoreCase = true)) {
                matchReasons.add("✓ Your course (${profile.course}) matches stream requirement")
            } else {
                matchReasons.add("✓ Course stream matched (Open to all fields)")
            }
        } else {
            warningReasons.add("⚠ Targeted for ${opportunity.allowedCourses} students")
        }

        // 5. State check
        totalCriteria += 1
        val stateMatched = opportunity.allowedStates.contains("All", ignoreCase = true) ||
                opportunity.allowedStates.contains(profile.state, ignoreCase = true)
        if (stateMatched) {
            score += 1
            if (!opportunity.allowedStates.contains("All", ignoreCase = true)) {
                matchReasons.add("✓ Your state (${profile.state}) matches regional scheme")
            } else {
                matchReasons.add("✓ State criteria matched (Pan-India / International)")
            }
        } else {
            warningReasons.add("⚠ Restricted to residents of ${opportunity.allowedStates}")
        }

        // 6. Income check
        totalCriteria += 1
        val userIncomeLevel = parseIncomeRank(profile.familyIncome)
        val maxIncomeRank = parseIncomeRank(opportunity.maxIncomeLimit)
        if (userIncomeLevel <= maxIncomeRank) {
            score += 1
            if (maxIncomeRank < 5) {
                matchReasons.add("✓ Your family income (${profile.familyIncome}) is within limit (${opportunity.maxIncomeLimit})")
            } else {
                matchReasons.add("✓ Income criteria satisfied (No upper income cap)")
            }
        } else {
            warningReasons.add("⚠ Requires annual family income under ${opportunity.maxIncomeLimit}")
        }

        // 7. Social Category check
        totalCriteria += 1
        val categoryMatched = opportunity.allowedCategories.contains("All", ignoreCase = true) ||
                opportunity.allowedCategories.contains(profile.socialCategory, ignoreCase = true)
        if (categoryMatched) {
            score += 1
            if (!opportunity.allowedCategories.contains("All", ignoreCase = true)) {
                matchReasons.add("✓ Your social category (${profile.socialCategory}) matches reservation criteria")
            } else {
                matchReasons.add("✓ Social category matched (All categories eligible)")
            }
        } else {
            warningReasons.add("⚠ Reserved for ${opportunity.allowedCategories} applicants")
        }

        // 8. Disability (PwD) check
        if (opportunity.requiresDisability) {
            totalCriteria += 1
            if (profile.isDisability) {
                score += 1
                matchReasons.add("✓ PwD (Disability) special quota applies to your profile")
            } else {
                warningReasons.add("⚠ Requires Persons with Benchmark Disabilities (PwD) status")
            }
        }

        // 9. Minority check
        if (opportunity.requiresMinority) {
            totalCriteria += 1
            if (profile.isMinority) {
                score += 1
                matchReasons.add("✓ Minority Community affirmative initiative applies")
            } else {
                warningReasons.add("⚠ Dedicated scheme for Minority Community students")
            }
        }

        val percentage = ((score.toDouble() / totalCriteria.toDouble()) * 100).toInt().coerceIn(40, 100)
        val isEligible = warningReasons.isEmpty()

        return MatchResult(
            matchPercentage = percentage,
            isEligible = isEligible,
            matchReasons = matchReasons,
            warningReasons = warningReasons
        )
    }

    private fun parseIncomeRank(incomeStr: String): Int {
        return when {
            incomeStr.contains("Below ₹1.0L", ignoreCase = true) -> 1
            incomeStr.contains("1.0L", ignoreCase = true) || incomeStr.contains("2.5L", ignoreCase = true) && !incomeStr.contains("5.0L") -> 2
            incomeStr.contains("2.5L - ₹5.0L", ignoreCase = true) -> 3
            incomeStr.contains("5.0L - ₹8.0L", ignoreCase = true) -> 4
            else -> 5 // Above 8.0L / No Limit
        }
    }
}
