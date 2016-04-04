package com.bili.diushoujuaner.fragment;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.entity.PictureVo;
import com.bili.diushoujuaner.widget.CircleIndicator;
import com.bili.diushoujuaner.widget.photodraweeview.MultiTouchViewPager;
import com.bili.diushoujuaner.widget.photodraweeview.PhotoDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/3/25.
 */
public class PictureFragment extends Fragment {

    @Bind(R.id.viewPager)
    MultiTouchViewPager viewPager;
    @Bind(R.id.indicator)
    CircleIndicator indicator;

    private ArrayList<PictureVo> pictureVoList;
    private int position;

    public static PictureFragment getInstance(Bundle bundle) {
        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((BaseFragmentActivity)getActivity()).tintManager.setStatusBarTintResource(R.color.COLOR_BLACK);
        Bundle bundle = getArguments();
        pictureVoList = bundle.getParcelableArrayList("PictureVoList");
        position = bundle.getInt("Position", 0);
        viewPager.setAdapter(new DraweePagerAdapter());
        indicator.setViewPager(viewPager);

        viewPager.setCurrentItem(position);
        viewPager.setFocusable(true);
        viewPager.setOnKeyListener(pressKeyListener);
        viewPager.setFocusableInTouchMode(true);
        viewPager.requestFocus();
    }

    public class DraweePagerAdapter extends PagerAdapter {

        private HashMap<Integer,PhotoDraweeView> hashMap = new HashMap<>();

        @Override public int getCount() {
            return pictureVoList.size();
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override public Object instantiateItem(ViewGroup viewGroup, int position) {
            final PhotoDraweeView photoDraweeView;
            if(hashMap.get(position) == null){
                photoDraweeView = new PhotoDraweeView(viewGroup.getContext());
                PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
                controller.setUri(Uri.parse(Common.getCompleteUrl(pictureVoList.get(position).getPicPath())));
                controller.setOldController(photoDraweeView.getController());
                controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (imageInfo == null) {
                            return;
                        }
                        photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                    }
                });
                photoDraweeView.setController(controller.build());

                try {
                    viewGroup.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                photoDraweeView = hashMap.get(position);
            }
            return photoDraweeView;
        }
    }

    private void exitFragment(View v) {
        if (!PictureFragment.this.isResumed()) {
            return;
        }
        final FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }
    }

    private View.OnKeyListener pressKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (event.getAction() != KeyEvent.ACTION_UP) {
                    return true;
                }
                exitFragment(v);
                return true;
            }
            return false;
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((BaseFragmentActivity) getActivity()).tintManager.setStatusBarTintResource(R.color.COLOR_THEME);
        ButterKnife.unbind(this);
    }
}
