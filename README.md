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
 ├─ src/
 │  ├─ main/
 │  │  ├─ java/
 │  │  │  ├─ Auth/                # AuthFilter
 │  │  │  ├─ dao/                 # DAO interfaces
 │  │  │  ├─ dao/impl/            # JDBC implementations
 │  │  │  ├─ model/               # POJOs (User, Product, Customer, Bill…)
 │  │  │  ├─ servlet/             # Servlets (controllers & API)
 │  │  │  └─ utils/               # DB/JNDI helpers, validators
 │  │  └─ webapp/
 │  │     ├─ index.jsp            # Admin dashboard
 │  │     ├─ employer-dashboard.jsp
 │  │     ├─ manage-products.jsp
 │  │     ├─ manage-customers.jsp
 │  │     ├─ addBill.jsp
 │  │     ├─ view_bills.jsp
 │  │     ├─ viewSingleBill.jsp
 │  │     ├─ Login.jsp
 │  │     ├─ WEB-INF/web.xml
 │  │     └─ (includes/, css/, etc. if present)
 │  └─ test/                      # JUnit tests
 ├─ pom.xml                       # Maven (WAR)
 └─ target/                       # Build output

```

---

## ⚙️ Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/SandaruP204/online-billing-system-pahanaedu.git
   cd onlinebill
   ```

2. **Database Setup**
```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS pahana"
mysql -u root -p pahana < sql/001_schema.sql
mysql -u root -p pahana < sql/010_seed.sql


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
