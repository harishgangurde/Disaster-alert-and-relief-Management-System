// package com.thinkspark.configuration;

// import java.io.FileInputStream;
// import java.io.IOException;


// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.cloud.firestore.Firestore;
// import com.google.cloud.firestore.spi.v1.FirestoreRpc;
// import com.google.cloud.firestore.v1.FirestoreClient;
// import com.google.firebase.FirebaseApp;
// import com.google.firebase.FirebaseOptions;

// public class FirebaseInitialization {
//     static {
//         try{
//             intializeFirebase();
//         } catch (Exception e){
//             e.printStackTrace();
//         }
//     }

//     private static void intializeFirebase() throws IOException{

//         FileInputStream serviceAccount = new FileInputStream("src//main//resources//firebase_api.json");
//         FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

//         FirebaseApp.initializeApp(options);

//         Firestore db = FirestoreClient.getFirestore();

        


//     }
// }