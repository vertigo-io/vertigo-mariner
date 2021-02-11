package BehaviorTree;

@FunctionalInterface
public interface BTNode {
	//	String getKey();

	BTStatus eval();
}
