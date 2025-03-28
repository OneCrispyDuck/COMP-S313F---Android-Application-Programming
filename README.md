# COMP-S313F---Android-Application-Programming


Mobile Application Programming: Year 3 Group Project.

## Mobile Web Development & Application Design

### Overview
Mobile Application Programming Project:
This project is part of the Mobile Application Programming course hosted by the Hong Kong Metropolitan University under the Bachelor of Science for Computer Science and Engineering majors.
The purpose of this project is for a team to explore and practice mobile application development using modern tools and frameworks. The project focuses on both web-based mobile applications and native applications to provide students with practical experience in mobile app development.

The team will focus on the following aspects:
- Understanding mobile web development options (e.g., adaptive vs dedicated mobile websites)
- Utilizing HTML, CSS, JavaScript, and other technologies for creating adaptive mobile websites
- Exploring native app development using platforms like Android Studio and Xcode
- Testing and deploying mobile applications effectively
- Documenting the development process and integrating feedback for improvement

By the end of the project, the group is expected to deliver a functional mobile application prototype and corresponding documentation.

### Learning Objectives
-- Explore technologies and frameworks for mobile web and native application development, including adaptive websites, hybrid apps, and native apps. These approaches leverage tools such as Cordova and native development SDKs, enabling efficient cross-platform solutions 
- Practice building adaptive mobile websites using responsive web design (RWD), which ensures layouts are optimized for various screen sizes and devices. This involves using techniques like HTML, CSS, and media queries to create a unified, mobile-friendly experience 
- Understand the deployment process for Android and iOS applications using tools such as Cordova CLI, Node.js, and platform-specific SDKs (e.g., Android Studio and Xcode). These tools facilitate the conversion of web-based apps into native apps and streamline the installation process on devices 
- Gain hands-on experience in designing, testing, and deploying mobile applications, focusing on both functionality and user interface design while adhering to platform-specific guidelines 

