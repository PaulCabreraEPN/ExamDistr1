import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class ClienteGUI extends JFrame {
    private JTextField idField; // Campo para ingresar el ID del estudiante
    private JTextArea resultArea; // Área de texto para mostrar los resultados
    private JButton consultaButton; // Botón para enviar la consulta al servidor
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort = 5000;

    public ClienteGUI() {
        // Configuración de la ventana
        setTitle("Consulta de Estudiantes");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana
        getContentPane().setBackground(new Color(255, 255, 255)); // Fondo blanco

        // Crear los componentes
        idField = new JTextField(15);
        consultaButton = new JButton("Consultar");
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false); // No editable por el usuario

        // Estilos de los componentes
        idField.setFont(new Font("Arial", Font.PLAIN, 14));
        idField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2)); // Borde azul para el campo de ID

        consultaButton.setFont(new Font("Arial", Font.BOLD, 14));
        consultaButton.setBackground(new Color(0, 123, 255)); // Color azul para el botón
        consultaButton.setForeground(Color.WHITE); // Texto blanco
        consultaButton.setFocusPainted(false); // Elimina el borde del botón cuando se hace clic
        consultaButton.setPreferredSize(new Dimension(120, 40)); // Tamaño más grande
        consultaButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambiar el cursor a mano

        consultaButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                consultaButton.setBackground(new Color(0, 102, 204)); // Color cuando el mouse entra
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                consultaButton.setBackground(new Color(0, 123, 255)); // Color original
            }
        });

        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2)); // Borde azul para el área de
                                                                                         // texto
        resultArea.setBackground(new Color(245, 245, 245)); // Fondo gris claro

        // Crear el layout
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20)); // Centrar los componentes y agregar espacio
        panel.setBackground(new Color(255, 255, 255)); // Fondo blanco
        panel.add(new JLabel("ID del Estudiante:"));
        panel.add(idField);
        panel.add(consultaButton);
        panel.add(new JScrollPane(resultArea)); // Para hacer scroll si el texto es muy largo

        add(panel);

        // Establecer la conexión UDP
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName("172.31.115.139"); // Cambiar a la IP del servidor
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Evento para el botón de consulta
        consultaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentId = idField.getText().trim();

                if (!studentId.isEmpty()) {
                    // Enviar la consulta al servidor
                    try {
                        sendRequest(studentId);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    resultArea.setText("Por favor, ingrese un ID válido.");
                }
            }
        });
    }

    // Método para enviar la consulta al servidor
    private void sendRequest(String studentId) throws IOException {
        // Enviar el mensaje con el ID del estudiante al servidor
        byte[] sendData = studentId.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
        socket.send(sendPacket);

        // Preparar para recibir la respuesta del servidor
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        // Convertir la respuesta del servidor en una cadena
        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
        displayResult(response);
    }

    // Método para mostrar la respuesta del servidor en el área de texto
    private void displayResult(String response) {
        // Usamos el método procesarEstudiante para mostrar los datos formateados
        String processedMessage = procesarEstudiante(response);
        resultArea.setText(processedMessage); // Mostrar los resultados procesados en el JTextArea
    }

    // Método para procesar el mensaje del servidor con la estructura
    // "Estudiante{id=1, nombre='Ana López', ...}"
    public static String procesarEstudiante(String mensaje) {
        // Eliminar la parte inicial "Estudiante{" y la parte final "}"
        mensaje = mensaje.replace("Estudiante{", "").replace("}", "");
    
        // Separar los diferentes campos por coma
        String[] campos = mensaje.split(", ");
    
        StringBuilder result = new StringBuilder();
    
        for (String campo : campos) {
            // Dividir cada campo en clave y valor por el carácter '='
            String[] claveValor = campo.split("=");
    
            if (claveValor.length == 2) {
                String clave = claveValor[0].trim(); // Clave (nombre del atributo)
                String valor = claveValor[1].trim(); // Valor del atributo
                
                // Si la clave es "gratuidad", cambiamos "true" a "Sí" y "false" a "No"
                if (clave.equalsIgnoreCase("gratuidad")) {
                    valor = valor.equals("true") ? "Sí" : (valor.equals("false") ? "No" : valor);
                }
                
                // Añadir los valores clave: valor al StringBuilder
                result.append(clave).append(": ").append(valor).append("\n");
            }
        }
    
        // Si no se encuentra ningún estudiante, mostramos un mensaje adecuado
        if (result.toString().isEmpty()) {
            return "No se encontró estudiante con este ID.";
        } else {
            return result.toString();
        }
    }
    public static void main(String[] args) {
        // Crear y mostrar la GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClienteGUI().setVisible(true);
            }
        });
    }
}
