/*
   This file is part of the Xapagy project
   Created on: Jul 12, 2014

   org.xapagy.debug.ShadowRecord

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.set.EnergyColors.EnergyColorType;

/**
 * This object captures the energies of a certain shadow at a given moment in
 * time.
 *
 * @author Ladislau Boloni
 *
 */
public class ShadowRecord<T extends XapagyComponent> implements Serializable {

    private static final long serialVersionUID = 7625070313530275657L;
    private Map<String, Double> energies = new HashMap<>();
    private T focusObject;
    /**
     * For empty shadow records, indicates if at least the focus object is
     * present or not.
     */
    private boolean hasFocus;
    private T shadowObject;
    /**
     * Shows that the shadow record had been obtained as the top model based on
     * some kind of sorting.
     */
    private String sortBy;

    /**
     * Create an empty shadow record, when there is no focus nor shadow object
     *
     * @param hasFocus
     */
    public ShadowRecord(boolean hasFocus, boolean isItInstance, Agent agent) {
        this.focusObject = null;
        this.shadowObject = null;
        this.hasFocus = hasFocus;
        this.sortBy = null;
        if (isItInstance) {
            for (String sc : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
                energies.put(sc, new Double(0));
            }
        } else {
            for (String sc : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
                energies.put(sc, new Double(0));
            }
        }
    }

    /**
     * Constructor, essentially, capture the values from the agent
     */
    public ShadowRecord(T focusObject, T shadowObject, String sortBy,
            Agent agent) {
        this.focusObject = focusObject;
        this.shadowObject = shadowObject;
        this.sortBy = sortBy;
        // now take the values from the agent
        if (focusObject instanceof Instance) {
            for (String sc : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
                double value =
                        agent.getShadows().getEnergy((Instance) focusObject,
                                (Instance) shadowObject, sc);
                energies.put(sc, value);
            }
            return;
        }
        if (focusObject instanceof VerbInstance) {
            for (String sc : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
                double value =
                        agent.getShadows().getEnergy(
                                (VerbInstance) focusObject,
                                (VerbInstance) shadowObject, sc);
                energies.put(sc, value);
            }
            return;
        }
    }

    /**
     * Returns the recorded energy of a given color
     *
     * @param ec
     * @return
     */
    public double getEnergy(String ec) {
        return energies.get(ec);
    }

    public T getFocusObject() {
        return focusObject;
    }

    public T getShadowObject() {
        return shadowObject;
    }

    public String getSortBy() {
        return sortBy;
    }

    public boolean isHasFocus() {
        return hasFocus;
    }

}
