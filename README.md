![image](https://github.com/user-attachments/assets/bc438017-f1c3-4a05-84d3-6918fa630c0a)# EventGram
EventGram is an Android application built with Kotlin and Firebase that allows users to log in, scan QR codes to join events, and upload photos to a shared storage. Each event has its own unique feed, where participants can view and share photos in an Instagram-like format.

### Features
- User Authentication: Users can log in to the application via Firebase Authentication.
- QR Code Scanning: Users can scan a QR code to join an event. The scanned code extracts the event ID, which is used to navigate to the corresponding event feed.
- Event-Specific Feed: Each event has a unique feed, allowing users to view and upload photos specific to that event.
- Photo Uploads: Users can upload photos to the event's shared storage, which is displayed in a gallery format.
- Event Management: Users can create new events with unique IDs, which are stored in Firebase and used to generate QR codes.
- Persistent Feed: The app remembers the last feed the user was viewing, allowing them to continue from where they left off when reopening the app.
### Screenshots

![image](https://github.com/user-attachments/assets/863baace-d688-45d6-bccd-2a825ea45a85 | width="300" height="200")
![image](https://github.com/user-attachments/assets/be2ead21-4408-47a6-b998-910e3d220cac | width="300" height="200")
![image](https://github.com/user-attachments/assets/a9fd0316-1914-47d2-87f7-011709c71d0f | width="300" height="200")
![image](https://github.com/user-attachments/assets/791c8838-b6a1-4808-889a-b20ed9bbb5ef | width="300" height="200")
![image](https://github.com/user-attachments/assets/2a3988e5-f363-4467-9f10-d50b559786ec | width="300" height="200")





### Installation
To run this project locally, follow these steps:

Clone the repository:

bash
copy this code
git clone https://github.com/yourusername/eventgram.git
cd eventgram
Open the project in Android Studio:

Launch Android Studio and select "Open an existing project".
Navigate to the directory where you cloned the repository and select the eventgram project.
Set up Firebase:

Go to the Firebase Console and create a new project.
Add an Android app to your Firebase project, and download the google-services.json file.
Place the google-services.json file in the app/ directory of your Android project.
Enable Firebase Authentication and Firebase Storage in the Firebase Console.
Build and Run:

Build the project by selecting "Build > Make Project" in Android Studio.
Run the app on an Android device or emulator.

### Usage
Log in: Use your credentials to log in to the app.
Create or Join an Event: Scan a QR code to join an existing event, or create a new one.
View Event Feed: Browse photos shared by other participants in the event feed.
Upload Photos: Share your photos with other participants by uploading them to the event feed.
