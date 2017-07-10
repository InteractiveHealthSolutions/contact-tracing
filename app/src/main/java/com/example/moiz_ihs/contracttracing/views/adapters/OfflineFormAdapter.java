package com.example.moiz_ihs.contracttracing.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moiz_ihs.contracttracing.views.activities.R;
import com.example.moiz_ihs.contracttracing.models.ContactDetail;
import com.example.moiz_ihs.contracttracing.views.activities.ContactFormActivity;

import java.util.List;

/**
 * Created by Moiz-IHS on 6/21/2017.
 */

public class OfflineFormAdapter extends RecyclerView.Adapter<OfflineFormAdapter.OfflineFormViewHolder>{

    public List<ContactDetail> data;
    private Context context;

    public OfflineFormAdapter( List<ContactDetail> data)
    {
        this.data = data;

    }

    @Override
    public OfflineFormViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item,parent,false);
        OfflineFormAdapter.OfflineFormViewHolder holder = new OfflineFormAdapter.OfflineFormViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OfflineFormViewHolder holder, int position) {
        int age = 0;
        if(!data.get(position).getAge().equals("")) {
            age = (int) Double.parseDouble(data.get(position).getAge());
        }
        else
        {
            age = 0;
        }


        holder.contactName.setText("Name : "+data.get(position).getContactName());
        holder.contactId.setText("Contact ID : "+data.get(position).getContactId());
        holder.indexId.setText("Gender : "+data.get(position).getGender()+"   Age : " + data.get(position).getAge() );

        if(data.get(position).getGender().equals("MALE"))
        {
            if(age >16)  // Man
            {
                holder.rowImage.setImageResource(R.drawable.ic_man2);
            }
            else  // boy
            {
                holder.rowImage.setImageResource(R.drawable.ic_man2);
            }

        }
        else if(data.get(position).getGender().equals("FEMALE"))
        {
            if(age >16)  // WoMan
            {
                holder.rowImage.setImageResource(R.drawable.ic_woman2);
            }
            else  // girl
            {
                holder.rowImage.setImageResource(R.drawable.ic_woman2);
            }


        }
      //  holder.item.setOnClickListener(new ContactsAdapter.itemListener(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }




    public class OfflineFormViewHolder extends RecyclerView.ViewHolder {
        public TextView contactName;
        public TextView contactId;
        public TextView indexId;
        public LinearLayout item;
        public ImageView rowImage;

        public OfflineFormViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            contactId = (TextView) itemView.findViewById(R.id.contact_id);
            indexId = (TextView) itemView.findViewById(R.id.index_id);
            item = (LinearLayout) itemView.findViewById(R.id.item_layout);
            rowImage = (ImageView) itemView.findViewById(R.id.dp);

        }
    }
}
