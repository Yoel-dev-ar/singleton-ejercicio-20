// TurnoConsultorio.java
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TurnoConsultorio {
    private final String profesional;
    private final LocalDate fecha;
    private final LocalTime horario;
    private String paciente;

    public TurnoConsultorio(String profesional, LocalDate fecha, LocalTime horario) {
        if (profesional == null || profesional.trim().isEmpty()) {
            throw new IllegalArgumentException("El profesional es obligatorio.");
        }
        if (fecha == null || horario == null) {
            throw new IllegalArgumentException("La fecha y el horario son obligatorios.");
        }
        this.profesional = profesional.trim();
        this.fecha = fecha;
        this.horario = horario;
        this.paciente = null;
    }

    public String getProfesional() {
        return profesional;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public String getPaciente() {
        return paciente;
    }

    public boolean estaDisponible() {
        return paciente == null;
    }

    public void asignarPaciente(String paciente) {
        this.paciente = paciente;
    }

    public void liberar() {
        this.paciente = null;
    }

    @Override
    public String toString() {
        String estado = estaDisponible() ? "DISPONIBLE" : "RESERVADO por " + paciente;
        return fecha + " " + horario + " | Dr/a. " + profesional + " -> " + estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TurnoConsultorio)) return false;
        TurnoConsultorio that = (TurnoConsultorio) o;
        return profesional.equalsIgnoreCase(that.profesional)
                && fecha.equals(that.fecha)
                && horario.equals(that.horario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profesional.toLowerCase(), fecha, horario);
    }
}