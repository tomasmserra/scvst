/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.scvst.server.servicio;

import ar.com.scvst.server.bean.CambioArchivo;
import ar.com.scvst.server.bean.Commit;
import ar.com.scvst.server.bean.Configuracion;
import java.util.List;

/**
 *
 * @author tserra
 */
public interface ScvServicio {
    
    /**
     * Clona el proyecto que se encontra en el repositorio en el directorio local
     * @param configuracion  configuración del sistema
     */
    public void clonarRepositorio(Configuracion configuracion);
    
    /**
     * Realiza el commit de los archivos enviados.
     * @param configuracion configuracion del sistema
     * @param archivos lista de archivos que se desean agregar en el commit
     * @param mensaje mensaje descriptivo que se desea agregar en el commit
     */
    public void commit(Configuracion configuracion, List<String> archivos, String mensaje);
    
    /**
     * Envia los commit realizado al repositorio
     * @param configuracion configuracion del sistema
     */
    public void push(Configuracion configuracion);
    
    /**
     * Obtiene los commit realizados en el repositorio
     * @param configuracion configuracion del sistema
     * @return Lista de string con los path de los archivos modificados
     */
    public List<String> pull(Configuracion configuracion);
    
    /**
     * Obtiene una lista de los archivos modificados
     * @param configuracion configuracion del sistema
     * @return Lista de String con los path de los archivos modificados
     */
    public List<CambioArchivo> obtenerArchivosModificados(Configuracion configuracion);
    
    /**
     * Inicia el repositorio el repositorio. 
     * @param direccionLocal Directorio local donde se encuentran los archivos.
     * @param repositorio Directorio donde se desea agregar el repositorio
     * @param repositorioEsNuevo indicar si el repositorio es nuevoo o no.
     * @param usuario usuario, si lo tiene
     * @param clave clave , si lo tiene
     * @return 
     */
    public Configuracion iniciar(String direccionLocal, String repositorio, boolean repositorioEsNuevo, String usuario, String clave);
    
    /**
     * Obtiene la lista del historial de commits
     * @param configuracion configuracion del sistema
     * @return Lista de commits
     */
    public List<Commit> obtenerHistorialCommits(Configuracion configuracion);
    
    public String obtenerCambiosCommit(Configuracion configuraacion, String hashID);
    
}
