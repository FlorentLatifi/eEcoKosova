package eco.kosova.presentation.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eco.kosova.presentation.dtos.CreateContainerRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import eco.kosova.startup.EcoKosovaApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = EcoKosovaApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ContainerManagementControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testCreateContainer() throws Exception {
        CreateContainerRequest request = new CreateContainerRequest();
        request.setId("TEST-CONT-001");
        request.setZoneId("ZONE-001");
        request.setType("GENERAL");
        request.setCapacity(1000);
        request.setLatitude(42.6629);
        request.setLongitude(21.1655);
        request.setStreet("Test Street");
        request.setCity("Prishtinë");
        request.setMunicipality("Prishtinë");
        request.setPostalCode("10000");
        request.setOperational(true);
        
        mockMvc.perform(post("/api/containers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("TEST-CONT-001"));
    }
    
    @Test
    void testCreateContainerWithInvalidData() throws Exception {
        CreateContainerRequest request = new CreateContainerRequest();
        // Missing required fields
        
        mockMvc.perform(post("/api/containers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void testGetContainerById() throws Exception {
        mockMvc.perform(get("/api/containers/NONEXISTENT"))
            .andExpect(status().isNotFound());
    }
}

