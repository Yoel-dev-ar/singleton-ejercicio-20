import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        AdministradorTurnos recepcion = AdministradorTurnos.getInstancia();
        AdministradorTurnos autoservicio = AdministradorTurnos.getInstancia();

        LocalDate fecha = LocalDate.of(2026, 12, 1);
        String profesional = "Dra. Gómez";
        LocalTime slotCompetido = LocalTime.of(10, 0);

        recepcion.cargarHorarioDisponible(profesional, fecha, LocalTime.of(9, 30));
        recepcion.cargarHorarioDisponible(profesional, fecha, slotCompetido);
        recepcion.cargarHorarioDisponible(profesional, fecha, LocalTime.of(10, 30));

        // 24 hilos (recepción y autoservicio) compiten por el mismo turno de las 10:00
        AtomicInteger exitos = new AtomicInteger(0);
        List<Thread> hilos = new ArrayList<>();

        for (int i = 1; i <= 24; i++) {
            final String pacienteFicticio = "Paciente Ficticio #" + i;
            final boolean desdeAutoservicio = (i % 2 == 0);
            Thread t = new Thread(() -> {
                AdministradorTurnos canal = desdeAutoservicio ? autoservicio : recepcion;
                if (canal.reservarTurno(pacienteFicticio, profesional, fecha, slotCompetido)) {
                    exitos.incrementAndGet();
                    System.out.println("Turno asignado a " + pacienteFicticio
                            + " (" + (desdeAutoservicio ? "Autoservicio" : "Recepción") + ")");
                }
            });
            hilos.add(t);
        }

        for (Thread t : hilos) {
            t.start();
        }
        for (Thread t : hilos) {
            t.join();
        }

        System.out.println("\nSolicitudes exitosas para las 10:00 (debe ser 1): " + exitos.get());

        // Cancelar un turno inexistente no altera la agenda
        boolean cancelacionInexistente = autoservicio.cancelarTurno(profesional, fecha, LocalTime.of(15, 0));
        System.out.println("Cancelar turno inexistente (15:00): " + cancelacionInexistente);

        System.out.println("\nAgenda consultada desde Autoservicio:");
        autoservicio.consultarAgenda(profesional, fecha).forEach(System.out::println);

        // Cancelar turno ocupado y verificar liberación
        recepcion.cancelarTurno(profesional, fecha, slotCompetido);
        System.out.println("\nAgenda consultada desde Recepción tras cancelar 10:00:");
        recepcion.consultarAgenda(profesional, fecha).forEach(System.out::println);
    }
}