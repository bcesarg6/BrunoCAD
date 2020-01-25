package com.example.brunocad;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.brunocad.adapters.AdapterMenu;
import com.example.brunocad.adapters.BotaoFerramenta;
import com.example.brunocad.drawings.Circle;
import com.example.brunocad.drawings.Drawing;
import com.example.brunocad.drawings.Line;
import com.example.brunocad.drawings.Rectangle;
import com.example.brunocad.drawings.Triangle;
import com.example.brunocad.dialogs.DialogAjuda;
import com.example.brunocad.utils.CADConstants;
import com.example.brunocad.utils.CADUtils;
import com.example.brunocad.utils.Toaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.brunocad.utils.CADConstants.toolsID;

public class MainActivity extends AppCompatActivity implements AdapterMenu.MenuFerramentas, CADCanvas.tapListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.canvas) CADCanvas canvas;
    @BindView(R.id.tv_tap_info) TextView tvTapInfo;
    @BindView(R.id.tv_context_info) TextView tvContextInfo;
    @BindView(R.id.tv_tool_info) TextView tvToolInfo;
    @BindView(R.id.rv_criar) RecyclerView rvCriar;
    @BindView(R.id.rv_editar) RecyclerView rvEditar;
    @BindView(R.id.fab_acao) FloatingActionButton fab;

    private boolean isMenuCriar = true;
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

        setSupportActionBar(toolbar);

        config();
    }

    private void config() {
        canvas.setListener(this);

        adapterMenuCriar = new AdapterMenu(this, this, CADUtils.getBotoesMenuCriar(this));
        adapterMenuEditar = new AdapterMenu(this, this, CADUtils.getBotoesMenuEditar(this));

        rvCriar.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvCriar.setAdapter(adapterMenuCriar);

        rvEditar.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvEditar.setAdapter(adapterMenuEditar);
    }

    @Override
    public void selecionarFerramenta(BotaoFerramenta botaoClicado) {

        if (botaoClicado.getId() == toolsID.CLEAR) onClearClicked();
        else {

            if (funcaoSelecionada == botaoClicado.getId()) cancelOperation();
            else {

                if (funcaoSelecionada != toolsID.NONE) {
                    Toaster.shortToast("operação anterior cancelada", this);

                    tvTapInfo.setVisibility(View.INVISIBLE);

                    coordinates.clear();
                }

                funcaoSelecionada = botaoClicado.getId();
                tvToolInfo.setText(botaoClicado.getNome());

                int cor;

                switch (funcaoSelecionada) {
                    case toolsID.LINE:
                        tvToolInfo.setText(R.string.instrucoes_linha);
                        tvContextInfo.setText(R.string.toque_2_pontos);
                        tvContextInfo.setVisibility(View.VISIBLE);
                        break;

                    case toolsID.TRIANGLE_STROKE:
                        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
                        Triangle triangle = new Triangle(2, 500f,200f, 750f, 400f, 100f, 400f, cor, false);
                        addDrawing(triangle);
                        break;

                    case toolsID.TRIANGLE:
                        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
                        Triangle triangleFilled = new Triangle(3, 300f,200f, 450f, 400f, 100f, 400f, cor, true);
                        addDrawing(triangleFilled);
                        break;

                    case toolsID.RECTANGLE_STROKE:
                        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
                        Rectangle rectangle = new Rectangle(4, 300f, 400f, 500f, 800f, cor, false);
                        addDrawing(rectangle);
                        break;

                    case toolsID.RECTANGLE:
                        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
                        Rectangle rectangleFilled = new Rectangle(5, 300f, 100f, 500f, 700f, cor, true);
                        addDrawing(rectangleFilled);
                        break;

                    case toolsID.CIRCLE_STROKE:
                        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
                        Circle circle = new Circle(6,400f,400f,80f, cor, false);
                        addDrawing(circle);
                        break;

                    case toolsID.CIRCLE:
                        cor = ContextCompat.getColor(this, CADUtils.getNextColor());
                        Circle circleFilled = new Circle(7,400f,400f,40f, cor, true);
                        addDrawing(circleFilled);
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
                break;

            case toolsID.TRIANGLE:
                break;

            case toolsID.RECTANGLE_STROKE:
                break;

            case toolsID.RECTANGLE:
                break;

            case toolsID.CIRCLE_STROKE:
                break;

            case toolsID.CIRCLE:
                break;

            default:
                break;
        }

    }

    //deseleciona a ferramenta e reseta a lista de coordenadas
    private void cancelOperation() {
        funcaoSelecionada = toolsID.NONE;
        tvToolInfo.setText(R.string.op_cancelada);

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

    @OnClick(R.id.fab_acao)
    void toggleMenu() {

        if (isMenuCriar) {
            //exibe menu editar
            rvCriar.setVisibility(View.INVISIBLE);
            rvEditar.setVisibility(View.VISIBLE);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_editar));

        } else {
            //exibe menu criar
            rvEditar.setVisibility(View.INVISIBLE);
            rvCriar.setVisibility(View.VISIBLE);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_criar));
        }

        isMenuCriar = !isMenuCriar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toaster.shortToast("configs", this);
            return true;
        }

        if (id == R.id.action_help) {
            abrirDialogAjuda();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void abrirDialogAjuda() {
        DialogAjuda dialogAjuda = new DialogAjuda(this);
        final AlertDialog dialog = dialogAjuda.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
