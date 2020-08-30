package com.swia.iabuilder.parsers;

import com.swia.iabuilder.models.Army;

/**
 * Converts an encoded army to the corresponding army object instance and vice versa.
 */
public interface ArmyMarshaller<T> {

    /**
     * Deserialize an encoded army into an army object.
     *
     * @param code The encode representing an army.
     * @param name The name of the deserialized army.
     * @return The corresponding army object instance.
     */
    Army deserialize(T code, String name);

    /**
     * Serialize an army object into an encoded army.
     *
     * @param army The army object instance.
     * @return The encode representing the army.
     */
    T serialize(Army army);
}