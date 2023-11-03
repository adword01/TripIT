package com.example.tripit

class PersonDataManager {
    private val personCheckBoxState = mutableMapOf<String, Boolean>()
    private val personPrices = mutableMapOf<String, String>()

    fun isPersonChecked(person: String): Boolean {
        return personCheckBoxState[person] == true
    }

    fun setPersonChecked(person: String, isChecked: Boolean) {
        personCheckBoxState[person] = isChecked
    }

    fun getPersonPrice(person: String): String {
        return personPrices[person] ?: ""
    }

    fun setPersonPrice(person: String, price: String) {
        personPrices[person] = price
    }

    fun getSelectedPeopleWithPrices(): Map<String, String> {
        val selectedPeopleWithPrices = mutableMapOf<String, String>()

        for ((personName, isChecked) in personCheckBoxState) {
            if (isChecked) {
                val price = personPrices[personName] ?: ""
                selectedPeopleWithPrices[personName] = price
            }
        }

        return selectedPeopleWithPrices
    }
}
