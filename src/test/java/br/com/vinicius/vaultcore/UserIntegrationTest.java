package br.com.vinicius.vaultcore;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.vinicius.vaultcore.dto.UserDTO;
import br.com.vinicius.vaultcore.model.UserType;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve criar um usuário com sucesso e retornar 201 Created")
    void shouldCreateUserWithSuccess() throws Exception {
        UserDTO userDTO = new UserDTO(
                "Jose",
                "Melo",
                "28803546090",
                "jose@teste.com",
                "senha123",
                new BigDecimal("1000.00"),
                UserType.COMMON
        );
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jose"))
                .andExpect(jsonPath("$.email").value("jose@teste.com"));
    }
}
