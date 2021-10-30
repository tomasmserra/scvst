package ar.com.scvst.server.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
public class JsonTool {

    private static final Logger LOG = Logger.getLogger(JsonTool.class.getName());

    public static <T> T convertirStringClase(String dato, Class<T> tipoClase, Type type) {
        try {
            return StringUtils.isNotBlank(dato) ? crearJSon().fromJson(dato, tipoClase != null ? tipoClase : type) : null;
        } catch (JsonSyntaxException jse) {
            LOG.log(Level.SEVERE, "Error al convertir dato a clase");
        }
        return null;
    }

    public static String convertirClaseString(Object dato) {
        try {
            return dato != null ? crearJSon().toJson(dato) : null;
        } catch (JsonSyntaxException jse) {
            LOG.log(Level.SEVERE, "Error al convertir dato a clase");
        }
        return null;
    }

    public static Gson crearJSon() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

}
