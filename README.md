# Online Billing System - Pahana Edu

A web-based billing management system developed in **Java EE** (JSP, Servlets, DAO pattern) with **MySQL** as the backend.  
The system is designed for bookshops (Pahana Edu) to efficiently manage **customers, items, and billing** processes.

---

## 🚀 Features

- **User Authentication**
  - Role-based login (Admin, Employer/Cashier)
  - Secure session handling

- **Customer Management**
  - Add, edit, view, and delete customers
  - Stores account numbers, name, address, and contact info

- **Product Management**
  - Add, update, delete products
  - Track product stock and prices

- **Billing System**
  - Create bills with multiple items
  - Calculate totals automatically
  - View single bill or all bills
  - Print-friendly bill format

- **Validation**
  - Prevents invalid or negative quantities
  - Handles missing customers/products gracefully

---

## 🛠️ Tech Stack

- **Frontend:** JSP, HTML, CSS  
- **Backend:** Java EE (Servlets, DAO pattern)  
- **Database:** MySQL 8.0  
- **Server:** Apache Tomcat  
- **Testing:** JUnit  

---

## 📂 Project Structure

```
onlinebill/
 ├── src/
 │   ├── dao/              # DAO Interfaces
 │   ├── dao/impl/         # DAO Implementations
 │   ├── model/            # Entity classes
 │   ├── servlet/          # Servlets (Controllers / API layer)
 │   └── utils/            # DB Connection, helpers
 ├── web/                  # JSP pages
 │   ├── customers.jsp
 │   ├── products.jsp
 │   ├── bills.jsp
 │   └── ...
 ├── test/                 # JUnit tests
 ├── sql/                  # Database schema and seed data
 ├── README.md             # Documentation (this file)
 └── pom.xml / build.xml   # Build configuration (if Maven/Ant used)
```

---

## ⚙️ Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/onlinebill.git
   cd onlinebill
   ```

2. **Database Setup**
   - Create a MySQL database:
     ```sql
     CREATE DATABASE pahana;
     ```
   - Import schema:
     ```bash
     mysql -u root -p pahana < sql/schema.sql
     ```

3. **Configure Database Connection**
   - Edit `DBConnection.java` with your DB username & password.

4. **Run on Apache Tomcat**
   - Deploy the project to Tomcat (`/webapps/`).
   - Start Tomcat and open [http://localhost:8080/onlinebill](http://localhost:8080/onlinebill).

---

## 👥 User Roles

- **Admin**
  - Full access (customers, products, billing, user management).
- **Employer / Cashier**
  - Limited to creating bills and viewing customers.

---

## 🧪 Running Tests

Run JUnit tests from the `test/` directory:

```bash
mvn test
# or if using Ant
ant test
```

---

## 🤝 Contributing

1. Fork the repo  
2. Create a new branch (`feature/my-feature`)  
3. Commit changes (`git commit -m "Added feature"`)  
4. Push branch (`git push origin feature/my-feature`)  
5. Open a Pull Request  

---

## 📜 License

This project is for **educational purposes** (Advanced Programming Assignment - ICBT Campus).  
You are free to use and modify it for learning.
