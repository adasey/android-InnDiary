package induk.inn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BaseAdapterEx extends BaseAdapter {

    Context mContext = null;
    ArrayList<NoteItem> mData = null;
    LayoutInflater mLayoutInflater = null;

    public BaseAdapterEx(Context context, ArrayList<NoteItem> data){
        mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public NoteItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        ImageView mMoodImageTv;
        ImageView mWeatherImageTv;
        TextView mTitleTv;
        TextView mContentsTv;
        TextView mDateTv;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemLayout = convertView;
        ViewHolder viewHolder = null;

        // 1. 어뎁터 뷰가 재사용할 뷰를 넘겨주지 않은 경우에만 새로운 뷰를 생성
        // ============================================================================================
        if (itemLayout == null){
            itemLayout = mLayoutInflater.inflate(R.layout.note_item, null); // 인플레이션

            // View holder를 생성하고 사용할 자식뷰를 찾아 View Holder에 참조시킨다.
            // 생성된 View Holder는 아이템에 설정해 두고, 다음에 아이템 재사용시 참조한다.
            //-----------------------------------------------------------------------------------
            viewHolder = new ViewHolder();

            viewHolder.mMoodImageTv = (ImageView) itemLayout.findViewById(R.id.moodImageView);
            viewHolder.mWeatherImageTv = (ImageView) itemLayout.findViewById(R.id.weatherImageView);
            viewHolder.mTitleTv = (TextView) itemLayout.findViewById(R.id.titleTextView);
            viewHolder.mContentsTv = (TextView) itemLayout.findViewById(R.id.contentsTextView);

            viewHolder.mDateTv = (TextView) itemLayout.findViewById(R.id.dateTextView);

            itemLayout.setTag(viewHolder);
            //-----------------------------------------------------------------------------------

        } else{
            // 재사용 아이템에는 이전에 View Holder 객체를 설정해 두었다.
            // 그러므로 설정된 View Holder 객체를 이용해서 findViewById 함수를
            // 사용하지 않고 원하는 뷰를 참조할 수 있다.
            viewHolder = (ViewHolder) itemLayout.getTag();
        }
        // ========================================================================================

        // 2. 이름, 학번, 학과 데이터를 참조하여 레이아웃을 갱신
        // ====================================================================
        viewHolder.mMoodImageTv.setImageResource(mData.get(position).mMoodImage);
        viewHolder.mWeatherImageTv.setImageResource(mData.get(position).mWeatherImage);
        viewHolder.mTitleTv.setText(mData.get(position).mTitle);
        viewHolder.mContentsTv.setText(mData.get(position).mContents); // 변수에 데이터를 넣어주고
        viewHolder.mDateTv.setText(mData.get(position).mDate);
        // ===================================================================


        return itemLayout; // 그 레이아웃을 보내준다. 뷰홀더랑 아이템 레이아웃이 가르키는 곳이 동일하다.
    }

    public void add(int index, NoteItem addData){
        mData.add(index, addData);
        notifyDataSetChanged(); // notify 공지, 공지후 리스트뷰에게 넘겨준다.
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
