package carelender.model.data;

import carelender.model.UndoManager;

public class QueryUndo extends QueryBase{

    public QueryUndo() {
        super(QueryType.UNDO);
    }

	@Override
	public void controllerExecute() {
		// TODO Auto-generated method stub
		UndoManager.getInstance().undo();
	}

	@Override
	public EventList searchExecute() {
		// TODO Auto-generated method stub
		return null;
	}
}
