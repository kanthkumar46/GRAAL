package edu.graal.graphs.types;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.HashMap;
import javaslang.control.Option;

import static edu.graal.graphs.types.VertexType.PENALTY_CONSTANT;

public enum VertexSubtype implements IVertexSubtype {
	LT, GT, LEQ, GEQ, EQ, INEQ, MOD, AND, OR, INCR, DECR, SH_PLUS, SH_MINUS, PRINT, CALL, ADD, SUB, MUL, COMP, LOGICAL;

	private static HashMap<Tuple2<? extends IVertexSubtype, ? extends IVertexSubtype>, Double> penaltyMap = penalty();
	private static HashMap<Tuple2<? extends IVertexSubtype, ? extends IVertexSubtype>, Double> penalty() {
		java.util.Map<Tuple2<? extends IVertexSubtype, ? extends IVertexSubtype>, Double> map = new java.util.HashMap<>();

		map.put(Tuple.of(LT, GT), 0.5);
		map.put(Tuple.of(LT, LEQ), 0.25);
		map.put(Tuple.of(LT, GEQ), 0.75);
		map.put(Tuple.of(LT, INEQ), 0.85);
		map.put(Tuple.of(LT, COMP), 0.5);

		map.put(Tuple.of(GT, LEQ), 0.75);
		map.put(Tuple.of(GT, GEQ), 0.25);
		map.put(Tuple.of(GT, COMP), 0.5);
		map.put(Tuple.of(GT, INEQ), 0.85);

		map.put(Tuple.of(LEQ, GEQ), 0.25);
		map.put(Tuple.of(LEQ, EQ), 0.75);
		map.put(Tuple.of(LEQ, INEQ), 0.5);
		map.put(Tuple.of(LEQ, COMP), 0.5);

		map.put(Tuple.of(GEQ, EQ), 0.75);
		map.put(Tuple.of(GEQ, INEQ), 0.5);
		map.put(Tuple.of(GEQ, COMP), 0.5);

		map.put(Tuple.of(EQ, INEQ), 0.75);
		map.put(Tuple.of(EQ, COMP), 0.5);

		map.put(Tuple.of(INEQ, COMP), 0.5);

		map.put(Tuple.of(MOD, SH_MINUS), 0.75);
		map.put(Tuple.of(MOD, SUB), 0.75);
		map.put(Tuple.of(MOD, MUL), 0.9);

		map.put(Tuple.of(AND, OR), 0.5);
		map.put(Tuple.of(AND, LOGICAL), 0.5);

		map.put(Tuple.of(OR, LOGICAL), 0.5);

		map.put(Tuple.of(INCR, DECR), 0.75);
		map.put(Tuple.of(INCR, SH_PLUS), 0.5);
		map.put(Tuple.of(INCR, SH_MINUS), 0.75);
		map.put(Tuple.of(INCR, ADD), 0.5);
		map.put(Tuple.of(INCR, SUB), 0.75);
		map.put(Tuple.of(INCR, MUL), 0.9);

		map.put(Tuple.of(DECR, SH_PLUS), 0.75);
		map.put(Tuple.of(DECR, SH_MINUS), 0.5);
		map.put(Tuple.of(DECR, ADD), 0.75);
		map.put(Tuple.of(DECR, SUB), 0.5);

		map.put(Tuple.of(SH_PLUS, SH_MINUS), 0.75);
		map.put(Tuple.of(SH_PLUS, ADD), 0.25);
		map.put(Tuple.of(SH_PLUS, SUB), 0.75);
		map.put(Tuple.of(SH_PLUS, MUL), 0.5);

		map.put(Tuple.of(SH_MINUS, ADD), 0.75);
		map.put(Tuple.of(SH_MINUS, SUB), 0.5);
		map.put(Tuple.of(SH_MINUS, MUL), 0.9);

		map.put(Tuple.of(ADD, SUB), 0.75);
		map.put(Tuple.of(ADD, MUL), 0.5);

		map.put(Tuple.of(SUB, MUL), 0.8);

		return HashMap.ofAll(map);
	}

	public static Double getPenalty(Tuple2<? extends IVertexSubtype, ? extends IVertexSubtype> vertexSubtypeTuple) {
		Option<Double> penalty = penaltyMap.get(vertexSubtypeTuple);
		if(penalty.isDefined()) {
			return penalty.get();
		} else {
			penalty = penaltyMap.get(vertexSubtypeTuple.swap());
			return penalty.isDefined() ? penalty.get() : PENALTY_CONSTANT;
		}
	}

	public static void addPenalty(IVertexSubtype v1, IVertexSubtype v2, double value) {
		penaltyMap.put(Tuple.of(v1, v2), value);
	}
}
