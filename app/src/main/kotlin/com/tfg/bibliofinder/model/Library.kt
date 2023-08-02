package com.tfg.bibliofinder.model

data class Library(
    val libraryId: Long,
    val name: String,
    val schedule: String,
    val capacity: Int,
    val address: String,
    val phone: String,
    val email: String,
    val isAdapted: Boolean,
    val type: String,
    val institution: String
)

object LibraryMockDataProvider {

    fun getMockLibraries(): List<Library> {
        val libraries = mutableListOf<Library>()
        libraries.add(
            Library(
                1L,
                "Biblioteca Xoana Capdevielle",
                "9:00 AM - 7:00 PM",
                654,
                "Campus de Elviña",
                "555-1234",
                "info@bibliotecaxoana.com",
                true,
                "Pública",
                "Universidad de A Coruña"
            )
        )
        libraries.add(
            Library(
                2L,
                "Centro Universitario de Riazor",
                "8:00 AM - 10:00 PM",
                630,
                "Campus de Riazor",
                "555-5678",
                "info@centrouriazor.com",
                true,
                "Académica",
                "Universidad de A Coruña"
            )
        )
        return libraries
    }
}