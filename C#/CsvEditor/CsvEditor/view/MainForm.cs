using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using CsvEditor.presenter;

namespace CsvEditor
{
    public partial class MainForm : Form
    {
        private const string FORM_NAME = "CsvEditor";

        private ICsvPresenter presenter;
        private string currentFile;

        /// <summary>
        /// Constructor
        /// </summary>
        public MainForm()
        {
            InitializeComponent();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        private void MainForm_Load( object sender, EventArgs args )
        {
            presenter = new CsvPresenter();
            currentFile = null;
        }

        /// <summary>
        /// Exits the program.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        private void exitToolStripMenuItem_Click( object sender, EventArgs args )
        {
            System.Windows.Forms.Application.Exit();
        }

        /// <summary>
        /// Presents the user with an open file dialog.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        private void openToolStripMenuItem_Click( object sender, EventArgs args )
        {
            OpenFileDialog dialog = new OpenFileDialog();
            if( dialog.ShowDialog() == DialogResult.OK )
            {
                currentFile = dialog.FileName;
                try
                {
                    presenter.openFile( currentFile );
                    this.Text = FORM_NAME + " - " + currentFile;
                    dataGridView.DataSource = presenter.getDataTable();
                }
                catch( Exception e )
                {
                    MessageBox.Show( "Error opening file: \n" + e.Message, "Error",
                        MessageBoxButtons.OK, MessageBoxIcon.Error );
                }
            }
        }

        /// <summary>
        /// Closes the current file, resetting the data model.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void closeToolStripMenuItem_Click( object sender, EventArgs e )
        {
            if( currentFile != null )
            {
                presenter.resetData();
                this.Text = FORM_NAME;
                dataGridView.DataSource = presenter.getDataTable();
            }
        }

        /// <summary>
        /// Saves the current file.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        private void saveToolStripMenuItem_Click( object sender, EventArgs args )
        {
            if( currentFile != null )
            {
                try
                {
                    presenter.saveFile( currentFile );
                }
                catch( Exception e )
                {
                    MessageBox.Show( "Error saving file: \n" + e.Message, "Error",
                        MessageBoxButtons.OK, MessageBoxIcon.Error );
                }
            }
        }

        /// <summary>
        /// Presents the user with a save file dialog.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        private void saveAsToolStripMenuItem_Click( object sender, EventArgs args )
        {
            SaveFileDialog dialog = new SaveFileDialog();
            if( dialog.ShowDialog() == DialogResult.OK )
            {
                try
                {
                    currentFile = dialog.FileName;
                    presenter.saveFile( currentFile );
                    this.Text = FORM_NAME + " - " + currentFile;
                }
                catch( Exception e )
                {
                    MessageBox.Show( "Error saving file: \n" + e.Message, "Error",
                           MessageBoxButtons.OK, MessageBoxIcon.Error );
                }
            }
        }
    }
}
