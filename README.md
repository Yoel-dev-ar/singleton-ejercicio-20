Ejercicio 10: Administrador Concurrente de Turnos de Consultorio (Patrón Singleton)
Temática
Gestión Hospitalaria — Reserva y cancelación atómica de turnos entre recepción y autoservicio en un entorno multihilo.

Consigna
Contexto del problema: Un hospital permite solicitar turnos desde recepción y desde un puesto de autoservicio. Ambos componentes se simulan dentro de una misma aplicación. Cuando consultan simultáneamente la disponibilidad de un profesional, pueden asignar el mismo horario a dos pacientes.
Objetivo de aprendizaje: Integrar Singleton, estado compartido y operaciones atómicas para evitar asignaciones duplicadas.
Requisitos funcionales:

Cargar una agenda de horarios disponibles por profesional y fecha.

Reservar un turno indicando paciente, profesional, fecha y horario.

Rechazar solicitudes para horarios ya ocupados.

Cancelar turnos y liberar el horario correspondiente.

Consultar la agenda actualizada desde ambos componentes.
Restricciones técnicas:

Debe existir una única instancia del administrador de turnos, creada de forma segura ante concurrencia.

La verificación de disponibilidad y la asignación deben ejecutarse como una operación atómica.

Se debe simular que al menos veinte hilos intentan reservar el mismo turno.

Solo una de esas solicitudes debe resultar exitosa.

Cancelar un turno inexistente no debe modificar la agenda.

Se deben utilizar datos ficticios de pacientes y mantener toda la simulación dentro de un único proceso.

Explicación lógica
Atributos: En AdministradorTurnos se declaran instancia (private static volatile AdministradorTurnos) para la instancia única segura entre hilos y agenda (private final Map<String, TurnoConsultorio>) para indexar cada turno por profesional, fecha y horario.

Instancia única: El constructor private evita duplicar administradores y getInstancia() aplica Double-Checked Locking con volatile; además, reservarTurno y cancelarTurno están sincronizados para que verificar disponibilidad y asignar sean atómicos.

Asignación y Acceso: En Main, 24 hilos simulan reservas simultáneas desde recepción y autoservicio sobre el mismo horario con pacientes ficticios, garantizando que solo una solicitud tenga éxito, que cancelar un horario inexistente no modifique la agenda y que ambos canales vean la agenda actualizada.

Estructura del ejercicio
TurnoConsultorio.java: Modela un turno de un profesional en una fecha y hora, su estado disponible/reservado y el paciente asignado.

AdministradorTurnos.java: Singleton thread-safe con operaciones atómicas para cargar agendas, reservar, cancelar y consultar turnos.

Main.java: Simula 24 hilos compitiendo por un turno, prueba cancelaciones y consulta la agenda desde ambos canales.