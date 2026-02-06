🚨 Disaster Alert & Relief Management System — SafeServe

A JavaFX-based disaster response system designed to streamline communication, alert generation, and relief coordination during natural or man-made disasters.
This system integrates role-based access, real-time relief workflow management, and AI-assisted disaster support to ensure fast and coordinated emergency response.

📌 Table of Contents
About the Project
Key Features
System Roles
Architecture
Tech Stack
Folder Structure
How to Run
Future Enhancements
Author

📖 About the Project
The SafeServe – Disaster Alert and Relief Management System is a desktop application built using Java and JavaFX, with Firebase as the backend.
It improves communication between citizens, NGOs, volunteers, and government authorities to ensure a smooth disaster response workflow.

This project includes features like disaster alert broadcasting, relief task assignments, AI-based chatbot support, and a multi-role login system.

⭐ Key Features
🔔 1. Disaster Alert Generation
Create and broadcast disaster alerts
Classify alerts (Flood, Fire, Earthquake, etc.)
Notify relevant authorities based on severity

🤝 2. Relief Coordination
Track victim requests
Assign tasks to volunteers
NGO–Government coordination panel
Real-time status updates

👤 3. User Management
Role-based access control
4 login types
Secure Firebase-backed authentication

🤖 4. AI-driven Disaster Assistant
Provides safety guidelines
Answers FAQs
Helps citizens access emergency services
Reduces manual workload

🧩 System Roles
Role	Description
Citizen	Raise help requests, view alerts, interact with chatbot
NGO	Provide resources and coordinate relief efforts
Volunteer	Accept tasks and update real-time status
Government	Issue alerts and manage all operations
🏗️ System Architecture (3-Tier)
Presentation Layer  →  JavaFX UI (Screens, Controllers)
Business Layer       →  Services, Validation, Workflow Logic
Data Layer           →  Firebase Realtime Database + Authentication

🛠️ Tech Stack
Category	Tools / Technologies
Language	Java
UI Framework	JavaFX
Backend	Firebase
Architecture	MVC + 3-Tier
Tools	VS Code / IntelliJ, Git, GitHub
📂 Folder Structure
src/
 └── main/
      └── java/
           └── com.thinkspark/
                 ├── controller/        # UI Controllers
                 ├── model/             # Data Models
                 ├── dao/               # Firebase Logic
                 ├── view/              # JavaFX Screens
                 ├── configuration/     # Firebase Initialization
                 └── Main.java          # Entry Point

▶️ How to Run Locally
Step 1: Clone the Repository
git clone https://github.com/harishgangurde/Disaster-alert-and-relief-Management-System.git
cd Disaster-alert-and-relief-Management-System

Step 2: Open in VS Code / IntelliJ

Step 3: Add Firebase Configuration
Place your Firebase config file inside the configuration folder.

Step 4: Run the Application

Run the file:
Main.java
🚀 Future Enhancements
Real-time map integration
SMS-based alert system
Multi-language chatbot
Volunteer path optimization
Flutter mobile app version

👤 Author
Harish Gangurde
Software Developer (Java | Flutter | Firebase)
Email: harishgangurde.nbnstic.entc@gmail.com

GitHub: https://github.com/harishgangurde

LinkedIn: https://linkedin.com/in/harish-gangurde
