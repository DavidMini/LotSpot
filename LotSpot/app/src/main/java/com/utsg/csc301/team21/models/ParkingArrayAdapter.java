package com.utsg.csc301.team21.models;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mapwithmarker.R;
import com.utsg.csc301.team21.models.AbstractParkingLot;

import java.util.Collection;


public class ParkingArrayAdapter extends ArrayAdapter<AbstractParkingLot> {


    private Context context;
    private LayoutInflater mInflater;
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a View to use when
     */
    public ParkingArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        //parkingLotList = new ArrayList<>(20);
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    @Override
    public void add(@Nullable AbstractParkingLot object) {
        super.add(object);
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this list
     * @throws NullPointerException          if the specified collection contains one
     *                                       or more null elements and this list does not permit null
     *                                       elements, or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this list
     */
    @Override
    public void addAll(@NonNull Collection<? extends AbstractParkingLot> collection) {
        super.addAll(collection);
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    @Override
    public void remove(@Nullable AbstractParkingLot object) {
        super.remove(object);
    }

    /**
     * Remove all elements from the list.
     */
    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, parent, R.layout.list_item_parking);
        //return super.getView(position, convertView, parent);
    }


    private @NonNull View createViewFromResource(@NonNull LayoutInflater inflater, int position,
                                                 @Nullable View convertView, @NonNull ViewGroup parent, int resource) {
        final View view;
        final RelativeLayout itemView;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            itemView = (RelativeLayout) view;
            TextView name = (TextView) itemView.findViewById(R.id.nameLotTextView);
            TextView price = (TextView) itemView.findViewById(R.id.priceTextView);
            TextView spaceCount = (TextView) itemView.findViewById(R.id.spaceCountTextView);
            checkView(name, R.id.nameLotTextView);
            checkView(price, R.id.priceTextView);
            checkView(spaceCount, R.id.spaceCountTextView);

            AbstractParkingLot lot = super.getItem(position);
            name.setText(lot.getName());
            price.setText(lot.getPricePerHour().toPlainString());
            spaceCount.setText( String.valueOf(lot.getCapacity()-lot.getOccupancy())
                    +"/" + String.valueOf(lot.getCapacity())); //display like 4/10

        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }
        return view;
    }

    private void checkView(View v, int id){
        if(v==null){
            throw new RuntimeException("Failed to find view with id "+
            context.getResources().getResourceName(id) + " in lotItemLayout" );
        }
    }

}
