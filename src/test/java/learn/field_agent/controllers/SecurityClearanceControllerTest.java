package learn.field_agent.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityClearanceControllerTest {

    @MockBean
    SecurityClearanceRepository repository;

    @Autowired
    MockMvc mvc;

    @Test
    void getShouldReturn404() throws Exception {

        var request = get("/api/security/clearance/999")
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void addShouldReturn400WhenEmpty() throws Exception {

        var request = post("/api/security/clearance")
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        SecurityClearance securityClearance = new SecurityClearance();
        String securityJson = jsonMapper.writeValueAsString(securityClearance);

        var request = post("/api/security/clearance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(securityJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    void addShouldReturn415WhenMultipart() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        SecurityClearance securityClearance = new SecurityClearance("O5");
        String securityJson = jsonMapper.writeValueAsString(securityClearance);

        var request = post("/api/security/clearance")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(securityJson);

        mvc.perform(request)
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void addShouldReturn201() throws Exception {
        SecurityClearance securityClearance = new SecurityClearance("O5");
        SecurityClearance expected = new SecurityClearance("O5");

        when(repository.add(any())).thenReturn(expected);
        ObjectMapper jsonMapper = new ObjectMapper();

        String securityJson = jsonMapper.writeValueAsString(securityClearance);
        String expectedJson = jsonMapper.writeValueAsString(expected);

        var request = post("/api/security/clearance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(securityJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }
}
