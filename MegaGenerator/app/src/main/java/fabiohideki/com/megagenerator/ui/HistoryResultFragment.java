package fabiohideki.com.megagenerator.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.l4digital.fastscroll.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.adapter.ResultHistoryAdapter;
import fabiohideki.com.megagenerator.model.Resultado;
import fabiohideki.com.megagenerator.repository.ResultsContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryResultFragment extends Fragment {

    @BindView(R.id.recyclerview_history)
    FastScrollRecyclerView mResultHistoryRecyclerView;

    List<Resultado> mResults = new ArrayList<>();

    private View rootView;


    public HistoryResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_history_result, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Cursor cursor;

        cursor = getContext().getContentResolver().query(ResultsContract.ResultEntry.CONTENT_URI,
                null,
                null,
                null,
                ResultsContract.ResultEntry.COLUMN_CONCURSO + " DESC");

        if (cursor != null && cursor.getCount() > 0) {

            Log.d("Fabio", "History:" + cursor.getCount());

            while (cursor.moveToNext()) {
                Resultado resultado = new Resultado();

                resultado.setNumero(cursor.getInt(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_CONCURSO)));
                resultado.setData(cursor.getString(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_DATE)));
                resultado.setDezenas(cursor.getString(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_NUMBERS)));

                mResults.add(resultado);

            }
            cursor.close();

            if (mResults != null && mResults.size() > 0) {
                setupRecycler();
            }

        }
    }

    private void setupRecycler() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        mResultHistoryRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mResultHistoryRecyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.line));

        mResultHistoryRecyclerView.addItemDecoration(dividerItemDecoration);
        ResultHistoryAdapter mAdapter = new ResultHistoryAdapter(mResults, getActivity());

        mResultHistoryRecyclerView.setAdapter(mAdapter);
    }

}
