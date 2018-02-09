package fabiohideki.com.megagenerator.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.l4digital.fastscroll.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.adapter.ResultHistoryAdapter;
import fabiohideki.com.megagenerator.model.Resultado;
import fabiohideki.com.megagenerator.repository.ResultsRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryResultFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String INSTANCE = "search_instance";

    @BindView(R.id.recyclerview_history)
    FastScrollRecyclerView mResultHistoryRecyclerView;

    private ResultHistoryAdapter resultHistoryAdapter;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindString(R.string.search_hint)
    String mSearchHint;

    private List<Resultado> mResults = new ArrayList<>();

    private View rootView;

    private SearchView searchView;

    private ResultsRepository resultsRepository;

    private String mCurrentSearch;

    private MenuItem mMenuItemSearch;

    public HistoryResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_history_result, container, false);
        ButterKnife.bind(this, rootView);

        MobileAds.initialize(getContext(), getString(R.string.AdMobAppID));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setHasOptionsMenu(true);
        resultsRepository = new ResultsRepository();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            setupRecycler();
            mCurrentSearch = savedInstanceState.getString(INSTANCE);

        } else {
            mResults = resultsRepository.listAll(getContext());

            if (mResults != null && mResults.size() > 0) {
                setupRecycler();
            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(INSTANCE, mCurrentSearch);
    }

    private void setupRecycler() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        mResultHistoryRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mResultHistoryRecyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.line));

        mResultHistoryRecyclerView.addItemDecoration(dividerItemDecoration);
        resultHistoryAdapter = new ResultHistoryAdapter(mResults, getActivity());

        mResultHistoryRecyclerView.setAdapter(resultHistoryAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.d("Fabio", "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.menu_search, menu);
        mMenuItemSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) mMenuItemSearch.getActionView();
        searchView.setOnQueryTextListener(HistoryResultFragment.this);
        searchView.setQueryHint(mSearchHint);
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);

        TextView searchText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text); //get ref to EditTExt
        searchText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)}); //add max lenght


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentSearch != null && !TextUtils.isEmpty(mCurrentSearch)) {
            Log.d("Fabio", "onActivityCreated: " + mCurrentSearch);

            searchView.setQuery(mCurrentSearch, true);
            searchView.setIconified(false);
            searchView.clearFocus();

        }


    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        onQueryTextChange(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        mCurrentSearch = newText;

        if (!TextUtils.isEmpty(newText)) {

            Resultado resultado = resultsRepository.getByNumber(Integer.parseInt(newText), getContext());

            if (resultado != null) {
                List<Resultado> resultados = new ArrayList<>();
                resultados.add(resultado);
                resultHistoryAdapter.setResults(resultados);
            }
        } else {
            resultHistoryAdapter.setResults(mResults);
        }

        return false;
    }
}
