package com.example.brunocad.utils;

import android.content.Context;

import com.example.brunocad.R;
import com.example.brunocad.adapters.BotaoFerramenta;

import java.util.ArrayList;
import java.util.List;

import static com.example.brunocad.utils.CADConstants.ferramentasID;

public class CADUtils {

    public static List<BotaoFerramenta> getBotoesMenuCriar(Context context) {
        List<BotaoFerramenta> ferramentasCriar = new ArrayList<>();

        ferramentasCriar.add(new BotaoFerramenta(ferramentasID.LINHA, "Linhas", context.getDrawable(R.drawable.ic_linha)));
        ferramentasCriar.add(new BotaoFerramenta(ferramentasID.TRIANGULO, "Triângulos", context.getDrawable(R.drawable.ic_triangulo)));
        ferramentasCriar.add(new BotaoFerramenta(ferramentasID.RETANGULO, "Retângulos", context.getDrawable(R.drawable.ic_retangulo)));
        ferramentasCriar.add(new BotaoFerramenta(ferramentasID.CIRCULO, "Circulos", context.getDrawable(R.drawable.ic_circulo)));

        return ferramentasCriar;
    }

    public static List<BotaoFerramenta> getBotoesMenuEditar(Context context) {
        List<BotaoFerramenta> ferramentasEditar = new ArrayList<>();

        ferramentasEditar.add(new BotaoFerramenta(ferramentasID.LIMPAR, "Limpar", context.getDrawable(R.drawable.ic_clear)));
        ferramentasEditar.add(new BotaoFerramenta(ferramentasID.TRANSLACAO, "Translação", context.getDrawable(R.drawable.ic_translacao)));
        ferramentasEditar.add(new BotaoFerramenta(ferramentasID.ROTACAO, "Rotação", context.getDrawable(R.drawable.ic_rotacao)));
        ferramentasEditar.add(new BotaoFerramenta(ferramentasID.MUDANCA_ESCALA, "Mudança de Escala", context.getDrawable(R.drawable.ic_escala)));
        ferramentasEditar.add(new BotaoFerramenta(ferramentasID.ZOOM_EXTEND, "Zoom Extend", context.getDrawable(R.drawable.ic_zoom_out)));

        return ferramentasEditar;
    }
}
