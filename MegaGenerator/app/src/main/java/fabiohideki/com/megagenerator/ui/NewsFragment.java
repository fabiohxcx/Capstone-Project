package fabiohideki.com.megagenerator.ui;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.adapter.NewsAdapter;
import fabiohideki.com.megagenerator.model.NewsItem;
import fabiohideki.com.megagenerator.utils.FeedXmlParser;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    @BindView(R.id.rv_news)
    RecyclerView mRecyclerViewNews;

    @BindView(R.id.cl_news_error)
    ConstraintLayout constraintLayoutError;

    private NewsAdapter mNewsAdapter;

    private View mRootView;
    private List<NewsItem> mListNews;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, mRootView);

        constraintLayoutError.setVisibility(View.GONE);

        setupRecyclerView();

        getNews();

        return mRootView;
    }

    private void getNews() {
        constraintLayoutError.setVisibility(View.GONE);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://feeds.folha.uol.com.br/loterias/rss091.xml";

        Log.d("Fabio", "NewsFragment: " + url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTvTest.setText("Response is: " + response.toString());
                        Log.d("Fabio", "Response: " + response.toString());

                        //mTvTest.setText(response);

                        FeedXmlParser feedXmlParser = new FeedXmlParser();

                        InputStream stream = new ByteArrayInputStream(response.getBytes(StandardCharsets.ISO_8859_1));

                        try {
                            mListNews = feedXmlParser.parse(stream);

                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (stream != null) {
                                try {
                                    stream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                        mNewsAdapter = new NewsAdapter(mListNews);
                        mRecyclerViewNews.setAdapter(mNewsAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constraintLayoutError.setVisibility(View.VISIBLE);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void setupRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewNews.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewNews.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.line));

        mRecyclerViewNews.addItemDecoration(dividerItemDecoration);

    }

    @OnClick(R.id.bt_retry_news)
    public void retry(View view) {
        getNews();
    }

}
