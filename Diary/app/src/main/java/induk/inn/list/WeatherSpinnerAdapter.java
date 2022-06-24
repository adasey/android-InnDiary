package induk.inn.list;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import induk.inn.R;

public class WeatherSpinnerAdapter extends ArrayAdapter<String> {

    String[] spinnerNames;
    int[] spinnerImages;
    Context mContext;

    public WeatherSpinnerAdapter(@NonNull Context context, String[] names, int[] images) {
        super(context, R.layout.spinner_row);

        this.spinnerNames = names;
        this.spinnerImages = images;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return spinnerNames.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder mViewHolder = new ViewHolder();

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_row, parent, false);

            mViewHolder.mImageTv = (ImageView) convertView.findViewById(R.id.imageview_spinner_image);
            mViewHolder.mNameTv = (TextView) convertView.findViewById(R.id.textview_spinner_name);
            convertView.setTag(mViewHolder);

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mImageTv.setImageResource(spinnerImages[position]);
        mViewHolder.mNameTv.setText(spinnerNames[position]);

        return convertView;
    }

    private static class ViewHolder {

        ImageView mImageTv;
        TextView mNameTv;
    }
}
