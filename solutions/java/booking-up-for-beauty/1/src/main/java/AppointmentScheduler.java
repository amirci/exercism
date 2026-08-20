import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

class AppointmentScheduler {
    private static final DateTimeFormatter APPOINTMENT_FORMATTER =
            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    private static final DateTimeFormatter DESCRIPTION_FORMATTER =
            DateTimeFormatter.ofPattern("'You have an appointment on' EEEE, MMMM d, yyyy, 'at' h:mm a'.'");

    public LocalDateTime schedule(String appointmentDateDescription) {
        return LocalDateTime.parse(appointmentDateDescription, APPOINTMENT_FORMATTER);
    }

    public boolean hasPassed(LocalDateTime appointmentDate) {
        return appointmentDate.isBefore(LocalDateTime.now());
    }

    public boolean isAfternoonAppointment(LocalDateTime appointmentDate) {
        var hour = appointmentDate.getHour();

        return hour >= 12 && hour < 18;
    }

    public String getDescription(LocalDateTime appointmentDate) {
        return appointmentDate.format(DESCRIPTION_FORMATTER);
    }

    public LocalDate getAnniversaryDate() {
        return LocalDate.of(LocalDate.now().getYear(), Month.SEPTEMBER, 15);
    }
}
