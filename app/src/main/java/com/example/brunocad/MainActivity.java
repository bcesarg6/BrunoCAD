package com.example.brunocad;

import android.os.Bundle;

import com.example.brunocad.adapters.AdapterMenu;
import com.example.brunocad.adapters.BotaoFerramenta;
import com.example.brunocad.utils.CADConstants;
import com.example.brunocad.utils.CADUtils;
import com.example.brunocad.utils.Torradeira;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.brunocad.utils.CADConstants.ferramentasID;

public class MainActivity extends AppCompatActivity implements AdapterMenu.MenuFerramentas {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_ferramenta_selecionada) TextView tvFerramentaSelecionada;
    @BindView(R.id.rv_criar) RecyclerView rvCriar;
    @BindView(R.id.rv_editar) RecyclerView rvEditar;
    @BindView(R.id.fab_acao) FloatingActionButton fab;

    private boolean isMenuCriar = true;
    private AdapterMenu adapterMenuCriar;
    private AdapterMenu adapterMenuEditar;

    private int funcaoSelecionada = ferramentasID.NENHUMA;

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

        if (funcaoSelecionada == botaoClicado.getId()) {
            //deseleciona a ferramenta
            funcaoSelecionada = ferramentasID.NENHUMA;
            tvFerramentaSelecionada.setText("nenhuma ferramenta selecionada");

        } else {
            //seleciona a ferramenta
            funcaoSelecionada = botaoClicado.getId();
            tvFerramentaSelecionada.setText(botaoClicado.getNome());
        }

        // TODO: 1/24/20

        adapterMenuCriar.atualizaBtnSelecionado(funcaoSelecionada);
        adapterMenuEditar.atualizaBtnSelecionado(funcaoSelecionada);
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
            Torradeira.shortToast("ajuda", this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
