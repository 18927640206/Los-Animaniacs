package com.uamishop.catalogo.domain;

import jakarta.persistence.Embeddable;

@Embeddable // <--- ESTO ES LO QUE FALTA
public class Imagen {
    private String url;
    private String descripcion;

    protected Imagen() {} // Requerido por JPA

    public Imagen(String url, String descripcion) {
        this.url = url;
        this.descripcion = descripcion;
    }

    // Getters
    public String getUrl() { return url; }
    public String getDescripcion() { return descripcion; }
}

/*package com.uamishop.catalogo.domain;

public class Imagen {
	private final String url;
    private final String textoAlternativo;
    private final int orden;
    
    public Imagen(String url, String textoAlternativo, int orden) {
        if (url == null || (!url.startsWith("http://") && !url.startsWith("https://"))) {
            throw new IllegalArgumentException("URL inválida");
        }
        this.url = url;
        this.textoAlternativo = textoAlternativo;
        this.orden = orden;
    }
    
    public String getUrl() { return url; }
}*/



