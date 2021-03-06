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
package org.xapagy.ui.formatters;

import org.junit.Test;
import org.xapagy.TestHelper;

/**
 * This class contains test for the iXwFormatter in its two incarnations, HTML and text
 * @author lboloni
 * Created Nov 07, 2016
 */
public class testIXwFormatter {

	@Test
	public void testTwoFormatters() {
		TestHelper.testStart("Testing PwFormatter and TwFormatter compatibility");
		TestHelper.verbose = true;
		PwFormatter pw = new PwFormatter();
		TwFormatter tw = new TwFormatter();
		printStuff(pw);
		TestHelper.printIfVerbose("PwFormatter:\n" + pw.toString());
		printStuff(tw);
		TestHelper.printIfVerbose("TwFormatter:\n" + tw.toString());
		TestHelper.testDone();
	}
	
	private void printStuff(IXwFormatter xw) {
		printAdd(xw);
	}
	
	private void printAdd(IXwFormatter xw) {
		xw.add("Adding some stuff here");
		xw.add("And some more stuff here");
	}
}
