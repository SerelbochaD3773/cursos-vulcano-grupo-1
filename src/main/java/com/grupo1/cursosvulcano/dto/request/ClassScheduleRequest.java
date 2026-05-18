package com.grupo1.cursosvulcano.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import jakarta.validation.constraints.NotNull;

@Data
public class ClassScheduleRequest {

    private Long studentId;
    private Long expertId;
    @NotNull
    private Long courseId;

    private LocalDateTime startTime;
    private String date;
    private String time;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    private String notes;
    private String status;

    public LocalDateTime getStartTime() {
        if (startTime != null) {
            return startTime;
        }
        if (date != null && time != null) {
            return parseDateTime(date, time);
        }
        return null;
    }

    private static LocalDateTime parseDateTime(String date, String time) {
        LocalDate localDate = parseDate(date);
        LocalTime localTime = parseTime(time);
        return LocalDateTime.of(localDate, localTime);
    }

    private static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format: " + dateString);
        }
    }

    private static LocalTime parseTime(String timeString) {
        String normalized = timeString.trim()
            .replace(".", "")
            .replaceAll(" +", " ")
            .toUpperCase(Locale.ENGLISH);

        try {
            if (normalized.endsWith("AM") || normalized.endsWith("PM")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
                return LocalTime.parse(normalized, formatter);
            }
            return LocalTime.parse(normalized, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid time format: " + timeString);
        }
    }
}