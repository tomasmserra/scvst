/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.scvst.server.bean;

import java.time.LocalDateTime;

/**
 *
 * @author tserra
 */
public class Commit {
    
    private String id;
    private LocalDateTime fecha;
    private String autor;
    private String mensaje;

    public Commit() {
    }

    public Commit(String id, LocalDateTime fecha, String autor, String mensaje) {
        this.id = id;
        this.fecha = fecha;
        this.autor = autor;
        this.mensaje = mensaje;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Commit{" + "id=" + id + ", fecha=" + fecha + ", autor=" + autor + ", mensaje=" + mensaje + '}';
    }
    
}
