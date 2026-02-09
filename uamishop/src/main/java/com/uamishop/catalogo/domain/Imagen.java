package com.uamishop.catalogo.domain;

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
}



