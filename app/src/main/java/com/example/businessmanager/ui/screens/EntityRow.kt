package com.example.businessmanager.ui.screens

/** UI model for one list row. Map each Room entity (Invoice, Product...) to this. */
enum class Tone { Success, Warning, Danger, Neutral }

data class EntityRow(
    val title: String,
    val subtitle: String,
    val amount: String? = null,
    val status: String? = null,
    val tone: Tone = Tone.Neutral
)

const val CURRENCY = "MAD"
