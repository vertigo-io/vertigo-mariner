package io.vertigo.ai.bt;

/**
 * The status returned after a node has been evaluated.
 * 
 * @author pchretien
 */
public enum BTStatus {
	Succeeded,
	Running,
	Failed;

	public boolean isFailed() {
		return this == Failed;
	}

	public boolean isSucceeded() {
		return this == Succeeded;
	}

	public boolean isRunning() {
		return this == Running;
	}
}
