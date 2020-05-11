# Introduction

> This document only provides a simple application introduction and instructions, please refer to <a href="https://github.com/FY-Zhang/Project_Group_4/blob/master/README.pdf" style="text-decoration:none">Instruction</a> for detailed usage steps.

- <a href="#introDescp" style="text-decoration:none">Description</a>
  - <a href="#introCan" style="text-decoration:none">What can you do</a>
  - <a href="#introHow" style="text-decoration:none">How to use</a>

- <a href="#introStart" style="text-decoration:none">Get start</a>
  - <a href="#introRunDev" style="text-decoration:none">Run on device</a>
  - <a href="#introRunAS" style="text-decoration:none">Run on Android Studio</a>
  - <a href="#introFb" style="text-decoration:none">Firebase</a>



**<span id="introDescp" style="font-size: 38px">Description of the app</span>**

## <span id="introCan">What can you do on this app?</span>

- **Channel post**: You can post on various types of channels, or check other people's posts, and like or add them to your "favourite". Nearby posts are available in Map page.

- **Friends:** You can send friends request via email, phone, or people nearby.

- **Chat:** You can chat with your friend and send text- image- or even location-based messages.

- **Map:** You can mark official check points, and you can also save the location from the chat page for easy viewing next time. Here you can also search nearby users or browse nearby posts.

- **Personal:** You can change your personal information in "Me" page, view all the posts you have uploaded or favourited, and log out.

  

## <span id="introHow">How to use the app?</span>

#### 1. Sign in

Enter your email and have a password validation. If you have already registered, you can continue to login, if not, it will jump to the register page.

#### 2. In Chats page

- **Friend List:** View all your friends, You can chat with your friend and send text- image- or even location-based messages, and friends profile are available as well.
- **Notifications:** View all notifications in history.
- **Search:** Find new friends by email or phone.
- **Map** 
  - ***Site:*** Positioning your location.
  - ***Check:*** Mark your current location. (Only available in specific locations.)
  - ***List:*** View marked and saved places.
  - ***Nearby:*** Search people nearby. (Can only find nearby users who are using this feature.)
  - ***Post:*** View nearby posts.

#### 3. In Discover page

- ***Channel:*** You can see eight different types of channels, select the channel you are interested in and click in, you can see posts on related topics.
- ***Post:*** Find the post you are interested in, then click to view the details, or click + in the lower right corner to send a new post. You can also save a post to favourite by long pressing.
  - ***New Post:*** Write the title and content, you can choose to add a picture and your own location, and then click Submit to send the post to the corresponding channel.

#### 4. In Me page

- **Edit:** You can change your information and avatar.
- **All Posts:** All your posts on all channels, you can delete them by long pressing.
- **Favourites:** All posts you add to your favourites, you can remove them by long pressing.
- **Logout:** Log out your currnt account.





**<span id="introStart" style="font-size: 38px">Getting Started</span>**

## <span id="introRunDev">How to get the app running on your device?</span>

1. Click Build APK in Android Studio to export the APK installation package, then install the APK file on the phone, and finally you should can run it.

2. Some mobile phones cannot install the application, you should add following code  in the gradle.properties file, and then export it.

   ```Gradle
   android.injected.testOnly = false
   ```

3. If the app still cannot be installed, check whether the app has been installed on this device, please remove the original version and reinstall it.



## <span id="introRunAs">How to run the app from Android Studio?</span>

You need to open the virtual device or connect an Android phone with **USB debugging mode**, and then click the Run button. The app will then run automatically on the device.



## <span id="introFb">Firebase</span>

To view the app Firebase database details, click <a href="https://console.firebase.google.com/project/groupproject-ffdc4/overview" style="text-decoration:none">Here</a>.

<div style="text-align:right">
    <a href="#introDescp" style="text-decoration:none"><button>Back to top</button></a>
</div>