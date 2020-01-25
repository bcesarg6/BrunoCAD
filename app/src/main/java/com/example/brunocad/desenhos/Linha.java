package com.example.brunocad.desenhos;

import android.graphics.Paint;

import com.example.brunocad.utils.CADConstants;

import java.util.ArrayList;
import java.util.List;

public class Linha extends Desenho {

    public Linha(long id, float startX, float startY, float stopX, float stopY, int cor) {
        super(id, CADConstants.tiposDesenhos.LINHA, cor);

        List<Float> pontos = new ArrayList<>();

        pontos.add(startX);
        pontos.add(startY);
        pontos.add(stopX);
        pontos.add(stopY);

        setPontos(pontos);
        configPaint(true, true, true);
    }
}
