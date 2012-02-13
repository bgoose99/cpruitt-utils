using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace StreamView.View
{
    public partial class NavigatorPanel : UserControl, INavigator
    {
        #region INavigator EventHandlers
        public event EventHandler PrevButtonPressed;
        public event EventHandler NextButtonPressed;
        public event EventHandler GotoButtonPressed;
        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor.
        /// </summary>
        public NavigatorPanel()
        {
            InitializeComponent();
        }

        #endregion

        #region Methods

        /// <summary>
        /// Called when the user presses the previous button.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void prevButton_Click( object sender, EventArgs e )
        {
            if( PrevButtonPressed != null )
                PrevButtonPressed( this, e );
        }

        /// <summary>
        /// Called when the user presses the next button.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void nextButton_Click( object sender, EventArgs e )
        {
            if( NextButtonPressed != null )
                NextButtonPressed( this, e );
        }

        /// <summary>
        /// Called when the user presses the goto button.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void gotoButton_Click( object sender, EventArgs e )
        {
            if( GotoButtonPressed != null )
                GotoButtonPressed( this, e );
        }

        #endregion

        #region INavigator Properties

        /// <summary cref="INavigator.LabelText">
        /// <see cref="INavigator.LabelText"/>
        /// </summary>
        public string LabelText
        {
            get { return label.Text; }
            set { label.Text = value; }
        }

        /// <summary cref="INavigator.GotoText">
        /// <see cref="INavigator.GotoText"/>
        /// </summary>
        public string GotoText
        {
            get { return gotoTextBox.Text; }
            set { gotoTextBox.Text = value; }
        }

        /// <summary cref="INavigator.Progress">
        /// <see cref="INavigator.Progress"/>
        /// </summary>
        public int Progress
        {
            get { return progressBar.Value; }
            set { progressBar.Value = value; }
        }

        /// <summary cref="INavigator.PrevEnabled">
        /// <see cref="INavigator.PrevEnabled"/>
        /// </summary>
        public bool PrevEnabled
        {
            get { return prevButton.Enabled; }
            set { prevButton.Enabled = value; }
        }

        /// <summary cref="INavigator.NextEnabled">
        /// <see cref="INavigator.NextEnabled"/>
        /// </summary>
        public bool NextEnabled
        {
            get { return nextButton.Enabled; }
            set { nextButton.Enabled = value; }
        }

        /// <summary cref="INavigator.GotoEnabled">
        /// <see cref="INavigator.GotoEnabled"/>
        /// </summary>
        public bool GotoEnabled
        {
            get { return gotoButton.Enabled; }
            set { gotoButton.Enabled = value; }
        }

        #endregion
    }
}
