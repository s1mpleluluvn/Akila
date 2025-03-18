package com.akila.service;

import com.akila.entity.CustomerEntity;
import com.akila.entity.data.Customer;
import com.akila.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EntityManager entityManager;

    //    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerRepository, entityManager);
    }

    //test register
    @Test
    void testRegister_Success() {
        // Arrange
        Customer customer = Customer.builder()
                .userName("testUser")
                .email("test@example.com").build();
        customer.setActivatedAt(LocalDateTime.now());

        when(customerRepository.findByUserName("testUser")).thenReturn(Optional.empty());
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(customerRepository.save(any(CustomerEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Customer result = customerService.register(customer);

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUserName());
        assertEquals("test@example.com", result.getEmail());
        verify(customerRepository, times(1)).findByUserName("testUser");
        verify(customerRepository, times(1)).findByEmail("test@example.com");
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void testRegister_UserAlreadyExists() {
        // Arrange
        Customer customer = Customer.builder()
                .userName("existingUser")
                .email("test@example.com").build();
        var customerEntity = new CustomerEntity();
        customerEntity.setCreatedAt(LocalDateTime.now());
        customerEntity.setUpdatedAt(LocalDateTime.now());


        when(customerRepository.findByUserName("existingUser")).thenReturn(Optional.of(customerEntity));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.register(customer);
        });
        assertEquals("User already exists", exception.getMessage());
        verify(customerRepository, times(1)).findByUserName("existingUser");
        verify(customerRepository, never()).findByEmail(anyString());
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        // Arrange
        Customer customer = Customer.builder()
                .userName("testUser")
                .email("existing@example.com").build();

        when(customerRepository.findByUserName("testUser")).thenReturn(Optional.empty());
        when(customerRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new CustomerEntity()));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.register(customer);
        });
        assertEquals("Email already exists", exception.getMessage());
        verify(customerRepository, times(1)).findByUserName("testUser");
        verify(customerRepository, times(1)).findByEmail("existing@example.com");
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }

    @Test
    void testFindByUserName_ByUsername() {
        // Arrange
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUserName("testUser");
        customerEntity.setEmail("test@example.com");

        when(customerRepository.findByUserName("testUser")).thenReturn(Optional.of(customerEntity));

        // Act
        Customer result = customerService.findByUserName("testUser");

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUserName());
        assertEquals("test@example.com", result.getEmail());
        verify(customerRepository, times(1)).findByUserName("testUser");
        verify(customerRepository, never()).findByEmail(anyString());
    }

    @Test
    void testFindByUserName_ByEmail() {
        // Arrange
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUserName("testUser");
        customerEntity.setEmail("test@example.com");

        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customerEntity));

        // Act
        Customer result = customerService.findByUserName("test@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUserName());
        assertEquals("test@example.com", result.getEmail());
        verify(customerRepository, never()).findByUserName(anyString());
        verify(customerRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testFindByUserName_UserNotFound() {
        // Arrange
        when(customerRepository.findByUserName("nonExistingUser")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.findByUserName("nonExistingUser");
        });
        assertEquals("User not found", exception.getMessage());
        verify(customerRepository, times(1)).findByUserName("nonExistingUser");
    }

    @Test
    void testChangePassword_Success() {
        // Arrange
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUserName("testUser");
        customerEntity.setPasswordHash("oldPassword");

        when(customerRepository.findByUserName("testUser")).thenReturn(Optional.of(customerEntity));
        when(customerRepository.save(any(CustomerEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        customerService.changePassword("testUser", "newPassword");

        // Assert
        assertEquals("newPassword", customerEntity.getPasswordHash());
        verify(customerRepository, times(1)).findByUserName("testUser");
        verify(customerRepository, times(1)).save(customerEntity);
    }

    @Test
    void testChangePassword_UserNotFound() {
        // Arrange
        when(customerRepository.findByUserName("nonExistingUser")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.changePassword("nonExistingUser", "newPassword");
        });
        assertEquals("User not found", exception.getMessage());
        verify(customerRepository, times(1)).findByUserName("nonExistingUser");
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }
}
