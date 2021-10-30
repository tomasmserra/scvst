/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.scvst.server.bean;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
public class Configuracion {
    
    public enum TipoRepositorio {
        BitBucket,
        GitHub,
        GitLab,
        GoogleDrive,
        DropBox
    }
    private String ubicacionLocal;
    private transient Repository repositorioLocal;
    private String ubicacionRemota;
    private transient TipoRepositorio tipoRepositorioRemoto;
    private transient Git git;
    private transient CredentialsProvider credenciales;
    private String usuario;
    private String clave;

    public Configuracion() {
    }
  
    public Configuracion(String ubicacionLocal, String ubicacionRemota) throws IOException {
        this.ubicacionLocal = ubicacionLocal;
        this.ubicacionRemota = ubicacionRemota;
        this.repositorioLocal = new FileRepository(ubicacionLocal + "/.git");
        git = new Git(repositorioLocal);
    }

    public Configuracion(String ubicacionLocal, String ubicacionRemota, TipoRepositorio tipoRepositorioRemoto) throws IOException {
        this.ubicacionLocal = ubicacionLocal;
        this.ubicacionRemota = ubicacionRemota;
        this.tipoRepositorioRemoto = tipoRepositorioRemoto;
        this.repositorioLocal = new FileRepository(ubicacionLocal + "/.git");
        git = new Git(repositorioLocal);
    }

    public Configuracion(String ubicacionLocal, String ubicacionRemota, String usuario, String clave, TipoRepositorio tipoRepositorioRemoto) throws IOException {
        this.ubicacionLocal = ubicacionLocal;
        this.ubicacionRemota = ubicacionRemota;
        this.tipoRepositorioRemoto = tipoRepositorioRemoto;
        this.repositorioLocal = new FileRepository(ubicacionLocal + "/.git");
        if (StringUtils.isNotBlank(clave) && StringUtils.isNoneBlank(usuario)) {
            this.usuario = usuario;
            this.clave = clave;
            credenciales = new UsernamePasswordCredentialsProvider(this.usuario, this.clave);
        }
        git = new Git(repositorioLocal);
    }

    public String getUbicacionLocal() {
        return ubicacionLocal;
    }

    public void setUbicacionLocal(String ubicacionLocal) {
        this.ubicacionLocal = ubicacionLocal;
    }

    public Repository getRepositorioLocal() {
        return repositorioLocal;
    }

    public void setRepositorioLocal(Repository repositorioLocal) {
        this.repositorioLocal = repositorioLocal;
    }

    public String getUbicacionRemota() {
        return ubicacionRemota;
    }

    public void setUbicacionRemota(String ubicacionRemota) {
        this.ubicacionRemota = ubicacionRemota;
    }

    public TipoRepositorio getTipoRepositorioRemoto() {
        return tipoRepositorioRemoto;
    }

    public void setTipoRepositorioRemoto(TipoRepositorio tipoRepositorioRemoto) {
        this.tipoRepositorioRemoto = tipoRepositorioRemoto;
    }

    public Git getGit() {
        return git;
    }

    public void setGit(Git git) {
        this.git = git;
    }

    public CredentialsProvider getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(CredentialsProvider credenciales) {
        this.credenciales = credenciales;
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

    @Override
    public String toString() {
        return "Configuracion{" + "ubicacionLocal=" + ubicacionLocal + ", repositorioLocal=" + repositorioLocal + ", ubicacionRemota=" + ubicacionRemota + ", tipoRepositorioRemoto=" + tipoRepositorioRemoto + ", git=" + git + ", credenciales=" + credenciales + ", usuario=" + usuario + ", clave=" + clave + '}';
    }
    
    

}
