package com.broscr.iptvplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.broscr.iptvplayer.databinding.CategoriesRowBinding;
import com.broscr.iptvplayer.utils.CategoryOnClick;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVHolder> {

    private final Context context;
    private final List<String> categories;
    private final CategoryOnClick categoryOnClick;

    public CategoryAdapter(Context context, List<String> categories, CategoryOnClick categoryOnClick) {
        this.context = context;
        this.categories = categories;
        this.categoryOnClick = categoryOnClick;
    }

    @NonNull
    @Override
    public CategoryVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryVHolder(CategoriesRowBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVHolder holder, int position) {
        String category = categories.get(position);

        if (category != null && !category.equals("")) {
            holder.binding.categoryRowName.setText(category);
            holder.binder(categoryOnClick, category);
        }
    }

    @Override
    public int getItemCount() {
        if (categories != null && categories.size() > 0) {
            return categories.size();
        } else {
            return 0;
        }
    }

    class CategoryVHolder extends RecyclerView.ViewHolder {

        private final CategoriesRowBinding binding;

        public CategoryVHolder(@NonNull CategoriesRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void binder(CategoryOnClick categoryOnClick, String category) {
            itemView.setOnClickListener(view -> categoryOnClick.categoryItem(category));
        }
    }
}
