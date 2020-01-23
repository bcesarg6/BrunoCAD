package com.example.brunocad.utils;

import android.content.Context;

import com.example.brunocad.R;
import com.example.brunocad.adapters.BotaoFerramenta;

import java.util.ArrayList;
import java.util.List;

public class CADUtils {

    public static List<BotaoFerramenta> getBotoesMenuCriar(Context context) {
        List<BotaoFerramenta> ferramentasCriar = new ArrayList<>();

        ferramentasCriar.add(new BotaoFerramenta(1, "Rotação", context.getDrawable(R.drawable.ic_rotacao)));
        ferramentasCriar.add(new BotaoFerramenta(2, "Translação", context.getDrawable(R.drawable.ic_rotacao))); // TODO: 1/23/20 mudar o icone aqui

        return ferramentasCriar;
    }
}
