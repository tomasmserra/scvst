package ar.com.scvst.server.dao.impl;

import ar.com.scvst.server.bean.Sistema;
import ar.com.scvst.server.dao.DaoSistema;
import ar.com.scvst.server.tools.JsonTool;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
@Component("daoSistema")
public class DaoSistemaImpl implements DaoSistema {

    private static final Logger LOG = Logger.getLogger(DaoSistemaImpl.class.getName());

    @Override
    public Sistema obtenerParametrosSistema() {
        InputStream iS = null;
        try {
            iS = DaoSistemaImpl.class.getResourceAsStream("/Sistema.json");
            StringWriter writer = new StringWriter();
            IOUtils.copy(iS, writer, Charset.defaultCharset());
            return JsonTool.convertirStringClase(writer.toString(), Sistema.class, null);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error al obtener el archivo de parametros del sistema", ex);
            return null;
        } finally {
            if (iS != null) {
                try {
                    iS.close();
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, "Error al serra el inputStream");
                }
            }
        }
    }

    @Override
    public void grabarParametrosSistema(Sistema sistema) {
        FileWriter writer = null;
        try {
            File fileToBeModified = new File("src/main/resources/Sistema.json");
            writer = new FileWriter(fileToBeModified);
            writer.write(JsonTool.convertirClaseString(sistema));
        } catch (IOException ex) {
            Logger.getLogger(DaoSistemaImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(DaoSistemaImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
