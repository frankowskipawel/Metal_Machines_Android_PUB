package com.pl.metalmachines.dao.enumDao.columnsNames;

public enum ParametersColumnNameDAO {
    PARAMETERS("PAR_ID",
            "PAR_ID_DEVICE",
            "PAR_MATERIAL",
            "PAR_GRUBOSC",
            "PAR_MODEL_DYSZY",
            "PAR_GAZ",
            "PAR_JSON_PARAMETERS");

    public String idParameters;
    public String idDevice;
    public String material;
    public String grubosc;
    public String modelDyszy;
    public String gaz;
    public String GSonParameters;

    ParametersColumnNameDAO(String idParameters,
                            String idDevice,
                            String material,
                            String grubosc,
                            String modelDyszy,
                            String gaz,
                            String GSonParameters) {
        this.idParameters = idParameters;
        this.idDevice = idDevice;
        this.material = material;
        this.grubosc = grubosc;
        this.modelDyszy = modelDyszy;
        this.gaz = gaz;
        this.GSonParameters = GSonParameters;
    }
}
