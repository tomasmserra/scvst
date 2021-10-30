package ar.com.scvst.server.servicio.impl;

import ar.com.scvst.server.bean.Configuracion;
import ar.com.scvst.server.bean.Desarrollador;
import ar.com.scvst.server.bean.Perfil;
import ar.com.scvst.server.bean.Proyecto;
import ar.com.scvst.server.bean.Sistema;
import ar.com.scvst.server.dao.DaoSistema;
import ar.com.scvst.server.servicio.ScvServicio;
import ar.com.scvst.server.servicio.SistemaServicio;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;

/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
@Service("sistemaServicio")
public class SistemaServicioImpl implements SistemaServicio {

    @Autowired
    private DaoSistema daoSistema;
    @Autowired
    private ScvServicio scvServicio;

    public static void main(String[] args) throws IOException, GitAPIException {
        SistemaServicioImpl sistemaServicio = new SistemaServicioImpl();
        Sistema sistema = sistemaServicio.obtenerDatosSistema();
        System.out.println(sistema);
        sistema.setUltimaModificacion(new Date());
        Desarrollador desarrollador = new Desarrollador();
        desarrollador.setNombre("Tomás Matías");
        desarrollador.setApellido("Serra");
        desarrollador.setMail("tomas@serra.com.ar");

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto en GoogleDrive");
        proyecto.setDescripcion("Ejemplo de uso");
        Configuracion configuracion = new Configuracion(
                "/Users/tserra/Documents/Tesis/pruebaProyectos/proyecto1",
                "/Users/tserra/Desktop/tesisPruebaProyecto/proyecto1/proyecto1.git",
                Configuracion.TipoRepositorio.GoogleDrive);
        proyecto.setConfiguracion(configuracion);
        Perfil perfil = new Perfil();
        perfil.setUsuario("tserra");
        perfil.setClave("hola2020");
        perfil.setDescripcion("Demostración de SCVST");
        perfil.setPoyectos(Arrays.asList(proyecto));
        perfil.setDesarrollador(desarrollador);
        sistema.setPerfiles(Arrays.asList(perfil));
        sistemaServicio.grabarSistema(sistema);
    }

    @Override
    public Sistema obtenerDatosSistema() {
        return daoSistema.obtenerParametrosSistema();
    }

    @Override
    public void grabarSistema(Sistema sistema) {
        daoSistema.grabarParametrosSistema(sistema);
    }

    @Override
    public boolean puedeIniciarSesion(String usuario, String clave) {
        Sistema sistema = obtenerDatosSistema();
        if (sistema == null) {
            return false;
        }

        Perfil perfil = sistema.getPerfiles().stream()
                .filter(p -> p.getUsuario().equalsIgnoreCase(usuario))
                .findFirst()
                .orElse(null);

        return perfil != null ? perfil.getClave().equals(clave) : false;
    }

    @Override
    public Perfil obtnerPefil(String usuario) {
        Sistema sistema = obtenerDatosSistema();
        if (sistema == null) {
            return null;
        }

        Perfil perfil = sistema.getPerfiles().stream()
                .filter(p -> p.getUsuario().equalsIgnoreCase(usuario))
                .findFirst()
                .orElse(null);
        if (perfil != null) {
            perfil.getPoyectos().stream().forEach((proyecto) -> {
                Configuracion conf = proyecto.getConfiguracion();
                proyecto.setConfiguracion(scvServicio.iniciar(
                        conf.getUbicacionLocal(),
                        conf.getUbicacionRemota(),
                        false,
                        conf.getUsuario(),
                        conf.getClave()));
            });

            Configuracion conf = perfil.getProyectoActual().getConfiguracion();
            perfil.getProyectoActual().setConfiguracion(scvServicio.iniciar(
                    conf.getUbicacionLocal(),
                    conf.getUbicacionRemota(),
                    false,
                    conf.getUsuario(),
                    conf.getClave()));
        }

        return perfil;
    }

    @Override
    public boolean grabarPerfil(Perfil perfil) {
        Sistema sistema = obtenerDatosSistema();
        if (sistema == null) {
            return false;
        }

        sistema.getPerfiles().add(perfil);
        grabarSistema(sistema);

        return true;
    }

    @Override
    public boolean modificarPerfil(Perfil perfil) {
        Sistema sistema = obtenerDatosSistema();
        if (sistema == null) {
            return false;
        }

        sistema.getPerfiles().remove(perfil);
        sistema.getPerfiles().add(perfil);
        grabarSistema(sistema);

        return true;
    }

    @Override
    public boolean eliminarPerfil(Perfil perfil) {
        Sistema sistema = obtenerDatosSistema();
        if (sistema == null) {
            return false;
        }

        sistema.getPerfiles().remove(perfil);
        grabarSistema(sistema);

        return true;
    }

    @Override
    public boolean generarZip(String nombre, String path) {
        try {
            ZipUtil.pack(new File(path), new File(nombre));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

}
