package com.swia.iabuilder.parsers;

import com.swia.iabuilder.parsers.ttadmiral.TabletopAdmiralArmyMarshaller;
import com.swia.iabuilder.parsers.ttadmiral.TabletopAdmiralLegacyArmyMarshaller;

public enum ArmyMarshallerType {
    TTA_EXTENDED(new TabletopAdmiralArmyMarshaller()),
    TTA_LEGACY(new TabletopAdmiralLegacyArmyMarshaller());

    private final BaseArmyMarshaller<String, ?> marshaller;

    ArmyMarshallerType(BaseArmyMarshaller<String, ?> marshaller) {
        this.marshaller = marshaller;
    }

    public BaseArmyMarshaller<String, ?> getMarshaller() {
        return marshaller;
    }

    public static ArmyMarshallerType fromURL(String url) {
        for (ArmyMarshallerType type: ArmyMarshallerType.values()) {
            BaseArmyMarshaller<String, ?> marshaller = type.getMarshaller();
            if (marshaller.isValid(url)) {
                return type;
            }
        }
        return null;
    }
}
