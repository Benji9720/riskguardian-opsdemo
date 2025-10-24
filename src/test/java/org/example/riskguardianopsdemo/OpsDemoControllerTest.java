package org.example.riskguardianopsdemo; // ✅ Updated package name

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OpsDemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Test simple pour l'endpoint /info (C18)
    @Test
    void getInfoShouldReturnOkAndVersion() throws Exception {
        mockMvc.perform(get("/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.modelVersion").exists()); // Vérifie que la version existe
    }

    // Test pour le cas nominal de /process (C18)
    @Test
    void processDataShouldReturnOk() throws Exception {
        mockMvc.perform(get("/process").param("data", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Processed: test"));
    }

    // Test pour le cas d'erreur simulé (NPE) (C18 & C21 Validation)
    // Ce test échouera si la correction C21 est décommentée dans le contrôleur
    @Test
    void processDataWithFailShouldReturnError() throws Exception {
        // Si la correction est appliquée, on s'attend à un 500 ou autre gestion
        // Si la correction n'est PAS appliquée, on peut tester le 500 ici
        mockMvc.perform(get("/process").param("data", "fail"))
                .andExpect(status().isInternalServerError());
        // Pour tester la correction (si décommentée), il faudrait tester le cas null/empty:
         /*
         mockMvc.perform(get("/process")) // Sans paramètre 'data'
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Processed default data"));
         */
    }
}