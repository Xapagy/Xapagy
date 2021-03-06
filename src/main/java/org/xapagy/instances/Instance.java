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
package org.xapagy.instances;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwInstance;
import org.xapagy.ui.prettyprint.PrettyPrint;

public class Instance implements Serializable, XapagyComponent {
	private static final long serialVersionUID = 9138931962515016923L;
	/**
	 * The attributes of the instance
	 */
	private ConceptOverlay concepts;
	private String identifier;
	/**
	 * The VIs which are referring to the instance. Only the VIs that actually
	 * had been instantiated (inserted in the focus) will be reported here, and
	 * the order in which they appear, is the order in which they were recorded
	 */
	private List<VerbInstance> referringVis = new ArrayList<>();

	/**
	 * The scene of the instance. If the instance _is_ a scene, it points to
	 * itself.
	 */
	private Instance scene;
	/**
	 * A collection of the instances which are members of the scene
	 */
	private List<Instance> sceneMembers;
	/**
	 * The scene preferences, it is non-null only if this instance is a scene
	 */
	private SceneParameters sceneParameters;
	/**
	 * The agent time when it was created
	 */
	private double creationTime;

	/**
	 * @return the creationTime
	 */
	public double getCreationTime() {
		return creationTime;
	}

	/**
	 * Creates a new instance. If the scene is null, this instance will be a
	 * scene.
	 * 
	 * @param instanceIds
	 * @param leadConcept
	 */
	public Instance(String identifier, Instance scene, Agent agent) {
		this.identifier = identifier;
		if (scene != null) {
			// not a scene
			this.scene = scene;
			scene.sceneMembers.add(this);
			sceneMembers = null;
		} else {
			// a scene
			this.scene = this;
			this.sceneParameters = new SceneParameters(agent);
			sceneMembers = new ArrayList<>();
		}
		concepts = new ConceptOverlay(agent);
		creationTime = agent.getTime();
	}

	/**
	 * Adds a referring VI.
	 * 
	 * @param vi
	 */
	public void addReferringVi(VerbInstance vi) {
		if (!referringVis.contains(vi)) {
			referringVis.add(vi);
		}
	}

	/**
	 * Returns the concept overlay that describes this instance
	 * 
	 * @return
	 */
	public ConceptOverlay getConcepts() {
		return concepts;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Returns an unmodifiable list of the referring VIs
	 * 
	 * @return
	 */
	public List<VerbInstance> getReferringVis() {
		return Collections.unmodifiableList(referringVis);
	}

	/**
	 * Returns the scene into which this instance is part of. If this instance
	 * is a scene, returns itself.
	 * 
	 * @return the scene
	 */
	public Instance getScene() {
		return scene;
	}

	/**
	 * Returns the members of the scene. It has guards to prevent it from being
	 * called on an instance which is not a scene.
	 * 
	 * @return the sceneMembers
	 */
	public List<Instance> getSceneMembers() {
		if (!isScene()) {
			TextUi.errorPrint("Scene members can only be accessed on a scene");
			throw new Error("Scene members can only be accessed on a scene");
		}
		List<Instance> retval = new ArrayList<>();
		retval.addAll(sceneMembers);
		return retval;
	}

	/**
	 * @return the scene parameters
	 */
	public SceneParameters getSceneParameters() {
		if (!isScene()) {
			TextUi.errorPrint("Scene members can only be accessed on a scene");
			throw new Error("Scene members can only be accessed on a scene");
		}
		return sceneParameters;
	}

	/**
	 * Returns true if this instance is a scene
	 * 
	 * @return
	 */
	public boolean isScene() {
		return scene == this;
	}

	@Override
	public String toString() {
		return xwInstance.xwSuperConcise(new TwFormatter(), this, PrettyPrint.lastAgent);
	}

}
