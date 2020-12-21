package com.jcs.where.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.api.response.ParentCategoryResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by zyf on 2020/12/14 9:55 PM
 */
public class CategoryGroupAdapter extends BaseNodeAdapter {

    public CategoryGroupAdapter() {
        super();
        addFullSpanNodeProvider(new GroupProvider());
        addNodeProvider(new ChildProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> list, int position) {
        BaseNode node = list.get(position);
        if (node instanceof ParentCategoryResponse) {
            return 0;
        } else if (node instanceof CategoryResponse) {
            return 1;
        }
        return -1;
    }

    public static class GroupProvider extends BaseNodeProvider {

        @Override
        public int getItemViewType() {
            return 0;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_category_expand_group;
        }

        @Override
        public void convert(@NotNull BaseViewHolder baseViewHolder, BaseNode baseNode) {
            ParentCategoryResponse parent = (ParentCategoryResponse) baseNode;
            baseViewHolder.setText(R.id.categoryGroupTitle, parent.getName());
            baseViewHolder.setGone(R.id.line, baseViewHolder.getAdapterPosition() == 0);
        }
    }

    public static class ChildProvider extends BaseNodeProvider {

        @Override
        public int getItemViewType() {
            return 1;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_category_expand_child;
        }

        @Override
        public void convert(@NotNull BaseViewHolder baseViewHolder, BaseNode baseNode) {
            CategoryResponse category = (CategoryResponse) baseNode;
            baseViewHolder.setText(R.id.categoryChildTitle, category.getName());
            ImageView childIcon = baseViewHolder.findView(R.id.categoryChildIcon);
            if (childIcon != null) {
                Glide.with(getContext()).load(category.getIcon()).into(childIcon);
            }
        }
    }
}
