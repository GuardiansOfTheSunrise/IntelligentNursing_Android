package com.gots.intelligentnursing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gots.intelligentnursing.R;
import com.gots.intelligentnursing.activity.DeviceManagementActivity;

/**
 * @author Accumulei
 * @date 2018/4/17.
 */
public class MinePageFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_mine, container, false);
        Button button = view.findViewById(R.id.bt_mine_device_management);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DeviceManagementActivity.class);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
