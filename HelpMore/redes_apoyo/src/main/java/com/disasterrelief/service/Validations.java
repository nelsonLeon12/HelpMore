package com.disasterrelief.service;

import com.disasterrelief.entities.Admin;
import com.disasterrelief.entities.AnonymousUser;
import com.disasterrelief.entities.HotlineManager;
import com.disasterrelief.entities.User;
import com.disasterrelief.service.interfaces.ValidationsServiceIMPL;

public class Validations implements ValidationsServiceIMPL {
/**
como debemos verificar la presencia de una hotline, usamos un metodo creado en la clase

 */

HotlineManager hline = new HotlineManager();
    @Override
    public void validateAdmin(Admin admin) throws Exception{
        if(admin.getName()==null&&admin.getName().isEmpty()){throw new Exception("Error, nombre INVALIDO");}
        if(admin.getId()<=0){throw new Exception("Error, coigo INVALIDO");}
        if(!hline.autoValidation()){throw new Exception("Hotline no valido");}
        if(admin.getZone()==null&&admin.getZone().isEmpty()){throw new Exception("Zona no valida");}
    }

    @Override
    public void validate(AnonymousUser anon) {

    }

    @Override
    public void valiadateUser(User user) {

    }
}
