/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
*/
package org.xapagy.debug;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.autobiography.ABStory;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.observers.AbstractAgentObserver.TraceWhat;

/**
 * @author Ladislau Boloni
 * Created on: Jul 13, 2014
 */
public class testViShadowEvolutionMatrix {
    
    /**
     * Illustrates the creation of a VI shadow evolution matrix and tests whether 
     * the resulting matrix is query-able or not
     */
    @Test
    public void test() {
        // create the story
        ABStory story = new ABStory();
        story.add("'Achilles'/ wa_v_av40 / 'Hector'.");
        story.add("'Achilles'/ wa_v_av41 / 'Hector'.");
        story.add("'Achilles'/ wa_v_av42 / 'Hector'.");
        //story.add("'Achilles'/ wa_v_av43 / 'Hector'.");
        //story.add("'Achilles'/ wa_v_av44 / 'Hector'.");
        // prepare the agent
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace(TraceWhat.COMPACT);
        r.printOn = true;
        r.exec("$Include 'P-FocusOnly'");
        // see the story for the first time
        r.exec("$CreateScene #one CloseOthers With Instances w_c_bai20 w_c_bai22 'Hector', w_c_bai21 w_c_bai22 'Achilles'");
        r.exec(story);
        r.exec("----");
        // ok, create the ChoiceEvolutionMatrix
        ViShadowEvolutionMatrix visem = new ViShadowEvolutionMatrix(r.agent);
        
        // filters for instances
        ViMatchFilter viAv40One = new ViMatchFilter("", ViType.S_V_O, "*", "wa_v_av40", "*");
        ViMatchFilter viAv40Two = new ViMatchFilter("", ViType.S_V_O, "*", "wa_v_av40 #x", "*");
        @SuppressWarnings("unused")
        ViMatchFilter viAny = new ViMatchFilter("", null, "*");
        // Hector with Hector
        ViShadowAccessFilter isaf = new ViShadowAccessFilter("av40-av40", viAv40Two, viAv40One);
        visem.addColumn(isaf);
        
        // Strongest for Hector
        isaf = new ViShadowAccessFilter("av40-any", EnergyColors.SHV_GENERIC, viAv40Two, viAv40One);
        visem.addColumn(isaf);
       
        story = new ABStory();
        story.add("'Achilles'/ wa_v_av40 #x / 'Hector'.");
        story.add("'Achilles'/ wa_v_av41 / 'Hector'.");
        story.add("'Achilles'/ wa_v_av42 / 'Hector'.");
        story.add("'Achilles'/ wa_v_av43 / 'Hector'.");
        story.add("'Achilles'/ wa_v_av44 / 'Hector'.");
        
        // run the story the second time
        r.exec("$Include 'P-All-NoInternal'");
        r.exec("$CreateScene #two CloseOthers With Instances w_c_bai20 w_c_bai22 'Hector', w_c_bai21 w_c_bai22 'Achilles'");

        for (int i = 0; i != story.length(); i++) {
            r.exec(story.getLine(i));
            visem.record();
        }
        TextUi.println(visem.printMatrix("shadow_generic"));
        // test for the querying
        //
        // Test for the querying of the isem
        // Query by name and row number
        //
        ShadowRecord<VerbInstance> sr = visem.query("av40-any", 0);
        double val = sr.getEnergy(EnergyColors.SHV_GENERIC);
        TextUi.println("av40-any / 0 - generic"
                + Formatter.fmt(val));
        assertEquals("av40-any / 0 - generic", 0.09, val, 0.01);
        // 
        // Query by name and vi 
        //
        sr = visem.query("av40-any", null, "wa_v_av42");
        val = sr.getEnergy(EnergyColors.SHV_GENERIC);
        TextUi.println("Achilles strongest / after wa_v_av42"
                + Formatter.fmt(val));
        assertEquals("Achilles strongest / 0 - generic", 0.58, val, 0.01);
        
    }

}
