# EmployeeConnect  (EConnect)

This android app is developed to bring employees of a company closer by easing the way information are shared, such as contact information and other information related to programming (employee's programming skills, project he is working on, team he has been assigned to, etc.)

## Implementation

 For this app to be flexible, scalable, easily extensible and maintainable, it is implemented in accordance with **Clean architecture** principle. **MVP architectural pattern** is used to model the presentation layer.

Implementation of this app and methods that I used are highly influenced by **Google's and Jetbrains most recommended book**, ["Kotlin for Android Developers 6th edition"](https://antonioleiva.com/kotlin-android-developers-book/) by **Antonio Leiva** and some of his blogs* and other relevant blogs*.

### References*

[Clean architecture for Android with Kotlin: a pragmatic approach for starters](https://antonioleiva.com/clean-architecture-android/)

[MVP for Android: how to organize the presentation layer](https://antonioleiva.com/mvp-android/)

[Clean Architecture Guide (with tested examples): Data Flow != Dependency Rule](https://proandroiddev.com/clean-architecture-data-flow-dependency-rule-615ffdd79e29)

[MVC vs MVP vs MVVM architecture in Android](https://blog.mindorks.com/mvc-mvp-mvvm-architecture-in-android)

[The MVC, MVP, and MVVM Smackdown](https://academy.realm.io/posts/eric-maxwell-mvc-mvp-and-mvvm-on-android/)


# How it works
- when one wants to use this app  first he needs to register
- after registration for user to login successfully, first it needs to be verified by one of company's app moderators
- when user is not verified, to other employees it is represented by yellow explanation mark icon in the employees list
- moderator can verify unverified user with long click on user profile shown in employees list
- furthermore, moderator can make other user a moderator with long click on user profile shown in employees list after verification
- when user is a moderator, to other employees it is represented with wrench icon by moderator's user picture  
- after profile validation, user is free to use **EConnect** app

## Registration  
![Demo Registration of new User alpha](https://media.giphy.com/media/ZFnwXWWuYVg8F17SEH/giphy.gif)  

## Verification  
![Demo New User alpha](https://media.giphy.com/media/U5Op3ycS23DYmgMuGg/giphy.gif)  

# What I learned
 - Developed a user interface within the xml files using ConstraintLayout, RecyclerView, CardView, ScrollView, navigation menu, bottom navigation bar, chancing shapes of different kind of views etc.
 - Working with Firebase (Cloud Firestore, Firebase Storage, Firebase Authentication)
 - Working with image processing library [hdodenhof/CircleImageView](https://github.com/hdodenhof/CircleImageView)
- Working with image processing library [Picasso](https://square.github.io/picasso/)
- Working with library for complex RecyclerView layouts [Groupie](https://github.com/lisawray/groupie)
- Working with Kotlin library [Anko](https://github.com/Kotlin/anko) library

# To Do list
- Implement local database (ObjectBox, Realm etc.)
