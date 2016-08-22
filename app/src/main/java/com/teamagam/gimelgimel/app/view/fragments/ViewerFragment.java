package com.teamagam.gimelgimel.app.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.map.viewModel.IMapView;
import com.teamagam.gimelgimel.app.map.viewModel.MapViewModel;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.utils.ImageUtil;
import com.teamagam.gimelgimel.app.view.MainActivity;
import com.teamagam.gimelgimel.app.view.viewer.GGMap;
import com.teamagam.gimelgimel.app.view.viewer.GGMapGestureListener;
import com.teamagam.gimelgimel.app.view.viewer.GGMapView;
import com.teamagam.gimelgimel.app.view.viewer.OnGGMapReadyListener;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Viewer Fragmant that handles all map events.
 */
public class ViewerFragment extends BaseFragment<GGApplication> implements OnGGMapReadyListener,
        IMapView {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String IMAGE_URI_KEY = "IMAGE_CAMERA_URI";

    @BindView(R.id.gg_map_view)
    GGMapView mGGMapView;

    private Uri mImageUri;
    private boolean mIsRestored;

    @Inject
    MapViewModel mMapViewModel;

//    private MessageMapEntitiesViewModel mMessageLocationVM;
//    private UsersLocationViewModel mUserLocationsVM;

    @Override
    @NotNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((MainActivity) getActivity()).getMapComponent().inject(this);
        mMapViewModel.setMapView(this);

        mGGMapView.setGGMapGestureListener(new GGMapGestureListener(this, mGGMapView));

        if (savedInstanceState != null) {
            mGGMapView.restoreViewState(savedInstanceState);
            mIsRestored = true;
        } else {
            mIsRestored = false;
        }

        secureGGMapViewInitialization();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapViewModel.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapViewModel.pause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mGGMapView.saveViewState(outState);
        outState.putParcelable(IMAGE_URI_KEY, mImageUri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mImageUri = savedInstanceState.getParcelable(IMAGE_URI_KEY);
        }
    }

    //image sending
    public void takePicture() {
        sLogger.userInteraction("Start camera activity button clicked");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // place where to store camera taken picture
        try {
            mImageUri = ImageUtil.getTempImageUri(mApp);
        } catch (IOException e) {
            sLogger.w("Can't create file to take picture!");
            return;
        }

        if (mImageUri != null) {
            sLogger.d(mImageUri.getPath());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            //start camera intent
            if (takePictureIntent.resolveActivity(mApp.getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            sLogger.w("image uri is null");
            Toast.makeText(mApp, "problem with taking images", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            mMapViewModel.sendCapturedImage(isImageCaptured(resultCode), mImageUri);
        }
    }

    private void secureGGMapViewInitialization() {
        if (mGGMapView.isReady()) {
            onGGMapViewReady();
        } else {
            mGGMapView.setOnReadyListener(this);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cesium;
    }

    @Override
    public void onDetach() {
        mMapViewModel.destroy();
        super.onDetach();
    }

    public void goToLocation(PointGeometry pointGeometry) {
        mGGMapView.flyTo(pointGeometry);
    }

    @Override
    public void addLayer(VectorLayer vectorLayer) {
        mGGMapView.addLayer(vectorLayer);
    }

    public GGMap getGGMap() {
        return mGGMapView;
    }

    @Override
    public void onGGMapViewReady() {
        if (!mIsRestored) {
            setInitialMapExtent();
        }
        mMapViewModel.mapReady();
    }

    public void clearSentLocationsLayer() {
        mMapViewModel.clearSentLocationsLayer();
    }

    public void clearReceivedLocationsLayer() {
        mMapViewModel.clearReceivedLocationsLayer();
    }

    private boolean isImageCaptured(int resultCode) {
        return resultCode == Activity.RESULT_OK && mImageUri != null;
    }

    /**
     * Sets GGMapView extent to configured bounding box values
     */
    private void setInitialMapExtent() {
        float east = Constants.MAP_VIEW_INITIAL_BOUNDING_BOX_EAST;
        float west = Constants.MAP_VIEW_INITIAL_BOUNDING_BOX_WEST;
        float north = Constants.MAP_VIEW_INITIAL_BOUNDING_BOX_NORTH;
        float south = Constants.MAP_VIEW_INITIAL_BOUNDING_BOX_SOUTH;
        mGGMapView.setExtent(west, south, east, north);
    }

    public void addMessageLocationPin(Message message) {
        mMapViewModel.addMessageLocationPin(message);
    }
}
