package com.at.cancerbero.domain.service.push.model;


import com.at.cancerbero.domain.model.AlarmStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AlarmStatusChanged extends Event {
    private AlarmStatus value;
    private String source;
    private String sourceName;
}
