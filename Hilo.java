import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Hilo extends Thread {
    private DatagramPacket paquete;
    private DatagramSocket socket;
    private ArrayList<Estudiante> estudiantes;

    public Hilo(DatagramPacket paquete, DatagramSocket socket, ArrayList<Estudiante> estudiantes) {
        this.paquete = paquete;
        this.socket = socket;
        this.estudiantes = estudiantes;
    }

    @Override
    public void run() {
        try {
            String mensaje = new String(paquete.getData(), 0, paquete.getLength()).trim();
            System.out.println("Solicitud recibida: " + mensaje);

            String respuesta;
            try {
                int idBuscado = Integer.parseInt(mensaje);
                Estudiante e = buscarEstudiante(idBuscado);
                if (e != null) {
                    respuesta = "Estudiante encontrado:\n" + e.toString();
                } else {
                    respuesta = "Estudiante con ID " + idBuscado + " no encontrado.";
                }
            } catch (NumberFormatException e) {
                respuesta = "Formato de ID inválido.";
            }

            byte[] datosRespuesta = respuesta.getBytes();
            DatagramPacket respuestaPaquete = new DatagramPacket(
                    datosRespuesta,
                    datosRespuesta.length,
                    paquete.getAddress(),
                    paquete.getPort()
            );
            socket.send(respuestaPaquete);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Estudiante buscarEstudiante(int id) {
        for (Estudiante e : estudiantes) {
            if (e.id == id) {
                return e;
            }
        }
        return null;
    }
}
