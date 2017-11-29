package edu.fdu.se.main.ast;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Update;

public class ActionNodeRelation {
	public static boolean isParentOf(Action a1, Action a2) {
		if (a1.getNode() == null || a2.getNode() == null) {
			return false;
		}
		if ((a1 instanceof Insert && a2 instanceof Insert)) {
			Insert i1 = (Insert) a1;
			Insert i2 = (Insert) a2;
			if (i2.getParent().equals(i1.getNode()))
				return true;

		}
		if ((a1 instanceof Update && a2 instanceof Update) || (a1 instanceof Delete && a2 instanceof Delete)) {
			if (a1.getNode().equals(a2.getNode().getParent()))
				return true;
		}
		return false;
	}

	public static boolean isSameParent(Action a1, Action a2) {
		if (a1.getNode() == null || a2.getNode() == null) {
			return false;
		}
		if ((a1 instanceof Insert && a2 instanceof Insert)) {
			Insert i1 = (Insert) a1;
			Insert i2 = (Insert) a2;
			if (i2.getParent().equals(i1.getParent()))
				return true;

		}
		if ((a1 instanceof Update && a2 instanceof Update) || (a1 instanceof Delete && a2 instanceof Delete)) {
			if (a1.getNode().getParent() == null)
				return false;
			if (a1.getNode().getParent().equals(a2.getNode().getParent()))
				return true;
		}
		return false;
	}

	// private boolean sameParentMoves(Action a1, Action a2) {
	// if (!(a1 instanceof Move && a2 instanceof Move))
	// return false;
	// Move m1 = (Move) a1;
	// Move m2 = (Move) a2;
	// if (m1.getNode() == null)
	// return false;
	// if (m2.getNode() == null)
	// return false;
	// if (m1.getNode().getParent().equals(m2.getNode().getParent()))
	// return true;
	// else
	// return false;
	// }

	private boolean sameValueUpdates(Action a1, Action a2) {
		if (!(a1 instanceof Update && a2 instanceof Update))
			return false;
		Update u1 = (Update) a1;
		Update u2 = (Update) a2;
		if (u1.getValue().equals(u2.getValue()))
			return true;
		else
			return false;
	}

}
