package com.virusbear.mayan.processor

data class Region(
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double,
    val language: String,
    val page: Page
) {
    enum class Page {
        First,
        Last
    }
}