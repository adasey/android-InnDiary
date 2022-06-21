/**
 * Copyright 2021 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.quickstart.InnDiary.java;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.quickstart.InnDiary.R;
import com.google.firebase.quickstart.InnDiary.databinding.FragmentChooserBinding;

/**
 * Simple list-based Fragment to redirect to one of the other Activities. This Fragment does not
 * contain any useful code related to Firebase Authentication. You may want to start with
 * one of the following Files:
 *     {@link GoogleSignInFragment}
 *     {@link EmailPasswordFragment}
 *     {@link CustomAuthFragment}
 *     {@link KakaoSignInFragment}
 */
public class ChooserWithKakaoFragment extends Fragment {

    private static final int[] NAV_ACTIONS = new int[]{
            R.id.action_google,
            R.id.action_emailpassword,
            R.id.action_customauth,
            R.id.action_kakao
    };

    private static final String [] CLASS_NAMES = new String[] {
            "GoogleSignInFragment",
            "EmailPasswordFragment",
            "CustomAuthFragment",
            "KakaoSingInFragment"
    };

    private static final int[] DESCRIPTION_IDS = new int[] {
            R.string.desc_google_sign_in,
            R.string.desc_emailpassword,
            R.string.desc_custom_auth,
            R.string.desc_kakao_sign_in
    };

    private FragmentChooserBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentChooserBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyArrayAdapter adapter = new MyArrayAdapter(requireContext(), android.R.layout.simple_list_item_2);
        adapter.setDescriptionIds(DESCRIPTION_IDS);

        mBinding.listView.setAdapter(adapter);
        mBinding.listView.setOnItemClickListener((parent, view1, position, id) -> {
            int destination = NAV_ACTIONS[position];
            NavHostFragment.findNavController(ChooserWithKakaoFragment.this)
                    .navigate(destination);
        });
    }

    public static class MyArrayAdapter extends ArrayAdapter<String> {

        private Context mContext;
        private int[] mDescriptionIds;

        public MyArrayAdapter(Context context, int resource) {
            super(context, resource, CLASS_NAMES);

            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(android.R.layout.simple_list_item_2, null);
            }

            ((TextView) view.findViewById(android.R.id.text1)).setText(CLASS_NAMES[position]);
            ((TextView) view.findViewById(android.R.id.text2)).setText(mDescriptionIds[position]);

            return view;
        }

        public void setDescriptionIds(int[] descriptionIds) {
            mDescriptionIds = descriptionIds;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
