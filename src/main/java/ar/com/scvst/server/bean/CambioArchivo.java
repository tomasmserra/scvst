package ar.com.scvst.server.bean;

/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
public class CambioArchivo {
    
    public enum Estado{
        MODIFICADO,
        AGREGADO,
        NO_COMPROBADO
    }
    
    private String nombre;
    private Estado estado;

    public CambioArchivo() {
    }

    public CambioArchivo(String nombre, Estado estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "CambioArchivo{" + "nombre=" + nombre + ", estado=" + estado + '}';
    }

}
