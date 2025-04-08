package com.example.tripit.viewmodels

data class Card(
    val city: String,
    val message: String,
    val country: String,
    val imageUrl: String
) {
    companion object {
        val MOCKED_ITEMS = listOf(
            Card(
                "Spiti Valley",
                "An iconic Tibetan Buddhist monastery perched on a hilltop with stunning views of the Spiti River valley.",
                "Key Monestary",
                "https://firebasestorage.googleapis.com/v0/b/tripit-1.appspot.com/o/Display%20Images%2FWhatsApp%20Image%202024-12-11%20at%209.57.16%20AM.jpeg?alt=media&token=4300c8a2-bd2a-4f8e-b43b-5c4b83789f9c"
            ),
            Card(
                "Tirthan Valley",
                "A charming wooden house surrounded by snow-laden pine trees, epitomizing serenity in the Himalayas.",
                "Traditional House",
                "https://firebasestorage.googleapis.com/v0/b/tripit-1.appspot.com/o/Display%20Images%2FWhatsApp%20Image%202024-12-11%20at%209.57.16%20AM%20(3).jpeg?alt=media&token=e8662d09-2105-414c-bcb2-5b6615714c79"
            ),
            Card(
                "Hidimba Devi Temple",
                " An ancient wooden temple dedicated to Hidimba Devi, surrounded by dense cedar forests.",
                "Manali",
                "https://firebasestorage.googleapis.com/v0/b/tripit-1.appspot.com/o/Display%20Images%2FWhatsApp%20Image%202024-12-11%20at%209.57.16%20AM%20(4).jpeg?alt=media&token=557de3a0-b0a3-4c61-a9cd-e795cbd4e33d"
            ),

            Card(
                "Masroor Rock-Cut Temples",
                "A group of exquisite monolithic rock-cut temples, often referred to as the \"Ellora of the North.",
                "Kangra",
                "https://firebasestorage.googleapis.com/v0/b/tripit-1.appspot.com/o/Display%20Images%2FWhatsApp%20Image%202024-12-11%20at%209.57.16%20AM%20(5).jpeg?alt=media&token=acb70017-71ea-4a53-a677-11953b6e6905"
            ),
            Card(
                "Chamba",
                "A serine river flowing between the Chamba valley",
                "Chamba Valley",
                "https://firebasestorage.googleapis.com/v0/b/tripit-1.appspot.com/o/Display%20Images%2FWhatsApp%20Image%202024-12-11%20at%209.57.16%20AM%20(6).jpeg?alt=media&token=fd5bf054-a8d2-47c4-8452-4857d09641ae"
            ),
            Card(
                "Shimla Toy Train",
                "Beautiful view of Shimla-Kalka toy train",
                "Shimla",
                "https://images.unsplash.com/photo-1585767928941-f71ce7702b89?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1352&q=80"
            )
        )
    }
}
