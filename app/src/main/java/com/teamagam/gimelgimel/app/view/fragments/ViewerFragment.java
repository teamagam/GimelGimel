package com.teamagam.gimelgimel.app.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.view.viewer.GGMapView;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.MultiPointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polygon;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polyline;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewerFragment extends BaseFragment<GGApplication> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GGMapView mGGMapView;

    private boolean flag = false;

    public ViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewerFragment newInstance(String param1, String param2) {
        ViewerFragment fragment = new ViewerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mGGMapView = (GGMapView) rootView.findViewById(R.id.gg_map_view);

        FloatingActionButton test = (FloatingActionButton) rootView.findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {

            private final Point marker1 = new Point("marker123", new PointGeometry(32.5, 34.5));
            private final VectorLayer vl = new VectorLayer("uniqueid1");

            @Override
            public void onClick(View v) {
                if (!flag) {
                    vl.addEntity(marker1);

                    mGGMapView.addLayer(vl);

                    Collection<PointGeometry> locs = new ArrayList<PointGeometry>();
                    locs.add(new PointGeometry(34.3, 35.3));
                    locs.add(new PointGeometry(34.33, 35.44));

                    MultiPointGeometry polylineMpg = new MultiPointGeometry(locs);

                    Polyline pl = new Polyline("polyline", polylineMpg);
                    vl.addEntity(pl);

                    Collection<PointGeometry> polygonLocs = new ArrayList<PointGeometry>();
                    polygonLocs.add(new PointGeometry(32.1, 34.77));
                    polygonLocs.add(new PointGeometry(32.1, 34.85));
                    polygonLocs.add(new PointGeometry(32, 34.85));
                    polygonLocs.add(new PointGeometry(32, 34.73));

                    MultiPointGeometry polygonMpg = new MultiPointGeometry(polygonLocs);

                    Polygon polygon = new Polygon("polygon", polygonMpg);
                    vl.addEntity(polygon);

                    flag = true;
                } else {
                    Point marker2 = new Point("marker2", new PointGeometry(31.4, 34.6));

                    vl.addEntity(marker2);
                    marker1.updateGeometry(new PointGeometry(34.4, 35.5));
                }
            }
        });

        return rootView;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cesium;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
