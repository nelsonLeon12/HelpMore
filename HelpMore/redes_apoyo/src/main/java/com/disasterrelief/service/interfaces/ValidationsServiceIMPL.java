package com.disasterrelief.service.interfaces;

import com.disasterrelief.entities.Admin;
import com.disasterrelief.entities.AnonymousUser;
import com.disasterrelief.entities.User;

public interface ValidationsServiceIMPL {


    /**
     *
     * Valida administrador
     */
    void validateAdmin(Admin admin) throws Exception;

    void validate(AnonymousUser anon);

    void valiadateUser(User user);
}