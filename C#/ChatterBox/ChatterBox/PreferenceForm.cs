using System;
using System.Windows.Forms;

namespace ChatterBox
{
    public partial class PreferenceForm : Form
    {
        /// <summary>
        /// Constructor
        /// </summary>
        public PreferenceForm()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Updates the user's preferences and exits this dialog.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void acceptButton_Click( object sender, EventArgs e )
        {
            bool inputValid = true;
            string validationErrors = "";
            
            string name = this.displayNameTextBox.Text.Trim();
            string host = this.hostNameTextBox.Text.Trim();
            string port = this.portTextBox.Text.Trim();

            if( name.Length < 1 )
            {
                validationErrors += "Name must not be emtpy\n";
                inputValid = false;
            }
            if( host.Length < 1 )
            {
                validationErrors += "Host must not be empty\n";
                inputValid = false;
            }
            if( port.Length < 1 )
            {
                validationErrors += "Port must not be empty\n";
                inputValid = false;
            }

            if( !inputValid )
            {
                MessageBox.Show( validationErrors, "Invalid input", MessageBoxButtons.OK, MessageBoxIcon.Exclamation );
                return;
            }

            Preferences.setPreference( "user", name );
            Preferences.setPreference( "host", host );
            Preferences.setPreference( "port", port );
            Preferences.setPreference( "autoconnect", this.autoConnectCheckBox.Checked.ToString() );
            
            DialogResult = DialogResult.OK;
            Dispose();
        }

        /// <summary>
        /// Exits this dialog. No changes will be saved to the user's preferences.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void cancelButton_Click( object sender, EventArgs e )
        {
            //
            DialogResult = DialogResult.Cancel;
            Dispose();
        }
    }
}
