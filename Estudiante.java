public class Estudiante {
    int id;
    String nombre;
    String telefono;
    String carrera;
    int semestre;
    boolean gratuidad;

    public Estudiante(int id, String nombre, String telefono, String carrera, int semestre, boolean gratuidad) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.carrera = carrera;
        this.semestre = semestre;
        this.gratuidad = gratuidad;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", carrera='" + carrera + '\'' +
                ", semestre=" + semestre +
                ", gratuidad=" + gratuidad +
                '}';
    }
}
