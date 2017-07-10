package com.example.moiz_ihs.contracttracing.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moiz_ihs.contracttracing.views.activities.R;
import com.example.moiz_ihs.contracttracing.models.ContactDetail;
import com.example.moiz_ihs.contracttracing.utils.Constants;
import com.example.moiz_ihs.contracttracing.views.activities.ContactTraceActivity;
import com.example.moiz_ihs.contracttracing.views.activities.ContactFormActivity;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Moiz-IHS on 5/30/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    public List<ContactDetail> data;
    private  Context context;
    public ContactFormActivity fragment;

    public ContactsAdapter(List<ContactDetail> data)
    {
        this.data = data;
        fragment = new ContactFormActivity();
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item,parent,false);
       ContactsViewHolder holder = new ContactsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
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
        holder.indexId.setText("Gender : "+data.get(position).getGender()+"   Age : " + String.valueOf(age) );
        //holder.item.setOnClickListener(new itemListener(position));



        if(data.get(position).getContactFormCompleted() == true) {
            holder.rowImage.setImageResource(R.drawable.success);
          //  holder.item.setBackgroundColor(context.getResources().getColor(R.color.aluminum));
            holder.item.setOnClickListener(null);
        }
        else
        {

            holder.item.setOnClickListener(new itemListener(position));

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
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /**
     * ViewHolder Of this RecyclerView
     */
    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView contactName;
        public TextView contactId;
        public TextView indexId;
        public LinearLayout item;
        public ImageView rowImage;

        public ContactsViewHolder(View itemView) {
            super(itemView);
           contactName = (TextView) itemView.findViewById(R.id.contact_name);
           contactId = (TextView) itemView.findViewById(R.id.contact_id);
           indexId = (TextView) itemView.findViewById(R.id.index_id);
           item = (LinearLayout) itemView.findViewById(R.id.item_layout);
            rowImage = (ImageView) itemView.findViewById(R.id.dp);

        }
    }

    private class itemListener implements View.OnClickListener {

        int position;
        public itemListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v)
        {
            startFormFragment(data.get(position),position);
        }
    }

    private void startFormFragment(ContactDetail contactDetail, int position) {
        Intent intent = new Intent(context,ContactFormActivity.class);
        Gson gson = new Gson();
        String contactDetailJson = gson.toJson(contactDetail);
        intent.putExtra("contactDetail",contactDetailJson);
        intent.putExtra("position", position);
        ((ContactTraceActivity)context).startActivityForResult(intent,Constants.REQUEST_CODE);

    }




    public void removeFragment()
    {
        //((ContactTraceActivity)context).getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }
}
