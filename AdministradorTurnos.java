import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdministradorTurnos {
    private static volatile AdministradorTurnos instancia;

    private final Map<String, TurnoConsultorio> agenda;

    private AdministradorTurnos() {
        this.agenda = new LinkedHashMap<>();
    }

    public static AdministradorTurnos getInstancia() {
        if (instancia == null) {
            synchronized (AdministradorTurnos.class) {
                if (instancia == null) {
                    instancia = new AdministradorTurnos();
                }
            }
        }
        return instancia;
    }

    private String generarClave(String profesional, LocalDate fecha, LocalTime horario) {
        return profesional.trim().toLowerCase() + "|" + fecha + "|" + horario;
    }

    public synchronized void cargarHorarioDisponible(String profesional, LocalDate fecha, LocalTime horario) {
        if (profesional == null || profesional.trim().isEmpty() || fecha == null || horario == null) {
            throw new IllegalArgumentException("Datos de agenda incompletos.");
        }
        String clave = generarClave(profesional, fecha, horario);
        agenda.putIfAbsent(clave, new TurnoConsultorio(profesional, fecha, horario));
    }

    public synchronized boolean reservarTurno(String paciente, String profesional, LocalDate fecha, LocalTime horario) {
        if (paciente == null || paciente.trim().isEmpty()
                || profesional == null || profesional.trim().isEmpty()
                || fecha == null || horario == null) {
            return false;
        }
        String clave = generarClave(profesional, fecha, horario);
        TurnoConsultorio turno = agenda.get(clave);
        if (turno == null || !turno.estaDisponible()) {
            return false;
        }
        turno.asignarPaciente(paciente.trim());
        return true;
    }

    public synchronized boolean cancelarTurno(String profesional, LocalDate fecha, LocalTime horario) {
        if (profesional == null || profesional.trim().isEmpty() || fecha == null || horario == null) {
            return false;
        }
        String clave = generarClave(profesional, fecha, horario);
        TurnoConsultorio turno = agenda.get(clave);
        if (turno == null || turno.estaDisponible()) {
            return false; // Turno inexistente o ya libre: no altera agenda
        }
        turno.liberar();
        return true;
    }

    public synchronized List<TurnoConsultorio> consultarAgenda(String profesional, LocalDate fecha) {
        List<TurnoConsultorio> resultado = new ArrayList<>();
        for (TurnoConsultorio turno : agenda.values()) {
            boolean coincideProf = (profesional == null || turno.getProfesional().equalsIgnoreCase(profesional.trim()));
            boolean coincideFecha = (fecha == null || turno.getFecha().equals(fecha));
            if (coincideProf && coincideFecha) {
                resultado.add(turno);
            }
        }
        return resultado;
    }
}