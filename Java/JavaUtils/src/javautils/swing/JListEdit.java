package javautils.swing;

import javautils.IDataUpdater;

import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/*******************************************************************************
 * This class represents an selection edit that takes place on a {@link JList}.
 * An {@link IDataUpdater} can optionally be specified in order to keep any
 * underlying data in sync with the state of the list selection.
 ******************************************************************************/
public class JListEdit implements UndoableEdit
{
    private JList<ListSelectionListener> list;
    private int[] oldValues;
    private int[] newValues;
    private IDataUpdater updater;

    /***************************************************************************
     * Constructor
     * 
     * @param list
     * @param oldValues
     * @param newValues
     **************************************************************************/
    public JListEdit( JList<ListSelectionListener> list, int[] oldValues,
            int[] newValues )
    {
        this( list, oldValues, newValues, null );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param list
     *            The list associated with this edit
     * @param oldValue
     *            The previous selection
     * @param newValue
     *            The new selection
     * @param updater
     *            Used to keep the underlying data in sync with edits (can be
     *            <b>null</b>)
     **************************************************************************/
    public JListEdit( JList<ListSelectionListener> list, int[] oldValues,
            int[] newValues, IDataUpdater updater )
    {
        this.list = list;
        this.oldValues = oldValues;
        this.newValues = newValues;
        this.updater = updater;

        // call initially, because a change just happened
        if( this.updater != null )
        {
            this.updater.updateData();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#undo()
     */
    @Override
    public void undo() throws CannotUndoException
    {
        ListSelectionListener[] listeners = list.getListSelectionListeners();
        // TODO: Find a better way
        for( ListSelectionListener l : listeners )
        {
            list.removeListSelectionListener( l );
        }
        list.setSelectedIndices( oldValues );
        for( ListSelectionListener l : listeners )
        {
            list.addListSelectionListener( l );
        }
        if( updater != null )
        {
            updater.updateData();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#canUndo()
     */
    @Override
    public boolean canUndo()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#redo()
     */
    @Override
    public void redo() throws CannotRedoException
    {
        ListSelectionListener[] listeners = list.getListSelectionListeners();
        // TODO: Find a better way
        for( ListSelectionListener l : listeners )
        {
            list.removeListSelectionListener( l );
        }
        list.setSelectedIndices( newValues );
        for( ListSelectionListener l : listeners )
        {
            list.addListSelectionListener( l );
        }
        if( updater != null )
        {
            updater.updateData();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#canRedo()
     */
    @Override
    public boolean canRedo()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#die()
     */
    @Override
    public void die()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#addEdit(javax.swing.undo.UndoableEdit)
     */
    @Override
    public boolean addEdit( UndoableEdit anEdit )
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.undo.UndoableEdit#replaceEdit(javax.swing.undo.UndoableEdit)
     */
    @Override
    public boolean replaceEdit( UndoableEdit anEdit )
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#isSignificant()
     */
    @Override
    public boolean isSignificant()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#getPresentationName()
     */
    @Override
    public String getPresentationName()
    {
        return "List selection changed";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#getUndoPresentationName()
     */
    @Override
    public String getUndoPresentationName()
    {
        return "Undo list selection";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#getRedoPresentationName()
     */
    @Override
    public String getRedoPresentationName()
    {
        return "Redo list selection";
    }

}
