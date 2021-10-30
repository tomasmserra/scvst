package ar.com.scvst.ui.main;

import ar.com.scvst.server.bean.Perfil;
import ar.com.scvst.server.bean.Proyecto;
import ar.com.scvst.server.servicio.SistemaServicio;
import ar.com.scvst.ui.paginas.AdministrarVersionPanel;
import ar.com.scvst.ui.paginas.CambiosPanel;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import ar.com.scvst.ui.paginas.HistorialDeCambiosPanel;
import ar.com.scvst.ui.paginas.PoyectoPanel;
import com.vaadin.flow.component.charts.model.HorizontalAlign;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "SCVST", shortName = "SCVST", enableInstallPrompt = false)
@Theme(themeFolder = "scvst")
public class MainView extends AppLayout {

    private final Tabs menu;
    private final SistemaServicio sistemaServicio;

    public MainView(@Autowired SistemaServicio sistemaServicio) {
        this.sistemaServicio = sistemaServicio;
        HorizontalLayout header = createHeader();
        menu = createMenuTabs();
        addToNavbar(createTopBar(header, menu));
    }

    private VerticalLayout createTopBar(HorizontalLayout header, Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.getThemeList().add("dark");
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(header, menu);
        return layout;
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setPadding(false);
        header.setSpacing(false);
        header.setWidthFull();
        header.setId("header");
        Image logo = new Image("images/logo.png", "SCVST logo");
        logo.setId("logo");
        header.add(logo);
        Avatar avatar = new Avatar();
        avatar.setId("avatar");
        header.add(new H1("SCVST"));
        header.add(crearBarraProyecto());
        header.add(avatar);

        return header;
    }

    private Component crearBarraProyecto() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setAlignItems(FlexComponent.Alignment.CENTER);
        Perfil perfil = sistemaServicio.obtnerPefil("tserra");
        ComboBox<Proyecto> seleccionProyectos = new ComboBox<>();
        hl.add(seleccionProyectos);
        seleccionProyectos.setItems(perfil.getPoyectos());
        seleccionProyectos.setItemLabelGenerator(Proyecto::getNombre);
        seleccionProyectos.setWidth("325px");
        if (perfil.getProyectoActual() != null) {
            seleccionProyectos.setValue(perfil.getProyectoActual());
        }

        seleccionProyectos.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                perfil.setProyectoActual(event.getValue());
                sistemaServicio.modificarPerfil(perfil);
            }
        });

        return hl;
    }

    private static Tabs createMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.getStyle().set("max-width", "100%");
        tabs.add(getAvailableTabs());
        return tabs;
    }

    private static Tab[] getAvailableTabs() {
        return new Tab[]{
            createTab("Proyectos", VaadinIcon.FOLDER_O, PoyectoPanel.class),
            createTab("Cambios", VaadinIcon.CLIPBOARD_TEXT, CambiosPanel.class),
            createTab("Historial de cambios", VaadinIcon.CLOCK, HistorialDeCambiosPanel.class),
            createTab("Version", VaadinIcon.ADD_DOCK, AdministrarVersionPanel.class)
        };
    }

    private static Tab createTab(String text, VaadinIcon vaadinIcon, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        RouterLink routeLink = new RouterLink(text, navigationTarget);
        VerticalLayout vL = new VerticalLayout();
        Icon icon = new Icon(vaadinIcon);
        vL.add(icon);
        vL.add(routeLink);
        vL.setAlignItems(FlexComponent.Alignment.CENTER);
        tab.add(vL);
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }
}
