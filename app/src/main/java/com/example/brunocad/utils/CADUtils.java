package com.example.brunocad.utils;

import android.content.Context;

import com.example.brunocad.R;
import com.example.brunocad.adapters.BotaoFerramenta;

import java.util.ArrayList;
import java.util.List;

import static com.example.brunocad.utils.CADConstants.toolsID;

public class CADUtils {
    private static long nextDrawingId = -1;
    private static int nextColorIdx = 0;

    private static int[] colors = { R.color.roxo,
                                    R.color.indigo,
                                    R.color.petroleo,
                                    R.color.azul,
                                    R.color.verde,
                                    R.color.amarelo,
                                    R.color.laranja,
                                    R.color.vermelho,
                                    R.color.rosa };

    public static void resetNextDrawingId() {
        nextDrawingId = -1;
    }

    public static long getNextDrawingId() {
        return ++nextDrawingId;
    }


    public static void resetNextColorIdx() {
        nextColorIdx = 0;
    }

    public static int getNextColor() {
        int color = colors[nextColorIdx];

        nextColorIdx++;
        if (nextColorIdx >= colors.length) nextColorIdx = 0;

        return color;
    }

    public static List<BotaoFerramenta> getBotoesMenuCriar(Context context) {
        List<BotaoFerramenta> ferramentasCriar = new ArrayList<>();

        ferramentasCriar.add(new BotaoFerramenta(toolsID.LINE, "Linhas", context.getDrawable(R.drawable.ic_linha)));

        ferramentasCriar.add(new BotaoFerramenta(toolsID.TRIANGLE_STROKE, "Triângulos", context.getDrawable(R.drawable.ic_triangulo)));
        ferramentasCriar.add(new BotaoFerramenta(toolsID.TRIANGLE, "Triângulos Preenchidos", context.getDrawable(R.drawable.ic_triangulo_fill)));

        ferramentasCriar.add(new BotaoFerramenta(toolsID.RECTANGLE_STROKE, "Retângulos", context.getDrawable(R.drawable.ic_retangulo)));
        ferramentasCriar.add(new BotaoFerramenta(toolsID.RECTANGLE, "Retângulos Preenchidos", context.getDrawable(R.drawable.ic_retangulo_fill)));

        ferramentasCriar.add(new BotaoFerramenta(toolsID.CIRCLE_STROKE, "Círculos", context.getDrawable(R.drawable.ic_circulo)));
        ferramentasCriar.add(new BotaoFerramenta(toolsID.CIRCLE, "Círculos Preenchidos", context.getDrawable(R.drawable.ic_circulo_fill)));

        return ferramentasCriar;
    }

    public static List<BotaoFerramenta> getBotoesMenuEditar(Context context) {
        List<BotaoFerramenta> ferramentasEditar = new ArrayList<>();

        ferramentasEditar.add(new BotaoFerramenta(toolsID.HELP, "Ajuda", context.getDrawable(R.drawable.ic_ajuda)));
        ferramentasEditar.add(new BotaoFerramenta(toolsID.UNDO, "Undo", context.getDrawable(R.drawable.ic_undo)));
        ferramentasEditar.add(new BotaoFerramenta(toolsID.CLEAR, "Limpar", context.getDrawable(R.drawable.ic_clear)));
        ferramentasEditar.add(new BotaoFerramenta(toolsID.TRANSLATION, "Translação", context.getDrawable(R.drawable.ic_translacao)));
        ferramentasEditar.add(new BotaoFerramenta(toolsID.ROTATION, "Rotação", context.getDrawable(R.drawable.ic_rotacao)));
        ferramentasEditar.add(new BotaoFerramenta(toolsID.CHANGE_ESCALE, "Mudança de Escala", context.getDrawable(R.drawable.ic_escala)));
        ferramentasEditar.add(new BotaoFerramenta(toolsID.ZOOM_EXTEND, "Zoom Extend", context.getDrawable(R.drawable.ic_zoom_out)));

        return ferramentasEditar;
    }

    public static List<Integer> getOffsetTranslacao() {
        List<Integer> offsets = new ArrayList<>();

        offsets.add(10);
        offsets.add(20);
        offsets.add(50);
        offsets.add(100);

        return offsets;
    }

    public static List<Integer> getOffsetRotacao() {
        List<Integer> offsets = new ArrayList<>();

        offsets.add(30);
        offsets.add(45);
        offsets.add(60);
        offsets.add(90);
        offsets.add(180);
        offsets.add(270);

        return offsets;
    }
}
