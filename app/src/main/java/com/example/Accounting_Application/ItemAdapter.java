package com.example.Accounting_Application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private Context mContext;
    public List<Item> mItemList;
    private RecyclerView recyclerView;
    private static final String TAG = "ItemAdapter";

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView imageView;
        private TextView item_name;
        private TextView item_type;
        private TextView item_value;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            imageView = (ImageView) view.findViewById(R.id.item_image);
            item_name = (TextView) view.findViewById(R.id.item_name);
            item_type = (TextView) view.findViewById(R.id.item_type);
            item_value = (TextView) view.findViewById(R.id.item_value);
        }

        public CardView getCardView() {
            return cardView;
        }

        public TextView getItem_name() {
            return item_name;
        }

        public void setItem_name(TextView item_name) {
            this.item_name = item_name;
        }

        public TextView getItem_type() {
            return item_type;
        }

        public void setItem_type(TextView item_type) {
            this.item_type = item_type;
        }

        public TextView getItem_value() {
            return item_value;
        }

        public void setItem_value(TextView item_value) {
            this.item_value = item_value;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }

    public ItemAdapter(Context context, RecyclerView rcv, List<Item> itemList) {
        this.mContext = context;
        this.mItemList = itemList;
        recyclerView = rcv;
        // 调用 initSwipe() 方法，用于初始化 ItemTouchHelper 对象和 RecyclerView 对象
        initSwipe();
    }

    public ItemAdapter(List<Item> itemList) {
        mItemList = itemList;
    }

    public void updateItemList(List<Item> itemList) {
        this.mItemList = itemList;
        // 通知适配器数据发生了变化
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        //删除数据库中的内容
        mItemList.get(position).delete();
        // 从 Item 列表中移除指定位置的 Item 对象
        mItemList.remove(position);
        // 通知适配器指定位置的数据被移除了
        notifyItemRemoved(position);
    }


    public void initSwipe() {
        // 创建一个 ItemTouchHelper.Callback 对象，用于定义卡片的滑动方向，滑动距离，滑动动画，滑动回调等
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            // 重写 getMovementFlags 方法，用于定义卡片的滑动方向
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 设置卡片可以左右滑动
                int dragFlags = 0;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                // 返回滑动标志
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            // 重写 onMove 方法，用于处理卡片的拖动事件
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 不处理卡片的拖动事件
                return false;
            }

            // 重写 onSwiped 方法，用于处理卡片的滑动事件
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 获取滑动的卡片的位置
                int position = viewHolder.getAdapterPosition();
                // 调用适配器的方法，移除滑动的卡片
                removeItem(position);
            }

            // 重写 getSwipeThreshold 方法，用于定义卡片的滑动距离
            @Override
            public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
                // 设置卡片滑动一半的距离就可以移除
                return 0.2f;
            }

            // 重写 getSwipeEscapeVelocity 方法，用于定义卡片的滑动速度
            @Override
            public float getSwipeEscapeVelocity(float defaultValue) {
                // 设置卡片滑动的速度为默认值的 10 倍
                return 10 * defaultValue;
            }

            // 重写 getSwipeVelocityThreshold 方法，用于定义卡片的滑动阈值
            @Override
            public float getSwipeVelocityThreshold(float defaultValue) {
                // 设置卡片滑动的阈值为默认值的一半
                return 0.3f * defaultValue;
            }

            // 重写 onChildDraw 方法，用于定义卡片的滑动动画
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // 获取滑动的卡片的视图
                View view = viewHolder.itemView;
                // 获取滑动的卡片的宽度
                int width = view.getWidth();
                // 计算滑动的卡片的旋转角度
                float rotation = dX / width * 0;
                // 设置滑动的卡片的旋转中心
                view.setPivotX(width / 2);
                view.setPivotY(view.getHeight());
                // 设置滑动的卡片的旋转角度
                view.setRotation(rotation);
                // 调用父类的方法，继续执行滑动动画
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // 创建一个 ItemTouchHelper 对象，用于将 ItemTouchHelper.Callback 对象和 RecyclerView 对象关联起来
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        // 将 ItemTouchHelper 对象和 RecyclerView 对象关联起来
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                Intent intent = new Intent(mContext, ItemActivity.class);
                intent.putExtra("item", mItemList.get(position));
                intent.putExtra("position", position);
                mContext.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getImageView().setImageResource(mItemList.get(position).getItem_image());
        holder.getItem_name().setText(mItemList.get(position).getItem_name());
        holder.getItem_type().setText(mItemList.get(position).getItem_type());
        holder.getItem_value().setText("" + mItemList.get(position).getItem_value());
    }

    public void AddItem(Item item) {
        mItemList.add(item);
        updateItemList(mItemList);
    }

    public void ReNewItem(int index, Item item) {

        mItemList.get(index).ReNewItem(item);
        updateItemList(mItemList);
    }


    @Override
    public int getItemCount() {
        return mItemList.size();
    }


    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
