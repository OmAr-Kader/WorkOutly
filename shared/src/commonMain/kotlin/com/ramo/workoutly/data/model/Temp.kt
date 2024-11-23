package com.ramo.workoutly.data.model

import com.ramo.workoutly.global.base.MSG_IMG
import com.ramo.workoutly.global.base.MSG_TEXT
import com.ramo.workoutly.global.base.MSG_VID
import com.ramo.workoutly.global.util.dateNow

@Suppress("SpellCheckingInspection")
val tempExercises: List<Exercise> get() {
    return listOf(
        Exercise(
            id = "0",
            title = "Plank Pike to Climber (male)",
            videoUri = "https://gymvisual.com/modules/productmedia/uploads/93441201preview.mp4",
            description = "The plank to pike jump is one of those rare ab moves that really works your entire body, all the way from your hands to your toes. It builds strength, cardio and flexibility — all with using just your body weight. That type of efficiency makes it worthy of adding to any fitness routine. Here's what you need to know to do just that.\n" +
                    "\n" +
                    "\u200BWhat is a plank to pike jump?\u200B It's a total-body exercise that involves starting in a plank position, jumping your feet in so that your legs are in a pike and your hips are in the air, and then jumping back out to a plank.\n" +
                    "\u200BWhat muscles does the plank to pike work?\u200B While most people think of it as a core-specific move, it truly works your whole body — shoulder muscles, lats, glutes, hamstrings and heart, included.\n" +
                    "\u200BWho can do it?\u200B Because there are some easy ways to modify this move, exercisers of all levels can do the core-builder, Ashley Patten, founder of Ashley Patten Pilates, tells LIVESTRONG. If you have joint issues or any heart issues, talk to your doctor before doing any jumping or high-intensity cardio.\n",
            views = 12,
            length = kotlin.random.Random.nextLong(1_600_000, 4_600_000),
            date = dateNow
        ),
        Exercise(
            "1",
            title = "Sitting Floor Diagonal Knee Raise\n",
            videoUri = "https://gymvisual.com/modules/productmedia/uploads/93431201preview.mp4",
            description = "The plank to pike jump is one of those rare ab moves that really works your entire body, all the way from your hands to your toes. It builds strength, cardio and flexibility — all with using just your body weight. That type of efficiency makes it worthy of adding to any fitness routine. Here's what you need to know to do just that.\n" +
                    "\n" +
                    "\u200BWhat is a plank to pike jump?\u200B It's a total-body exercise that involves starting in a plank position, jumping your feet in so that your legs are in a pike and your hips are in the air, and then jumping back out to a plank.\n" +
                    "\u200BWhat muscles does the plank to pike work?\u200B While most people think of it as a core-specific move, it truly works your whole body — shoulder muscles, lats, glutes, hamstrings and heart, included.\n" +
                    "\u200BWho can do it?\u200B Because there are some easy ways to modify this move, exercisers of all levels can do the core-builder, Ashley Patten, founder of Ashley Patten Pilates, tells LIVESTRONG. If you have joint issues or any heart issues, talk to your doctor before doing any jumping or high-intensity cardio.\n",
            views = 12,
            length = kotlin.random.Random.nextLong(1_600_000, 4_600_000),
            date = dateNow
        ),
        Exercise(
            "2",
            title = "Sitting Floor Alternating Toe Touch\n",
            videoUri = "https://gymvisual.com/modules/productmedia/uploads/93421201preview.mp4",
            description = "The plank to pike jump is one of those rare ab moves that really works your entire body, all the way from your hands to your toes. It builds strength, cardio and flexibility — all with using just your body weight. That type of efficiency makes it worthy of adding to any fitness routine. Here's what you need to know to do just that.\n" +
                    "\n" +
                    "\u200BWhat is a plank to pike jump?\u200B It's a total-body exercise that involves starting in a plank position, jumping your feet in so that your legs are in a pike and your hips are in the air, and then jumping back out to a plank.\n" +
                    "\u200BWhat muscles does the plank to pike work?\u200B While most people think of it as a core-specific move, it truly works your whole body — shoulder muscles, lats, glutes, hamstrings and heart, included.\n" +
                    "\u200BWho can do it?\u200B Because there are some easy ways to modify this move, exercisers of all levels can do the core-builder, Ashley Patten, founder of Ashley Patten Pilates, tells LIVESTRONG. If you have joint issues or any heart issues, talk to your doctor before doing any jumping or high-intensity cardio.\n",
            views = 12,
            length = kotlin.random.Random.nextLong(1_600_000, 4_600_000),
            date = dateNow
        ),
        Exercise(
            "3",
            title = "Dumbbell Plank Pass Through Push up\n",
            videoUri = "https://gymvisual.com/modules/productmedia/uploads/93411201preview.mp4",
            description = "The plank to pike jump is one of those rare ab moves that really works your entire body, all the way from your hands to your toes. It builds strength, cardio and flexibility — all with using just your body weight. That type of efficiency makes it worthy of adding to any fitness routine. Here's what you need to know to do just that.\n" +
                    "\n" +
                    "\u200BWhat is a plank to pike jump?\u200B It's a total-body exercise that involves starting in a plank position, jumping your feet in so that your legs are in a pike and your hips are in the air, and then jumping back out to a plank.\n" +
                    "\u200BWhat muscles does the plank to pike work?\u200B While most people think of it as a core-specific move, it truly works your whole body — shoulder muscles, lats, glutes, hamstrings and heart, included.\n" +
                    "\u200BWho can do it?\u200B Because there are some easy ways to modify this move, exercisers of all levels can do the core-builder, Ashley Patten, founder of Ashley Patten Pilates, tells LIVESTRONG. If you have joint issues or any heart issues, talk to your doctor before doing any jumping or high-intensity cardio.\n",
            views = 12,
            length = kotlin.random.Random.nextLong(1_600_000, 4_600_000),
            date = dateNow
        ),
    )
}

val messages: List<Message> get() {
    return listOf(
        Message("0", 0L, "Omar", "It's a total-body exercise that involves starting in a plank position", type = MSG_TEXT),
        Message("1", 1L, "Ahmed", "exercisers of all levels can do the core-builder", type = MSG_TEXT),
        Message("2", 1L, "Ahmed", "", "https://goodrequest-web-development.s3.eu-central-1.amazonaws.com/61bc806440f0c568c0900490_Jetpack_20_Compose_20_Basics_20_20_Modal_20_Bottom_20_Sheet_bb7392c41d.png", type = MSG_IMG),
        Message("3", 2L, "Sore", "", "https://gymvisual.com/modules/productmedia/uploads/93411201preview.mp4", type = MSG_VID),
        Message("4", 0L, "Omar", "The plank to pike jump is one of those rare ab moves that really", type = MSG_TEXT),
        Message("5", 0L, "Omar", "The plank to pike jump is one of those rare ab moves that really", type = MSG_TEXT),
        Message("6", 0L, "Omar", "The plank to pike jump is one of those rare ab moves that really", type = MSG_TEXT),
        Message("7", 0L, "Omar", "The plank to pike jump is one of those rare ab moves that really", type = MSG_TEXT),
    )
}