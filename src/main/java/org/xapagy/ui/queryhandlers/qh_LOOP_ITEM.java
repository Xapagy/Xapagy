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
package org.xapagy.ui.queryhandlers;

import org.xapagy.agents.Agent;
import org.xapagy.agents.liHlsChoiceBased;
import org.xapagy.agents.liXapiReading;
import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.agents.liXapiScheduled;
import org.xapagy.agents.liViBased;
import org.xapagy.agents.AbstractLoopItem.LoopItemState;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygeneral.xwLiHlsChoiceBased;
import org.xapagy.ui.prettygeneral.xwLiViBased;
import org.xapagy.ui.prettygeneral.xwLiXapiReading;
import org.xapagy.ui.prettygeneral.xwLiXapiScheduled;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.ui.smartprint.XapiPrint;

public class qh_LOOP_ITEM implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String identifier = query.getAttribute(Q_ID);
        AbstractLoopItem li = agent.getLoop().getAllLoopItems().get(identifier);
        if (li == null) {
            fmt.addErrorMessage("The LoopItem with the identifier "
                    + identifier + " could not be found");
            return;
        } 
        //
        // Identifier block
        //
        String redheader = "LoopItem " + fmt.getEmpty().addIdentifier(li);
        fmt.addH2(redheader, "class=identifier");
        if (li instanceof liXapiScheduled) {
        	xwLiXapiScheduled.xwDetailed(fmt, (liXapiScheduled)li, agent);
        }
        if (li instanceof liHlsChoiceBased) {
        	xwLiHlsChoiceBased.xwDetailed(fmt, (liHlsChoiceBased)li, agent);
        }
        if (li instanceof liXapiReading) {
        	xwLiXapiReading.xwDetailed(fmt, (liXapiReading)li, agent);
        }
        if (li instanceof liViBased) {
        	xwLiViBased.xwDetailed(fmt, (liViBased)li, agent);
        }
    }

    /**
     * Try to summarize in one line the essence of the loop item FIXME: this is
     * for the time being the same as the one in the PpLoopItem
     * 
     * @param li
     * @param agent
     * @return
     */
    public static String pwConcise(PwFormatter fmt, AbstractLoopItem li, Agent agent) {
        // prefix: executed / not executed
        switch (li.getState()) {
        case EXECUTED:
            fmt.addEnum(Formatter.fmt(li.getExecutionTime()));
            break;
        case NOT_EXECUTED:
            break;
        }
        //
        if (li instanceof liXapiScheduled) {
            fmt.addEnum("Scheduled: ");
            fmt.add("-- dont know how to handle external");
        }
        if (li instanceof liHlsChoiceBased) {
        	liHlsChoiceBased li2 = (liHlsChoiceBased) li;
            fmt.addEnum("Internal: ");
            fmt.add(PrettyPrint.ppConcise(li2.getChoice(), agent));        	
        }
        if (li instanceof liViBased) {
        	liViBased li2 = (liViBased) li;
            fmt.addEnum("Forced: ");
            fmt.add(XapiPrint.ppsViXapiForm(li2.getForcedVi(), agent));
        }
        if (li instanceof liXapiReading) {
            fmt.addEnum("Reading: ");
            fmt.add(((liXapiReading)li).getXapiText());
        }
        return fmt.toString();
    }


    /**
     * OLD IMPLEMENTATION, based on explicit writing, replaced by the Xw-based
     * ones
     * 
     * Prints a LoopItem in such a way that it can be embedded in other
     * components
     * 
     * @param fmt
     * @param agent
     * @param li
     * @param query
     * @return
     */
    public static String pwDetailedXXX(PwFormatter fmt, Agent agent, AbstractLoopItem li,
            RESTQuery query) {
        fmt.is("Type", fmt.getEmpty().addEnum(li.getClass().getName()));
        fmt.is("State", fmt.getEmpty().addEnum(li.getState().toString()));
        if (li.getState().equals(LoopItemState.EXECUTED)) {
            fmt.is("Execution time", li.getExecutionTime());
            fmt.addBold("Execution results:");
            fmt.indent();
            for (VerbInstance vi : li.getExecutionResult()) {
                fmt.openP();
                PwQueryLinks.linkToVi(fmt, agent, query, vi);
                fmt.closeP();
            }
            fmt.deindent();
        }
        if (li instanceof liXapiScheduled) {
            fmt.is("scheduled time", ((liXapiScheduled)li).getScheduledExecutionTime());
            fmt.is("text", ((liXapiScheduled)li).getXapiText());        	
        }
        if (li instanceof liHlsChoiceBased) {
        	liHlsChoiceBased li2 = (liHlsChoiceBased) li;
            fmt.addBold("choice");
            fmt.startEmbed();
            qh_CHOICE.pwDetailed(fmt, agent, li2.getChoice(), query);
            fmt.endEmbed();
        }
        if (li instanceof liXapiReading) {
        		liXapiReading li2 = (liXapiReading) li;
            String header = "Reading: ";
            if (li2.getFileName() != null) {
                header =
                        header + " (" + li2.getFileName() + ":"
                                + li2.getFileLineNo() + ")";
            } else {
                header = header + "(directly added)";
            }
            fmt.add(header);
            fmt.add(li2.getXapiText());
        }
        if (li instanceof liViBased) {
        	fmt.addPre(PrettyPrint.ppDetailed(li, agent));
        }
        return fmt.toString();
    }

}
