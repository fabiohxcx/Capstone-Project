package fabiohideki.com.megagenerator.ui;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.adapter.LotteryNearbyAdapter;
import fabiohideki.com.megagenerator.model.LotteryPlace;
import fabiohideki.com.megagenerator.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearLotteryFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.tv_your_location)
    TextView mTvYourLocation;

    @BindView(R.id.tv_distance)
    TextView mTvDistance;

    @BindView(R.id.rv_lottery)
    RecyclerView mRecyclerViewLottery;

    @BindView(R.id.sb_distance)
    SeekBar mSeekBarDistance;

    @BindView(R.id.cl_progress_bar)
    ConstraintLayout constraintLayoutProgressBar;

    @BindString(R.string.location_permission_denied)
    String mLocationPermissionDenied;

    @BindString(R.string.retry)
    String mRetryLabel;

    @BindString(R.string.network_error)
    String mNetworkErrorLabel;

    private LotteryNearbyAdapter lotteryNearbyAdapter;
    private List<LotteryPlace> mListLottery;
    private View mRootView;

    private static final int REQUEST = 113;
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Context context;

    private int mMinimumValueSeekBar = 5;
    private int mProgressStepSeekBar = 100;
    private int mDistanceMetersFinal = 1000;


    public NearLotteryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Utils.hasPermissions(context, PERMISSIONS)) {
                requestPermissions(PERMISSIONS, REQUEST);
            } else {
                buildGoogleApiClient();
            }
        } else {
            buildGoogleApiClient();
        }

    }

    private void setupSeekBar() {

        mTvDistance.setText(getString(R.string.distance) + ": " + (mDistanceMetersFinal) + " " + context.getString(R.string.meters_label));
        mSeekBarDistance.setProgress(5);
        mSeekBarDistance.setMax(95);
        mSeekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mDistanceMetersFinal = ((mMinimumValueSeekBar + progress) * mProgressStepSeekBar);

                if (mDistanceMetersFinal > 999) {
                    float distanceInKm = (mDistanceMetersFinal / 1000);
                    mTvDistance.setText(getString(R.string.distance) + ": " + distanceInKm + " " + getString(R.string.km_label));
                } else {
                    mTvDistance.setText(getString(R.string.distance) + ": " + mDistanceMetersFinal + "" + getString(R.string.meters_label));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mListLottery = null;

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    getCurrentLocation(mLastLocation);

                } else {
                    requestPermissions(PERMISSIONS, REQUEST);
                }
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewLottery.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewLottery.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.line));

        mRecyclerViewLottery.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();

                    if (mGoogleApiClient != null) {
                        mGoogleApiClient.connect();
                    }

                } else {
                    permissionSnackBar();
                }
            }
        }
    }

    private void permissionSnackBar() {
        Snackbar snackbar = Snackbar.make(mRootView, mLocationPermissionDenied, Snackbar.LENGTH_LONG);

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        TextView tvText = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
        TextView tvAction = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_action);

        Typeface font = Typer.set(context).getFont(Font.ROBOTO_MEDIUM);
        tvText.setTypeface(font);

        Typeface font2 = Typer.set(context).getFont(Font.ROBOTO_BOLD);
        tvAction.setTypeface(font2);

        snackbar.setAction(mRetryLabel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(PERMISSIONS, REQUEST);
            }
        });

        snackbar.show();
    }

    private void networkErrorSnackBar() {
        Snackbar snackbar = Snackbar.make(mRootView, mNetworkErrorLabel, Snackbar.LENGTH_LONG);

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        TextView tvText = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
        TextView tvAction = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_action);

        Typeface font = Typer.set(context).getFont(Font.ROBOTO_MEDIUM);
        tvText.setTypeface(font);

        Typeface font2 = Typer.set(context).getFont(Font.ROBOTO_BOLD);
        tvAction.setTypeface(font2);

        snackbar.setAction(mRetryLabel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation(mLastLocation);
            }
        });

        snackbar.show();
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_near_by_lottery, container, false);
        ButterKnife.bind(this, mRootView);

        setupRecyclerView();
        setupSeekBar();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        return mRootView;
    }


    @Override
    public void onDestroyView() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onDestroyView();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            getCurrentLocation(mLastLocation);
        }
    }


    private void getCurrentLocation(Location location) {

        if (location != null) {

            double lat = location.getLatitude(), lon = location.getLongitude();
            Address locationAddress = getAddress(lat, lon);

            if (locationAddress != null) {
                String address0 = locationAddress.getAddressLine(0);
                mTvYourLocation.setText(address0);

                if (mListLottery == null || mListLottery.isEmpty()) {
                    getLotteryPlacesWebService(lat, lon, mDistanceMetersFinal);
                } else {
                    lotteryNearbyAdapter = new LotteryNearbyAdapter(mListLottery, getActivity(), mLastLocation);
                    mRecyclerViewLottery.setAdapter(lotteryNearbyAdapter);
                    constraintLayoutProgressBar.setVisibility(View.GONE);
                }
            }

        } else {
            networkErrorSnackBar();
        }
    }

    private void getLotteryPlacesWebService(double lat, double lon, int radius) {

        constraintLayoutProgressBar.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?language=" + Locale.getDefault().toString() +
                "&location=" + lat + "," + lon + "&radius=" + radius + "&keyword=loterica&key=" + getString(R.string.api_key);

        Log.d("Fabio", "getCurrentLocation: " + url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTvTest.setText("Response is: " + response.toString());
                        Log.d("Fabio", "Response: " + response);


                        JsonParser jsonParser = new JsonParser();
                        JsonObject jo = (JsonObject) jsonParser.parse(response);
                        JsonArray jsonArr = jo.getAsJsonArray("results");

                        Gson gson = new Gson();
                        mListLottery = gson.fromJson(jsonArr, new TypeToken<List<LotteryPlace>>() {
                        }.getType());

                        lotteryNearbyAdapter = new LotteryNearbyAdapter(mListLottery, getActivity(), mLastLocation);

                        mRecyclerViewLottery.setAdapter(lotteryNearbyAdapter);
                        constraintLayoutProgressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkErrorSnackBar();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.iv_update_location)
    public void refreshCurrentLocation(View view) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            getCurrentLocation(mLastLocation);
            Toast.makeText(context, getString(R.string.updating_location), Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions(PERMISSIONS, REQUEST);
        }
    }


    public Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            return (Address) addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

