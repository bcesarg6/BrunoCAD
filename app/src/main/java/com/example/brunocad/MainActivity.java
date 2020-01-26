package com.example.brunocad;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private int abaSelecionada = TabsID.CREATE;

    private AdapterMenu adapterMenuCriar;
    private AdapterMenu adapterMenuEditar;

    private int funcaoSelecionada = toolsID.NONE;

    List<Drawing> drawings = new ArrayList<>();
    List<Point> coordinates = new ArrayList<>();

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
    }

    @Override
    public void selecionarFerramenta(BotaoFerramenta botaoClicado) {

        if (botaoClicado.getId() == toolsID.CLEAR) onClearClicked();
        else if (botaoClicado.getId() == toolsID.HELP) abrirDialogAjuda();
        else {

            if (funcaoSelecionada == botaoClicado.getId()) cancelOperation();
            else {

                if (funcaoSelecionada != toolsID.NONE && !coordinates.isEmpty()) {
                    Toaster.shortToast("operação anterior cancelada", this);

                    tvTapInfo.setVisibility(View.INVISIBLE);

                    coordinates.clear();
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
        addDrawing(line);
    }

    private void createTriangle(Point p1, Point p2, Point p3, boolean isFill) {
        int cor;
        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
        Triangle triangle = new Triangle(CADUtils.getNextDrawingId(), p1.x,p1.y, p2.x, p2.y, p3.x, p3.y, cor, isFill);
        addDrawing(triangle);
    }

    private void createRectangle(Point start, Point stop, boolean isFill) {
        int cor;
        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
        Rectangle rectangle = new Rectangle(CADUtils.getNextDrawingId(), start.x, start.y, stop.x, stop.y, cor, isFill);
        addDrawing(rectangle);
    }

    private void createCircle(Point center, float radius, boolean isFill) {
        int cor;
        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
        Circle circle = new Circle(CADUtils.getNextDrawingId(),center.x, center.y,radius, cor, isFill);
        addDrawing(circle);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTapCanvas(int x, int y) {
        tvTapInfo.setText(String.format("(%d,%d)", x, y));
        tvTapInfo.setVisibility(View.VISIBLE);

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

    }

    //deseleciona a ferramenta e reseta a lista de coordenadas
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
        drawings.add(drawing);
    }

    //limpa o canvas e a lista de drawings
    private void onClearClicked() {
        clear();
        Toaster.shortToast("canvas limpo", this);

        resetTools();

        tvContextInfo.setVisibility(View.INVISIBLE);
        tvTapInfo.setVisibility(View.INVISIBLE);
    }

    private void clear() {
        drawings.clear();
        canvas.clearCanvas();
    }

    private void draw() {
        canvas.draw(drawings);
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
            // TODO: 1/26/20
        }
    }

    private void abrirDialogAjuda() {
        DialogAjuda dialogAjuda = new DialogAjuda(this);
        final AlertDialog dialog = dialogAjuda.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
