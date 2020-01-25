package com.example.brunocad.utils;

import android.content.Context;

import com.example.brunocad.R;
import com.example.brunocad.adapters.BotaoFerramenta;

import java.util.ArrayList;
import java.util.List;

import static com.example.brunocad.utils.CADConstants.toolsID;

public class CADUtils {

    public static List<BotaoFerramenta> getBotoesMenuCriar(Context context) {
        List<BotaoFerramenta> ferramentasCriar = new ArrayList<>();

        ferramentasCriar.add(new BotaoFerramenta(toolsID.LINE, "Linhas", context.getDrawable(R.drawable.ic_linha)));

        ferramentasCriar.add(new BotaoFerramenta(toolsID.TRIANGLE_STROKE, "Triângulos", context.getDrawable(R.drawable.ic_triangulo)));
        ferramentasCriar.add(new BotaoFerramenta(toolsID.TRIANGLE, "Triângulos Preenchidos", context.getDrawable(R.drawable.ic_triangulo_fill)));

        ferramentasCriar.add(new BotaoFerramenta(toolsID.RECTANGLE_STROKE, "Retângulos", context.getDrawable(R.drawable.ic_retangulo)));
        ferramentasCriar.add(new BotaoFerramenta(toolsID.RECTANGLE, "Retângulos Preenchidos", context.getDrawable(R.drawable.ic_retangulo_fill)));

        ferramentasCriar.add(new BotaoFerramenta(toolsID.CIRCLE_STROKE, "Circulos", context.getDrawable(R.drawable.ic_circulo)));
        ferramentasCriar.add(new BotaoFerramenta(toolsID.CIRCLE, "Circulos Preenchidos", context.getDrawable(R.drawable.ic_circulo_fill)));

        return ferramentasCriar;
    }

    public static List<BotaoFerramenta> getBotoesMenuEditar(Context context) {
        List<BotaoFerramenta> ferramentasEditar = new ArrayList<>();

        ferramentasEditar.add(new BotaoFerramenta(toolsID.CLEAR, "Limpar", context.getDrawable(R.drawable.ic_clear)));
        ferramentasEditar.add(new BotaoFerramenta(toolsID.TRANSLATION, "Translação", context.getDrawable(R.drawable.ic_translacao)));
        ferramentasEditar.add(new BotaoFerramenta(toolsID.ROTATION, "Rotação", context.getDrawable(R.drawable.ic_rotacao)));
        ferramentasEditar.add(new BotaoFerramenta(toolsID.CHANGE_ESCALE, "Mudança de Escala", context.getDrawable(R.drawable.ic_escala)));
        ferramentasEditar.add(new BotaoFerramenta(toolsID.ZOOM_EXTEND, "Zoom Extend", context.getDrawable(R.drawable.ic_zoom_out)));

        return ferramentasEditar;
    }
}
