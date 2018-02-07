package com.at.cancerbero.domain.service.converters;


import com.at.cancerbero.domain.model.AlarmModule;
import com.at.cancerbero.domain.model.AlarmPin;
import com.at.cancerbero.domain.model.AlarmPinChangeEvent;
import com.at.cancerbero.domain.model.AlarmStatusChangeEvent;
import com.at.cancerbero.domain.model.Node;
import com.at.cancerbero.domain.model.NodeModules;

import java.util.HashMap;
import java.util.Map;

public class NodeConverter {
    public Node convert(com.at.cancerbero.domain.data.repository.model.Node input) {
        Node result = null;

        if (input != null) {
            result = new Node(
                    input.getId(),
                    input.getName(),
                    input.getType(),
                    input.getLastPing(),
                    convertModules(input.getModules())
            );
        }
        return result;
    }

    private NodeModules convertModules(com.at.cancerbero.domain.data.repository.model.NodeModules input) {
        return new NodeModules(
                convertAlarmModule(input.getAlarm()),
                null
        );
    }

    private AlarmModule convertAlarmModule(com.at.cancerbero.domain.data.repository.model.AlarmModule input) {
        return new AlarmModule(
                convert(input.getStatus()),
                convertPins(input.getPins())
        );
    }

    private Map<String, AlarmPin> convertPins(Map<String, com.at.cancerbero.domain.data.repository.model.AlarmPin> input) {
        Map<String, AlarmPin> result = new HashMap<>();

        for (String pinId : input.keySet()) {
            result.put(pinId, convertPin(input.get(pinId)));
        }

        return result;
    }

    private AlarmPin convertPin(com.at.cancerbero.domain.data.repository.model.AlarmPin input) {
        return new AlarmPin(
                input.getId(),
                input.getName(),
                input.getType(),
                input.getInput(),
                input.getMode(),
                input.getUnit(),
                input.getThreshold(),
                convert(input.getActivations()),
                convert(input.getReadings())
        );
    }

    private AlarmPinChangeEvent convert(com.at.cancerbero.domain.data.repository.model.AlarmPinChangeEvent input) {
        return new AlarmPinChangeEvent(
                input.getId(),
                input.getTimestamp(),
                input.getValue()
        );
    }

    private AlarmStatusChangeEvent convert(com.at.cancerbero.domain.data.repository.model.AlarmStatusChangeEvent input) {
        return new AlarmStatusChangeEvent(
                input.getId(),
                input.getSource(),
                input.getTimestamp(),
                input.getValue()
        );
    }
}
