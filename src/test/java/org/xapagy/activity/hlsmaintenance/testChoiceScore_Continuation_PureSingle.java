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
package org.xapagy.activity.hlsmaintenance;

import static org.junit.Assert.assertEquals;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.ChoiceEvolutionMatrix;
import org.xapagy.debug.Runner;
import org.xapagy.debug.storygenerator.RsFrequentNarratives;
import org.xapagy.debug.storygenerator.RsTestingUnit;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;

/**
 * 
 * Tests for the scoring of the continuations for the case of a pure single item
 * This is the simplest test for choice scoring. It considers a linear, single
 * scene story, which is seen only once by the agent. The story is pure in the
 * sense that none of the concepts or verbs had been ever seen by the agent
 * again.
 * 
 * @author Ladislau Boloni
 * Created on: Jul 16, 2014
 */
public class testChoiceScore_Continuation_PureSingle {
    /**
     * The simplest test, the one with 3 items
     */
    // @Test
    public void testDefault() {
        TestHelper.testStart("testChoiceScore_Continuation_PureSingle:Default");
        RsTestingUnit rtu = RsFrequentNarratives.createPureRepetition(1);
        TextUi.println(rtu.getFullStory());
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        rtu.runHistory(r);
        rtu.runShadowStory(r);
        // ok, create the ChoiceEvolutionMatrix
        ChoiceEvolutionMatrix cem = new ChoiceEvolutionMatrix(r.agent);
        cem.addColumn("H wa_v_av10 A", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Hector\"", "wa_v_av10", "\"Achilles\"");
        cem.addColumn("A wa_v_av11 H", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Achilles\"", "wa_v_av11", "\"Hector\"");
        cem.addColumn("H wa_v_av12 A", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Hector\"", "wa_v_av12", "\"Achilles\"");
        r.setChoiceEvolutionMatrix(cem);
        // set the parameter of the RTU
        rtu.setParamsFocus(new ABStory("$Include 'P-All-NoInternal'"));
        rtu.runFocusStory(r);
        TextUi.println(cem.printMatrix("idmIDM"));
        //
        // tests
        //
        // all the values for the "H wa_v_av10 A" will be zero (continuation
        // choice, does not go backwards)
        for (int row = 0; row != cem.getRowCount(); row++) {
            double val = cem.queryIndependent("H wa_v_av10 A", row);
            assertEquals("av10 supposed to be -1", -1, val, 0.0001);
        }
        double val = cem.queryIndependent("A wa_v_av11 H", 4);
        TextUi.println("val = " + val);
        TestHelper.testDone();
    }
}
