package com.dam.framework;

import com.dam.framework.core.Configuration;
import com.dam.framework.core.Session;
import com.dam.framework.core.SessionFactory;
import com.dam.framework.core.Transaction;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

/**
 * Example application demonstrating DAM Framework usage.
 * 
 * @author Dev 1 & Dev 2
 */
public class ExampleApp {
    
    private static SessionFactory sessionFactory;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       DAM FRAMEWORK - Database Access Management         ║");
        System.out.println("║                    Demo Application                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        try {
            // Initialize framework
            System.out.println("[*] Initializing DAM Framework...");
            Configuration config = Configuration.getInstance();
            sessionFactory = config.buildSessionFactory();
            System.out.println("[✓] Framework initialized successfully!");
            System.out.println();
            
            // Initialize database schema (for H2 in-memory database)
            initializeSchema();
            
            // Run demo menu
            runMenu();
            
        } catch (Exception e) {
            System.err.println("[✗] Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
                System.out.println("\n[*] SessionFactory closed. Goodbye!");
            }
        }
    }
    
    /**
     * Initialize database schema - creates tables if they don't exist.
     * This is especially useful for H2 in-memory database.
     */
    private static void initializeSchema() {
        System.out.println("[*] Initializing database schema...");
        try (Session session = sessionFactory.openSession()) {
            Connection conn = session.getConnection();
            try (Statement stmt = conn.createStatement()) {
                // Create users table
                stmt.execute(
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "  id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "  username VARCHAR(100)," +
                    "  email VARCHAR(255)," +
                    "  age INT" +
                    ")"
                );
                System.out.println("[✓] Table 'users' ready!");
                
                // Insert sample data if empty
                var rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
                rs.next();
                if (rs.getInt(1) == 0) {
                    stmt.execute("INSERT INTO users (username, email, age) VALUES ('john_doe', 'john@example.com', 25)");
                    stmt.execute("INSERT INTO users (username, email, age) VALUES ('jane_doe', 'jane@example.com', 30)");
                    stmt.execute("INSERT INTO users (username, email, age) VALUES ('bob_smith', 'bob@example.com', 35)");
                    System.out.println("[✓] Sample data inserted!");
                }
            }
        } catch (Exception e) {
            System.err.println("[!] Warning: Could not auto-initialize schema: " + e.getMessage());
            System.err.println("[!] If using MySQL/PostgreSQL, please run the SQL scripts manually.");
        }
        System.out.println();
    }
    
    private static void runMenu() {
        while (true) {
            printMenu();
            System.out.print("Choose option: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    demoCreate();
                    break;
                case "2":
                    demoRead();
                    break;
                case "3":
                    demoUpdate();
                    break;
                case "4":
                    demoDelete();
                    break;
                case "5":
                    demoQueryBuilder();
                    break;
                case "6":
                    demoTransaction();
                    break;
                case "7":
                    demoFullCRUD();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    private static void printMenu() {
        System.out.println("\n┌─────────────────── MENU ───────────────────┐");
        System.out.println("│  1. CREATE - Insert new user                │");
        System.out.println("│  2. READ   - Find user by ID                │");
        System.out.println("│  3. UPDATE - Update user information        │");
        System.out.println("│  4. DELETE - Delete a user                  │");
        System.out.println("│  5. QUERY  - Demo QueryBuilder              │");
        System.out.println("│  6. TRANSACTION - Demo Transaction          │");
        System.out.println("│  7. FULL CRUD - Demo complete workflow      │");
        System.out.println("│  0. EXIT                                    │");
        System.out.println("└─────────────────────────────────────────────┘");
    }
    
    /**
     * Demo CREATE operation
     */
    private static void demoCreate() {
        System.out.println("\n=== CREATE (INSERT) Demo ===\n");
        
        try (Session session = sessionFactory.openSession()) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter age: ");
            int age = Integer.parseInt(scanner.nextLine());
            
            User user = new User(username, email, age);
            
            Transaction tx = session.beginTransaction();
            try {
                session.save(user);
                tx.commit();
                System.out.println("\n[✓] User created successfully!");
                System.out.println("    Generated ID: " + user.getId());
                System.out.println("    " + user);
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }
    
    /**
     * Demo READ operation
     */
    private static void demoRead() {
        System.out.println("\n=== READ (SELECT) Demo ===\n");
        
        try (Session session = sessionFactory.openSession()) {
            System.out.print("Enter user ID to find: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            User user = session.find(User.class, id);
            
            if (user != null) {
                System.out.println("\n[✓] User found:");
                System.out.println("    " + user);
            } else {
                System.out.println("\n[!] User not found with ID: " + id);
            }
        }
    }
    
    /**
     * Demo UPDATE operation
     */
    private static void demoUpdate() {
        System.out.println("\n=== UPDATE Demo ===\n");
        
        try (Session session = sessionFactory.openSession()) {
            System.out.print("Enter user ID to update: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            User user = session.find(User.class, id);
            if (user == null) {
                System.out.println("[!] User not found with ID: " + id);
                return;
            }
            
            System.out.println("Current: " + user);
            System.out.print("New username (or press Enter to keep): ");
            String newUsername = scanner.nextLine();
            System.out.print("New email (or press Enter to keep): ");
            String newEmail = scanner.nextLine();
            System.out.print("New age (or press Enter to keep): ");
            String newAge = scanner.nextLine();
            
            if (!newUsername.isEmpty()) user.setUsername(newUsername);
            if (!newEmail.isEmpty()) user.setEmail(newEmail);
            if (!newAge.isEmpty()) user.setAge(Integer.parseInt(newAge));
            
            Transaction tx = session.beginTransaction();
            try {
                session.update(user);
                tx.commit();
                System.out.println("\n[✓] User updated successfully!");
                System.out.println("    " + user);
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }
    
    /**
     * Demo DELETE operation
     */
    private static void demoDelete() {
        System.out.println("\n=== DELETE Demo ===\n");
        
        try (Session session = sessionFactory.openSession()) {
            System.out.print("Enter user ID to delete: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            User user = session.find(User.class, id);
            if (user == null) {
                System.out.println("[!] User not found with ID: " + id);
                return;
            }
            
            System.out.println("Found: " + user);
            System.out.print("Are you sure you want to delete? (y/n): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("y")) {
                Transaction tx = session.beginTransaction();
                try {
                    session.delete(user);
                    tx.commit();
                    System.out.println("\n[✓] User deleted successfully!");
                } catch (Exception e) {
                    tx.rollback();
                    throw e;
                }
            } else {
                System.out.println("[*] Delete cancelled.");
            }
        }
    }
    
    /**
     * Demo QueryBuilder
     */
    private static void demoQueryBuilder() {
        System.out.println("\n=== QueryBuilder Demo ===\n");
        
        try (Session session = sessionFactory.openSession()) {
            // List all users
            System.out.println("1. All users:");
            List<User> allUsers = session.createQuery(User.class).execute();
            for (User u : allUsers) {
                System.out.println("   " + u);
            }
            
            // Query with WHERE
            System.out.println("\n2. Users with age > 20:");
            List<User> adults = session.createQuery(User.class)
                .where("age", ">", 20)
                .orderBy("username ASC")
                .execute();
            for (User u : adults) {
                System.out.println("   " + u);
            }
            
            // Query with LIKE
            System.out.println("\n3. Users with email containing '@example':");
            List<User> exampleUsers = session.createQuery(User.class)
                .where("email", "LIKE", "%@example%")
                .execute();
            for (User u : exampleUsers) {
                System.out.println("   " + u);
            }
            
            // Query with pagination
            System.out.println("\n4. First 2 users (pagination):");
            List<User> page = session.createQuery(User.class)
                .orderBy("id ASC")
                .limit(2)
                .offset(0)
                .execute();
            for (User u : page) {
                System.out.println("   " + u);
            }
            
            // Count
            System.out.println("\n5. Total users count:");
            long count = session.createQuery(User.class).count();
            System.out.println("   Count: " + count);
        }
    }
    
    /**
     * Demo Transaction with rollback
     */
    private static void demoTransaction() {
        System.out.println("\n=== Transaction Demo ===\n");
        
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            
            try {
                // Create user
                User user1 = new User("tx_user_1", "tx1@test.com", 25);
                session.save(user1);
                System.out.println("[*] Created: " + user1);
                
                User user2 = new User("tx_user_2", "tx2@test.com", 30);
                session.save(user2);
                System.out.println("[*] Created: " + user2);
                
                System.out.print("\nCommit transaction? (y/n): ");
                String choice = scanner.nextLine();
                
                if (choice.equalsIgnoreCase("y")) {
                    tx.commit();
                    System.out.println("[✓] Transaction committed!");
                } else {
                    tx.rollback();
                    System.out.println("[*] Transaction rolled back! Data NOT saved.");
                }
                
            } catch (Exception e) {
                tx.rollback();
                System.out.println("[!] Error occurred, transaction rolled back: " + e.getMessage());
            }
        }
    }
    
    /**
     * Demo complete CRUD workflow
     */
    private static void demoFullCRUD() {
        System.out.println("\n=== Full CRUD Workflow Demo ===\n");
        
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            
            try {
                // CREATE
                System.out.println("1. CREATE - Creating new user...");
                User user = new User("demo_user", "demo@test.com", 28);
                session.save(user);
                System.out.println("   Created: " + user);
                
                // READ
                System.out.println("\n2. READ - Finding user by ID...");
                User found = session.find(User.class, user.getId());
                System.out.println("   Found: " + found);
                
                // UPDATE
                System.out.println("\n3. UPDATE - Updating user...");
                found.setUsername("demo_user_updated");
                found.setAge(29);
                session.update(found);
                System.out.println("   Updated: " + found);
                
                // QUERY
                System.out.println("\n4. QUERY - Using QueryBuilder...");
                List<User> users = session.createQuery(User.class)
                    .where("username", "LIKE", "%demo%")
                    .execute();
                System.out.println("   Found " + users.size() + " user(s) matching 'demo'");
                
                // DELETE
                System.out.println("\n5. DELETE - Deleting user...");
                session.delete(found);
                System.out.println("   Deleted user with ID: " + found.getId());
                
                // Verify deletion
                User deleted = session.find(User.class, found.getId());
                System.out.println("   Verification: " + (deleted == null ? "User not found (deleted)" : "Still exists!"));
                
                tx.commit();
                System.out.println("\n[✓] Full CRUD workflow completed successfully!");
                
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }
}
