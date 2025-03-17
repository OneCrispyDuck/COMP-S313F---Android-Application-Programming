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
```
