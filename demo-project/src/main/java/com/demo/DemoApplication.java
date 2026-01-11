package com.demo;

import com.dam.framework.core.*;
import com.dam.framework.query.QueryBuilder;
import com.demo.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * DAM Framework Demo Application
 * 
 * This demo showcases all features of DAM Framework:
 * 1. CRUD Operations (Create, Read, Update, Delete)
 * 2. QueryBuilder with WHERE, ORDER BY, LIMIT
 * 3. Transaction management
 * 4. Entity relationships (OneToMany, ManyToOne)
 * 
 * @author DAM Framework Team
 */
public class DemoApplication {
    
    private static SessionFactory sessionFactory;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        printBanner();
        
        try {
            // Initialize framework
            System.out.println("\n[*] Initializing DAM Framework...");
            Configuration config = Configuration.getInstance();
            sessionFactory = config.buildSessionFactory();
            System.out.println("[✓] Framework initialized successfully!\n");
            
            // Run demo menu
            runMenu();
            
        } catch (Exception e) {
            System.err.println("\n[✗] Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
                System.out.println("\n[*] Framework closed. Goodbye!");
            }
        }
    }
    
    private static void printBanner() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       DAM FRAMEWORK - Database Access Management         ║");
        System.out.println("║              Demo Application v1.0                        ║");
        System.out.println("║                                                          ║");
        System.out.println("║  Testing CRUD, QueryBuilder, Transactions                ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }
    
    private static void runMenu() {
        boolean running = true;
        
        while (running) {
            printMenu();
            
            System.out.print("\nEnter choice: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    demoCRUD_Create();
                    break;
                case "2":
                    demoCRUD_Read();
                    break;
                case "3":
                    demoCRUD_Update();
                    break;
                case "4":
                    demoCRUD_Delete();
                    break;
                case "5":
                    demoQueryBuilder();
                    break;
                case "6":
                    demoTransaction();
                    break;
                case "7":
                    demoFullWorkflow();
                    break;
                case "8":
                    listAllData();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private static void printMenu() {
        System.out.println("\n┌─────────────────── MENU ───────────────────┐");
        System.out.println("│  1. CREATE   - Add new product              │");
        System.out.println("│  2. READ     - Find product by ID           │");
        System.out.println("│  3. UPDATE   - Update product               │");
        System.out.println("│  4. DELETE   - Delete product               │");
        System.out.println("│  5. QUERY    - Demo QueryBuilder            │");
        System.out.println("│  6. TRANSACTION - Demo Transaction          │");
        System.out.println("│  7. FULL WORKFLOW - Complete demo           │");
        System.out.println("│  8. LIST ALL - Show all data                │");
        System.out.println("│  0. EXIT                                    │");
        System.out.println("└─────────────────────────────────────────────┘");
    }
    
    // =========================================
    // CRUD Operations Demo
    // =========================================
    
    private static void demoCRUD_Create() {
        System.out.println("\n=== CREATE Demo ===");
        
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            
            // Create new product
            Product product = new Product();
            product.setName("Demo Product " + System.currentTimeMillis());
            product.setPrice(new BigDecimal("99.99"));
            product.setStock(50);
            product.setDescription("Created by DAM Framework Demo");
            
            System.out.println("[*] Creating product: " + product.getName());
            
            session.save(product);
            tx.commit();
            
            System.out.println("[✓] Product created successfully!");
            System.out.println("    ID: " + product.getId());
            System.out.println("    " + product);
            
        } catch (Exception e) {
            System.err.println("[✗] Create failed: " + e.getMessage());
        }
    }
    
    private static void demoCRUD_Read() {
        System.out.println("\n=== READ Demo ===");
        
        System.out.print("Enter Product ID to find: ");
        String input = scanner.nextLine().trim();
        
        try {
            Long id = Long.parseLong(input);
            
            try (Session session = sessionFactory.openSession()) {
                Product product = session.find(Product.class, id);
                
                if (product != null) {
                    System.out.println("[✓] Product found!");
                    System.out.println("    ID: " + product.getId());
                    System.out.println("    Name: " + product.getName());
                    System.out.println("    Price: $" + product.getPrice());
                    System.out.println("    Stock: " + product.getStock());
                    System.out.println("    Description: " + product.getDescription());
                } else {
                    System.out.println("[!] Product not found with ID: " + id);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("[✗] Invalid ID format");
        } catch (Exception e) {
            System.err.println("[✗] Read failed: " + e.getMessage());
        }
    }
    
    private static void demoCRUD_Update() {
        System.out.println("\n=== UPDATE Demo ===");
        
        System.out.print("Enter Product ID to update: ");
        String input = scanner.nextLine().trim();
        
        try {
            Long id = Long.parseLong(input);
            
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                
                Product product = session.find(Product.class, id);
                
                if (product != null) {
                    System.out.println("[*] Current product: " + product);
                    
                    // Update product
                    String newName = product.getName() + " (Updated)";
                    BigDecimal newPrice = product.getPrice().add(new BigDecimal("10.00"));
                    
                    product.setName(newName);
                    product.setPrice(newPrice);
                    
                    session.update(product);
                    tx.commit();
                    
                    System.out.println("[✓] Product updated successfully!");
                    System.out.println("    New name: " + product.getName());
                    System.out.println("    New price: $" + product.getPrice());
                } else {
                    System.out.println("[!] Product not found with ID: " + id);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("[✗] Invalid ID format");
        } catch (Exception e) {
            System.err.println("[✗] Update failed: " + e.getMessage());
        }
    }
    
    private static void demoCRUD_Delete() {
        System.out.println("\n=== DELETE Demo ===");
        
        System.out.print("Enter Product ID to delete: ");
        String input = scanner.nextLine().trim();
        
        try {
            Long id = Long.parseLong(input);
            
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                
                Product product = session.find(Product.class, id);
                
                if (product != null) {
                    System.out.println("[*] Deleting product: " + product);
                    
                    session.delete(product);
                    tx.commit();
                    
                    System.out.println("[✓] Product deleted successfully!");
                } else {
                    System.out.println("[!] Product not found with ID: " + id);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("[✗] Invalid ID format");
        } catch (Exception e) {
            System.err.println("[✗] Delete failed: " + e.getMessage());
        }
    }
    
    // =========================================
    // QueryBuilder Demo
    // =========================================
    
    private static void demoQueryBuilder() {
        System.out.println("\n=== QueryBuilder Demo ===");
        
        try (Session session = sessionFactory.openSession()) {
            System.out.println("\n[1] Simple WHERE clause:");
            System.out.println("    Query: SELECT * FROM products WHERE price > 50");
            
            List<Product> expensiveProducts = session.createQuery(Product.class)
                .where("price", ">", new BigDecimal("50"))
                .execute();
            
            System.out.println("    Found " + expensiveProducts.size() + " products:");
            for (Product p : expensiveProducts) {
                System.out.println("      - " + p.getName() + " ($" + p.getPrice() + ")");
            }
            
            System.out.println("\n[2] Multiple WHERE + ORDER BY:");
            System.out.println("    Query: SELECT * FROM products WHERE stock > 20 ORDER BY price DESC");
            
            List<Product> sortedProducts = session.createQuery(Product.class)
                .where("stock", ">", 20)
                .orderBy("price DESC")
                .execute();
            
            System.out.println("    Found " + sortedProducts.size() + " products (sorted by price DESC):");
            for (Product p : sortedProducts) {
                System.out.println("      - " + p.getName() + " ($" + p.getPrice() + ", stock: " + p.getStock() + ")");
            }
            
            System.out.println("\n[3] WHERE + ORDER BY + LIMIT:");
            System.out.println("    Query: SELECT * FROM products WHERE price < 100 ORDER BY name LIMIT 5");
            
            List<Product> limitedProducts = session.createQuery(Product.class)
                .where("price", "<", new BigDecimal("100"))
                .orderBy("name ASC")
                .limit(5)
                .execute();
            
            System.out.println("    Top 5 affordable products:");
            for (Product p : limitedProducts) {
                System.out.println("      - " + p.getName() + " ($" + p.getPrice() + ")");
            }
            
            System.out.println("\n[4] Complex query with multiple conditions:");
            System.out.println("    Query: SELECT * FROM products WHERE price >= 20 AND stock >= 50");
            
            List<Product> complexQuery = session.createQuery(Product.class)
                .where("price", ">=", new BigDecimal("20"))
                .where("stock", ">=", 50)
                .execute();
            
            System.out.println("    Found " + complexQuery.size() + " products meeting criteria:");
            for (Product p : complexQuery) {
                System.out.println("      - " + p.getName() + " ($" + p.getPrice() + ", stock: " + p.getStock() + ")");
            }
            
            System.out.println("\n[✓] QueryBuilder demo completed!");
            
        } catch (Exception e) {
            System.err.println("[✗] QueryBuilder demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // =========================================
    // Transaction Demo
    // =========================================
    
    private static void demoTransaction() {
        System.out.println("\n=== Transaction Demo ===");
        
        try (Session session = sessionFactory.openSession()) {
            
            // Demo 1: Successful transaction
            System.out.println("\n[1] Successful Transaction (COMMIT):");
            
            Transaction tx1 = session.beginTransaction();
            
            Customer customer = new Customer();
            customer.setName("Transaction Test Customer");
            customer.setEmail("tx_test_" + System.currentTimeMillis() + "@email.com");
            customer.setPhone("0999999999");
            customer.setAddress("Test Address");
            
            System.out.println("    [*] Saving customer...");
            session.save(customer);
            
            System.out.println("    [*] Committing transaction...");
            tx1.commit();
            
            System.out.println("    [✓] Transaction committed! Customer ID: " + customer.getId());
            
            // Demo 2: Rollback transaction
            System.out.println("\n[2] Rollback Transaction Demo:");
            
            Transaction tx2 = session.beginTransaction();
            
            Customer tempCustomer = new Customer();
            tempCustomer.setName("Will Be Rolled Back");
            tempCustomer.setEmail("rollback_" + System.currentTimeMillis() + "@email.com");
            
            System.out.println("    [*] Saving temporary customer...");
            session.save(tempCustomer);
            Long tempId = tempCustomer.getId();
            System.out.println("    [*] Temporary customer ID: " + tempId);
            
            System.out.println("    [*] Rolling back transaction...");
            tx2.rollback();
            System.out.println("    [✓] Transaction rolled back!");
            
            // Verify rollback
            Customer verifyCustomer = session.find(Customer.class, tempId);
            if (verifyCustomer == null) {
                System.out.println("    [✓] Verified: Customer was NOT persisted (rollback worked)");
            } else {
                System.out.println("    [!] Customer still exists (rollback may have failed)");
            }
            
            System.out.println("\n[✓] Transaction demo completed!");
            
        } catch (Exception e) {
            System.err.println("[✗] Transaction demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // =========================================
    // Full Workflow Demo
    // =========================================
    
    private static void demoFullWorkflow() {
        System.out.println("\n=== Full CRUD Workflow Demo ===");
        System.out.println("This demo will: Create → Read → Update → Query → Delete");
        
        Long productId = null;
        
        try (Session session = sessionFactory.openSession()) {
            
            // Step 1: CREATE
            System.out.println("\n[Step 1/5] CREATE - Creating new product...");
            Transaction tx1 = session.beginTransaction();
            
            Product product = new Product();
            product.setName("Demo Laptop Pro");
            product.setPrice(new BigDecimal("1499.99"));
            product.setStock(25);
            product.setDescription("High-end laptop for demo purposes");
            
            session.save(product);
            tx1.commit();
            productId = product.getId();
            
            System.out.println("    [✓] Product created with ID: " + productId);
            System.out.println("    " + product);
            
            // Step 2: READ
            System.out.println("\n[Step 2/5] READ - Finding product by ID...");
            
            Product foundProduct = session.find(Product.class, productId);
            System.out.println("    [✓] Product found: " + foundProduct.getName());
            
            // Step 3: UPDATE
            System.out.println("\n[Step 3/5] UPDATE - Updating product price...");
            Transaction tx2 = session.beginTransaction();
            
            BigDecimal oldPrice = foundProduct.getPrice();
            foundProduct.setPrice(new BigDecimal("1299.99"));
            foundProduct.setStock(30);
            
            session.update(foundProduct);
            tx2.commit();
            
            System.out.println("    [✓] Price updated: $" + oldPrice + " → $" + foundProduct.getPrice());
            System.out.println("    [✓] Stock updated: 25 → " + foundProduct.getStock());
            
            // Step 4: QUERY
            System.out.println("\n[Step 4/5] QUERY - Finding all products with price > $1000...");
            
            List<Product> expensiveProducts = session.createQuery(Product.class)
                .where("price", ">", new BigDecimal("1000"))
                .orderBy("price DESC")
                .execute();
            
            System.out.println("    [✓] Found " + expensiveProducts.size() + " expensive products:");
            for (Product p : expensiveProducts) {
                System.out.println("      - " + p.getName() + " ($" + p.getPrice() + ")");
            }
            
            // Step 5: DELETE
            System.out.println("\n[Step 5/5] DELETE - Removing demo product...");
            Transaction tx3 = session.beginTransaction();
            
            // Re-find the product to ensure we have the latest state
            Product toDelete = session.find(Product.class, productId);
            if (toDelete != null) {
                session.delete(toDelete);
                tx3.commit();
                System.out.println("    [✓] Product deleted successfully!");
            }
            
            // Verify deletion
            Product deletedProduct = session.find(Product.class, productId);
            if (deletedProduct == null) {
                System.out.println("    [✓] Verified: Product no longer exists");
            }
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("✅ Full CRUD Workflow completed successfully!");
            System.out.println("=".repeat(50));
            
        } catch (Exception e) {
            System.err.println("[✗] Workflow failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // =========================================
    // List All Data
    // =========================================
    
    private static void listAllData() {
        System.out.println("\n=== Database Overview ===");
        
        try (Session session = sessionFactory.openSession()) {
            
            // Categories
            System.out.println("\n--- Categories ---");
            List<Category> categories = session.createQuery(Category.class).execute();
            System.out.println("Total: " + categories.size());
            for (Category c : categories) {
                System.out.println("  " + c);
            }
            
            // Products
            System.out.println("\n--- Products ---");
            List<Product> products = session.createQuery(Product.class)
                .orderBy("name ASC")
                .execute();
            System.out.println("Total: " + products.size());
            for (Product p : products) {
                System.out.println("  " + p);
            }
            
            // Customers
            System.out.println("\n--- Customers ---");
            List<Customer> customers = session.createQuery(Customer.class).execute();
            System.out.println("Total: " + customers.size());
            for (Customer c : customers) {
                System.out.println("  " + c);
            }
            
            // Orders
            System.out.println("\n--- Orders ---");
            List<Order> orders = session.createQuery(Order.class).execute();
            System.out.println("Total: " + orders.size());
            for (Order o : orders) {
                System.out.println("  " + o);
            }
            
            System.out.println("\n[✓] Data overview completed!");
            
        } catch (Exception e) {
            System.err.println("[✗] List data failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
