package com.jegumi.shopping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jegumi.shopping.R;
import com.jegumi.shopping.model.Category;

import java.util.List;

public class MenuArrayAdapter extends ArrayAdapter<Category> {

    public static class ViewHolder {
        private TextView captionTextView;
    }

    private Context context;

    public MenuArrayAdapter(Context context, List<Category> objects) {
        super(context, R.layout.menu_item, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Category category = getItem(position);
        if (convertView == null) {
            convertView = inflateView();
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.captionTextView.setText(category.Name);

        return convertView;
    }

    private View inflateView() {
        final LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(com.jegumi.shopping.R.layout.menu_item, null);

        ViewHolder holder = new ViewHolder();
        holder.captionTextView = (TextView) view.findViewById(com.jegumi.shopping.R.id.category_name_text_view);
        view.setTag(holder);

        return view;
    }
}

