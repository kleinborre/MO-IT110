### **🔐 Login Credentials for Users**

- **All employees** can log in using their **Employee Number** and **Username**.  
- **Username** is based on the employee number (default format — can be modified via admin access).  
  - 📌 **Examples:**  
    - `10001` → `employee1`  
    - `10002` → `employee2`  
    - `10003` → `employee3`  
- **Default Password:** `Motorph2025_` *(changeable via admin access)*  

---

### **🛡️ Roles & Access Control**

- All employees authenticate using the **Employee** role by default.  
- 🔄 **Special Users** may be assigned **dual roles**, configurable via the **role selection box**.  
  - They can still log in using the same **Username/Employee ID** and **Password**.

#### **👤 Special User Role Mapping:**  
- `10005 - employee5` → *Employee / System Administrator*  
- `10006 - employee6` → *Employee / HR Manager*  
- `10011 - employee11` → *Employee / Payroll Manager*  

---

### **🧪 Internal & External Testing Link**  
📋 [Google Spreadsheet](https://docs.google.com/spreadsheets/d/1aCH1qzQfVXXTyw_8ZTWDNwNq-k6L9_K9M-WwkOl83lQ/edit?usp=sharing)

---

### **🚀 System Access (JAR File)**  
The Payroll System can be accessed directly by running the JAR file:  
📁 `target/MotorPHOOP-1.0.0-jar-with-dependencies.jar`  
after cloning the repository.
