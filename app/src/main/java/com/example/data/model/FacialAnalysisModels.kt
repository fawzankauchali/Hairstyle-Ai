package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FacialProportions(
    @Json(name = "jawline") val jawline: String = "Defined & Angular",
    @Json(name = "cheekbones") val cheekbones: String = "Prominent & High",
    @Json(name = "forehead") val forehead: String = "Balanced Width",
    @Json(name = "facialRatio") val facialRatio: String = "1.45:1 (Balanced Proportion)",
    @Json(name = "symmetryScore") val symmetryScore: String = "94% High Facial Symmetry",
    @Json(name = "goldenRatioFit") val goldenRatioFit: String = "Classical 1:1:1 Facial Thirds",
    @Json(name = "keyFeatures") val keyFeatures: List<String> = listOf(
        "Defined mandibular angle",
        "Balanced forehead-to-chin proportion",
        "High cheekbone structure"
    )
)

@JsonClass(generateAdapter = true)
data class HaircutRecommendation(
    @Json(name = "id") val id: String = "1",
    @Json(name = "styleName") val styleName: String,
    @Json(name = "category") val category: String,
    @Json(name = "whyItWorks") val whyItWorks: String,
    @Json(name = "barberInstructions") val barberInstructions: String,
    @Json(name = "stylingProduct") val stylingProduct: String,
    @Json(name = "maintenanceInterval") val maintenanceInterval: String,
    @Json(name = "difficulty") val difficulty: String,
    @Json(name = "imagePreviewEditPrompt") val imagePreviewEditPrompt: String
)

@JsonClass(generateAdapter = true)
data class FacialAnalysisData(
    @Json(name = "faceShape") val faceShape: String = "Oval",
    @Json(name = "faceShapeDescription") val faceShapeDescription: String = "Slightly rounded jawline with forehead slightly wider than jaw, considered the most versatile proportion.",
    @Json(name = "hairlinePattern") val hairlinePattern: String = "Straight",
    @Json(name = "hairlineDescription") val hairlineDescription: String = "Even, linear hairline offering balanced framing across the upper third.",
    @Json(name = "proportions") val proportions: FacialProportions = FacialProportions(),
    @Json(name = "recommendations") val recommendations: List<HaircutRecommendation> = emptyList(),
    @Json(name = "barberConsultationNotes") val barberConsultationNotes: String = "Focus on maintaining volume on top to balance facial symmetry, with clean faded edges."
)

enum class FaceShapeType(val displayName: String, val description: String, val idealStyles: String) {
    OVAL("Oval", "Balanced length-to-width ratio with soft rounded edges.", "Versatile: Textured crop, quiff, pompadour, classic taper."),
    ROUND("Round", "Equal width and length with soft curved jawline.", "Angular quiffs, high fades, side parts, volume on top."),
    SQUARE("Square", "Strong angular jawline with width equal to length.", "Textured fringe, buzz cut with fade, French crop, messy crew cut."),
    HEART("Heart", "Broad forehead tapering down to a pointed sharp chin.", "Mid-length messy fringes, side-swept quiff, textured flow."),
    DIAMOND("Diamond", "Widest at cheekbones with narrow forehead and pointed chin.", "Fringe styles, textured crop, messy brush up, taper fade."),
    OBLONG("Oblong", "Longer face shape with uniform width from forehead to jaw.", "Layered scissor cut, side part, textured fringe, low fade.")
}

enum class HairlineType(val displayName: String, val description: String) {
    STRAIGHT("Straight", "Uniform linear horizontal hairline with clean temple alignment."),
    RECEDING_M("Receding / M-Shaped", "Hairline peaks back at the temples creating a distinct M-contour."),
    HIGH("High Forehead", "Hairline begins higher on the cranium, giving a taller upper third."),
    LOW("Low Forehead", "Hairline sits closer to the brow line, compacting the upper third."),
    WIDOWS_PEAK("Widow's Peak", "Distinct downward V-shape point in the center of the forehead.")
}
