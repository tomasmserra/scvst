/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.scvst.server.dao;

import ar.com.scvst.server.bean.Sistema;

/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
public interface DaoSistema {
    
    public Sistema obtenerParametrosSistema();
    
    public void grabarParametrosSistema(Sistema sistema);
}
