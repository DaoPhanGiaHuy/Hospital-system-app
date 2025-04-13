# 🏥 Smart Hospital Management System

A smart and modular Android application for managing hospital operations including patients, doctors, appointments, rooms, and prescriptions.

---

## 📌 Overview

This project aims to build an intelligent hospital management system that supports the following key features:

- ✅ **Patient, Doctor, and Staff Information Management**
- 📅 **Appointment Scheduling** and **Medical History Tracking**
- 🏠 **Clinic Room and Ward Management**
- 📲 **Patient Account Booking System** with doctor selection and symptom description
- 🔐 **Role-based Login & Access Control** (Admin, Doctor, Patient)
- 💊 **Medicine Inventory Management**, Prescription Issuance, and Stock Entry

---

## 🧩 Feature Workflow

- **Patient**:
  - Registers and logs in
  - Selects a doctor, appointment date/time, symptom description
  - Chooses a room (predefined by Doctor)
  
- **Doctor**:
  - Views scheduled appointments with patient details
  - Prescribes medicine (from Admin-added inventory)
  - Manages available rooms

- **Admin**:
  - Manages user roles and information
  - Adds and updates medicine inventory
  - Views and controls hospital-wide operations

---

## 💡 Tech Stack

- 💻 Language: Java
- 📱 Platform: Android
- 🗃️ Database: SQLite (via `DatabaseHelper`)
- 🎨 UI: XML layouts with black-themed buttons and structured views

---

