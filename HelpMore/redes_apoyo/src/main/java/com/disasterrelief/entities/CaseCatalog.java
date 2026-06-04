package com.disasterrelief.entities;

/**
 * Define un tipo de caso de emergencia e indica si puede ser
 * atendido directamente por helpers voluntarios.
 * Los casos de alta complejidad se escalan a las autoridades.
 */
public class CaseCatalog {

    private static int idCounter = 1;

    private final int    id;
    private       String caseType;
    private       boolean handleableByHelper;
    private       String description;

    public CaseCatalog(String caseType, boolean handleableByHelper, String description) {
        this.id                 = idCounter++;
        this.caseType           = caseType;
        this.handleableByHelper = handleableByHelper;
        this.description        = description;
    }

    /**
     * Imprime si este tipo de caso puede ser gestionado por helpers.
     */
    public void evaluateHandlability() {
        if (handleableByHelper) {
            System.out.println("El caso '" + caseType + "' puede ser atendido por helpers.");
        } else {
            System.out.println("El caso '" + caseType
                    + "' requiere autoridades competentes. No apto para helpers.");
        }
    }

    // ── Getters / Setters ───────────────────────────────────────────────────

    public int     getId()                           { return id; }
    public String  getCaseType()                     { return caseType; }
    public void    setCaseType(String caseType)      { this.caseType = caseType; }
    public boolean isHandleableByHelper()            { return handleableByHelper; }
    public void    setHandleableByHelper(boolean h)  { this.handleableByHelper = h; }
    public String  getDescription()                  { return description; }
    public void    setDescription(String d)          { this.description = d; }

    @Override
    public String toString() {
        return "CaseCatalog{id=" + id + ", caseType='" + caseType
                + "', handleableByHelper=" + handleableByHelper + "}";
    }
}
