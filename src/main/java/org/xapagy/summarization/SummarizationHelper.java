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
package org.xapagy.summarization;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;

/**
 * Functions which decide things about summarization VIs
 * 
 * @author Ladislau Boloni Created on: Dec 27, 2014
 */
public class SummarizationHelper {

	/**
	 * Returns the energy color this particular VI should use in the focus
	 * (either the default, or some kind of summarization energy)
	 * 
	 * @param vi
	 * @return
	 */
	public static String getFocusEnergyColor(VerbInstance vi) {
		return getFocusEnergyColor(vi.getSummarizationLevel());
	}

	
	/**
	 * Returns the energy color for a certain summarization level
	 * 
	 * @param level
	 * @return 
	 */
	public static String getFocusEnergyColor(int level) {
		String retval = null;
		if (level == 0) {
			retval = EnergyColors.FOCUS_VI;
		} else {
			retval = EnergyColors.FOCUS_SUMMARIZATION_VI + "_" + level;
		}
		return retval;
	}
	
	
	
	/**
	 * Returns the energy color the instance of this particular VI should use in
	 * the focus (either the default, or some kind of summarization energy)
	 * 
	 * @param vi
	 * @return
	 */
	public static String getFocusEnergyColorForInstance(VerbInstance vi) {
		String retval = null;
		int level = vi.getSummarizationLevel();
		if (level == 0) {
			retval = EnergyColors.FOCUS_INSTANCE;
		} else {
			retval = EnergyColors.FOCUS_SUMMARIZATION_INSTANCE + "_" + level;
		}
		return retval;
	}

	/**
	 * A VI is a summarization if its summarization level is above 0
	 * 
	 * @param agent
	 * @param vi
	 * @return
	 */
	public static boolean isSummarization(Agent agent, VerbInstance vi) {
		//ViSet begins = agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_ELABORATION_BEGIN);
		//if (begins.isEmpty()) {
		//	return false;
		//}
		return vi.getSummarizationLevel() > 0;
	}

	/**
	 * A VI is a closed summarization if it is a summarization and it has a
	 * SUMMARIZATION_CLOSE link.
	 * 
	 * @param agent
	 * @param vi
	 * @return
	 */
	public static boolean isClosedSummarization(Agent agent, VerbInstance vi) {
		if (!isSummarization(agent, vi)) {
			return false;
		}
		ViSet viset = agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_ELABORATION_CLOSE);
		if (viset.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * A VI is an open summarization if it is a summarization and it does not
	 * have a SUMMARIZATION_CLOSE link.
	 * 
	 * @param agent
	 * @param vi
	 * @return
	 */
	public static boolean isOpenSummarization(Agent agent, VerbInstance vi) {
		if (!isSummarization(agent, vi)) {
			return false;
		}
		ViSet viset = agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_ELABORATION_CLOSE);
		if (viset.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Forces a summarization to be closed. 
	 * 
	 * Jan 2017: The current approach is to add a self-summarization close link.  
	 * 
	 * @param agent
	 * @param vi
	 */
	public static void forceSummarizationClosed(Agent agent, VerbInstance vi) {
		agent.getLinks().changeLinkByName(Hardwired.LINK_SUMMARIZATION_CLOSE, vi, vi, 1.0, 
				"visuviAction - Closes");
	}
	
}
