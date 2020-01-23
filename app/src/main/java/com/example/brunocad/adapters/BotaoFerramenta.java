package com.example.brunocad.adapters;

import android.graphics.drawable.Drawable;

public class BotaoFerramenta {

    private int id;
    private String nome;
    private Drawable icone;

    public BotaoFerramenta(int id, String nome, Drawable icone) {
        this.id = id;
        this.nome = nome;
        this.icone = icone;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Drawable getIcone() {
        return icone;
    }
}
