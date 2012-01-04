using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;

namespace CsUtils.Forms
{
    /// <summary>
    /// This is a custome DataGridView class that displays row numbers in
    /// the row header.
    /// </summary>
    public class CustomDataGridView : DataGridView
    {
        /// <summary>
        /// Default constructor.
        /// </summary>
        public CustomDataGridView()
        {
        }
        
        /// <summary>
        /// Custom paint method.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnRowPostPaint( DataGridViewRowPostPaintEventArgs e )
        {
            string strRowNumber = ( e.RowIndex + 1 ).ToString();

            // add leading zeros
            while( strRowNumber.Length < this.RowCount.ToString().Length )
                strRowNumber = "0" + strRowNumber;

            // calculate size of string
            SizeF size = e.Graphics.MeasureString( strRowNumber, this.Font );

            // adjust column width, if necessary
            if( this.RowHeadersWidth < (int)( size.Width + 20 ) )
                this.RowHeadersWidth = (int)( size.Width + 20 );

            // get the brush we'll use
            Brush b = SystemBrushes.ControlText;

            // draw row number
            e.Graphics.DrawString( strRowNumber, this.Font, b, e.RowBounds.Location.X + 15, e.RowBounds.Location.Y + ( ( e.RowBounds.Height - size.Height ) / 2 ) );

            // call base method
            base.OnRowPostPaint( e );
        }
    }
}
