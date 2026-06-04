package com.disasterrelief.repository;

import com.disasterrelief.entities.CaseCatalog;
import com.disasterrelief.entities.Database;

/**
 * Repositorio de acceso a datos para el catalogo de casos de emergencia.
 */
public class CatalogRepository {

    private final Database db = Database.getInstance();

    public CaseCatalog getCatalog() {
        return db.getCaseCatalog();
    }

    public boolean isHandleableByHelper() {
        return db.getCaseCatalog().isHandleableByHelper();
    }

    public void setHandleableByHelper(boolean handleable) {
        db.getCaseCatalog().setHandleableByHelper(handleable);
        System.out.println("Atendibilidad por helpers actualizada a: " + handleable);
    }
}
