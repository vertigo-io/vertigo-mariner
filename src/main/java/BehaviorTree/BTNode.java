package BehaviorTree;

@FunctionalInterface
public interface BTNode {
	//	String getKey();

	BTStatus eval();

	static BTNode sequence(final BTNode... nodes) {
		return new BTSequence(nodes);
	}

	static BTNode selector(final BTNode... nodes) {
		return new BTSelector(nodes);
	}
}
