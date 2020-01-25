package com.example.brunocad.desenhos;

import com.example.brunocad.utils.CADConstants;

import java.util.ArrayList;
import java.util.List;

public class Circulo extends Desenho {

    public Circulo(long id, float centerX, float centerY, float radius, int cor) {
        super(id, CADConstants.tiposDesenhos.CIRCULO, cor);

        List<Float> pontos = new ArrayList<>();

        pontos.add(centerX);
        pontos.add(centerY);
        pontos.add(radius);

        setPontos(pontos);
        configPaint(true, true, true);
    }
}
