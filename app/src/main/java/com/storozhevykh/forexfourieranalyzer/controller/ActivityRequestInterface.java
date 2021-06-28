package com.storozhevykh.forexfourieranalyzer.controller;

import androidx.fragment.app.Fragment;

import com.storozhevykh.forexfourieranalyzer.view.ChartFragment;

public interface ActivityRequestInterface {

    void showQuotes();
    Fragment getFragment();
}
