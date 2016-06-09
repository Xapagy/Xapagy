/*
   This file is part of the Xapagy project
   Created on: May 26, 2011
 
   org.xapagy.activity.SaContHlsToShadow
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.hlsmaintenance;

import org.xapagy.activity.SpikeActivity;
import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.ViSet;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * Initializes the shadow of an internal LI based on its HLS
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaHlsmInternalLiInitialShadow extends SpikeActivity {
    private static final long serialVersionUID = -6424879790512111393L;

    private Choice choice;

    /**
     * 
     * @param agent
     * @param name
     * @param vi
     * @param choice
     *            - the choice which had been currently instantiated
     */
    public SaHlsmInternalLiInitialShadow(Agent agent, String name,
            Choice choice) {
        super(agent, name);
        this.choice = choice;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.SpikeActivity#apply()
     */
    @Override
    public void applyInner(VerbInstance vi) {
        ViSet vis = choice.getChoiceScore().getVirtualShadow();
        agent.getShadows().mergeInShadow(vi, vis, 1.0,
                "SaHlsmInternalInitialShadow");
    }

    @Override
    public String toString() {
        Formatter fmt = new Formatter();
        fmt.add(super.toString());
        fmt.indent();
        fmt.add("hls");
        fmt.addIndented(choice.getHls());
        return fmt.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters
     * .IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaHlsmInternalInitialShadow");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}
