package com.utsg.csc301.team21.models;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mapwithmarker.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LotInfoBoxFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LotInfoBoxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LotInfoBoxFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "capacity";
    private static final String ARG_PARAM3 = "occupancy";
    private static final String ARG_PARAM4 = "pricePerHour";

    // Lot Detail parameters
    private String name;
    private int capacity;
    private int occupancy;
    private double pricePerHour;

    private OnFragmentInteractionListener mListener;

    public LotInfoBoxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name Name of the lot.
     * @param capacity The max capacity of the lot.
     * @param occupancy The current amount of cars in the lot.
     * @param pricePerHour The price per hour of the lot.
     * @return A new instance of fragment LotInfoBoxFragment.
     */
    public static LotInfoBoxFragment newInstance(String name, int capacity, int occupancy,
                                                 double pricePerHour) {
        LotInfoBoxFragment fragment = new LotInfoBoxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, name);
        args.putInt(ARG_PARAM2, capacity);
        args.putInt(ARG_PARAM3, occupancy);
        args.putDouble(ARG_PARAM4, pricePerHour);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.name = getArguments().getString(ARG_PARAM1);
            this.capacity = getArguments().getInt(ARG_PARAM2);
            this.occupancy = getArguments().getInt(ARG_PARAM3);
            this.pricePerHour = getArguments().getDouble(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View lotView = inflater.inflate(R.layout.fragment_lot_details_box, container, false);

        // Update text to match lot data
        ((TextView)(lotView.findViewById(R.id.infoboxName))).setText(this.name);
        ((TextView)(lotView.findViewById(R.id.infoboxSpaces))).append(" " + this.occupancy + "/" + this.capacity);
        ((TextView)(lotView.findViewById(R.id.infoboxCost))).append(" $" + this.pricePerHour);

        return lotView;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
