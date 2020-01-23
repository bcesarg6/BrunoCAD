package com.example.brunocad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brunocad.R;
import com.example.brunocad.utils.Torradeira;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.CustomViewHolder> {

    private Context context;
    private MenuFerramentas listener;
    private List<BotaoFerramenta> botoes;
    private int idBtnSelecionado;

    public AdapterMenu(Context context, MenuFerramentas listener, List<BotaoFerramenta> botoes) {
        this.context = context;
        this.listener = listener;
        this.botoes = botoes;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.itemlist_btn_ferramenta, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final BotaoFerramenta botao = botoes.get(position);

        holder.tvNome.setText(botao.getNome());
        holder.icone.setImageDrawable(botao.getIcone());

        if (botao.getId() == idBtnSelecionado) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvNome.setTextColor(context.getResources().getColor(R.color.branco));
            holder.icone.setColorFilter(context.getResources().getColor(R.color.branco));

        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.brancoEscuro));
            holder.tvNome.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.icone.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.itemView.setOnClickListener(v -> Torradeira.shortToast(botao.getNome() + " selecionado", context));
    }

    @Override
    public int getItemCount() {
        return botoes != null ? botoes.size() : 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_btn) CardView cardView;
        @BindView(R.id.img_icone) ImageView icone;
        @BindView(R.id.tv_nome) TextView tvNome;

        CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface MenuFerramentas {
        void selecionarFerramenta(BotaoFerramenta botaoClicado);
    }

}
