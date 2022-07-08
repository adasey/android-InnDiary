package induk.inn.rest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import induk.inn.R;


public class RestAdapterEx extends BaseAdapter {

    Context mContext = null;
    ArrayList<RestItem> mData = null;
    LayoutInflater mLayoutInflater = null;

    public RestAdapterEx(Context context, ArrayList<RestItem> data){
        mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public RestItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        TextView mLeftMonthTv;
        TextView mLeftNameTv;
        TextView mLeftDayTv;

        LinearLayout mRightLy;
        TextView mRightMonthTv;
        TextView mRightNameTv;
        TextView mRightDayTv;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemLayout = convertView;
        ViewHolder viewHolder = null;

        // 1. 어뎁터 뷰가 재사용할 뷰를 넘겨주지 않은 경우에만 새로운 뷰를 생성
        // ============================================================================================
        if (itemLayout == null){
            itemLayout = mLayoutInflater.inflate(R.layout.rest_item, null); // 인플레이션

            // View holder를 생성하고 사용할 자식뷰를 찾아 View Holder에 참조시킨다.
            // 생성된 View Holder는 아이템에 설정해 두고, 다음에 아이템 재사용시 참조한다.
            //-----------------------------------------------------------------------------------
            viewHolder = new ViewHolder();

            viewHolder.mLeftMonthTv = (TextView) itemLayout.findViewById(R.id.left_rest_month);
            viewHolder.mLeftNameTv = (TextView) itemLayout.findViewById(R.id.left_rest_name);
            viewHolder.mLeftDayTv = (TextView) itemLayout.findViewById(R.id.left_rest_day);

            viewHolder.mRightLy = (LinearLayout) itemLayout.findViewById(R.id.right_layout);
            viewHolder.mRightMonthTv = (TextView) itemLayout.findViewById(R.id.right_rest_month);
            viewHolder.mRightNameTv = (TextView) itemLayout.findViewById(R.id.right_rest_name);
            viewHolder.mRightDayTv = (TextView) itemLayout.findViewById(R.id.right_rest_day);


            itemLayout.setTag(viewHolder);
            //-----------------------------------------------------------------------------------

        } else{
            // 재사용 아이템에는 이전에 View Holder 객체를 설정해 두었다.
            // 그러므로 설정된 View Holder 객체를 이용해서 findViewById 함수를
            // 사용하지 않고 원하는 뷰를 참조할 수 있다.
            viewHolder = (ViewHolder) itemLayout.getTag();
        }
        // ========================================================================================

        // 2. 데이터를 참조하여 레이아웃을 갱신
        // ====================================================================

            viewHolder.mLeftMonthTv.setText(mData.get(position).mLeftMoth);
            viewHolder.mLeftNameTv.setText(mData.get(position).mLeftName);
            viewHolder.mLeftDayTv.setText(mData.get(position).mLeftDay);


            viewHolder.mRightMonthTv.setText(mData.get(position).mRightMoth);
            viewHolder.mRightNameTv.setText(mData.get(position).mRightName);
            viewHolder.mRightDayTv.setText(mData.get(position).mRightDay);
            viewHolder.mRightLy.setVisibility(mData.get(position).mRightLayout);

        // ===================================================================


        return itemLayout; // 그 레이아웃을 보내준다. 뷰홀더랑 아이템 레이아웃이 가르키는 곳이 동일하다.
    }

    public void add(int index, RestItem addData){
        mData.add(index, addData);
        notifyDataSetChanged(); // notify 공지, 공지후 리스트뷰에게 넘겨준다.
    }

    public void update(int index, RestItem addData){
        mData.set(index, addData);
        notifyDataSetChanged();
    }

    public void delete(int index){
        mData.remove(index);
        notifyDataSetChanged();
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }
}
