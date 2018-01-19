package com.at.cancerbero.adapter;

import com.at.cancerbero.CancerberoApp.R;
import com.at.cancerbero.domain.model.AlarmStatus;

public class NodeUtils {

    public static int getImage(AlarmStatus status) {
        switch (status) {
            case SABOTAGE:
                return R.drawable.status_sabotage;
            case SAFETY:
                return R.drawable.status_safety;
            case ALARMED:
                return R.drawable.status_alarmed;
            case SUSPICIOUS:
                return R.drawable.status_suspicious;
            case ACTIVATED:
                return R.drawable.status_activated;
            case ACTIVATING:
                return R.drawable.status_activating;
            default:
                return R.drawable.status_idle;
        }
    }

    public static int getText(AlarmStatus status) {
        switch (status) {
            case SABOTAGE:
                return R.string.label_status_sabotage;
            case SAFETY:
                return R.string.label_status_safety;
            case ALARMED:
                return R.string.label_status_alarmed;
            case SUSPICIOUS:
                return R.string.label_status_suspicious;
            case ACTIVATED:
                return R.string.label_status_activated;
            case ACTIVATING:
                return R.string.label_status_activating;
            default:
                return R.string.label_status_idle;
        }
    }
}