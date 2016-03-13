package com.teamagam.gimelgimel.app.view.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.view.viewer.GGMapView;
import com.teamagam.gimelgimel.app.view.viewer.data.KMLLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewerFragment extends BaseFragment<GGApplication> implements GoToDialogFragment.NoticeDialogListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GGMapView mGGMapView;

    private int stepNum = 0;

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

        final DialogFragment newFragment = new GoToDialogFragment();
        newFragment.setTargetFragment(ViewerFragment.this, 0);
        newFragment.show(getActivity().getFragmentManager(), "dialog");

        mGGMapView = (GGMapView) rootView.findViewById(R.id.gg_map_view);
//        ((View) mGGMapView).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                Toast.makeText(mApp, "Touch coordinates : " +
////                                String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()),
////                        Toast.LENGTH_SHORT)
////                        .show();
//                return true;
//            }
//        });

        FloatingActionButton test = (FloatingActionButton) rootView.findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {

            private final Point marker1 = new Point("marker123", new PointGeometry(32.5, 34.5));
            private final VectorLayer vl = new VectorLayer("uniqueid1");
            private final KMLLayer vkml1 = new KMLLayer("radarKML", "SampleData/kml/facilities.kml");
            private final KMLLayer vkml2 = new KMLLayer("waterKML", "SampleData/kml/MaineScubaDiving.kml");


            @Override
            public void onClick(View v) {
                switch (stepNum) {
                    case 0:
                        mGGMapView.zoomTo(32,32,200000);
                    default:
                        mGGMapView.readAsyncCenterPosition(new ValueCallback<PointGeometry>() {
                            @Override
                            public void onReceiveValue(PointGeometry point) {
                                Toast.makeText(mApp, String.format("N:%.4f E:%.4f", point.latitude, point.longitude), Toast.LENGTH_SHORT).show();
                            }
                        });
//                        Log.i(TAG_FRAGMENT, String.format("%s%s", point.latitude, point.longitude));
//                        Toast.makeText(mApp, String.format("%s%s", point.latitude, point.longitude), Toast.LENGTH_SHORT).show();
//                        throw new IllegalArgumentException("Too much steps");
                }
                stepNum++;
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


    @Override
    public void onPositionDialogPositiveClick(DialogFragment dialog, float x, float y, float z) {
        if (z == -1)
            mGGMapView.zoomTo(x,y);
        else
            mGGMapView.zoomTo(x,y,z);
    }

    @Override
    public void onPositionDialogNegativeClick(DialogFragment dialog) {
        //do nothing
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
