package com.development.markin.markinlauncher.view.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.markin.markinlauncher.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sbt-markin-aa on 03.05.17.
 */

public class MarkinLauncherFragment extends Fragment {
    public static final String TAG = "MarkinLauncherFragment";
    private RecyclerView mRecyclerView;

    public static MarkinLauncherFragment newInstance(){
        return new MarkinLauncherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.markin_launcher_fragment,container,false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.markin_launcher_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return view;
    }

    private void setupAdapter(){
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(startupIntent,0);

        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        o1.loadLabel(pm).toString(),
                        o2.loadLabel(pm).toString());
            }
        });

        mRecyclerView.setAdapter(new ActivityAdapter(activities));

        Log.i(TAG, "Found " + activities.size() + " activities.");
    }


    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;
        private ImageView mImageView;

        public ActivityHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.name_app_text_view);
            mImageView = (ImageView) itemView.findViewById(R.id.icon_fpr_app_image_view);
            itemView.setOnClickListener(this);
        }

            public void bindActivity (ResolveInfo resolveInfo){
                mResolveInfo = resolveInfo;
                PackageManager pm = getActivity().getPackageManager();
                String appName = mResolveInfo.loadLabel(pm).toString();
                Drawable image = mResolveInfo.loadIcon(pm);
                mNameTextView.setText(appName);
                mImageView.setImageDrawable(image);
            }

        @Override
        public void onClick(View v) {
            ActivityInfo activityInfo = mResolveInfo.activityInfo;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>{
        private final List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities){
            mActivities=activities;
        }
        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_activities, parent, false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }

}
