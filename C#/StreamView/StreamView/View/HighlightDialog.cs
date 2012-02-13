using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace StreamView.View
{
    public partial class HighlightDialog : Form
    {
        private int bitOffset;
        private int bitLength;

        /// <summary>
        /// Bit offset to highlight.
        /// </summary>
        public int BitOffset
        {
            get { return bitOffset; }
        }

        /// <summary>
        /// Bit length to highlight.
        /// </summary>
        public int BitLength
        {
            get { return bitLength; }
        }

        /// <summary>
        /// Default constructor
        /// </summary>
        public HighlightDialog()
        {
            InitializeComponent();
        }

        /// <summary>
        /// clickety button
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button1_Click( object sender, EventArgs e )
        {
            try
            {
                bitOffset = int.Parse( textBox1.Text );
                bitLength = int.Parse( textBox2.Text );
            }
            catch( Exception )
            {
                bitOffset = 0;
                bitLength = 0;
            }
        }

        /// <summary>
        /// buttony click
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button2_Click( object sender, EventArgs e )
        {
            bitOffset = 0;
            bitLength = 0;
        }
    }
}
