package fabiohideki.com.megagenerator.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.model.NewsItem;

/**
 * Created by hidek on 21/02/2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private List<NewsItem> listNews;

    public NewsAdapter(List<NewsItem> listNews) {
        this.listNews = listNews;
    }

    @Override
    public NewsAdapter.NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsHolder holder, int position) {

        if (listNews != null && listNews.size() > 0) {

            NewsItem item = listNews.get(position);

            holder.mTitle.setText(item.getTitle());
            holder.mDescription.setText(Html.fromHtml(item.getDescription()));

        }

    }

    @Override
    public int getItemCount() {
        return listNews != null ? listNews.size() : 0;
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_news_title)
        TextView mTitle;

        @BindView(R.id.tv_news_description)
        TextView mDescription;


        public NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mDescription.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
