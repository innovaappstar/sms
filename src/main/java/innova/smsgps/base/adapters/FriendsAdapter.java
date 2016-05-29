package innova.smsgps.base.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import innova.smsgps.CircleImageView;
import innova.smsgps.R;
import innova.smsgps.application.Globals;
import innova.smsgps.constantes.CONSTANT;
import innova.smsgps.entities.Friend;
import innova.smsgps.utils.Utils;

/**
 * Created by USUARIO on 21/05/2016.
 */
public class FriendsAdapter extends BaseAdapter
{
    ArrayList<Friend> alFriend      = new ArrayList<Friend>();
    LayoutInflater inflater;
    Context context;


    public FriendsAdapter(ArrayList<Friend> alFriend, Context context) {
        this.alFriend = alFriend;
        this.context = context;
        inflater            = LayoutInflater.from(this.context);
    }
    @Override
    public int getCount() {
        return alFriend.size();
    }

    @Override
    public Friend getItem(int position)
    {
        return alFriend.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_list_friends, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Friend friend = getItem(position);
        viewHolder.tvNombreFriend.setText(friend.getNombre());
        try
        {
            Globals.imageLoader.displayImage(CONSTANT.PATH_WS_IMAGES + friend.getFotoURL(), viewHolder.ivFriend);
        }catch (Exception e)
        {
            Utils.LOG(e.getMessage() + " EXCEPTION IMAGELOADER");
        }

        return convertView;
    }



    private class ViewHolder
    {
        CircleImageView ivFriend ;
        TextView tvNombreFriend;

        public ViewHolder(View item)
        {
            tvNombreFriend    = (TextView) item.findViewById(R.id.tvNombreFriend);
            ivFriend    = (CircleImageView) item.findViewById(R.id.ivFriend);
        }
    }
}

