package com.screentimex.nouzze.models

object ConstPoints {

    val MAP_APP_TYPE = hashMapOf(
        "Instagram" to "Influencers",
        "X" to "Influencers",
        "Threads" to "Influencers",
        "WhatsApp" to "Messaging",
        "Snapchat" to "Messaging",
        "Telegram" to "Messaging",
        "Messenger" to "Messaging",
        "LinkedIn" to "Productivity social media app",
        "Reddit" to "Productivity social media app",
        "YouTube" to "Free Entertainment",
        "Hotstar" to "Free Entertainment",
        "Spotify" to "Free Entertainment",
        "Netflix" to "Paid Entertainment",
        "Prime Video" to "Paid Entertainment",
        "Sony Liv" to "Paid Entertainment",
        "Amazon" to "Shopping",
        "Flipkart" to "Shopping",
        "Myntra" to "Shopping",
        "Zomato" to "Grocery and Food",
        "Swiggy" to "Grocery and Food",
        "Zepto" to "Grocery and Food",
        "Blinkit" to "Grocery and Food",
        "Ola" to "Travel",
        "Uber" to "Travel",
        "Rapido" to "Travel"
    )

    val MAP_AGE_PROFESSION_TIME = hashMapOf(
        "Influencers" to 48L,
            "Messaging" to 86L, "Productivity social media app" to 55L,
            "Free Entertainment" to 99L, "Paid Entertainment" to 65L,
            "Shopping" to 115L, "Grocery and Food" to 126L, "Travel" to 111L)


    val MAP_EVERYTHING = hashMapOf(
        "Engineering" to hashMapOf("Influencers" to Pair(70, -20),
            "Messaging" to Pair(70, -10), "Productivity social media app" to Pair(100, 40),
            "Free Entertainment" to Pair(70, -10), "Paid Entertainment" to Pair(70, -20),
            "Shopping" to Pair(70, -30), "Grocery and Food" to Pair(70, -30), "Travel" to Pair(80, 20)),

            "Banking" to hashMapOf("Influencers" to Pair(70, -20),
                "Messaging" to Pair(70, -10), "Productivity social media app" to Pair(100, 40),
                "Free Entertainment" to Pair(70, -10), "Paid Entertainment" to Pair(70, -20),
                "Shopping" to Pair(70, -30), "Grocery and Food" to Pair(70, -30), "Travel" to Pair(80, 20)),

            "Finance" to hashMapOf("Influencers" to Pair(70, -20),
                "Messaging" to Pair(70, -10), "Productivity social media app" to Pair(100, 40),
                "Free Entertainment" to Pair(70, -10), "Paid Entertainment" to Pair(70, -20),
                "Shopping" to Pair(70, -30), "Grocery and Food" to Pair(70, -30), "Travel" to Pair(80, 20)),

            "Doctors" to hashMapOf("Influencers" to Pair(70, -20),
                "Messaging" to Pair(70, -10), "Productivity social media app" to Pair(100, 40),
                "Free Entertainment" to Pair(70, -10), "Paid Entertainment" to Pair(70, -20),
                "Shopping" to Pair(70, -30), "Grocery and Food" to Pair(70, -30), "Travel" to Pair(80, 20)),


            "Teaching and Education" to hashMapOf("Influencers" to Pair(80, -10),
                "Messaging" to Pair(70, -10), "Productivity social media app" to Pair(100, 40),
                "Free Entertainment" to Pair(80, -10), "Paid Entertainment" to Pair(80, -20),
                "Shopping" to Pair(90, -20), "Grocery and Food" to Pair(90, -20), "Travel" to Pair(80, 25)),

            "Business" to hashMapOf("Influencers" to Pair(70, 30),
                "Messaging" to Pair(100, 20), "Productivity social media app" to Pair(100, 40),
                "Free Entertainment" to Pair(80, -10), "Paid Entertainment" to Pair(90, -20),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -10), "Travel" to Pair(100, 30)),

            "Media and Entertainment" to hashMapOf("Influencers" to Pair(100, 40),
                "Messaging" to Pair(100, 30), "Productivity social media app" to Pair(100, 40),
                "Free Entertainment" to Pair(100, 30), "Paid Entertainment" to Pair(100, 20),
                "Shopping" to Pair(90, 10), "Grocery and Food" to Pair(90, 10), "Travel" to Pair(90, 40)),

            "Sales and Marketing" to hashMapOf("Influencers" to Pair(90, 30),
                "Messaging" to Pair(90, 30), "Productivity social media app" to Pair(100, 40),
                "Free Entertainment" to Pair(100, 20), "Paid Entertainment" to Pair(100, 30),
                "Shopping" to Pair(100, 20), "Grocery and Food" to Pair(100, 20), "Travel" to Pair(80, 30)),

            "Social Work" to hashMapOf("Influencers" to Pair(100, 50),
                "Messaging" to Pair(80, 20), "Productivity social media app" to Pair(100, 50),
                "Free Entertainment" to Pair(100, 10), "Paid Entertainment" to Pair(100, 10),
                "Shopping" to Pair(90, 20), "Grocery and Food" to Pair(90, 20), "Travel" to Pair(90, 30)),

            "Sports and Athletics" to hashMapOf("Influencers" to Pair(60, -10),
                "Messaging" to Pair(60, -10), "Productivity social media app" to Pair(80, 10),
                "Free Entertainment" to Pair(100, -10), "Paid Entertainment" to Pair(100, -20),
                "Shopping" to Pair(100, -30), "Grocery and Food" to Pair(100, -30), "Travel" to Pair(90, 30)),

            "Driver" to hashMapOf("Influencers" to Pair(60, -20),
                "Messaging" to Pair(60, -20), "Productivity social media app" to Pair(90, 10),
                "Free Entertainment" to Pair(90, 20), "Paid Entertainment" to Pair(100, 10),
                "Shopping" to Pair(90, 20), "Grocery and Food" to Pair(90, 20), "Travel" to Pair(100, 40)),

            "Student" to hashMapOf("Influencers" to Pair(80, -10),
                "Messaging" to Pair(80, -10), "Productivity social media app" to Pair(100, 20),
                "Free Entertainment" to Pair(80, -10), "Paid Entertainment" to Pair(90, -20),
                "Shopping" to Pair(100, -10), "Grocery and Food" to Pair(100, -10), "Travel" to Pair(80, -20)),

            "House Maker" to hashMapOf("Influencers" to Pair(89, -10),
                "Messaging" to Pair(89, -10), "Productivity social media app" to Pair(80, 10),
                "Free Entertainment" to Pair(80, -20), "Paid Entertainment" to Pair(90, -30),
                "Shopping" to Pair(100, -20), "Grocery and Food" to Pair(100, -20), "Travel" to Pair(90, -20)),

            "Others" to hashMapOf("Influencers" to Pair(70, -15),
                "Messaging" to Pair(70, -15), "Productivity social media app" to Pair(90, 30),
                "Free Entertainment" to Pair(90, -10), "Paid Entertainment" to Pair(100, -20),
                "Shopping" to Pair(90, -20), "Grocery and Food" to Pair(90, -20), "Travel" to Pair(80, 0))
            )

}