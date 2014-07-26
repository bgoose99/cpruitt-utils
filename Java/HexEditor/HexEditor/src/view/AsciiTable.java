package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javautils.Utils;
import javautils.swing.FrameRunner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/*******************************************************************************
 * This is a {@link JTable} that can be used to display ASCII character codes.
 ******************************************************************************/
public class AsciiTable extends JTable
{
    /***************************************************************************
     * Constructor
     **************************************************************************/
    public AsciiTable()
    {
        super.setModel( new AsciiTableModel() );
        setFillsViewportHeight( true );
        getTableHeader().setResizingAllowed( false );
        getTableHeader().setReorderingAllowed( false );
        setCellSelectionEnabled( true );
        setPreferredScrollableViewportSize( new Dimension( 800, 520 ) );
        setPreferredSize( new Dimension( 800, 520 ) );

        // cell renderer for making decimal values bold
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent( JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column )
            {
                super.getTableCellRendererComponent( table, value, isSelected,
                        hasFocus, row, column );
                setFont( new Font( "Sans Serif", Font.BOLD, 12 ) );
                setHorizontalAlignment( SwingConstants.CENTER );
                return this;
            }
        };

        // set up columns
        for( int i = 0; i < getColumnCount(); i++ )
        {
            TableColumn c = getColumnModel().getColumn( i );

            if( i == 3 )
                c.setPreferredWidth( 120 );
            else
                c.setPreferredWidth( 15 );

            if( i % 4 == 0 )
                c.setCellRenderer( cellRenderer );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JTable#setModel(javax.swing.table.TableModel)
     */
    @Override
    public void setModel( TableModel dataModel )
    {
        // overridden to prevent changing the TableModel
    }

    /***************************************************************************
     * Custom {@link TableModel} for displaying ASCII character codes.
     **************************************************************************/
    private class AsciiTableModel extends AbstractTableModel
    {
        /***********************************************************************
         * Constructor
         **********************************************************************/
        public AsciiTableModel()
        {
            super();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        @Override
        public int getColumnCount()
        {
            return 16;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getRowCount()
         */
        @Override
        public int getRowCount()
        {
            return 32;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        @Override
        public String getColumnName( int column )
        {
            switch( column % 4 )
            {
            case 0:
                return "Dec";
            case 1:
                return "Hex";
            case 2:
                return "Oct";
            case 3:
                return "Char";
            default:
                return "N/A";
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        @Override
        public Object getValueAt( int rowIndex, int columnIndex )
        {
            int mod = rowIndex + ( ( columnIndex / 4 ) * 32 );
            switch( columnIndex % 4 )
            {
            case 0:
                return mod;
            case 1:
                return Utils.getPaddedString( Integer.toHexString( mod )
                        .toUpperCase(), 2, '0', true );
            case 2:
                return Utils.getPaddedString( Integer.toOctalString( mod )
                        .toUpperCase(), 3, '0', true );
            case 3:
                return Utils.getPrintableChar( mod );
            }

            return null;
        }
    }

    /***************************************************************************
     * Demo
     * 
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        class DemoRunner extends FrameRunner
        {
            @Override
            protected JFrame createFrame()
            {
                JFrame frame = new JFrame();

                AsciiTable table = new AsciiTable();
                JScrollPane pane = new JScrollPane( table );

                frame.setLayout( new GridBagLayout() );
                frame.add( pane, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                frame.setTitle( "AsciiTable Demo" );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );

                return frame;
            }

            @Override
            protected boolean validate()
            {
                return false;
            }
        }

        SwingUtilities.invokeLater( new DemoRunner() );
    }
}
