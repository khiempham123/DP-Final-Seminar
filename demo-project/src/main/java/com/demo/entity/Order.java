package com.demo.entity;

import com.dam.framework.annotation.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Order entity - Demo relationships and transactions
 */
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @Column(name = "order_date")
    private Timestamp orderDate;
    
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    
    @Column(name = "status")
    private String status;
    
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> items;
    
    // Constructors
    public Order() {
        this.status = "PENDING";
        this.totalAmount = BigDecimal.ZERO;
    }
    
    public Order(Customer customer) {
        this();
        this.customer = customer;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    
    @Override
    public String toString() {
        return String.format("Order{id=%d, customerId=%d, totalAmount=%s, status='%s'}", 
            id, customer != null ? customer.getId() : null, totalAmount, status);
    }
}
