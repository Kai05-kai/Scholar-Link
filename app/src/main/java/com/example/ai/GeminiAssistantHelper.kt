package com.example.ai

import com.example.BuildConfig
import com.example.data.matcher.OpportunityMatcher
import com.example.data.model.OpportunityEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiAssistantHelper {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun generateAnswer(
        question: String,
        profile: UserProfileEntity?,
        opportunities: List<OpportunityEntity>
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val dbContextBuilder = StringBuilder()
                dbContextBuilder.append("USER PROFILE:\n")
                if (profile != null && profile.isCompleted) {
                    dbContextBuilder.append("Name: ${profile.name}, Age: ${profile.age}, Gender: ${profile.gender}, ")
                    dbContextBuilder.append("State: ${profile.state}, Education: ${profile.educationLevel}, Course: ${profile.course}, ")
                    dbContextBuilder.append("Income: ${profile.familyIncome}, Category: ${profile.socialCategory}, ")
                    dbContextBuilder.append("PwD: ${profile.isDisability}, Minority: ${profile.isMinority}\n\n")
                } else {
                    dbContextBuilder.append("Profile not fully completed / Guest mode.\n\n")
                }

                dbContextBuilder.append("AVAILABLE OPPORTUNITIES IN SCHOLARLINK DATABASE:\n")
                opportunities.filter { !it.isExpired }.forEachIndexed { idx, opp ->
                    val match = if (profile != null) OpportunityMatcher.evaluateMatch(opp, profile) else null
                    dbContextBuilder.append("${idx + 1}. [${opp.category}] ${opp.title}\n")
                    dbContextBuilder.append("   Organization: ${opp.organization}\n")
                    dbContextBuilder.append("   Deadline: ${opp.deadline}\n")
                    dbContextBuilder.append("   Benefits: ${opp.benefits}\n")
                    dbContextBuilder.append("   Eligibility: ${opp.eligibilitySummary}\n")
                    dbContextBuilder.append("   Required Documents: ${opp.requiredDocuments}\n")
                    dbContextBuilder.append("   Official Website: ${opp.officialWebsite}\n")
                    if (match != null) {
                        dbContextBuilder.append("   Match Score: ${match.matchPercentage}%\n")
                        dbContextBuilder.append("   Match Status: ${if (match.isEligible) "Eligible" else "Review criteria"}\n")
                    }
                    dbContextBuilder.append("\n")
                }

                val systemPrompt = """
                    You are ScholarBot, an AI Assistant inside ScholarLink, a student opportunity discovery platform.
                    
                    STRICT RULES YOU MUST FOLLOW:
                    1. Answer ONLY using the information provided in the database context above.
                    2. Never invent, hallucinate, or suggest opportunities, scholarships, or criteria that are not listed in the context.
                    3. Address the student warmly and reference their profile when explaining why they qualify for specific items.
                    4. Always include the official website links from the database context when recommending an opportunity.
                    5. If the information requested is uncertain or not found in the provided database context, explicitly state that and advise the user to verify on official government/university portals.
                    6. Keep responses clean, concise, bulleted, and actionable.
                """.trimIndent()

                // Construct Gemini REST JSON Request using org.json
                val rootJson = JSONObject()

                val sysInstructionJson = JSONObject()
                val sysPartsArray = JSONArray()
                sysPartsArray.put(JSONObject().put("text", systemPrompt))
                sysInstructionJson.put("parts", sysPartsArray)
                rootJson.put("systemInstruction", sysInstructionJson)

                val contentsArray = JSONArray()
                val userContentObj = JSONObject()
                val userPartsArray = JSONArray()
                userPartsArray.put(JSONObject().put("text", "DATABASE CONTEXT:\n$dbContextBuilder\n\nUSER QUESTION: $question"))
                userContentObj.put("parts", userPartsArray)
                contentsArray.put(userContentObj)
                rootJson.put("contents", contentsArray)

                val requestBody = rootJson.toString().toRequestBody("application/json".toMediaType())
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

                val httpRequest = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                val response = client.newCall(httpRequest).execute()
                val responseStr = response.body?.string()

                if (response.isSuccessful && !responseStr.isNullOrBlank()) {
                    val resJson = JSONObject(responseStr)
                    val candidates = resJson.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val cand = candidates.getJSONObject(0)
                        val content = cand.optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val replyText = parts.getJSONObject(0).optString("text", "")
                            if (replyText.isNotBlank()) {
                                return@withContext replyText
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Fallback to local grounded logic below
            }
        }

        // Local Grounded Engine (Offline / Fallback)
        generateLocalGroundedAnswer(question, profile, opportunities)
    }

    private fun generateLocalGroundedAnswer(
        question: String,
        profile: UserProfileEntity?,
        opportunities: List<OpportunityEntity>
    ): String {
        val q = question.lowercase()
        val activeOpps = opportunities.filter { !it.isExpired }

        if (q.contains("scholarship") || q.contains("scheme")) {
            val scholarships = activeOpps.filter { it.category.equals("Scholarship", ignoreCase = true) || it.category.equals("Government Scheme", ignoreCase = true) }
            if (scholarships.isEmpty()) {
                return "Currently, there are no active scholarships listed in the database. Please check back later or verify on official portals."
            }
            val sb = StringBuilder()
            sb.append("🎓 **Scholarships Found in ScholarLink Database:**\n\n")
            scholarships.forEach { opp ->
                val match = if (profile != null) OpportunityMatcher.evaluateMatch(opp, profile) else null
                val matchText = if (match != null) " (${match.matchPercentage}% Match)" else ""
                sb.append("• **${opp.title}**$matchText\n")
                sb.append("  *Provider:* ${opp.organization}\n")
                sb.append("  *Benefits:* ${opp.benefits}\n")
                sb.append("  *Deadline:* ${opp.deadline}\n")
                sb.append("  *Official Link:* ${opp.officialWebsite}\n\n")
            }
            sb.append("ℹ *Note:* Please verify eligibility details and deadline updates on the official website provided.")
            return sb.toString()
        }

        if (q.contains("internship") || q.contains("fellowship") || q.contains("work")) {
            val internships = activeOpps.filter { it.category.equals("Internship", ignoreCase = true) || it.category.equals("Fellowship", ignoreCase = true) || it.category.equals("Research", ignoreCase = true) }
            val sb = StringBuilder()
            sb.append("💼 **Internships & Fellowships in Database:**\n\n")
            internships.forEach { opp ->
                val match = if (profile != null) OpportunityMatcher.evaluateMatch(opp, profile) else null
                val matchText = if (match != null) " (${match.matchPercentage}% Match)" else ""
                sb.append("• **${opp.title}**$matchText\n")
                sb.append("  *Organization:* ${opp.organization}\n")
                sb.append("  *Perks:* ${opp.benefits}\n")
                sb.append("  *Official Link:* ${opp.officialWebsite}\n\n")
            }
            sb.append("ℹ *Note:* Always submit your application through the official portal link.")
            return sb.toString()
        }

        if (q.contains("document") || q.contains("require") || q.contains("need") || q.contains("apply")) {
            val sb = StringBuilder()
            sb.append("📄 **Common Documents Required for Applications:**\n\n")
            val docs = activeOpps.flatMap { it.requiredDocuments.split(",") }
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .distinct()
            docs.forEach { doc ->
                sb.append("• $doc\n")
            }
            sb.append("\n💡 *Tip:* Keep digital scanned copies (PDF/JPEG) ready before starting your official online application!")
            return sb.toString()
        }

        if (q.contains("eligible") || q.contains("match") || q.contains("qualify") || q.contains("profile")) {
            if (profile == null || !profile.isCompleted) {
                return "To see personalized recommendations, please complete your profile questionnaire first!"
            }
            val matches = activeOpps.map { opp ->
                Pair(opp, OpportunityMatcher.evaluateMatch(opp, profile))
            }.sortedByDescending { it.second.matchPercentage }

            val sb = StringBuilder()
            sb.append("✨ **Personalized Matches for ${profile.name}:**\n\n")
            matches.take(4).forEach { (opp, match) ->
                sb.append("• **${opp.title}** (${match.matchPercentage}% Match)\n")
                sb.append("  *Category:* ${opp.category}\n")
                sb.append("  *Why You Match:* ${match.matchReasons.firstOrNull() ?: "Matches profile"}\n")
                sb.append("  *Link:* ${opp.officialWebsite}\n\n")
            }
            sb.append("Visit the **Discover** tab to explore full match breakdowns!")
            return sb.toString()
        }

        // Default response grounded in DB
        val sb = StringBuilder()
        sb.append("Hello! I am **ScholarBot**, your grounded ScholarLink assistant.\n\n")
        sb.append("I can help you explore opportunities currently in the app database:\n")
        sb.append("• **Scholarships & Government Schemes**\n")
        sb.append("• **Internships & Fellowships**\n")
        sb.append("• **Required Documents & Eligibility Check**\n\n")
        sb.append("Currently, there are **${activeOpps.size} active opportunities** in ScholarLink. What would you like to know?")
        return sb.toString()
    }
}
