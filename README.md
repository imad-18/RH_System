## 💼Human Resources Leave Management System

This project is a JavaFX-based desktop application that provides an intuitive and powerful solution for managing employees, departments, roles, and HR-related activities within a company. The system connects to a MySQL database to persist and track all data reliably.

## Overview

The HRMS supports two types of users:

- Admins: who manage employees, departments, and leave requests.

- Employees: who can view their information, request leave, apply for training, and access payslips.
  
## Features

✅ **Authentication**

- Secure login with email and password

- Role-based redirection (Admin or Employee)

- Session management using a shared Session class

**👨‍💼 Employee Features**

- View their own profile information (read-only).

- View their salary and payslips.

- Possibility to download their salary file.

- Submit leave requests with:

  - Leave type (e.g., Paid, Sick, Emergency)

  - Start and end dates

  - View status of submitted leave requests

  - Only sees their own leaves

- Apply for courses & events

**🛠️ Admin Features**

- Add, update, and delete employee records.

- Assign users to departments.

- Create login accounts for new employees.

- Manage departments and assign department chefs.

- Set user roles (Admin or Employee).

- Admin dashboard with a list of all leave requests

- Ability to approve or reject employee leave

- View all users and leave history

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

**Admin Dashboard:**

![admin-home-page1](https://github.com/user-attachments/assets/d75b6351-b075-44c2-a818-ee417d3b0093)

**Manage Employees:**

![admin-manage-employees-page3](https://github.com/user-attachments/assets/cad49bb5-53ba-4f09-a825-03d8603dc1d4)

**Manage Users:**







