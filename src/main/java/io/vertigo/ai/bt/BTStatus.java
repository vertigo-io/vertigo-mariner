package io.vertigo.ai.bt;

/**
 * The status returned after a node has been evaluated.
 * 
 * @author pchretien
 */
public enum BTStatus {
	Succeeded,
	Stopped,
	Failed;

	public boolean isFailed() {
		return this == Failed;
	}

	public boolean isSucceeded() {
		return this == Succeeded;
	}

	public boolean isStopped() {
		return this == Stopped;
	}
}
