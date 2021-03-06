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
package org.xapagy.xapi;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * Created on: Feb 22, 2011
 */
public class testWordResolutionHelper {

    @Test
    public void testComposition() throws XapiParserException {
        String description = "Composition of words for an overlay";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.printOn = true;
        List<String> words = new ArrayList<>();
        words.add("w_c_bai20");
        words.add("w_c_bai21");
        ConceptOverlay co =
                WordResolutionHelper.resolveWordsToCo(r.agent, words);
        r.ah.coContains(co, "c_bai20");
        r.ah.coContains(co, "c_bai21");
        TestHelper.testDone();
    }

}
