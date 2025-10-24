package org.example.riskguardianopsdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OpsDemoController {

    private static final Logger log = LoggerFactory.getLogger(OpsDemoController.class);

    @Value("${model.version}")
    private String modelVersion;

    // Endpoint simple pour vérifier le déploiement et la version du "modèle" (C13)
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        log.info("Request received for /info endpoint. Current model version: {}", modelVersion); // Pour C20
        return ResponseEntity.ok(Map.of("status", "UP", "modelVersion", modelVersion));
    }

    // Endpoint pour simuler un incident (C21)
    @GetMapping("/process")
    public ResponseEntity<Map<String, String>> processData(@RequestParam(required = false) String data) {
        log.info("Request received for /process endpoint with data: {}", data); // Pour C20
        try {
            // FIXME: Potential NullPointerException if 'data' is null (C21 Bug Simulation)
            if (data.equalsIgnoreCase("fail")) {
                throw new NullPointerException("Simulated processing error");
            }
            // --- Début de la correction C21 (à commenter/décommenter) ---
            /*
            if (data == null || data.isEmpty()) {
                log.warn("/process called with null or empty data. Returning default."); // C20 Log
                return ResponseEntity.ok(Map.of("result", "Processed default data", "modelVersion", modelVersion));
            }
            if (data.equalsIgnoreCase("fail")) {
                 log.error("Simulating NPE for data='fail'"); // C20 Log
                 throw new NullPointerException("Simulated processing error");
            }
            */
            // --- Fin de la correction C21 ---

            log.info("Processing data: {}", data); // C20 Log
            return ResponseEntity.ok(Map.of("result", "Processed: " + data, "modelVersion", modelVersion));

        } catch (NullPointerException e) {
            log.error("Incident! NullPointerException during processing. Data was: {}", data, e); // C20/C21 Log d'erreur
            // Pour C21, cette erreur serait vue dans les logs centralisés (à expliquer)
            return ResponseEntity.status(500).body(Map.of("error", "Internal Processing Error", "details", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during processing: {}", e.getMessage(), e); // C20 Log
            return ResponseEntity.status(500).body(Map.of("error", "Unexpected Error", "details", e.getMessage()));
        }
    }
}