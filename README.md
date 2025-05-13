## 💼Human Resources Leave Management System

This project is a JavaFX-based desktop application that provides an intuitive and powerful solution for managing employees, departments, roles, and HR-related activities within a company. The system connects to a MySQL database to persist and track all data reliably.


## Overview

**The HRMS supports two types of users:**

- Admins: who manage employees, departments, leave requests, and training programs.

- Employeess: View their information, mark attendance, request leave, apply for training, and access payslips.
  
## Features

✅ **Authentication**

- Secure login with email and password

- Role-based redirection (Admin or Employee)

- Session management using a shared Session class

**👨‍💼 Employee Features**

- View their own profile information (read-only).

- View their salary details and download salary slips.

- Mark daily attendance via Check-In and Check-Out buttons.

- View attendance history for the last four days plus current day.

- Submit leave requests with:

  - Leave type (e.g., Paid, Sick, Emergency)

  - Start and end dates

  - View status of submitted leave requests

  - Only sees their own leaves

- View enrolled training programs and courses
  


**🛠️ Admin Features**

- Comprehensive dashboard showing department-wise employee statistics

- Daily attendance tracking (present, absent, late, pending check-ins)

- Add, update, and delete employee records.

- Assign users to departments.

- Create login accounts for new employees.

- Manage departments and assign department chefs.

- Set user roles (Admin or Employee).

- View and manage leave requests with ability to approve or reject

- Ability to approve or reject employee leave

- View all users and leave history


**🧱Project Architecture**
- The application follows the Model-View-Controller (MVC) architecture pattern:
Structure

**🎨View Layer:**

Contains all user interface files
Interfaces designed using Scene Builder
Separated into Admin and Employee folders for organization

**🧠Model Layer:**

Includes classes that reflect the database schema
Represents entities like Employee, Department, Leave, Attendance, etc.


**🎮Controller Layer:**

Divided into Admin and Employee controllers
Handles role-specific logic and user interactions


**💾DAO Classes:**

Handle all database operations
Prevent overcrowding controllers with SQL-related logic


**🗄️Database Connection:**

Managed via a dedicated DBConnection class
Placed in utilities folder for better structure



**Database Design:**
The database is designed to efficiently store all relevant attributes for both admin and employee roles:

- Employee details
- Attendance records
- Salary information
- Leave applications
- Training enrollments



**🗂️ Technologies Used**

|     Rank    | Languages |
|------------|-----------|
|JavaFX       | UI Development Framework |
|MySQL        | Relational Database      |
|JDBC         | Database Connectivity    |
|SceneBuilder | UI Design Tool for JavaFX|


## Demo
**Sign In Page:**

![signIn-page](https://github.com/user-attachments/assets/e13299ab-93e6-48bf-aca1-e4d72dc4d561)

**Employee Profile:**

![Capture d'écran 2025-05-13 044224](https://github.com/user-attachments/assets/8f31c96b-61ab-4afb-9b0e-6aca72d143f4)

**Employe Dashboard -Home-:**

![Capture d'écran 2025-05-13 044154](https://github.com/user-attachments/assets/ee2f4967-6fd0-4580-9bf9-eefb3f2d6685)

**Employee Leave Requests:**

![employee-manages-leaves-page2](https://github.com/user-attachments/assets/7b8882a0-c102-4314-9dc4-35599a3a1d1c)

**Employee Course & Events Enrollment:**

![Capture d'écran 2025-05-13 044304](https://github.com/user-attachments/assets/1e39d1ae-31be-4b61-b1ae-fcb8bb63e16a)

**Admin Dashboard:**

![admin-home-page1](https://github.com/user-attachments/assets/d75b6351-b075-44c2-a818-ee417d3b0093)

**Manage Employees:**

![admin-manage-employees-page3](https://github.com/user-attachments/assets/cad49bb5-53ba-4f09-a825-03d8603dc1d4)

**Manage Users:**

![admin-manage-users-page1](https://github.com/user-attachments/assets/8fd94dc6-648a-4e4f-b10f-de46c64a6791)

**Manage Leaves:**

![admin-manage-leaves-page2](https://github.com/user-attachments/assets/97b829ca-82a1-4d1f-8b83-4a1fe746bbaf)


## Under the supervision of:

**- Professor Mehdia Ajana**








