package br.com.memocellcio.http;

import java.util.List;

public class Api {

    private List<Card> listaCartas;

    public List<Card> getListaCartas() {
        return listaCartas;
    }

    public void setListaCartas(List<Card> listaCartas) {
        this.listaCartas = listaCartas;
    }
}
