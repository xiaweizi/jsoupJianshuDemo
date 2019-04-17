package com.xiaweizi.jianshu;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.xiaweizi.jianshu.FlowerAdapter
 *     e-mail : 1012126908@qq.com
 *     time   : 2019/04/17
 *     desc   :
 * </pre>
 */

public class FlowerAdapter extends BaseQuickAdapter<FlowersBean, BaseViewHolder> {
    private Context context;

    public FlowerAdapter(Context context) {
        super(R.layout.item_flower);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FlowersBean item) {
        Glide.with(context).load(item.imgUrl).crossFade().into((ImageView) helper.getView(R.id.iv_flower));
        helper.setText(R.id.tv_title, item.title);
    }
}
