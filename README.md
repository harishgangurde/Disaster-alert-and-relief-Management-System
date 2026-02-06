# 🚨 SafeServe - Disaster Alert & Relief Management System

A JavaFX-based disaster response system designed to streamline communication, alert generation, and relief coordination during natural or man-made disasters. This system integrates role-based access, real-time workflow management, and AI-assisted support to ensure fast and coordinated emergency response.

---

## 📖 About the Project

**SafeServe** is a desktop application built using **Java + JavaFX**, with **Firebase** as the backend. It enhances coordination between citizens, volunteers, NGOs, and government authorities during disasters.

### The project includes:
- 🔔 Disaster alert broadcasting
- 🤝 Relief task assignment
- 🤖 AI chatbot for help & safety guidance
- 👥 Multi-role login system

---

## ⭐ Key Features

### 🔔 Disaster Alert Generation
- Create and broadcast alerts
- Classify alerts (Flood, Fire, Earthquake, etc.)
- Notify authorities automatically

### 🤝 Relief Coordination
- Track help requests
- Assign tasks to volunteers
- NGO–Government coordination
- Live status updates

### 👤 User Management
- 4 role-based logins
- Secure Firebase authentication
- Permission-controlled workflows

### 🤖 AI-based Disaster Assistant
- Answers FAQs
- Provides safety guidelines
- Helps users find emergency services
- Reduces manual workload

---

## 🧩 System Roles

| Role       | Responsibilities                                      |
|------------|-------------------------------------------------------|
| **Citizen**    | Raise requests, view alerts, chatbot help             |
| **NGO**        | Provide resources, join relief operations             |
| **Volunteer**  | Accept tasks, update real-time field status           |
| **Government** | Issue alerts, oversee complete system                 |

---

## 🏗️ System Architecture (3-Tier)

```
Presentation Layer  →  JavaFX UI (Screens, Controllers)
Business Layer      →  Services, Validation, Workflow Logic
Data Layer          →  Firebase Realtime DB + Authentication
```

---

## 🛠️ Tech Stack

| Category      | Tools / Technologies                    |
|---------------|-----------------------------------------|
| **Language**  | Java                                    |
| **UI Framework** | JavaFX                               |
| **Backend**   | Firebase                                |
| **Architecture** | MVC + 3-Tier                         |
| **Tools**     | VS Code / IntelliJ, Git, GitHub         |

---

## 📂 Folder Structure

```
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
```

---

## ▶️ How to Run Locally

### Step 1 — Clone the Repository

```bash
git clone https://github.com/harishgangurde/Disaster-alert-and-relief-Management-System.git
cd Disaster-alert-and-relief-Management-System
```

### Step 2 — Open in VS Code or IntelliJ
Open the project in your preferred IDE.

### Step 3 — Add Firebase Configuration
Place your Firebase config file inside the `configuration` folder.

### Step 4 — Run the Application
Run:

```bash
Main.java
```

---

## 🚀 Future Enhancements

- [ ] Real-time disaster map integration
- [ ] SMS alert system
- [ ] Multi-language chatbot
- [ ] Volunteer path guidance
- [ ] Mobile app version using Flutter

---

## 👤 Author

**Harish Gangurde**

- 📧 Email: harishgangurde.nbnstic.entc@gmail.com
- 🐙 GitHub: [@harishgangurde](https://github.com/harishgangurde)
- 💼 LinkedIn: [harish-gangurde](https://linkedin.com/in/harish-gangurde)

---

## 📄 License

This project is open source and available for educational purposes.

---

## 🙏 Acknowledgments

Special thanks to all contributors and the open-source community for their support in building disaster management solutions.

---

<div align="center">
  <strong>Built with ❤️ for safer communities</strong>
</div>
