package fabiohideki.com.megagenerator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_history);
        ButterKnife.bind(this);

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

    }
}
