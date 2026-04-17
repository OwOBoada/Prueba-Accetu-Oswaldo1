package com.accenture.franchiseapi.service;

import com.accenture.franchiseapi.domain.entity.Branch;
import com.accenture.franchiseapi.domain.entity.Franchise;
import com.accenture.franchiseapi.domain.entity.Product;
import com.accenture.franchiseapi.dto.request.CreateBranchRequest;
import com.accenture.franchiseapi.dto.request.CreateFranchiseRequest;
import com.accenture.franchiseapi.dto.request.CreateProductRequest;
import com.accenture.franchiseapi.dto.request.UpdateNameRequest;
import com.accenture.franchiseapi.dto.request.UpdateStockRequest;
import com.accenture.franchiseapi.dto.response.TopStockProductByBranchResponse;
import com.accenture.franchiseapi.exception.BusinessException;
import com.accenture.franchiseapi.exception.ResourceNotFoundException;
import com.accenture.franchiseapi.repository.BranchRepository;
import com.accenture.franchiseapi.repository.FranchiseRepository;
import com.accenture.franchiseapi.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public FranchiseService(
            FranchiseRepository franchiseRepository,
            BranchRepository branchRepository,
            ProductRepository productRepository
    ) {
        this.franchiseRepository = franchiseRepository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Franchise> getAllFranchises() {
        return franchiseRepository.findAll();
    }

    @Transactional
    public Branch addBranch(Long franchiseId, CreateBranchRequest request) {
        Franchise franchise = getFranchiseOrThrow(franchiseId);
        Branch branch = new Branch();
        branch.setName(request.name().trim());
        branch.setFranchise(franchise);
        return branchRepository.save(branch);
    }

    @Transactional
    public Product addProduct(Long branchId, CreateProductRequest request) {
        validateStock(request.stock());
        Branch branch = getBranchOrThrow(branchId);
        Product product = new Product();
        product.setName(request.name().trim());
        product.setStock(request.stock());
        product.setBranch(branch);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProductFromBranch(Long branchId, Long productId) {
        Branch branch = getBranchOrThrow(branchId);
        Product product = getProductOrThrow(productId);
        if (!product.getBranch().getId().equals(branch.getId())) {
            throw new BusinessException("El producto no pertenece a la sucursal indicada");
        }
        productRepository.delete(product);
    }

    @Transactional
    public Product updateProductStock(Long branchId, Long productId, UpdateStockRequest request) {
        validateStock(request.stock());
        Branch branch = getBranchOrThrow(branchId);
        Product product = getProductOrThrow(productId);
        if (!product.getBranch().getId().equals(branch.getId())) {
            throw new BusinessException("El producto no pertenece a la sucursal indicada");
        }
        product.setStock(request.stock());
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<TopStockProductByBranchResponse> getTopStockProductsByBranch(Long franchiseId) {
        Franchise franchise = franchiseRepository.findByIdWithBranchesAndProducts(franchiseId)
                .orElseThrow(() -> new ResourceNotFoundException("Franquicia no encontrada con id: " + franchiseId));

        return franchise.getBranches()
                .stream()
                .map(branch -> branch.getProducts()
                        .stream()
                        .max(Comparator.comparing(Product::getStock))
                        .map(product -> new TopStockProductByBranchResponse(
                                branch.getId(),
                                branch.getName(),
                                product.getId(),
                                product.getName(),
                                product.getStock()
                        )))
                .flatMap(java.util.Optional::stream)
                .toList();
    }

    @Transactional
    public Franchise updateFranchiseName(Long franchiseId, UpdateNameRequest request) {
        Franchise franchise = getFranchiseOrThrow(franchiseId);
        franchise.setName(request.name().trim());
        return franchiseRepository.save(franchise);
    }

    @Transactional
    public Branch updateBranchName(Long branchId, UpdateNameRequest request) {
        Branch branch = getBranchOrThrow(branchId);
        branch.setName(request.name().trim());
        return branchRepository.save(branch);
    }

    @Transactional
    public Product updateProductName(Long productId, UpdateNameRequest request) {
        Product product = getProductOrThrow(productId);
        product.setName(request.name().trim());
        return productRepository.save(product);
    }

    public Franchise getFranchiseOrThrow(Long id) {
        return franchiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Franquicia no encontrada con id: " + id));
    }

    private Branch getBranchOrThrow(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con id: " + id));
    }

    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    private void validateStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }
    }
}
