package com.cse5236groupthirteen.utilities;

import java.util.ArrayList;

import com.cse5236groupthirteen.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	 
    private ArrayList<MessageDetail> _data;
    Context _c;
    
    public CustomAdapter (Context c, ArrayList<MessageDetail> data){
        _data = data;
        _c = c;
    }
   
    public int getCount() {
        // TODO Auto-generated method stub
        return _data.size();
    }
    
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return _data.get(position);
    }
 
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
   
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
         View v = convertView;
         if (v == null)
         {
            LayoutInflater vi = (LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_row, null);
         }
 
           ImageView image = (ImageView) v.findViewById(R.id.list_image);
           TextView CommentsView = (TextView)v.findViewById(R.id.list_comments);
           TextView WtimeView = (TextView)v.findViewById(R.id.list_waitingtime);
           TextView timeView = (TextView)v.findViewById(R.id.list_time);
 
           MessageDetail msg = _data.get(position);
           int rate=msg.getIcon();
           if(rate==1){
        	   image.setImageResource(R.drawable.rating_happy);
           }
           if(rate==0){
        	   image.setImageResource(R.drawable.rating_neutral);
           }
           if(rate==-1){
        	   image.setImageResource(R.drawable.rating_sad);
           }
           CommentsView.setText("Comments: "+msg.getComm());
           WtimeView.setText("Waiting time:"+msg.getWaitingTime()+"seconds");
           timeView.setText("("+msg.getTime()+")");                             
                        
        return v;
}
}
