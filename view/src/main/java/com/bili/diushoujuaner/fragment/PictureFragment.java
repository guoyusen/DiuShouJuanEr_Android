package com.bili.diushoujuaner.fragment;

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
import com.bili.diushoujuaner.widget.zoomfresco.zoomable.ZoomableDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/3/25.
 */
public class PictureFragment extends Fragment {

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.txtIndicator)
    TextView txtIndicator;
    @Bind(R.id.layoutIndex)
    RelativeLayout layoutIndex;
    @Bind(R.id.mask)
    View mask;

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
//        runEnterAnimation();
        Bundle bundle = getArguments();
        pictureVoList = bundle.getParcelableArrayList("PictureVoList");
        position = bundle.getInt("Position", 0);
        txtIndicator.setText((position + 1) + "/" + pictureVoList.size());

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pictureVoList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int pos) {
                ZoomableDraweeView view = new ZoomableDraweeView(container.getContext());
                view.setController(Fresco.newDraweeControllerBuilder()
                        .setUri(Uri.parse(Common.getCompleteUrl(pictureVoList.get(pos).getPicPath())))
                        .build());
                GenericDraweeHierarchy hierarchy =
                        new GenericDraweeHierarchyBuilder(container.getResources())
                                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                                .build();

                view.setHierarchy(hierarchy);

                container.addView(view,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                txtIndicator.setText((position + 1) + "/" + pictureVoList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(position);
        viewPager.setFocusable(true);
        viewPager.setOnKeyListener(pressKeyListener);
        viewPager.setFocusableInTouchMode(true);
        viewPager.requestFocus();
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
            if (keyCode == KeyEvent.KEYCODE_BACK) {//只监听返回键
                if (event.getAction() != KeyEvent.ACTION_UP) {
                    return true;
                }
                exitFragment(v);
                return true;
            }
            return false;
        }
    };


    private void runEnterAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(300);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        mask.startAnimation(alphaAnimation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((BaseFragmentActivity) getActivity()).tintManager.setStatusBarTintResource(R.color.COLOR_THEME);
        ButterKnife.unbind(this);
    }
}
