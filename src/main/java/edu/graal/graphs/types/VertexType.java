package edu.graal.graphs.types;

import javaslang.collection.Array;
import javaslang.collection.Set;

public enum VertexType {
	DECL(1),
	ASSIGN(1),
	CTRL(2), //CTRL_GRT, CTRL_LESS, CTRL_GRTEQL, CTRL_LESSEQL, CTRL_INEQL, CTRL_EQL, CTRL_MOD_EQL, CTRL_MOD_INEQL, CTRL_COMBINATION,
	CALL(3),
	RETURN(4),
	BREAK(5);

	private int value;
	VertexType(int value) {
		this.value = value;
	}

	public static final double PENALTY_CONSTANT = 0.2;
	public static final double MAX_PENALTY = 999999;

	public static double getPenalty(PDGVertex v1, PDGVertex v2) {
		double totalPenalty = 0.0;
		if(v1.getType().value != v2.getType().value) {
			totalPenalty += MAX_PENALTY;
		}
		Set<VertexSubtype> diff = v1.subTypes().size() > v2.subTypes().size() ? v1.subTypes().diff(v2.subTypes())
				: v2.subTypes().diff(v1.subTypes());
		totalPenalty += Array.ofAll(diff)
				.map(subtype -> PENALTY_CONSTANT)
				.sum().doubleValue();

		return totalPenalty;
	}

}
