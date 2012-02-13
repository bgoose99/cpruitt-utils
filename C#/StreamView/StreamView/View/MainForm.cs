using System.Windows.Forms;
using StreamView.View;

namespace StreamView
{
    public partial class MainForm : Form
    {
        /// <summary>
        /// Default constructor.
        /// </summary>
        public MainForm()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Open button click method.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void openToolStripMenuItem_Click( object sender, System.EventArgs e )
        {
            OpenFileDialog dialog = new OpenFileDialog();
            if( dialog.ShowDialog() == DialogResult.OK )
                streamViewPanel.openFile( dialog.FileName );
        }

        /// <summary>
        /// Highlight button click method.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void highlightToolStripMenuItem_Click( object sender, System.EventArgs e )
        {
            HighlightDialog dialog = new HighlightDialog();
            if( dialog.ShowDialog() == DialogResult.OK )
                streamViewPanel.highlightSelection( dialog.BitOffset, dialog.BitLength );
        }
    }
}
