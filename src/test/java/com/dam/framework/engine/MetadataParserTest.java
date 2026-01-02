package com.dam.framework.engine;

import com.dam.framework.User;
import com.dam.framework.annotation.Entity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test case for MetadataParser.
 * 
 * @author Dev 2
 */
public class MetadataParserTest {
    
    @Test
    public void testParseEntityClass() {
        // Test parsing User entity
        EntityMetadata metadata = MetadataParser.parse(User.class);
        
        // Verify metadata
        assertNotNull(metadata, "Metadata should not be null");
        assertEquals("users", metadata.getTableName(), "Table name should be 'users'");
        assertNotNull(metadata.getIdField(), "ID field should not be null");
        assertEquals("id", metadata.getIdField().getName(), "ID field name should be 'id'");
        assertTrue(metadata.getFields().size() > 0, "Should have fields");
    }
    
    @Test
    public void testNonEntityClass() {
        // Test with non-entity class
        class NotAnEntity {
            private String field;
        }
        
        assertThrows(IllegalArgumentException.class, () -> {
            MetadataParser.parse(NotAnEntity.class);
        }, "Should throw exception for non-entity class");
    }
    
    @Test
    public void testEntityWithoutId() {
        // TODO: Dev 2 - Add test for entity without @Id annotation
    }
    
    @Test
    public void testColumnNameMapping() {
        // TODO: Dev 2 - Add test for column name mapping
    }
}
