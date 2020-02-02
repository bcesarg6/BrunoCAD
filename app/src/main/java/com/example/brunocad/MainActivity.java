package com.example.brunocad;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brunocad.adapters.AdapterMenu;
import com.example.brunocad.adapters.BotaoFerramenta;
import com.example.brunocad.dialogs.DialogAjuda;
import com.example.brunocad.drawings.Circle;
import com.example.brunocad.drawings.Drawing;
import com.example.brunocad.drawings.Line;
import com.example.brunocad.drawings.Rectangle;
import com.example.brunocad.drawings.Triangle;
import com.example.brunocad.utils.CADConstants.TabsID;
import com.example.brunocad.utils.CADUtils;
import com.example.brunocad.utils.Toaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

import static com.example.brunocad.utils.CADConstants.drawingTypes;
import static com.example.brunocad.utils.CADConstants.toolsID;

public class MainActivity extends AppCompatActivity implements AdapterMenu.MenuFerramentas, CADCanvas.tapListener {

    @BindView(R.id.tv_tap_info) TextView tvTapInfo;
    @BindView(R.id.tv_context_info) TextView tvContextInfo;
    @BindView(R.id.tv_tool_info) TextView tvToolInfo;

    @BindView(R.id.canvas) CADCanvas canvas;

    @BindView(R.id.btn_aba_criar) Button btnAbaCriar;
    @BindView(R.id.btn_aba_ferramentas) Button btnAbaFerramentas;
    @BindView(R.id.btn_aba_editar) Button btnAbaEditar;

    @BindView(R.id.rv_criar) RecyclerView rvCriar;
    @BindView(R.id.rv_ferramentas) RecyclerView rvFerramentas;
    @BindView(R.id.ll_editar) LinearLayout llEditar;

    @BindView(R.id.tv_nenhuma_ferramenta) TextView tvNenhumaFerramenta;

    @BindView(R.id.ct_translacao) ConstraintLayout ctTranslacao;
    @BindView(R.id.tv_ids_selecionados_translacao) TextView tvIdsSelecionadosTranslacao;
    @BindView(R.id.spinner_valor_translacao) MaterialSpinner spinnerValorTranslacao;

    @BindView(R.id.ct_mudanca_escala) ConstraintLayout ctMudancaEscala;
    @BindView(R.id.tv_ids_selecionados_escala) TextView tvIdsSelecionadosEscala;

    @BindView(R.id.ct_rotacao) ConstraintLayout ctRotacao;
    @BindView(R.id.tv_ids_selecionados_rotacao) TextView tvIdsSelecionadosRotacao;
    @BindView(R.id.spinner_valor_rotacao) MaterialSpinner spinnerValorRotacao;

    private int abaSelecionada = TabsID.CREATE;

    private AdapterMenu adapterMenuCriar;
    private AdapterMenu adapterMenuEditar;

    private int funcaoSelecionada = toolsID.NONE;

    private List<Long> objetosSelecionados = new ArrayList<>();

    private Integer offsetTranslacao = 10;
    private Integer offsetRotacao = 30;

    Map<Long, Drawing> drawingMap = new HashMap<>();
    List<Point> coordinates = new ArrayList<>();

