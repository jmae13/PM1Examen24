package com.example.pm1examen3.configuracion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.pm1examen3.R;

import java.util.ArrayList;


public class Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<medicamentos> listItem;
    private ArrayList<medicamentos> filterlist;
    private CustomFilter filter;



    public Adapter(Context context, ArrayList<medicamentos> listItem) {
        this.context = context;
        this.listItem = listItem;

        this.filterlist = listItem;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int i) {
        return listItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        medicamentos item = (medicamentos) getItem(i);

        view = LayoutInflater.from(context).inflate(R.layout.next, null);


        TextView titulo =(TextView) view.findViewById(R.id.itemTitulo);
        TextView descripcion =(TextView) view.findViewById(R.id.itemNote);
        TextView id =(TextView) view.findViewById(R.id.itemObjetId);

        titulo.setText(item.toString());
        descripcion.setText(item.getCantidad());
        id.setText(item.getId()+"");



        return view;
    }


    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults filterResults = new FilterResults();

            if(charSequence != null && charSequence.length()>0){

                charSequence = charSequence.toString().toUpperCase();

                ArrayList<medicamentos> filters = new ArrayList<medicamentos>();

                for(int i = 0;i < filterlist.size(); i++){

                    if(filterlist.get(i).getDescripcion().toUpperCase().contains(charSequence)){

                        filters.add(filterlist.get(i));
                    }
                }

                filterResults.count = filters.size();
                filterResults.values = filters;

            }else {

                filterResults.count = filterlist.size();
                filterResults.values = filterlist;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            listItem = (ArrayList<medicamentos>) filterResults.values;
            notifyDataSetChanged();
        }
    }

    public Filter getFilter(){

        if(filter == null){
            filter = new CustomFilter();
        }

        return filter;
    }

    public ArrayList<medicamentos> getFilterlist(){
        return filterlist;
    }

}


