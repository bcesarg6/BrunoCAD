package com.example.brunocad.desenhos;

import com.example.brunocad.utils.CADConstants;

import java.util.ArrayList;
import java.util.List;

public class Triangulo extends Desenho {

    public Triangulo(long id,
                     float p1x, float p1y,
                     float p2x, float p2y,
                     float p3x, float p3y,
                     int cor) {

        super(id, CADConstants.tiposDesenhos.TRIANGULO, cor);

        List<Float> pontos = new ArrayList<>();

        pontos.add(p1x);
        pontos.add(p1y);
        pontos.add(p2x);
        pontos.add(p2y);
        pontos.add(p3x);
        pontos.add(p3y);

        setPontos(pontos);
        configPaint(true, true, true);
    }
}
