package com.productions.pieter.notificationanalyzer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;


public class OverviewFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        List<NotificationView> list = new LinkedList<NotificationView>();
        list.add(new NotificationView("Facebook", 10, 15));
        list.add(new NotificationView("WhatsApp", 15, 15));
        list.add(new NotificationView("Snapchat", 7, 15));
        list.add(new NotificationView("Swarn", 5, 15));
        list.add(new NotificationView("Hangouts", 11, 15));
        list.add(new NotificationView("Twitter", 2, 15));
        NotificationAdapter adapter = new NotificationAdapter(view.getContext(), list);

        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        return view;
    }
}
