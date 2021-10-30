package ar.com.scvst.server.bean;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
public class Sistema {
    private Date ultimaModificacion;
    private String nombreSistema;
    private String version;
    private List<Perfil> perfiles;

    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    public String getNombreSistema() {
        return nombreSistema;
    }

    public void setNombreSistema(String nombreSistema) {
        this.nombreSistema = nombreSistema;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Perfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<Perfil> perfiles) {
        this.perfiles = perfiles;
    }

    @Override
    public String toString() {
        return "Sistema{" + "ultimaModificacion=" + ultimaModificacion + ", nombreSistema=" + nombreSistema + ", version=" + version + ", perfiles=" + perfiles + '}';
    } 
}
