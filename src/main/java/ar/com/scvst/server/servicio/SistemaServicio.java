package ar.com.scvst.server.servicio;

import ar.com.scvst.server.bean.Perfil;
import ar.com.scvst.server.bean.Sistema;

/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
public interface SistemaServicio {

    public Sistema obtenerDatosSistema();
    
    public void grabarSistema(Sistema sistema);
    
    public boolean puedeIniciarSesion(String usuario, String clave);
    
    public Perfil obtnerPefil(String usuario);
    
    public boolean grabarPerfil(Perfil perfil);
    
    public boolean modificarPerfil(Perfil perfil);
    
    public boolean eliminarPerfil(Perfil perfil);
    
    public boolean generarZip(String nombre, String path);

}
