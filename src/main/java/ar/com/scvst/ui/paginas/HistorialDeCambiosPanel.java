/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.scvst.ui.paginas;

import ar.com.scvst.server.bean.Commit;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tserra
 */
@Route(value = "historialCambios", layout = MainView.class)
@PageTitle("Historial de cambios")
public class HistorialDeCambiosPanel extends Div {

    private final SistemaServicio sistemaServicio;
    private final ScvServicio scvServicio;
    private final Grid<Commit> grilla = new Grid<>(Commit.class, false);
    private Perfil perfil;
    private Commit commitSeleccionado;
    private Button verCambios;

    public HistorialDeCambiosPanel(@Autowired SistemaServicio sistemaServicio, @Autowired ScvServicio scvServicio) {
        this.sistemaServicio = sistemaServicio;
        this.scvServicio = scvServicio;
        perfil = sistemaServicio.obtnerPefil("tserra");
        add(crearGrilla());
        generarBotonera();
        add(verCambios);
    }

    private void generarBotonera() {
        verCambios = new Button("Ver cambio", event -> {
            verInformacionCambio();
        });
    }

    private Component crearGrilla() {
        Div wrapper = new Div();
        wrapper.setId("grillaHistorialCambios");
        wrapper.setHeightFull();
        grilla.addColumn(new LocalDateTimeRenderer<>(
                Commit::getFecha,
                "dd/MM/yyyy HH:mm:ss"))
                .setHeader("Fecha")
                .setAutoWidth(true);
        grilla.addColumn("autor").setAutoWidth(true);
        grilla.addColumn("mensaje").setAutoWidth(true);
        grilla.addSelectionListener(evento -> {
            if (evento.getFirstSelectedItem().isPresent()) {
                wrapper.setHeight("150px");
            } else {
                wrapper.setHeightFull();
            }
        });

        grilla.addSelectionListener(evento -> {
            if (evento.getFirstSelectedItem().isPresent()) {
                commitSeleccionado = evento.getFirstSelectedItem().get();
                verCambios.setEnabled(true);
            } else {
                verCambios.setEnabled(false);
            }
        });

        if (perfil != null) {

            grilla.setItems(scvServicio.obtenerHistorialCommits(perfil
                    .getProyectoActual()
                    .getConfiguracion())
            );
        }

        wrapper.add(grilla);

        return wrapper;
    }

    private void verInformacionCambio() {
        Dialog dialog = new Dialog();
        String cambios = scvServicio.obtenerCambiosCommit(perfil.getProyectoActual().getConfiguracion(), commitSeleccionado.getId());
        TextArea textArea = new TextArea();
        textArea.setPlaceholder(cambios);
        textArea.setSizeFull();
        dialog.add(textArea);
        dialog.setWidth("800px");
        dialog.setHeight("600px");
        dialog.open();

    }
}
