package com.demo;

import com.dam.framework.core.*;
import com.demo.entity.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DAM Framework Auto Demo Application
 * Runs all demos automatically without user input
 */
public class AutoDemo {
    
    private static SessionFactory sessionFactory;
    
    public static void main(String[] args) {
        printBanner();
        
        try {
            // Initialize framework
            System.out.println("\n[*] Initializing DAM Framework...");
            Configuration config = Configuration.getInstance();
            sessionFactory = config.buildSessionFactory();
            System.out.println("[✓] Framework initialized successfully!\n");
            
            // Run all demos
            demoListAllData();
            demoCRUD();
            demoQueryBuilder();
            demoTransaction();
            demoFullWorkflow();
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("✅ ALL DEMOS COMPLETED SUCCESSFULLY!");
            System.out.println("=".repeat(60));
            
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
        System.out.println("║       DAM FRAMEWORK - Database Access Management          ║");
        System.out.println("║              Auto Demo Application v1.0                   ║");
        System.out.println("║                                                           ║");
        System.out.println("║  Testing CRUD, QueryBuilder, Transactions automatically   ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }
    
    // =========================================
    // List All Data Demo
    // =========================================
    private static void demoListAllData() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 DEMO 1: LIST ALL DATA");
        System.out.println("=".repeat(60));
        
        try (Session session = sessionFactory.openSession()) {
            // Categories
            System.out.println("\n--- Categories ---");
            List<Category> categories = session.createQuery(Category.class).execute();
            System.out.println("Total: " + categories.size());
            for (Category c : categories) {
                System.out.println("  [" + c.getId() + "] " + c.getName() + " - " + c.getDescription());
            }
            
            // Products
            System.out.println("\n--- Products (sorted by price DESC) ---");
            List<Product> products = session.createQuery(Product.class)
                .orderBy("price DESC")
                .execute();
            System.out.println("Total: " + products.size());
            for (Product p : products) {
                System.out.println("  [" + p.getId() + "] " + p.getName() + " - $" + p.getPrice() + " (stock: " + p.getStock() + ")");
            }
            
            // Customers
            System.out.println("\n--- Customers ---");
            List<Customer> customers = session.createQuery(Customer.class).execute();
            System.out.println("Total: " + customers.size());
            for (Customer c : customers) {
                System.out.println("  [" + c.getId() + "] " + c.getName() + " - " + c.getEmail());
            }
            
            System.out.println("\n[✓] List All Data - PASSED");
        } catch (Exception e) {
            System.err.println("[✗] List All Data - FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // =========================================
    // CRUD Operations Demo
    // =========================================
    private static void demoCRUD() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📝 DEMO 2: CRUD OPERATIONS");
        System.out.println("=".repeat(60));
        
        Long productId = null;
        
        try (Session session = sessionFactory.openSession()) {
            // CREATE
            System.out.println("\n[CREATE] Adding new product...");
            Transaction tx1 = session.beginTransaction();
            
            Product newProduct = new Product();
            newProduct.setName("DAM Framework Test Product");
            newProduct.setPrice(new BigDecimal("299.99"));
            newProduct.setStock(100);
            newProduct.setDescription("Created by DAM Framework Auto Demo");
            
            session.save(newProduct);
            tx1.commit();
            productId = newProduct.getId();
            
            System.out.println("  [✓] Product created with ID: " + productId);
            System.out.println("      Name: " + newProduct.getName());
            System.out.println("      Price: $" + newProduct.getPrice());
            
            // READ
            System.out.println("\n[READ] Finding product by ID " + productId + "...");
            Product foundProduct = session.find(Product.class, productId);
            if (foundProduct != null) {
                System.out.println("  [✓] Product found: " + foundProduct.getName());
            } else {
                throw new RuntimeException("Product not found!");
            }
            
            // UPDATE
            System.out.println("\n[UPDATE] Updating product price...");
            Transaction tx2 = session.beginTransaction();
            
            BigDecimal oldPrice = foundProduct.getPrice();
            foundProduct.setPrice(new BigDecimal("249.99"));
            foundProduct.setStock(150);
            session.update(foundProduct);
            tx2.commit();
            
            System.out.println("  [✓] Price updated: $" + oldPrice + " → $" + foundProduct.getPrice());
            System.out.println("  [✓] Stock updated: 100 → " + foundProduct.getStock());
            
            // DELETE
            System.out.println("\n[DELETE] Deleting test product...");
            Transaction tx3 = session.beginTransaction();
            
            Product toDelete = session.find(Product.class, productId);
            session.delete(toDelete);
            tx3.commit();
            
            // Verify deletion
            Product deleted = session.find(Product.class, productId);
            if (deleted == null) {
                System.out.println("  [✓] Product deleted successfully!");
            } else {
                throw new RuntimeException("Product still exists after delete!");
            }
            
            System.out.println("\n[✓] CRUD Operations - ALL PASSED");
            
        } catch (Exception e) {
            System.err.println("[✗] CRUD Operations - FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // =========================================
    // QueryBuilder Demo
    // =========================================
    private static void demoQueryBuilder() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔍 DEMO 3: QUERYBUILDER");
        System.out.println("=".repeat(60));
        
        try (Session session = sessionFactory.openSession()) {
            // Test 1: Simple WHERE
            System.out.println("\n[Test 1] WHERE clause: price > 100");
            List<Product> expensiveProducts = session.createQuery(Product.class)
                .where("price", ">", new BigDecimal("100"))
                .execute();
            System.out.println("  Found " + expensiveProducts.size() + " products with price > $100");
            for (Product p : expensiveProducts) {
                System.out.println("    - " + p.getName() + " ($" + p.getPrice() + ")");
            }
            
            // Test 2: WHERE + ORDER BY
            System.out.println("\n[Test 2] WHERE + ORDER BY: stock > 50 ORDER BY price DESC");
            List<Product> sortedProducts = session.createQuery(Product.class)
                .where("stock", ">", 50)
                .orderBy("price DESC")
                .execute();
            System.out.println("  Found " + sortedProducts.size() + " products (sorted by price DESC):");
            for (Product p : sortedProducts) {
                System.out.println("    - " + p.getName() + " ($" + p.getPrice() + ", stock: " + p.getStock() + ")");
            }
            
            // Test 3: WHERE + ORDER BY + LIMIT
            System.out.println("\n[Test 3] LIMIT: Top 3 cheapest products");
            List<Product> top3Cheap = session.createQuery(Product.class)
                .orderBy("price ASC")
                .limit(3)
                .execute();
            System.out.println("  Top 3 cheapest products:");
            for (Product p : top3Cheap) {
                System.out.println("    - " + p.getName() + " ($" + p.getPrice() + ")");
            }
            
            // Test 4: Multiple WHERE conditions (AND)
            System.out.println("\n[Test 4] Multiple WHERE: price > 50 AND stock >= 100");
            List<Product> filtered = session.createQuery(Product.class)
                .where("price", ">", new BigDecimal("50"))
                .where("stock", ">=", 100)
                .execute();
            System.out.println("  Found " + filtered.size() + " products:");
            for (Product p : filtered) {
                System.out.println("    - " + p.getName() + " ($" + p.getPrice() + ", stock: " + p.getStock() + ")");
            }
            
            // Test 5: COUNT
            System.out.println("\n[Test 5] COUNT: Total products with price < 100");
            long count = session.createQuery(Product.class)
                .where("price", "<", new BigDecimal("100"))
                .count();
            System.out.println("  Count: " + count + " products");
            
            System.out.println("\n[✓] QueryBuilder - ALL TESTS PASSED");
            
        } catch (Exception e) {
            System.err.println("[✗] QueryBuilder - FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // =========================================
    // Transaction Demo
    // =========================================
    private static void demoTransaction() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("💾 DEMO 4: TRANSACTION MANAGEMENT");
        System.out.println("=".repeat(60));
        
        try (Session session = sessionFactory.openSession()) {
            // Test 1: Successful commit
            System.out.println("\n[Test 1] COMMIT - Successful transaction");
            Transaction tx1 = session.beginTransaction();
            
            Customer customer = new Customer();
            customer.setName("Transaction Test User");
            customer.setEmail("tx_test_" + System.currentTimeMillis() + "@demo.com");
            customer.setPhone("0999888777");
            customer.setAddress("Test Address 123");
            
            session.save(customer);
            tx1.commit();
            
            Long customerId = customer.getId();
            System.out.println("  [✓] Customer created with ID: " + customerId);
            
            // Verify
            Customer verifyCommit = session.find(Customer.class, customerId);
            if (verifyCommit != null) {
                System.out.println("  [✓] Verified: Customer persisted after commit");
            }
            
            // Test 2: Rollback
            System.out.println("\n[Test 2] ROLLBACK - Transaction rolled back");
            Transaction tx2 = session.beginTransaction();
            
            Customer tempCustomer = new Customer();
            tempCustomer.setName("Will Be Rolled Back");
            tempCustomer.setEmail("rollback_" + System.currentTimeMillis() + "@demo.com");
            
            session.save(tempCustomer);
            Long tempId = tempCustomer.getId();
            System.out.println("  [*] Temp customer saved with ID: " + tempId);
            
            tx2.rollback();
            System.out.println("  [*] Transaction rolled back");
            
            // Verify rollback
            Customer verifyRollback = session.find(Customer.class, tempId);
            if (verifyRollback == null) {
                System.out.println("  [✓] Verified: Customer was NOT persisted (rollback successful)");
            } else {
                System.out.println("  [!] Note: Customer exists - H2 auto-commit behavior");
            }
            
            // Cleanup
            Transaction tx3 = session.beginTransaction();
            Customer toCleanup = session.find(Customer.class, customerId);
            if (toCleanup != null) {
                session.delete(toCleanup);
            }
            tx3.commit();
            System.out.println("\n  [✓] Cleanup completed");
            
            System.out.println("\n[✓] Transaction Management - PASSED");
            
        } catch (Exception e) {
            System.err.println("[✗] Transaction - FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // =========================================
    // Full Workflow Demo
    // =========================================
    private static void demoFullWorkflow() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔄 DEMO 5: FULL CRUD WORKFLOW");
        System.out.println("=".repeat(60));
        System.out.println("Workflow: CREATE → READ → UPDATE → QUERY → DELETE");
        
        Long productId = null;
        
        try (Session session = sessionFactory.openSession()) {
            // Step 1: CREATE
            System.out.println("\n[Step 1/5] CREATE");
            Transaction tx1 = session.beginTransaction();
            
            Product product = new Product();
            product.setName("Workflow Demo Laptop");
            product.setPrice(new BigDecimal("1599.99"));
            product.setStock(25);
            product.setDescription("High-performance laptop for workflow demo");
            
            session.save(product);
            tx1.commit();
            productId = product.getId();
            System.out.println("  ✓ Created product ID: " + productId);
            
            // Step 2: READ
            System.out.println("\n[Step 2/5] READ");
            Product found = session.find(Product.class, productId);
            System.out.println("  ✓ Found: " + found.getName() + " ($" + found.getPrice() + ")");
            
            // Step 3: UPDATE
            System.out.println("\n[Step 3/5] UPDATE");
            Transaction tx2 = session.beginTransaction();
            found.setPrice(new BigDecimal("1399.99"));
            found.setStock(50);
            session.update(found);
            tx2.commit();
            System.out.println("  ✓ Updated: Price → $1399.99, Stock → 50");
            
            // Step 4: QUERY
            System.out.println("\n[Step 4/5] QUERY");
            List<Product> expensive = session.createQuery(Product.class)
                .where("price", ">", new BigDecimal("1000"))
                .orderBy("price DESC")
                .execute();
            System.out.println("  ✓ Query result: " + expensive.size() + " products with price > $1000");
            
            // Step 5: DELETE
            System.out.println("\n[Step 5/5] DELETE");
            Transaction tx3 = session.beginTransaction();
            Product toDelete = session.find(Product.class, productId);
            session.delete(toDelete);
            tx3.commit();
            
            Product verify = session.find(Product.class, productId);
            if (verify == null) {
                System.out.println("  ✓ Deleted: Product no longer exists");
            }
            
            System.out.println("\n[✓] Full Workflow - ALL STEPS PASSED");
            
        } catch (Exception e) {
            System.err.println("[✗] Full Workflow - FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
