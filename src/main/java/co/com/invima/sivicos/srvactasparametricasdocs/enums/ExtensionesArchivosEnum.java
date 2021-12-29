package co.com.invima.sivicos.srvactasparametricasdocs.enums;

public enum ExtensionesArchivosEnum {
    DOC("application/msword", ".doc"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),
    XLS("application/vnd.ms-excel", ".xls"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");



    private String tipoContenido;
    private String extension;

    /**
     *
     * @param tipoContenido
     * @param extension
     */
    private ExtensionesArchivosEnum(String tipoContenido, String extension) {
        this.tipoContenido = tipoContenido;
        this.extension = extension;
    }

    /**
     *
     * @param tipoContenido
     * @return
     */
    static public ExtensionesArchivosEnum obtenerExtensionPorTipoContenido(String tipoContenido) {
        tipoContenido = tipoContenido.toLowerCase();
        ExtensionesArchivosEnum[] extensionesArchivos = ExtensionesArchivosEnum.values();
        ExtensionesArchivosEnum extensioArchivoEncontrado = null;
        for (ExtensionesArchivosEnum extensionArchivo : extensionesArchivos) {
            String valorTipoContenido = extensionArchivo.getTipoContenido().toLowerCase();
            if (valorTipoContenido.equals(tipoContenido)) {
                extensioArchivoEncontrado = extensionArchivo;
                break;
            } //End if
        } //End for

        return extensioArchivoEncontrado;
    }

    /**
     *
     * @param tipoContenido
     */
    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    /**
     *
     * @return
     */
    public String getTipoContenido() {
        return tipoContenido;
    }

    /**
     *
     * @param extension
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     *
     * @return
     */
    public String getExtension() {
        return extension;
    }

}