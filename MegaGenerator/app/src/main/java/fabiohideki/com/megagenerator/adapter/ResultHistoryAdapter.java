package fabiohideki.com.megagenerator.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.l4digital.fastscroll.FastScroller;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.model.Resultado;

/**
 * Created by fabio.lagoa on 06/02/2018.
 */

public class ResultHistoryAdapter extends RecyclerView.Adapter<ResultHistoryAdapter.ResultHistoryHolder> implements FastScroller.SectionIndexer {

    private List<Resultado> results;
    private Context context;

    public ResultHistoryAdapter(List<Resultado> results, Context context) {
        this.results = results;
        this.context = context;
    }


    @Override
    public ResultHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ResultHistoryHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_result, parent, false));

    }

    @Override
    public void onBindViewHolder(ResultHistoryHolder holder, int position) {

        if (results != null && results.size() > 0) {
            Resultado resultado = results.get(position);

            holder.mTvResultDate.setText(resultado.getNumero() + " (" + resultado.getData() + ") ");
            String[] bals = resultado.getDezenas().split("-");
            holder.mBola1.setText(bals[0]);
            holder.mBola2.setText(bals[1]);
            holder.mBola3.setText(bals[2]);
            holder.mBola4.setText(bals[3]);
            holder.mBola5.setText(bals[4]);
            holder.mBola6.setText(bals[5]);

        }

    }

    @Override
    public int getItemCount() {
        return results != null ? results.size() : 0;
    }

    @Override
    public String getSectionText(int position) {
        return results != null ? results.get(position).getNumero() + "" : "";
    }


    public void setResults(List<Resultado> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    public class ResultHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.bola1)
        TextView mBola1;

        @BindView(R.id.bola2)
        TextView mBola2;

        @BindView(R.id.bola3)
        TextView mBola3;

        @BindView(R.id.bola4)
        TextView mBola4;

        @BindView(R.id.bola5)
        TextView mBola5;

        @BindView(R.id.bola6)
        TextView mBola6;

        @BindView(R.id.tv_item_result_date)
        TextView mTvResultDate;

        @BindView(R.id.bt_item_history_share)
        ImageButton mItemShare;

        @BindView(R.id.bt_item_history_copy)
        ImageButton mItemCopy;

        @BindString(R.string.result_copied)
        String resultCopied;

        @BindString(R.string.result)
        String result;

        @BindString(R.string.date)
        String date;

        @BindString(R.string.numbers)
        String numbers;

        @BindString(R.string.app_name)
        String appName;

        @BindString(R.string.megasena)
        String megaSena;


        public ResultHistoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mItemShare.setOnClickListener(this);
            mItemCopy.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int id = view.getId();

            Resultado resultado = results.get(getAdapterPosition());

            if (id == R.id.bt_item_history_share) {


                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                intentShare.putExtra(Intent.EXTRA_SUBJECT, appName);
                intentShare.putExtra(Intent.EXTRA_TEXT, result + " " + megaSena + ": " + resultado.getNumero() + " - " + date + ": " + resultado.getData() + " - " + numbers + ": " + resultado.getDezenas());
                context.startActivity(intentShare);

            } else if (id == R.id.bt_item_history_copy) {


                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData clip = ClipData.newPlainText(resultCopied, result + " " + megaSena + ": " + resultado.getNumero() + " - " + date + ": " + resultado.getData() + " - " + numbers + ": " + resultado.getDezenas());
                clipboard.setPrimaryClip(clip);

            }

        }
    }
}
