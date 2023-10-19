package com.example.final2


import java.util.*

data class StudentModel(
    //generate AutoId
    var id: Int = getAutoId(),
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var pass: String = "",
) {
    // This is a companion object. It's a way to include functionality that isn't tied to any instance of the class.
    // So the 'getAutoId()' function can be called without creating an instance of the 'StudentModel' class.
    companion object {
        // A function that returns a random integer between 0 and 99.
        fun getAutoId(): Int {
            // Create an instance of the Random class.
            val random = Random()
            // Return a random integer between 0 (inclusive) and 100 (exclusive)
            return random.nextInt(100)

        }
    }
}
