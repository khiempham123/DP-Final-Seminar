package com.dam.framework.query;

import com.dam.framework.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test case for QueryBuilder.
 * 
 * @author Dev 2
 */
public class QueryBuilderTest {
    
    @Test
    public void testSimpleWhereClause() {
        // TODO: Dev 2 - Implement test
        QueryBuilder<User> builder = new QueryBuilder<>(User.class);
        builder.where("username", "=", "john");
        
        // Test SQL generation
        // String sql = builder.buildSQL();
        // assertTrue(sql.contains("WHERE username = ?"));
    }
    
    @Test
    public void testMultipleWhereClause() {
        // TODO: Dev 2 - Test multiple WHERE conditions with AND
        QueryBuilder<User> builder = new QueryBuilder<>(User.class);
        builder.where("age", ">", 18)
               .where("status", "=", "ACTIVE");
        
        // Verify parameters
        // List<Object> params = builder.getParameters();
        // assertEquals(2, params.size());
    }
    
    @Test
    public void testOrWhereClause() {
        // TODO: Dev 2 - Test OR condition
    }
    
    @Test
    public void testGroupByAndHaving() {
        // TODO: Dev 2 - Test GROUP BY and HAVING
        QueryBuilder<User> builder = new QueryBuilder<>(User.class);
        builder.select("COUNT(*) as count")
               .groupBy("age")
               .having("COUNT(*) > 5");
        
        // Verify SQL
        // String sql = builder.buildSQL();
        // assertTrue(sql.contains("GROUP BY"));
        // assertTrue(sql.contains("HAVING"));
    }
    
    @Test
    public void testOrderByAndLimit() {
        // TODO: Dev 2 - Test ORDER BY with LIMIT/OFFSET
        QueryBuilder<User> builder = new QueryBuilder<>(User.class);
        builder.orderBy("username ASC")
               .limit(10)
               .offset(0);
        
        // Verify SQL
    }
}