    Stack<Map<Long, Drawing>> drawingMapStack = new Stack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        config();
    }

    private void config() {
        canvas.setListener(this);

        adapterMenuCriar = new AdapterMenu(this, this, CADUtils.getBotoesMenuCriar(this));
        adapterMenuEditar = new AdapterMenu(this, this, CADUtils.getBotoesMenuEditar(this));

        rvCriar.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvCriar.setAdapter(adapterMenuCriar);

        rvFerramentas.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvFerramentas.setAdapter(adapterMenuEditar);

        spinnerValorTranslacao.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CADUtils.getOffsetTranslacao()));
        spinnerValorTranslacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                offsetTranslacao = CADUtils.getOffsetTranslacao().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                offsetTranslacao = 10;
            }
        });

        spinnerValorRotacao.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CADUtils.getOffsetRotacao()));
        spinnerValorRotacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                offsetRotacao = CADUtils.getOffsetRotacao().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                offsetRotacao = 30;
            }
        });
    }

    @Override
    public void selecionarFerramenta(BotaoFerramenta botaoClicado) {

        if (botaoClicado.getId() == toolsID.CLEAR) onClearClicked();
        else if (botaoClicado.getId() == toolsID.HELP) abrirDialogAjuda();
        else if (botaoClicado.getId() == toolsID.UNDO) undo();
        else {

            if (funcaoSelecionada == botaoClicado.getId()) cancelOperation();
            else {

                if (funcaoSelecionada != toolsID.NONE) {

                    tvTapInfo.setVisibility(View.INVISIBLE);

                    resetaFuncaoEdicao();

                    if (!coordinates.isEmpty()) {
                        Toaster.shortToast("operação anterior cancelada", this);
                        coordinates.clear();
                    }
                }

                funcaoSelecionada = botaoClicado.getId();

                switch (funcaoSelecionada) {
                    case toolsID.LINE:
                        tvToolInfo.setText(R.string.instrucoes_linha);
                        tvToolInfo.setVisibility(View.VISIBLE);
                        tvContextInfo.setText(R.string.toque_2_pontos);
                        tvContextInfo.setVisibility(View.VISIBLE);
                        break;

                    case toolsID.TRIANGLE_STROKE:
                    case toolsID.TRIANGLE:
                        tvToolInfo.setText(R.string.instrucoes_triangulo);
                        tvToolInfo.setVisibility(View.VISIBLE);
                        tvContextInfo.setText(R.string.toque_3_pontos);
                        tvContextInfo.setVisibility(View.VISIBLE);
                        break;

                    case toolsID.RECTANGLE_STROKE:
                    case toolsID.RECTANGLE:
                        tvToolInfo.setText(R.string.instrucoes_retangulo);
                        tvToolInfo.setVisibility(View.VISIBLE);
                        tvContextInfo.setText(R.string.toque_2_pontos);
                        tvContextInfo.setVisibility(View.VISIBLE);
                        break;

                    case toolsID.CIRCLE_STROKE:
                    case toolsID.CIRCLE:
                        tvToolInfo.setText(R.string.instrucoes_circulo);
                        tvToolInfo.setVisibility(View.VISIBLE);
                        tvContextInfo.setText(R.string.toque_2_pontos);
                        tvContextInfo.setVisibility(View.VISIBLE);
                        break;

                    case toolsID.TRANSLATION:
                        tvNenhumaFerramenta.setVisibility(View.GONE);

                        tvToolInfo.setText(R.string.instrucoes_translacao);
                        tvToolInfo.setVisibility(View.VISIBLE);

                        tvContextInfo.setText(R.string.selecione_objeto);
                        tvContextInfo.setVisibility(View.VISIBLE);

                        ctMudancaEscala.setVisibility(View.GONE);
                        ctRotacao.setVisibility(View.GONE);

                        ctTranslacao.setVisibility(View.VISIBLE);

                        btnAbaEditar.performClick();
                        break;

                    case toolsID.CHANGE_ESCALE:
                        tvNenhumaFerramenta.setVisibility(View.GONE);

                        tvToolInfo.setText(R.string.instrucoes_escala);
                        tvToolInfo.setVisibility(View.VISIBLE);

                        tvContextInfo.setText(R.string.selecione_objeto);
                        tvContextInfo.setVisibility(View.VISIBLE);

                        ctTranslacao.setVisibility(View.GONE);
                        ctRotacao.setVisibility(View.GONE);

                        ctMudancaEscala.setVisibility(View.VISIBLE);

                        btnAbaEditar.performClick();
                        break;

                    case toolsID.ROTATION:
                        tvNenhumaFerramenta.setVisibility(View.GONE);

                        tvToolInfo.setText(R.string.instrucoes_rotacao);
                        tvToolInfo.setVisibility(View.VISIBLE);

                        tvContextInfo.setText(R.string.selecione_objeto);
                        tvContextInfo.setVisibility(View.VISIBLE);

                        ctTranslacao.setVisibility(View.GONE);
                        ctMudancaEscala.setVisibility(View.GONE);

                        ctRotacao.setVisibility(View.VISIBLE);

                        btnAbaEditar.performClick();
                        break;

                    default:
                        break;
                }
            }
        }

        adapterMenuCriar.atualizaBtnSelecionado(funcaoSelecionada);
        adapterMenuEditar.atualizaBtnSelecionado(funcaoSelecionada);
    }

    private void createLine(Point start, Point stop) {
        int cor;
        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
        Line line = new Line(CADUtils.getNextDrawingId(), start.x, start.y, stop.x, stop.y, cor);

        line.addPoint(start);
        line.addPoint(stop);

        addDrawing(line);
    }

    private void createTriangle(Point p1, Point p2, Point p3, boolean isFill) {
        int cor;
        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
        Triangle triangle = new Triangle(CADUtils.getNextDrawingId(), p1.x,p1.y, p2.x, p2.y, p3.x, p3.y, cor, isFill);

        triangle.addPoint(p1);
        triangle.addPoint(p2);
        triangle.addPoint(p3);

        addDrawing(triangle);
    }

    private void createRectangle(Point start, Point stop, boolean isFill) {
        int cor;
        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
        Rectangle rectangle = new Rectangle(CADUtils.getNextDrawingId(), start.x, start.y, stop.x, stop.y, cor, isFill);

        rectangle.addPoint(start);
        rectangle.addPoint(stop);

        addDrawing(rectangle);
    }

    private void createCircle(Point center, float radius, boolean isFill) {
        int cor;
        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
        Circle circle = new Circle(CADUtils.getNextDrawingId(),center.x, center.y,radius, cor, isFill);

        circle.addPoint(center);

        addDrawing(circle);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTapCanvas(int x, int y) {
        tvTapInfo.setText(String.format("(%d,%d)", x, y));
        tvTapInfo.setVisibility(View.VISIBLE);

        if (isCreating()) {
            coordinates.add(new Point(x,y));

            switch (funcaoSelecionada) {

                case toolsID.LINE:

                    if (coordinates.size() >= 2) {
                        createLine(coordinates.get(0), coordinates.get(1));
                        draw();

                        Toaster.shortToast(getString(R.string.linha_criada), this);

                        tvContextInfo.setText(R.string.toque_2_pontos);
                        tvContextInfo.setVisibility(View.VISIBLE);

                        coordinates.clear();

                    } else {
                        tvContextInfo.setText(R.string.toque_1_ponto);
                        tvContextInfo.setVisibility(View.VISIBLE);
                    }

                    break;

                case toolsID.TRIANGLE_STROKE:
                case toolsID.TRIANGLE:

                    if (coordinates.size() >= 3) {
                        createTriangle(coordinates.get(0), coordinates.get(1), coordinates.get(2), funcaoSelecionada == toolsID.TRIANGLE ? true : false);
                        draw();

                        Toaster.shortToast(getString(R.string.triangulo_criada), this);

                        tvContextInfo.setText(R.string.toque_3_pontos);
                        tvContextInfo.setVisibility(View.VISIBLE);

                        coordinates.clear();

                    } else {
                        tvContextInfo.setText(coordinates.size() < 2 ? R.string.toque_2_pontos : R.string.toque_1_ponto);
                        tvContextInfo.setVisibility(View.VISIBLE);
                    }

                    break;

                case toolsID.RECTANGLE_STROKE:
                case toolsID.RECTANGLE:

                    if (coordinates.size() >= 2) {

                        Point start = coordinates.get(0);
                        Point stop = coordinates.get(1);

                        if (start.x > stop.x) {
                            int aux = start.x;
                            start.x = stop.x;
                            stop.x = aux;
                        }

                        if (start.y > stop.y) {
                            int aux = start.y;
                            start.y = stop.y;
                            stop.y = aux;
                        }

                        createRectangle(start, stop, funcaoSelecionada == toolsID.RECTANGLE ? true : false);
                        draw();

                        Toaster.shortToast(getString(R.string.retangulo_criada), this);

                        tvContextInfo.setText(R.string.toque_2_pontos);
                        tvContextInfo.setVisibility(View.VISIBLE);

                        coordinates.clear();

                    } else {
                        tvContextInfo.setText(R.string.toque_1_ponto);
                        tvContextInfo.setVisibility(View.VISIBLE);
                    }

                    break;

                case toolsID.CIRCLE_STROKE:
                case toolsID.CIRCLE:

                    if (coordinates.size() >= 2) {

                        Point center = coordinates.get(0);
                        Point border = coordinates.get(1);

                        double radius = Math.sqrt(Math.pow((double) (border.x - center.x), 2f) + Math.pow((double) (border.y - center.y), 2f));

                        createCircle(center, (float) radius, funcaoSelecionada == toolsID.CIRCLE ? true : false);
                        draw();

                        Toaster.shortToast(getString(R.string.ciruclo_criada), this);

                        tvContextInfo.setText(R.string.toque_2_pontos);
                        tvContextInfo.setVisibility(View.VISIBLE);

                        coordinates.clear();

                    } else {
                        tvContextInfo.setText(R.string.toque_1_ponto);
                        tvContextInfo.setVisibility(View.VISIBLE);
                    }

                    break;

                default:
                    break;
            }

        } else if (isEditing()) {
            Point tap = new Point(x,y);

            for (Drawing d : drawingMap.values()) {

                switch (d.getType()) {

                    case drawingTypes.RECTANGLE_STROKE:
                    case drawingTypes.RECTANGLE:

                        Point start = d.getPoints().get(0);
                        Point stop = d.getPoints().get(1);

                        if (tap.x > start.x && tap.x < stop.x && tap.y > start.y && tap.y < stop.y) {
                            if (objetosSelecionados.contains(d.getId())) objetosSelecionados.remove(d.getId());
                            else objetosSelecionados.add(d.getId());
                        }

                        break;

                    case drawingTypes.LINE:

//                        Não consegui fazer isso aqui funcionar :(

//                        Path line = canvas.getLinePath(d.getId());
//
//                        if (line != null) {
//
//                            Path tempPath = new Path(); // Create temp Path
//                            tempPath.moveTo(tap.x, tap.y); // Move cursor to point
//                            RectF rectangle = new RectF(tap.x-1, tap.y-1, tap.x+1, tap.y+1); // create rectangle with size 2xp
//                            tempPath.addRect(rectangle, Path.Direction.CW); // add rect to temp path
//                            tempPath.op(line, Path.Op.UNION); // get difference with our PathToCheck
//
//                            if (tempPath.isEmpty()) {
//                                if (objetosSelecionados.contains(d.getId())) objetosSelecionados.remove(d.getId());
//                                else objetosSelecionados.add(d.getId());
//                            }
//                        }

                        break;

                    case drawingTypes.TRIANGLE_STROKE:
                    case drawingTypes.TRIANGLE:

                        Region r = canvas.getDrawingRegion(d.getId());

                        if (r != null) {
                            if (r.contains(tap.x, tap.y)) {
                                if (objetosSelecionados.contains(d.getId())) objetosSelecionados.remove(d.getId());
                                else objetosSelecionados.add(d.getId());
                            }
                        }

                        break;

                    case drawingTypes.CIRCLE_STROKE:
                    case drawingTypes.CIRCLE:

                        Point center = d.getPoints().get(0);

                        double distance = Math.sqrt(Math.pow(tap.x - center.x, 2) + Math.pow(tap.y - center.y, 2));

                        if (distance < ((Circle)d).getRadius()) {
                            if (objetosSelecionados.contains(d.getId())) objetosSelecionados.remove(d.getId());
                            else objetosSelecionados.add(d.getId());
                        }

                        break;

                    default:
                        break;
                }
            }

            String idsSelecionados = "";

            if (!objetosSelecionados.isEmpty()) {

                for (Long id : objetosSelecionados) idsSelecionados += "#" + id + " ";

                tvIdsSelecionadosTranslacao.setText(idsSelecionados);
                tvIdsSelecionadosEscala.setText(idsSelecionados);
                tvIdsSelecionadosRotacao.setText(idsSelecionados);

            } else {

                tvIdsSelecionadosTranslacao.setText(R.string.sem_objetos_selecionados);
                tvIdsSelecionadosEscala.setText(R.string.sem_objetos_selecionados);
                tvIdsSelecionadosRotacao.setText(R.string.sem_objetos_selecionados);
            }
        }
    }

    private boolean isCreating() {
        boolean isCreating = false;

        isCreating = isCreating || funcaoSelecionada == toolsID.LINE;
        isCreating = isCreating || funcaoSelecionada == toolsID.TRIANGLE_STROKE;
        isCreating = isCreating || funcaoSelecionada == toolsID.TRIANGLE;
        isCreating = isCreating || funcaoSelecionada == toolsID.RECTANGLE_STROKE;
        isCreating = isCreating || funcaoSelecionada == toolsID.RECTANGLE;
        isCreating = isCreating || funcaoSelecionada == toolsID.CIRCLE_STROKE;
        isCreating = isCreating || funcaoSelecionada == toolsID.CIRCLE;

        return isCreating;
    }

    private boolean isEditing() {
        boolean isEditing = false;

        isEditing = isEditing || funcaoSelecionada == toolsID.TRANSLATION;
        isEditing = isEditing || funcaoSelecionada == toolsID.ROTATION;
        isEditing = isEditing || funcaoSelecionada == toolsID.CHANGE_ESCALE;

        return isEditing;
    }

    /**
     * deseleciona a ferramenta e reseta a lista de coordenadas
     */
    private void cancelOperation() {
        funcaoSelecionada = toolsID.NONE;
        tvToolInfo.setText(R.string.op_cancelada);
        tvToolInfo.setVisibility(View.VISIBLE);

        tvContextInfo.setVisibility(View.INVISIBLE);
        tvTapInfo.setVisibility(View.INVISIBLE);

        coordinates.clear();
    }

    private void resetTools() {
        funcaoSelecionada = toolsID.NONE;
        tvToolInfo.setVisibility(View.INVISIBLE);

        adapterMenuCriar.atualizaBtnSelecionado(funcaoSelecionada);
        adapterMenuEditar.atualizaBtnSelecionado(funcaoSelecionada);

        coordinates.clear();
    }

    private void addDrawing(Drawing drawing) {
        drawingMapStack.push(clone(drawingMap));
        drawingMap.put(drawing.getId(), drawing);
    }

    /**
     * limpa o canvas e a lista de drawings
     */
    private void onClearClicked() {
        clear(false);
        Toaster.shortToast("canvas limpo", this);

        resetTools();

        tvContextInfo.setVisibility(View.INVISIBLE);
        tvTapInfo.setVisibility(View.INVISIBLE);

        resetaFuncaoEdicao();
    }

    private void resetaFuncaoEdicao() {
        tvNenhumaFerramenta.setVisibility(View.VISIBLE);
        ctTranslacao.setVisibility(View.GONE);
        ctMudancaEscala.setVisibility(View.GONE);
        ctRotacao.setVisibility(View.GONE);
    }

    private void clear(boolean isUndo) {
        if (!isUndo) drawingMapStack.push(clone(drawingMap));

        drawingMap.clear();
        objetosSelecionados.clear();
        canvas.clearCanvas();
    }

    private void draw() {
        canvas.draw(new ArrayList<>(drawingMap.values()));
    }

    private void undo() {
        if (drawingMapStack.empty()) {
            Toaster.shortToast("nada para voltar", this);
            return;
        }

        drawingMap = clone(drawingMapStack.pop());

        if (drawingMap.isEmpty()) clear(true);
        else draw();

        resetTools();
        Toaster.shortToast("undo realizado", this);
    }


    public Map<Long,Drawing> clone(@org.jetbrains.annotations.NotNull Map<Long,Drawing> original) {
        Map<Long,Drawing> copy = new HashMap<>();

        for (Map.Entry<Long,Drawing> entry: original.entrySet()) {
            try {
                copy.put(entry.getKey(), (Drawing) entry.getValue().clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        return copy;
    }

    /**
     * Translação de objetos
     * @param x offset horizontal
     * @param y offset vertical
     */
    private void translateDrawingsBy(float x, float y) {
        boolean success = false;

        drawingMapStack.push(clone(drawingMap));

        for (Long id : objetosSelecionados) {
            if (drawingMap.containsKey(id)) {
                Drawing d = drawingMap.get(id);

                switch (d.getType()) {
                    case drawingTypes.RECTANGLE_STROKE:
                    case drawingTypes.RECTANGLE:
                        Point start = d.getPoints().get(0);
                        Point stop = d.getPoints().get(1);

                        start.x += x;
                        start.y += y;

                        stop.x += x;
                        stop.y += y;

                        d.clearPoints();
                        d.addPoint(start);
                        d.addPoint(stop);

                        ((Rectangle)d).UpdateRectangle(start.x, start.y, stop.x, stop.y);
                        drawingMap.put(id,d);
                        success = true;

                        break;

                    case drawingTypes.TRIANGLE_STROKE:
                    case drawingTypes.TRIANGLE:
                        Point p1 = d.getPoints().get(0);
                        Point p2 = d.getPoints().get(1);
                        Point p3 = d.getPoints().get(2);

                        p1.x += x;
                        p1.y += y;

                        p2.x += x;
                        p2.y += y;

                        p3.x += x;
                        p3.y += y;

                        d.clearPoints();
                        d.addPoint(p1);
                        d.addPoint(p2);
                        d.addPoint(p3);

                        ((Triangle)d).updateTriangle(p1.x, p1.y,
                                                     p2.x, p2.y,
                                                     p3.x, p3.y);

                        drawingMap.put(id,d);
                        success = true;

                        break;

                    case drawingTypes.CIRCLE_STROKE:
                    case drawingTypes.CIRCLE:

                        Point center = d.getPoints().get(0);

                        center.x += x;
                        center.y += y;

                        d.clearPoints();
                        d.addPoint(center);

                        ((Circle)d).updateCircle(center.x, center.y);

                        drawingMap.put(id, d);
                        success = true;

                        break;

                    default:
                        break;
                }

            }
        }

        if (success) draw();
        else {
            Toaster.longToast("operação de translação não realizada: nenhum objeto selecionado", this);
            drawingMapStack.pop();
        }
    }

    @OnClick(R.id.btn_esquerda)
    void transladarEsq() {
        translateDrawingsBy(offsetTranslacao * -1.0f,0f);
    }

    @OnClick(R.id.btn_cima)
    void transladarCima() {
        translateDrawingsBy(0f,offsetTranslacao * -1.0f);
    }

    @OnClick(R.id.btn_direita)
    void transladarDir() {
        translateDrawingsBy(offsetTranslacao,0f);
    }

    @OnClick(R.id.btn_baixo)
    void transladarBaixo() {
        translateDrawingsBy(0f,offsetTranslacao);
    }

    /**
     * Mudança de escala
     * @param e fator da escala
     */
    private void changeScale(float e) {
        boolean success = false;
        drawingMapStack.push(clone(drawingMap));

        for (Long id : objetosSelecionados) {
            if (drawingMap.containsKey(id)) {
                Drawing d = drawingMap.get(id);

                if (d != null) {
                    d.applyScale(e);
                    success = true;
                }

                switch (d.getType()) {
                    case drawingTypes.RECTANGLE_STROKE:
                    case drawingTypes.RECTANGLE:

                        Point start = d.getPoints().get(0);
                        Point stop = d.getPoints().get(1);

                        float xDistance = (stop.x - start.x) * (e - 1f);
                        float yDistance = (stop.y - start.y) * (e - 1f);

                        start.y -= yDistance / 2f;
                        start.x -= xDistance / 2f;

                        stop.x += xDistance / 2f;
                        stop.y += yDistance / 2f;

                        d.clearPoints();
                        d.addPoint(start);
                        d.addPoint(stop);

                        ((Rectangle)d).UpdateRectangle(start.x, start.y, stop.x, stop.y);
                        drawingMap.put(id, d);
                        success = true;

                        break;

                    case drawingTypes.CIRCLE_STROKE:
                    case drawingTypes.CIRCLE:

                        ((Circle)d).scaleCircle(e);
                        drawingMap.put(id, d);
                        success = true;
                        break;

                    default:
                        break;
                }

            }
        }

        if (success) draw();
        else  {
            Toaster.longToast("operação de mudança de escala não realizada: nenhum objeto selecionado", this);
            drawingMapStack.pop();
        }
    }

    @OnClick(R.id.btn_escala_025)
    void mudarEscala025() {
        changeScale(0.25f);
    }

    @OnClick(R.id.btn_escala_05)
    void mudarEscala05() {
        changeScale(0.5f);
    }

    @OnClick(R.id.btn_escala_075)
    void mudarEscala075() {
        changeScale(0.75f);
    }

    @OnClick(R.id.btn_escala_125)
    void mudarEscala125() {
        changeScale(1.25f);
    }

    @OnClick(R.id.btn_escala_150)
    void mudarEscala150() {
        changeScale(1.5f);
    }

    @OnClick(R.id.btn_escala_2)
    void mudarEscala2() {
        changeScale(2.0f);
    }

    private void RotateDrawings(float angle) {
        boolean success = false;
        drawingMapStack.push(clone(drawingMap));

        for (Long id : objetosSelecionados) {
            if (drawingMap.containsKey(id)) {
                Drawing d = drawingMap.get(id);

                if (d != null)  {
                    d.addAngle(angle);
                    success = true;
                }
            }
        }

        if (success) draw();
        else  {
            Toaster.longToast("operação de rotação não realizada: nenhum objeto selecionado", this);
            drawingMapStack.pop();
        }
    }

    @OnClick(R.id.btn_rot_l)
    void RotateLeft() {
        RotateDrawings(offsetRotacao * -1.0f);
    }

    @OnClick(R.id.btn_rot_r)
    void RotateRight() {
        RotateDrawings(offsetRotacao);
    }

    @OnClick(R.id.btn_aba_criar)
    void onClickAbaCriar() {
        if (abaSelecionada != TabsID.CREATE) {
            abaSelecionada = TabsID.CREATE;

            btnAbaCriar.setBackground(getDrawable(R.color.branco));
            btnAbaCriar.setTextColor(getColor(R.color.colorAccent));
            btnAbaCriar.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.colorAccent)));

            btnAbaFerramentas.setBackground(getDrawable(R.color.brancoEscuro));
            btnAbaFerramentas.setTextColor(getColor(R.color.cinzaClaro));
            btnAbaFerramentas.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.cinzaClaro)));

            btnAbaEditar.setBackground(getDrawable(R.color.brancoEscuro));
            btnAbaEditar.setTextColor(getColor(R.color.cinzaClaro));
            btnAbaEditar.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.cinzaClaro)));

            rvFerramentas.setVisibility(View.INVISIBLE);
            llEditar.setVisibility(View.INVISIBLE);
            rvCriar.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_aba_ferramentas)
    void onClickAbaFerramentas() {
        if (abaSelecionada != TabsID.TOOLS) {
            abaSelecionada = TabsID.TOOLS;

            btnAbaFerramentas.setBackground(getDrawable(R.color.branco));
            btnAbaFerramentas.setTextColor(getColor(R.color.colorAccent));
            btnAbaFerramentas.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.colorAccent)));

            btnAbaCriar.setBackground(getDrawable(R.color.brancoEscuro));
            btnAbaCriar.setTextColor(getColor(R.color.cinzaClaro));
            btnAbaCriar.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.cinzaClaro)));

            btnAbaEditar.setBackground(getDrawable(R.color.brancoEscuro));
            btnAbaEditar.setTextColor(getColor(R.color.cinzaClaro));
            btnAbaEditar.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.cinzaClaro)));


            rvCriar.setVisibility(View.INVISIBLE);
            llEditar.setVisibility(View.INVISIBLE);
            rvFerramentas.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_aba_editar)
    void onClickAbaEditar() {
        if (abaSelecionada != TabsID.EDIT) {
            abaSelecionada = TabsID.EDIT;

            btnAbaEditar.setBackground(getDrawable(R.color.branco));
            btnAbaEditar.setTextColor(getColor(R.color.colorAccent));
            btnAbaEditar.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.colorAccent)));

            btnAbaCriar.setBackground(getDrawable(R.color.brancoEscuro));
            btnAbaCriar.setTextColor(getColor(R.color.cinzaClaro));
            btnAbaCriar.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.cinzaClaro)));

            btnAbaFerramentas.setBackground(getDrawable(R.color.brancoEscuro));
            btnAbaFerramentas.setTextColor(getColor(R.color.cinzaClaro));
            btnAbaFerramentas.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.cinzaClaro)));

            rvCriar.setVisibility(View.INVISIBLE);
            rvFerramentas.setVisibility(View.INVISIBLE);
            llEditar.setVisibility(View.VISIBLE);
        }
    }

    private void abrirDialogAjuda() {
        DialogAjuda dialogAjuda = new DialogAjuda(this);
        final AlertDialog dialog = dialogAjuda.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
