package com.alura.screenmatch.excepcion;

public class ErrorEnConversionDeDuracionExcepcion extends RuntimeException {
    private String mensaje;

    public ErrorEnConversionDeDuracionExcepcion(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMessage() { return this.mensaje;}
}
