package com.example.brunocad.desenhos;

import com.example.brunocad.utils.CADConstants;

import java.util.ArrayList;
import java.util.List;

public class Retangulo extends Desenho {

    public Retangulo(long id, float left, float top, float right, float bottom, int cor) {
        super(id, CADConstants.tiposDesenhos.RETANGULO, cor);

        List<Float> pontos = new ArrayList<>();

        pontos.add(left);
        pontos.add(top);
        pontos.add(right);
        pontos.add(bottom);

        setPontos(pontos);
        configPaint(true, true, true);
    }
}
