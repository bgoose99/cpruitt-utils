package view;

public interface NavPanel
{
    /***************************************************************************
     * Sets the current file details.
     * 
     * @param filename
     *            : the current filename
     * @param bytes
     *            : file size, in bytes
     **************************************************************************/
    public void setFileDetails( String filename, long bytes );

    /***************************************************************************
     * Sets the current offset into the file.
     * 
     * @param offset
     *            : current file/selection offset
     **************************************************************************/
    public void setOffset( long offset );

    /***************************************************************************
     * Sets the currently displayed block and file block count.
     * 
     * @param blockNumber
     *            : currently displayed block number
     * @param blockCount
     *            : current file's block count
     **************************************************************************/
    public void setBlock( int blockNumber, int blockCount );

    /***************************************************************************
     * Returns the user-specified block number. (Usually needed when the goto
     * action is executed.)
     * 
     * @return
     **************************************************************************/
    public int getUserSpecifiedBlock();

    /***************************************************************************
     * Sets the action used for going to a user-specified block.
     * 
     * @param gotoAction
     **************************************************************************/
    public void setGotoAction( javax.swing.Action gotoAction );

    /***************************************************************************
     * Sets the action for going to the previous block.
     * 
     * @param gotoPrevAction
     **************************************************************************/
    public void setGotoPrevAction( javax.swing.Action gotoPrevAction );

    /***************************************************************************
     * Sets the action for going to the next block.
     * 
     * @param gotoNextAction
     **************************************************************************/
    public void setGotoNextAction( javax.swing.Action gotoNextAction );

    /***************************************************************************
     * Sets the currently selected byte.
     * 
     * @param b
     **************************************************************************/
    public void setCurrentByte( byte b );

    /***************************************************************************
     * Returns the {@link java.awt.Component} used to display this panel.
     * 
     * @return
     **************************************************************************/
    public java.awt.Component getComponent();
}
