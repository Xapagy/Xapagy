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
package org.xapagy.ui.matlab;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.xapagy.util.FileWritingUtil;

/**
 * 
 * Various utilities for Octave code generation - similar but not quite the same
 * as matlab
 * 
 * @author Ladislau Boloni
 * Created on: Aug 6, 2013
 */
public class OctaveUtil {
    /**
     * Generates octave the basic graph prefix
     * 
     * @param buffer
     */
    public static String generateBasicGraphPrefix(int xsize, int ysize) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("figure1 = figure('Position', [0 0 " + xsize + " "
                + ysize + "]);\n");
        buffer.append("axes1 = axes('Parent',figure1);\n");
        buffer.append("legend('show')\n");
        buffer.append("hold on\n");
        return buffer.toString();
    }

    /**
     * Generates octave text for basic graph properties
     */
    public static String generateBasicGraphProperties(
            AbstractGraphDescription graphDescription) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("hold on\n");
        if (graphDescription.getTitle() != null) {
            buffer.append("title('" + graphDescription.getTitle() + "')\n");
        }
        buffer.append("hold on\n");
        buffer.append("xlabel('" + graphDescription.getXLabel() + "')\n");
        buffer.append("hold on\n");
        buffer.append("ylabel('" + graphDescription.getYLabel() + "')\n");
        return buffer.toString();
    }

    /**
     * Generates a file for processing all the figures in a directory and
     * convert them to pdf...
     * 
     * @throws IOException
     */
    public static void generateProcessAll(File dir) throws IOException {
        String outputName = "processAllFiles.m";
        // It is also possible to filter the list of returned files.
        // This example does not return any files that start with `.'.
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir2, String name) {
                return name.endsWith(".m");
            }
        };
        String matlabFiles[] = dir.list(filter);
        StringBuffer buf = new StringBuffer();
        for (String fileName : matlabFiles) {
            String command = fileName.substring(0, fileName.length() - 2);
            if (command.startsWith("process")) {
                continue;
            }
            buf.append(command + ";\n");
            buf.append("print " + command + ".pdf;\n");
            buf.append("print " + command + ".jpg;\n");
            buf.append("close\n");
        }
        FileWritingUtil.writeToTextFile(new File(dir, outputName),
                buf.toString());
    }

}
