package com.accenture.franchiseapi.integration;

import com.accenture.franchiseapi.dto.request.CreateBranchRequest;
import com.accenture.franchiseapi.dto.request.CreateFranchiseRequest;
import com.accenture.franchiseapi.dto.request.CreateProductRequest;
import com.accenture.franchiseapi.dto.request.UpdateNameRequest;
import com.accenture.franchiseapi.dto.request.UpdateStockRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FranchiseApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void endToEndFlowShouldWork() throws Exception {
        Long franchiseId = createFranchise("Franquicia Uno");
        Long branchId = addBranch(franchiseId, "Sucursal Centro");
        Long productId = addProduct(branchId, "Producto A", 20);

        mockMvc.perform(patch("/api/branches/{branchId}/products/{productId}/stock", branchId, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateStockRequest(30))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(30));

        mockMvc.perform(get("/api/franchises/{franchiseId}/top-stock-products-by-branch", franchiseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].branchName").value("Sucursal Centro"))
                .andExpect(jsonPath("$[0].productName").value("Producto A"))
                .andExpect(jsonPath("$[0].stock").value(30));

        mockMvc.perform(patch("/api/franchises/{franchiseId}/name", franchiseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateNameRequest("Franquicia Renombrada"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Franquicia Renombrada"));

        mockMvc.perform(delete("/api/branches/{branchId}/products/{productId}", branchId, productId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRejectNegativeStock() throws Exception {
        Long franchiseId = createFranchise("Franquicia Dos");
        Long branchId = addBranch(franchiseId, "Sucursal Sur");

        mockMvc.perform(post("/api/branches/{branchId}/products", branchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateProductRequest("Producto X", -1))))
                .andExpect(status().isBadRequest());
    }

    private Long createFranchise(String name) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/franchises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateFranchiseRequest(name))))
                .andExpect(status().isCreated())
                .andReturn();
        return readId(result);
    }

    private Long addBranch(Long franchiseId, String name) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/franchises/{franchiseId}/branches", franchiseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateBranchRequest(name))))
                .andExpect(status().isCreated())
                .andReturn();
        return readId(result);
    }

    private Long addProduct(Long branchId, String name, Integer stock) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/branches/{branchId}/products", branchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateProductRequest(name, stock))))
                .andExpect(status().isCreated())
                .andReturn();
        return readId(result);
    }

    private Long readId(MvcResult result) throws Exception {
        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        return node.get("id").asLong();
    }
}
