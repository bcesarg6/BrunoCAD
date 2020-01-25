package com.example.brunocad;

import android.app.AlertDialog;
import android.graphics.Color;
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
import com.example.brunocad.utils.CADUtils;
import com.example.brunocad.utils.Torradeira;
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

import static com.example.brunocad.utils.CADConstants.ferramentasID;

public class MainActivity extends AppCompatActivity implements AdapterMenu.MenuFerramentas {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.canvas) CADCanvas canvas;
    @BindView(R.id.tv_ferramenta_selecionada) TextView tvFerramentaSelecionada;
    @BindView(R.id.rv_criar) RecyclerView rvCriar;
    @BindView(R.id.rv_editar) RecyclerView rvEditar;
    @BindView(R.id.fab_acao) FloatingActionButton fab;

    private boolean isMenuCriar = true;
    private AdapterMenu adapterMenuCriar;
    private AdapterMenu adapterMenuEditar;

    private int funcaoSelecionada = ferramentasID.NENHUMA;

    List<Drawing> drawings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        config();
    }

    private void config() {
        adapterMenuCriar = new AdapterMenu(this, this, CADUtils.getBotoesMenuCriar(this));
        adapterMenuEditar = new AdapterMenu(this, this, CADUtils.getBotoesMenuEditar(this));

        rvCriar.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvCriar.setAdapter(adapterMenuCriar);

        rvEditar.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvEditar.setAdapter(adapterMenuEditar);
    }

    @Override
    public void selecionarFerramenta(BotaoFerramenta botaoClicado) {

        if (botaoClicado.getId() == ferramentasID.LIMPAR) {
            //limpa o canvas e a lista de drawings
            clear();
            Torradeira.shortToast("canvas limpo", this);
            funcaoSelecionada = ferramentasID.NENHUMA;

        } else {

            if (funcaoSelecionada == botaoClicado.getId()) {
                //deseleciona a ferramenta
                funcaoSelecionada = ferramentasID.NENHUMA;
                tvFerramentaSelecionada.setText("nenhuma ferramenta selecionada");

            } else {
                //seleciona a ferramenta
                funcaoSelecionada = botaoClicado.getId();
                tvFerramentaSelecionada.setText(botaoClicado.getNome());
            }

            int cor;

            // TODO: 1/25/20
            switch (funcaoSelecionada) {
                case ferramentasID.LINHA:
                    cor = ContextCompat.getColor(this, R.color.rosa);
                    Line line = new Line(1, 100f, 100f, 300f, 300f, cor);
                    addDrawing(line);
                    break;

                case ferramentasID.TRIANGULO:
                    cor = ContextCompat.getColor(this, R.color.petroleo);
                    Triangle triangle = new Triangle(2, 300f,200f, 450f, 400f, 100f, 400f, cor);
                    addDrawing(triangle);
                    break;

                case ferramentasID.RETANGULO:
                    cor = ContextCompat.getColor(this, R.color.azul);
                    Rectangle rectangle = new Rectangle(3, 300f, 100f, 500f, 700f, cor);
                    addDrawing(rectangle);
                    break;

                case ferramentasID.CIRCULO:
                    cor = ContextCompat.getColor(this, R.color.vermelho);
                    Circle circle = new Circle(4,400f,400f,80f, cor);
                    addDrawing(circle);
                    break;

                default:
                    break;
            }

            draw();
        }

        adapterMenuCriar.atualizaBtnSelecionado(funcaoSelecionada);
        adapterMenuEditar.atualizaBtnSelecionado(funcaoSelecionada);
    }

    private void addDrawing(Drawing drawing) {
        drawings.add(drawing);
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
            rvCriar.setVisibility(View.GONE);
            rvEditar.setVisibility(View.VISIBLE);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_editar));

        } else {
            //exibe menu criar
            rvEditar.setVisibility(View.GONE);
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
            Torradeira.shortToast("configs", this);
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
