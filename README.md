# webcapture-android

Code testing project, all the required functions were implemented. However the deletion of a capture record from the database does not delete the image file that was stored on the device.

The project was implemented in Kotlin with Android JetPack libraries. 
The app used MVVM pattern, view model hides all the business logic from the UI. Room is used for database layer, Kotlin Coroutine to handle with all the async calls and main/background thread swicthing.

