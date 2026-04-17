package com.accenture.franchiseapi.service;

import com.accenture.franchiseapi.domain.entity.Branch;
import com.accenture.franchiseapi.domain.entity.Franchise;
import com.accenture.franchiseapi.domain.entity.Product;
import com.accenture.franchiseapi.dto.request.CreateProductRequest;
import com.accenture.franchiseapi.dto.request.UpdateStockRequest;
import com.accenture.franchiseapi.dto.response.TopStockProductByBranchResponse;
import com.accenture.franchiseapi.exception.BusinessException;
import com.accenture.franchiseapi.repository.BranchRepository;
import com.accenture.franchiseapi.repository.FranchiseRepository;
import com.accenture.franchiseapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseServiceTest {

    @Mock
    private FranchiseRepository franchiseRepository;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FranchiseService franchiseService;

    private Branch branch;

    @BeforeEach
    void setup() {
        branch = new Branch();
        branch.setId(1L);
        branch.setName("Sucursal Centro");
    }

    @Test
    void shouldRejectNegativeStockOnAddProduct() {
        assertThrows(BusinessException.class, () -> franchiseService.addProduct(1L, new CreateProductRequest("Gaseosa", -1)));
    }

    @Test
    void shouldUpdateStockWhenProductBelongsToBranch() {
        Product product = new Product();
        product.setId(2L);
        product.setName("Producto A");
        product.setStock(5);
        product.setBranch(branch);

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updated = franchiseService.updateProductStock(1L, 2L, new UpdateStockRequest(10));
        assertEquals(10, updated.getStock());
    }

    @Test
    void shouldReturnTopStockProductByBranch() {
        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Mi Franquicia");

        Branch b1 = new Branch();
        b1.setId(3L);
        b1.setName("Norte");
        b1.setFranchise(franchise);

        Product p1 = new Product();
        p1.setId(10L);
        p1.setName("A");
        p1.setStock(4);
        p1.setBranch(b1);

        Product p2 = new Product();
        p2.setId(11L);
        p2.setName("B");
        p2.setStock(12);
        p2.setBranch(b1);

        b1.getProducts().add(p1);
        b1.getProducts().add(p2);
        franchise.getBranches().add(b1);

        when(franchiseRepository.findByIdWithBranchesAndProducts(1L)).thenReturn(Optional.of(franchise));

        List<TopStockProductByBranchResponse> response = franchiseService.getTopStockProductsByBranch(1L);
        assertEquals(1, response.size());
        assertEquals("B", response.getFirst().productName());
        assertEquals(12, response.getFirst().stock());
    }
}
