/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.scvst.ui.paginas;

import ar.com.scvst.server.bean.CambioArchivo;
import ar.com.scvst.server.bean.Perfil;
import ar.com.scvst.server.servicio.ScvServicio;
import ar.com.scvst.server.servicio.SistemaServicio;
import ar.com.scvst.ui.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tserra
 */
@Route(value = "cambios/:cambiosID?/:action?(edit)", layout = MainView.class)
@PageTitle("Cambios")
public class CambiosPanel extends Div {

    private final SistemaServicio sistemaServicio;
    private final ScvServicio scvServicio;
    private final Grid<CambioArchivo> grilla = new Grid<>(CambioArchivo.class, false);
    private Perfil perfil;
    private Button guardarCambios;

    public CambiosPanel(@Autowired SistemaServicio sistemaServicio, @Autowired ScvServicio scvServicio) {
        this.sistemaServicio = sistemaServicio;
        this.scvServicio = scvServicio;
        perfil = sistemaServicio.obtnerPefil("tserra");
        add(crearGrilla());
        generarBotonera();
        add(guardarCambios);

    }

    private void generarBotonera() {
        guardarCambios = new Button("Guardar cambios", event -> {
            Dialog dialog = new Dialog();
            dialog.add(new Text("Cambios Guardados"));
            dialog.open();
            //verInformacionCambio();
        });
    }

    private Component crearGrilla() {
        Div wrapper = new Div();
        wrapper.setId("grillaProyecto");
        wrapper.setHeightFull();
        grilla.addColumn("nombre").setAutoWidth(true);
        grilla.addColumn("estado").setAutoWidth(true);
        grilla.addSelectionListener(evento -> {
            if (evento.getFirstSelectedItem().isPresent()) {

                wrapper.setHeight("150px");
            } else {
                wrapper.setHeightFull();
            }
        });

        if (perfil != null) {

            grilla.setItems(scvServicio.obtenerArchivosModificados(perfil
                    .getProyectoActual()
                    .getConfiguracion())
            );
        }

        wrapper.add(grilla);

        return wrapper;
    }
}
