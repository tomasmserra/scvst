package ar.com.scvst.server.bean;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
public class Perfil {
    private String usuario;
    private String clave;
    private String descripcion;
    private Date fechaCreacion;
    private Desarrollador desarrollador;
    private Proyecto proyectoActual;
    private List<Proyecto> poyectos;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.usuario);
        hash = 47 * hash + Objects.hashCode(this.clave);
        hash = 47 * hash + Objects.hashCode(this.descripcion);
        hash = 47 * hash + Objects.hashCode(this.fechaCreacion);
        hash = 47 * hash + Objects.hashCode(this.desarrollador);
        hash = 47 * hash + Objects.hashCode(this.poyectos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Perfil other = (Perfil) obj;
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        if (!Objects.equals(this.fechaCreacion, other.fechaCreacion)) {
            return false;
        }
        return true;
    }
    
    

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Desarrollador getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(Desarrollador desarrollador) {
        this.desarrollador = desarrollador;
    }

    public List<Proyecto> getPoyectos() {
        return poyectos;
    }

    public void setPoyectos(List<Proyecto> poyectos) {
        this.poyectos = poyectos;
    }

    public Proyecto getProyectoActual() {
        return proyectoActual;
    }

    public void setProyectoActual(Proyecto proyectoActual) {
        this.proyectoActual = proyectoActual;
    }
    
    @Override
    public String toString() {
        return "Perfil{" + "usuario=" + usuario + ", clave=" + clave + ", descripcion=" + descripcion + ", fechaCreacion=" + fechaCreacion + ", desarrollador=" + desarrollador + ", poyectos=" + poyectos + '}';
    }
 
}
