package io.vertigo.ai.bt;

/**
 * BT are composed of nodes 'BTNode' with a single root 'BTRoot' 
 * This nodes are memory less, they don't store data.
 * 
 * a BTNode have only one method called eval()
 * this method returns a status 'BTStatus' which contains the following states :
 *  - Failed
 *  - Succeeded
 *  - Stopped
 * 
 * Nodes are splitted into two categories
 *  - Leaves 
 *  - Composites
 * 
 * Composite are composed from one to many other nodes 
 * They control the flow 
 *  - Sequence 
 *  - Selector
 *  - Switch (a special kind of selector)
 *  - Loop (a repeatable sequence)
 *  - Try 
 * 
 * Leaves are themselves splitted into
 *  - Conditions 
 *  - Tasks
 *   
 * @author pchretien
 */
@FunctionalInterface
public interface BTNode {
	/**
	 * The core of the BT.
	 * @return status after the node evaluation
	 */
	BTStatus eval();
}
