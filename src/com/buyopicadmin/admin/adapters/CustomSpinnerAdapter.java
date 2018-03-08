package com.buyopicadmin.admin.adapters;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyopicadmin.admin.R;
import com.buyopicadmin.admin.models.OrderList;
 
/***** Adapter class extends with ArrayAdapter ******/
public class CustomSpinnerAdapter extends ArrayAdapter<String>{
     
    private Context activity;
    private ArrayList data;
    public Resources res;
    LayoutInflater inflater;
    OrderList mOrderItem;
     
    /*************  CustomAdapter Constructor *****************/
    public CustomSpinnerAdapter(
    		Context ordersFragment, 
                          int textViewResourceId,   
                          ArrayList objects,
                          Resources resLocal,
                          OrderList orderItem
                         ) 
     {
        super(ordersFragment, textViewResourceId, objects);
         
        /********** Take passed values **********/
        activity = ordersFragment;
        data     = objects;
        res      = resLocal;
        mOrderItem=orderItem;
    
        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
      }
 
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {
 
        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_rows, parent, false);
        
        TextView orderstatus=(TextView) row.findViewById(R.id.orderstatus);
       orderstatus.setText(data.get(position).toString());
        ImageView orderStatusImage=(ImageView)row.findViewById(R.id.image);
		if (data.get(position).toString().equalsIgnoreCase("inbox")) {
			orderStatusImage.setImageResource(R.drawable.icon_message);
		}  if (data.get(position).toString().equalsIgnoreCase("confirmed")) {
			orderStatusImage.setImageResource(R.drawable.icon_confirmed);
		}  if (data.get(position).toString().equalsIgnoreCase("dispatched")) {
			orderStatusImage.setImageResource(R.drawable.icon_dispatched);
		}  if (data.get(position).toString().equalsIgnoreCase("delivered")) {
			orderStatusImage.setImageResource(R.drawable.icon_delivered);
		}  if (data.get(position).toString().equalsIgnoreCase("canceled")) {
			orderStatusImage.setImageResource(R.drawable.icon_canceled);
		}
        
       /* switch (position) {
		case 0:
			orderStatusImage.setImageResource(R.drawable.icon_message);
			break;
		case 1:
			orderStatusImage.setImageResource(R.drawable.icon_confirmed);
			break;
		case 2:
			orderStatusImage.setImageResource(R.drawable.icon_dispatched);
			break;
		case 3:
			orderStatusImage.setImageResource(R.drawable.icon_delivered);
			break;
		case 4:
			orderStatusImage.setImageResource(R.drawable.icon_canceled);
			break;

		default:
			break;
		}
*/        /***** Get each Model object from Arraylist ********/
         
         
       
 
        return row;
      }
 }