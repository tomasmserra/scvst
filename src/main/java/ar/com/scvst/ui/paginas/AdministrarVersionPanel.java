/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.scvst.ui.paginas;

import ar.com.scvst.server.bean.Perfil;
import ar.com.scvst.server.servicio.ScvServicio;
import ar.com.scvst.server.servicio.SistemaServicio;
import ar.com.scvst.ui.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tserra
 */
@Route(value = "administrarVersion", layout = MainView.class)
@PageTitle("Version")
public class AdministrarVersionPanel extends VerticalLayout {

    private final SistemaServicio sistemaServicio;
    private final ScvServicio scvServicio;
    private Anchor download;
    private Button downloadButton = new Button("Generar Versión");
    private Perfil perfil;

    public AdministrarVersionPanel(@Autowired SistemaServicio sistemaServicio, @Autowired ScvServicio scvServicio) {
        this.scvServicio = scvServicio;
        this.sistemaServicio = sistemaServicio;
        perfil = sistemaServicio.obtnerPefil("tserra");
        download = new Anchor();
        download.add(downloadButton);
        download.getElement().setAttribute("download", "true");
        downloadButton.addClickListener(e -> {
            String zip = perfil.getProyectoActual().getConfiguracion().getUbicacionLocal() + ".zip";
            if (sistemaServicio.generarZip(zip, perfil.getProyectoActual().getConfiguracion().getUbicacionLocal())) {
                
                File file = new File(zip);
                StreamResource resource = getStreamResource("Version.zip", file);
                download.setHref(resource);
                final StreamRegistration regn = VaadinSession.getCurrent().getResourceRegistry().registerResource(resource);
                UI.getCurrent().getPage().setLocation(regn.getResourceUri());
                
                file.delete();
            }
        });
        setAlignItems(Alignment.CENTER);
        add(download);
    }

    public static StreamResource getStreamResource(String fileName, File file) {
        return new StreamResource(fileName, () -> {
            try {
                return new FileInputStream(file);
            } catch (IOException e) {
                System.out.println("Error in getStreamResource: ");
                e.printStackTrace();
            }
            return null;
        });
    }

}