![Project Screenshot](https://github.com/your-username/mobile-app-project/screenshot.png)

## Table of Contents
- [Features](#features)
- [Getting Started](#getting-started)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Testing and Deployment](#testing-and-deployment)
- [Contributing](#contributing)
- [License](#license)

## Features
- Responsive web design for adaptive mobile websites
- Use of HTML5 local storage for offline capabilities 
- Basic mobile app prototypes for Android and iOS platforms
- Deployment-ready .apk files for Android devices 
- CSS media queries for mobile-friendly layouts

## Getting Started

### Prerequisites
```bash
# Required software, dependencies, etc.
Node.js v14+
npm v6+
Android Studio
Xcode (for iOS development)
Cordova CLI

# Required accounts and permissions
Android Developer Account
Apple iOS Developer Account
```

### Installation
```bash
# Clone the repository
git clone https://github.com/your-username/mobile-app-project.git

# Navigate to the project directory
cd mobile-app-project

# Install dependencies
npm install

# Prepare the Cordova project
cordova create my-app com.example.myapp MyApp

# Add desired platforms
cordova platform add android
cordova platform add ios
```

### Usage
1. **Mobile Web Development**
    - Use HTML, CSS, and JavaScript to create adaptive websites.
    - Implement responsive web design techniques using media queries 
    - Test the adaptive layout across different devices and screen sizes.

2. **Native Application Development**
    - Develop Android apps using Android Studio and the Cordova framework 
    - Use iOS development tools like Xcode for iPhone applications 
    - Build your app using the following commands:
      - `cordova build android` for Android 
      - `cordova build ios` for iOS 

### Configuration
```json
{
  "cordova": {
    "platforms": ["android", "ios"],
    "plugins": ["cordova-plugin-device", "cordova-plugin-network-information"]
  },
  "environment": {
    "development": true
  },
  "testing": {
    "tools": ["browser dev tools", "mobile device emulators"]
  }
}
```

### Testing and Deployment
1. Test your app on both web browsers and mobile devices:
   - Open the `index.html` file in a browser for web testing 
   - Use mobile emulators in Android Studio or Xcode for native app testing
2. Deploy the Android app:
   - Locate the `.apk` file in the `outputs` directory and transfer it to a device
   - Test and verify installation and functionality on Android devices.

### Contributing
1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/NewFeature`.
3. Commit changes: `git commit -m 'Add NewFeature'`.
4. Push to the branch: `git push origin feature/NewFeature`.
5. Open a pull request for review.

### License
This project was created as part of the Mobile Application Programming course (COMP-S313F) at Hong Kong Metropolitan University. All rights and usage are governed by the university's academic policies.

## Documentation
Detailed documentation can be found in the `/docs` directory:
- [HTML5 and CSS3 Guide](/docs/html5-css3.md)
- [Cordova Commands and Tips](/docs/cordova.md)
- [Testing and Deployment Guide](/docs/testing-deployment.md)


# Project Directory/File Structure

The following is the recommended directory and file structure for the COMP-S313F Mobile Application Programming project. This structure is organized to accommodate both mobile web development and native app development using tools like Cordova and Android Studio.

## Root Directory
```
mobile-app-project/
├── app/
│   ├── build/
│   └── src/
│       ├── build.gradle.kts
│       └── proguard-rules.pro
├── gradle/
│   └── wrapper/
│       └── libs.versions.toml
├── .git/
│   ├── branches/
│   ├── hooks/
│   ├── objects/
│   └── refs/
├── .idea/
│   ├── caches/
│   └── workspace.xml
├── gradlew
├── gradlew.bat
├── build.gradle.kts
├── settings.gradle.kts
├── README.md
└── .gitignore
```

### Description of Folders and Files:

1. app/
- Contains the source code and build files for the Android app.
- src/: Houses the main application logic and configuration files, including build.gradle.kts and proguard-rules.pro.
2. gradle/
- Contains Gradle wrapper files for managing build scripts and dependencies.
3. .git/
- Git directory for version control, including branches, commits, and hooks.
4. .idea/
- IntelliJ IDEA project configuration files.
5. build.gradle.kts and settings.gradle.kts
- Project-wide Gradle configuration and settings.

1. **`app/`**
   - Contains the main source code and build files for the Android application.
   - Example structure:
     ```
     app/
     ├── build/                     # Contains Gradle build outputs
     └── src/                       # Contains source code and resources
         ├── main/                  # Main source directory
         │   ├── AndroidManifest.xml  # App's manifest file defining metadata and permissions
         │   ├── java/              # Java/Kotlin source code for the app
         │   └── res/               # Resources such as layouts, drawables, and values
         └── build.gradle.kts       # Gradle build configuration for the app
     ```
   - This directory is the core of the native application development process, housing both configuration files and source code 

2. **`gradle/`**
   - Contains Gradle wrapper configuration files that manage project dependencies and build scripts.
   - Example:
     ```
     gradle/
     └── wrapper/
         └── libs.versions.toml  # Defines dependency versions for the project
     ```
   - The `libs.versions.toml` file allows for centralized dependency management, making updates and changes to libraries easier 

3. **`.git/`**
   - The Git directory used for version control. It tracks changes, branches, and commits throughout the project lifecycle.
   - Example structure:
     ```
     .git/
     ├── branches/
     ├── hooks/
     ├── objects/
     └── refs/
     ```
   - This folder is automatically created when initializing a Git repository and is essential for collaboration and version tracking

4. **`.idea/`**
   - Contains IntelliJ IDEA project configuration files.
   - Example:
     ```
     .idea/
     ├── caches/
     ├── workspace.xml
     └── other-config-files.xml
     ```
   - These files store IDE-specific settings, such as caching and project metadata. They are useful for maintaining a consistent environment but should be excluded from version control when not collaborating on the same IDE 

5. **`build.gradle.kts`**
   - The project-level Gradle build file, written in Kotlin DSL, which defines build settings and dependencies for the entire project.
   - This file is essential for configuring how the project is built and tested 

6. **`settings.gradle.kts`**
   - Specifies the project structure and settings for Gradle. It lists the included modules and their configurations.

7. **`README.md`**
   - Contains project documentation, including an overview, setup instructions, and usage details.
   - This file is the main guide for contributors and users of the project, providing essential information for understanding and working with the repository 

8. **`.gitignore`**
   - Specifies files and directories to exclude from version control.
   - Example:
     ```
     .gitignore
     ├── /build/
     ├── /.idea/
     ├── /node_modules/
     └── *.apk
     ```
   - Ensures unnecessary files, such as build outputs, IDE configuration files, and sensitive data, are not tracked in Git 

## Android Studio-Specific Structure (for Native Apps)
If building directly with Android Studio, the structure under the `android/` directory will include:
```
android/
├── app/
│   ├── manifests/
│   │   └── AndroidManifest.xml  # Defines app properties and permissions
│   ├── java/
│   │   └── com/example/myapp/
│   │       └── MainActivity.java  # Main activity of the app
│   └── res/
│       ├── drawable/  # Images and drawable resources
│       ├── layout/  # UI layout XML files
│       ├── mipmap/  # Launcher icons for different densities
│       └── values/  # Strings, colors, and styles
├── build.gradle  # Build configuration file
└── settings.gradle  # Project settings file
```
This structure is automatically generated by Android Studio and allows for easy customization of native Android apps 

## Cordova Workflow Summary
1. Add web assets to the `www/` directory.
2. Use `cordova platform add android` to generate the Android-specific structure under `platforms/`.
3. Build the app with `cordova build android`, which creates the `.apk` file in the `platforms/android/app/build/outputs/apk/debug/` directory 
4. Deploy the `.apk` file to Android devices for testing and usage 

This directory structure ensures an organized workflow for both mobile web and native app development, making it easier to develop, test, and deploy the application across platforms.
