package com.bili.diushoujuaner.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Imageloader;
import com.bili.diushoujuaner.utils.entity.PictureVo;
import com.bili.diushoujuaner.widget.MaterialCircleView;
import com.bili.diushoujuaner.widget.picture.ImageInfo;
import com.bili.diushoujuaner.widget.picture.PhotoView;
import com.bili.diushoujuaner.widget.picture.ReboundViewPager;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/3/25.
 */
public class PictureFragment extends Fragment {

    @Bind(R.id.viewPager)
    ReboundViewPager viewPager;
    @Bind(R.id.txtIndicator)
    TextView txtIndicator;
    @Bind(R.id.layoutIndex)
    RelativeLayout layoutIndex;
    @Bind(R.id.mask)
    View mask;

    private ArrayList<PictureVo> pictureVoList;
    private ImageInfo imageInfo;
    private ArrayList<ImageInfo> imageInfoList;
    private int position;
    private ImageRequest imageRequest = null;
    private DataSource<CloseableReference<CloseableImage>> dataSource = null;

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
        runEnterAnimation();
        Bundle bundle = getArguments();
        pictureVoList = bundle.getParcelableArrayList("PictureVoList");
        imageInfo = bundle.getParcelable("ImageInfo");
        imageInfoList = bundle.getParcelableArrayList("ImageInfoList");

        position = bundle.getInt("Position", 0);
        txtIndicator.setText((position + 1) + "/" + pictureVoList.size());

        viewPager.getOverscrollView().setAdapter(new PagerAdapter() {
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
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_picture_detail, null, false);
                final PhotoView photoView = (PhotoView) view.findViewById(R.id.image_detail);
                final MaterialCircleView progressBar = (MaterialCircleView) view.findViewById(R.id.progress);
                if (position == pos) {
                    photoView.animateFrom(imageInfo);
                }

                imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(Common.getCompleteUrl(pictureVoList.get(pos).getPicPath())))
                        .setProgressiveRenderingEnabled(true)
                        .build();
                dataSource = Fresco.getImagePipeline().fetchDecodedImage(imageRequest, getContext());
                dataSource.subscribe(new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {
                        photoView.setImageDrawable(new BitmapDrawable(getResources(),bitmap));
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    }
                }, CallerThreadExecutor.getInstance());

                photoView.setFocusableInTouchMode(true);
                photoView.requestFocus();
                photoView.setOnKeyListener(pressKeyListener);//add key listener to listen back press
                photoView.setOnClickListener(onClickListener);
                photoView.setTag(pos);
                photoView.touchEnable(true);

                container.addView(view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        viewPager.getOverscrollView().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        viewPager.getOverscrollView().setCurrentItem(position);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exitFragment(v);
        }
    };

    private void exitFragment(View v) {
        //退出时点击的位置
        int position = (int) v.getTag();
        //回到上个界面该view的位置
        if (((FrameLayout) v.getParent()).getChildAt(1).getVisibility() == View.VISIBLE) {
            popFragment();
        } else {
            runExitAnimation(v);
            ((PhotoView) v).animateTo(imageInfoList.get(position), new Runnable() {
                @Override
                public void run() {
                    popFragment();
                }
            });
        }
    }

    private void popFragment() {
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

    public void runExitAnimation(final View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mask.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                ((BaseFragmentActivity)getActivity()).tintManager.setStatusBarTintResource(R.color.COLOR_THEME);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mask.startAnimation(alphaAnimation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
