package fabiohideki.com.megagenerator.ui;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.adapter.LotteryNearbyAdapter;
import fabiohideki.com.megagenerator.model.LotteryPlace;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearByLotteryFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback {

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

    private LotteryNearbyAdapter lotteryNearbyAdapter;
    private List<LotteryPlace> listLottery;

    private static final int REQUEST = 113;
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Context context;

    private int mMinimumValueSeekBar = 5;
    private int mProgressStepSeekBar = 100;
    private int mDistanceMetersFinal = 1000;


    public NearByLotteryFragment() {
        // Required empty public constructor
    }


    private void setupSeekBar() {

        mTvDistance.setText(getString(R.string.distance) + ": " + (mDistanceMetersFinal) + " m");
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        if (Build.VERSION.SDK_INT >= 23) {
            if (!hasPermissions(context, PERMISSIONS)) {
                requestPermissions(PERMISSIONS, REQUEST);
            } else {
                buildGoogleApiClient();
            }
        } else {
            buildGoogleApiClient();
        }

    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
                    Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
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

        View rootView = inflater.inflate(R.layout.fragment_near_by_lottery, container, false);
        ButterKnife.bind(this, rootView);

        setupRecyclerView();
        setupSeekBar();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        return rootView;
    }


    @Override
    public void onDestroyView() {
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

                getLotteryPlacesWebService(lat, lon, mDistanceMetersFinal);
            }

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
                        Log.d("Fabio", "Response: " + response.toString());


                        JsonParser jsonParser = new JsonParser();
                        JsonObject jo = (JsonObject) jsonParser.parse(response.toString());
                        JsonArray jsonArr = jo.getAsJsonArray("results");

                        Gson gson = new Gson();
                        listLottery = gson.fromJson(jsonArr, new TypeToken<List<LotteryPlace>>() {
                        }.getType());

                        lotteryNearbyAdapter = new LotteryNearbyAdapter(listLottery, getActivity(), mLastLocation);

                        mRecyclerViewLottery.setAdapter(lotteryNearbyAdapter);
                        constraintLayoutProgressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTvTest.setText("That didn't work!");
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

            Toast.makeText(context, getString(R.string.updating_location), Toast.LENGTH_SHORT).show();
            getCurrentLocation(mLastLocation);

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

