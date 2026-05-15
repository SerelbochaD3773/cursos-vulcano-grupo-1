package com.grupo1.cursosvulcano.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class DatabaseMigrationRunner implements CommandLineRunner {

    private final DataSource dataSource;

    @Override
    public void run(String... args) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // Elimina la restricción NOT NULL de student_id si aún existe.
            // Esto es idempotente: si ya fue eliminada, el bloque catch lo ignora.
            stmt.execute("ALTER TABLE class_schedules ALTER COLUMN student_id DROP NOT NULL");
            System.out.println("[Migration] student_id constraint dropped successfully.");

        } catch (Exception e) {
            // La constraint ya no existe o fue eliminada anteriormente. Se ignora.
            System.out.println("[Migration] student_id constraint already nullable or not found: " + e.getMessage());
        }
    }
}
