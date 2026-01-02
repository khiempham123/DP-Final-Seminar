package com.dam.framework;

import com.dam.framework.core.Configuration;
import com.dam.framework.core.Session;
import com.dam.framework.core.SessionFactory;
import com.dam.framework.core.Transaction;

/**
 * Example application demonstrating framework usage.
 * 
 * TODO: Dev 1 & Dev 2 - Complete this demo after implementation
 * 
 * @author Dev 1 & Dev 2
 */
public class ExampleApp {
    
    public static void main(String[] args) {
        
        System.out.println("=== DAM Framework Demo ===\n");
        
        // TODO: Uncomment after implementation is complete
        
        /*
        // 1. Initialize configuration
        Configuration config = Configuration.getInstance();
        SessionFactory sessionFactory = config.buildSessionFactory();
        
        // 2. Open session
        try (Session session = sessionFactory.openSession()) {
            
            // 3. Begin transaction
            Transaction tx = session.beginTransaction();
            
            try {
                // CREATE - Insert new user
                System.out.println("1. Creating new user...");
                User newUser = new User("john_doe", "john@example.com", 25);
                session.save(newUser);
                System.out.println("   Created: " + newUser);
                
                // READ - Find user by ID
                System.out.println("\n2. Finding user by ID...");
                User foundUser = session.find(User.class, newUser.getId());
                System.out.println("   Found: " + foundUser);
                
                // UPDATE - Modify user
                System.out.println("\n3. Updating user...");
                foundUser.setEmail("newemail@example.com");
                foundUser.setAge(26);
                session.update(foundUser);
                System.out.println("   Updated: " + foundUser);
                
                // QUERY - Find users with criteria
                System.out.println("\n4. Querying users...");
                List<User> users = session.createQuery(User.class)
                    .where("age", ">", 18)
                    .orderBy("username ASC")
                    .execute();
                System.out.println("   Found " + users.size() + " users:");
                users.forEach(u -> System.out.println("   - " + u));
                
                // DELETE - Remove user
                System.out.println("\n5. Deleting user...");
                session.delete(foundUser);
                System.out.println("   Deleted: " + foundUser.getId());
                
                // Commit transaction
                tx.commit();
                System.out.println("\nTransaction committed successfully!");
                
            } catch (Exception e) {
                // Rollback on error
                tx.rollback();
                System.err.println("Transaction rolled back due to error: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close session factory
            sessionFactory.close();
        }
        */
        
        System.out.println("\nTODO: Implement framework to run this demo!");
        System.out.println("See README.md for development guide.");
    }
}
