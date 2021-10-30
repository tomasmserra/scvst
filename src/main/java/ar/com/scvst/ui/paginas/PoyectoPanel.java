/*
 * Copyright (c) 2021, Serra Tomás Matías (STecno).
 * All rights reserved.
 */
package ar.com.scvst.ui.paginas;

import ar.com.scvst.server.bean.Configuracion;
import ar.com.scvst.server.bean.Perfil;
import ar.com.scvst.server.bean.Proyecto;
import ar.com.scvst.server.servicio.ScvServicio;
import ar.com.scvst.server.servicio.SistemaServicio;
import ar.com.scvst.ui.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
@Route(value = "protectos/:proyectoID?/:action?(edit)", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Proyectos")
public class PoyectoPanel extends Div {

    private static final Logger LOG = Logger.getLogger(PoyectoPanel.class.getName());

    private final SistemaServicio sistemaServicio;
    private final ScvServicio scvServicio;
    private final Grid<Proyecto> grilla = new Grid<>(Proyecto.class, false);
    private final Binder<Proyecto> binderProyecto = new Binder<>();
    private final Binder<Configuracion> binderConfiguracion = new Binder<>();
    private final SplitLayout splitLayout = new SplitLayout();
    private Perfil perfil;
    private FormLayout formLayout;
    private Button guardar;
    private Button cancelar;
    private Button nuevo;
    private Proyecto proyectoSelecccionado;

    public PoyectoPanel(@Autowired SistemaServicio sistemaServicio, @Autowired ScvServicio scvServicio) {
        this.scvServicio = scvServicio;
        this.sistemaServicio = sistemaServicio;
        this.formLayout = null;
        this.guardar = null;
        this.cancelar = null;
        this.nuevo = null;
        this.proyectoSelecccionado = null;
        perfil = sistemaServicio.obtnerPefil("tserra");
        splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        splitLayout.setSizeFull();
        splitLayout.addToPrimary(crearGrilla());
        splitLayout.addToSecondary(crearEditor());
        cambiarEstadoVisible(false);
        add(splitLayout);
    }

    private Component crearGrilla() {
        Div wrapper = new Div();
        wrapper.setId("grillaProyecto");
        wrapper.setHeightFull();
        grilla.addColumn("nombre").setAutoWidth(true);
        grilla.addColumn("descripcion").setAutoWidth(true);
        grilla.addSelectionListener(evento -> {
            if (evento.getFirstSelectedItem().isPresent()) {
                proyectoSelecccionado = evento.getFirstSelectedItem().get();
                wrapper.setHeight("150px");
                cambiarEstadoVisible(true);
                binderProyecto.readBean(proyectoSelecccionado);
                binderConfiguracion.readBean(proyectoSelecccionado.getConfiguracion());
            } else {
                cambiarEstadoVisible(false);
                wrapper.setHeightFull();
            }
        });

        if (perfil != null) {
            grilla.setItems(perfil.getPoyectos());
        }

        wrapper.add(grilla);

        return wrapper;
    }

    private void cambiarEstadoVisible(boolean valor) {
        formLayout.setVisible(valor);
        guardar.setVisible(valor);
        cancelar.setVisible(valor);
    }

    private Component crearEditor() {
        Div editorLayoutDiv = new Div();
        //editorLayoutDiv.setClassName("flex flex-col");

        Div editorDiv = new Div();
        //editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        formLayout = new FormLayout();
        TextField nombre = new TextField("Nombre");
        TextField descripcion = new TextField("Descripcion");
        TextField ubicacionLocal = new TextField("Ubicación local");
        TextField ubicacionRemota = new TextField("Ubicación remota");
        TextField usuario = new TextField("Usuario");
        TextField clave = new TextField("Clave");
        ComboBox<Configuracion.TipoRepositorio> tipoRepositorio = new ComboBox<>("Tipo repositorio");
        tipoRepositorio.setItems(Configuracion.TipoRepositorio.values());
        formLayout.add(nombre, descripcion, tipoRepositorio, ubicacionLocal, ubicacionRemota, usuario, clave);

        binderProyecto.forField(nombre)
                .bind(Proyecto::getNombre, Proyecto::setNombre);
        binderProyecto.forField(descripcion)
                .bind(Proyecto::getDescripcion, Proyecto::setDescripcion);
        binderConfiguracion.forField(ubicacionLocal)
                .bind(Configuracion::getUbicacionLocal, Configuracion::setUbicacionLocal);
        binderConfiguracion.forField(ubicacionRemota)
                .bind(Configuracion::getUbicacionRemota, Configuracion::setUbicacionRemota);
        binderConfiguracion.forField(usuario)
                .bind(Configuracion::getUsuario, Configuracion::setUsuario);
        binderConfiguracion.forField(clave)
                .bind(Configuracion::getClave, Configuracion::setClave);
        binderConfiguracion.forField(tipoRepositorio)
                .bind(Configuracion::getTipoRepositorioRemoto, Configuracion::setTipoRepositorioRemoto);

        editorDiv.add(formLayout);
        editorDiv.add(crearBotoneraFormulario());
        return editorLayoutDiv;
    }

    private void actualizarGrilla() {
        perfil = sistemaServicio.obtnerPefil("tserra");
        grilla.setItems(perfil.getPoyectos());
        cambiarEstadoVisible(false);
    }

    private Component crearBotoneraFormulario() {
        HorizontalLayout botonera = new HorizontalLayout();
        botonera.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        botonera.setSpacing(true);
        cancelar = new Button("Cancelar", event -> {
            cambiarEstadoVisible(false);
        });
        cancelar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        guardar = new Button("Guardar", event -> {
            try {
                cambiarEstadoVisible(true);
                Proyecto proyecto = new Proyecto();
                proyecto.setConfiguracion(new Configuracion());
                binderProyecto.writeBean(proyectoSelecccionado);
                binderConfiguracion.writeBean(proyectoSelecccionado.getConfiguracion());
                proyectoSelecccionado.setConfiguracion(scvServicio.iniciar(
                        proyectoSelecccionado.getConfiguracion().getUbicacionLocal(),
                        proyectoSelecccionado.getConfiguracion().getUbicacionRemota(),
                        false,
                        proyectoSelecccionado.getConfiguracion().getUsuario(),
                        proyectoSelecccionado.getConfiguracion().getClave()
                ));
                sistemaServicio.modificarPerfil(perfil);
                actualizarGrilla();
            } catch (ValidationException ex) {
                LOG.log(Level.SEVERE, "Error al guardar el proyecto");
            }
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        nuevo = new Button("Nuevo", event -> {
            cambiarEstadoVisible(true);
            proyectoSelecccionado = new Proyecto();
            proyectoSelecccionado.setConfiguracion(new Configuracion());
            binderProyecto.readBean(proyectoSelecccionado);
            binderConfiguracion.readBean(proyectoSelecccionado.getConfiguracion());
            perfil.getPoyectos().add(proyectoSelecccionado);
        });
        nuevo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        botonera.add(nuevo, guardar, cancelar);

        return botonera;
    }
}
