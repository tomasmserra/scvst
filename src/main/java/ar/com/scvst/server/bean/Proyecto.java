/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.scvst.server.bean;

/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
public class Proyecto {
    private String nombre;
    private String descripcion;
    private Configuracion configuracion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuracion configuracion) {
        this.configuracion = configuracion;
    }
    
    @Override
    public String toString() {
        return "Proyecto{" + "nombre=" + nombre + ", descripcion=" + descripcion + ", configuracion=" + configuracion + '}';
    }
}
