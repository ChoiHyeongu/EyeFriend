package songpatechnicalhighschool.motivation.eyefriend.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import songpatechnicalhighschool.motivation.eyefriend.Activity.InduceActivity;
import songpatechnicalhighschool.motivation.eyefriend.Fragment.ScanFragment;
import songpatechnicalhighschool.motivation.eyefriend.R;

public class CustomAdapter extends BaseAdapter {

    private List<String> macsArrayList = new ArrayList<String>();
    private String mac;
    private List<String> names = new ArrayList<String>();
    private List<String> locates = new ArrayList<String>();
    private Context mContext = null;
    private int sz = 0;

    public CustomAdapter() {
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void addElement(String mac, String name, String locate) {
        macsArrayList.add(mac);
        names.add(name);
        locates.add(locate);
        sz++;
    }

    public int getCount() {
        return (sz);
    }

    @Override
    public Object getItem(int position) {
        return macsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position >= getCount())
            return (convertView);

        final ViewHolder holder;
        ScanFragment scanFragment = new ScanFragment();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_device, parent, false);
            holder = new ViewHolder();
            holder.itemLayout = convertView.findViewById(R.id.itme_device_layout);
            holder.deviceName = convertView.findViewById(R.id.item_device_name);
            holder.locateName = convertView.findViewById(R.id.item_device_locate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.deviceName.setText(names.get(position));
        holder.locateName.setText(locates.get(position));
        return convertView;
    }

    public String getMacs(int position) {
        return macsArrayList.get(position);
    }

    private class ViewHolder {
        TextView deviceName;
        TextView locateName;
        LinearLayout itemLayout;
    }
}