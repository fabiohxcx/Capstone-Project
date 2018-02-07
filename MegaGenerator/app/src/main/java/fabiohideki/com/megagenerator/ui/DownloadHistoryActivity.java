package fabiohideki.com.megagenerator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.network.DownloadAllResultsTask;
import fabiohideki.com.megagenerator.network.TaskCallBack;

public class DownloadHistoryActivity extends AppCompatActivity implements TaskCallBack {

    @BindView(R.id.progressbar_download)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_percentage)
    TextView mPercentage;

    @BindView(R.id.constraint_layout_history)
    ConstraintLayout constraintLayout;

    @BindString(R.string.retry)
    String mRetry;

    @BindString(R.string.network_error)
    String mNetworkError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_history);
        ButterKnife.bind(this);

        runTask();

    }

    private void runTask() {
        DownloadAllResultsTask downloadAllResultsTask = new DownloadAllResultsTask(this, mPercentage, this);
        downloadAllResultsTask.execute();
    }

    @Override
    public void onStartTask() {

    }

    @Override
    public void onTaskCompleted() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTaskError() {

        Snackbar snackbar = Snackbar
                .make(constraintLayout, mNetworkError, Snackbar.LENGTH_LONG).setAction(mRetry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        runTask();
                    }
                });

        snackbar.show();

    }
}
