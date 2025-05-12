import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class ServidorUDP {
    private static ArrayList<Estudiante> estudiantes = new ArrayList<>();

    public static void main(String[] args) {
        llenarEstudiantes();

        try (DatagramSocket socket = new DatagramSocket(5000)) {
            System.out.println("Servidor UDP escuchando en el puerto 5000...");

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket paqueteEntrada = new DatagramPacket(buffer, buffer.length);
                socket.receive(paqueteEntrada);

                // Crear un hilo por solicitud
                Hilo hilo = new Hilo(paqueteEntrada, socket, estudiantes);
                hilo.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void llenarEstudiantes() {
        estudiantes.add(new Estudiante(1, "Ana López", "987654321", "Ingeniería Civil", 3, true));
        estudiantes.add(new Estudiante(2, "Carlos Pérez", "912345678", "Medicina", 5, false));
        estudiantes.add(new Estudiante(3, "María Gómez", "923456789", "Derecho", 2, true));
        estudiantes.add(new Estudiante(4, "Jorge Ramírez", "934567890", "Arquitectura", 4, true));
        estudiantes.add(new Estudiante(5, "Sofía Torres", "945678901", "Psicología", 6, false));
        estudiantes.add(new Estudiante(6, "Luis Martínez", "956789012", "Ingeniería Comercial", 1, true));
        estudiantes.add(new Estudiante(7, "Valentina Ríos", "967890123", "Periodismo", 7, false));
        estudiantes.add(new Estudiante(8, "Tomás Fuentes", "978901234", "Diseño Gráfico", 3, true));
        estudiantes.add(new Estudiante(9, "Camila Soto", "989012345", "Trabajo Social", 8, false));
        estudiantes.add(new Estudiante(10, "Felipe Vargas", "990123456", "Contador Auditor", 2, true));
    }
}
