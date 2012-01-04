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

            AcceptButton = acceptButton;

            // attempt to fill components with current preferences
            displayNameTextBox.Text = CsUtils.Utils.Preferences.getPreference( "user" );
            hostNameTextBox.Text = CsUtils.Utils.Preferences.getPreference( "host" );
            portTextBox.Text = CsUtils.Utils.Preferences.getPreference( "port" );
            try
            {
                autoConnectCheckBox.Checked = bool.Parse( CsUtils.Utils.Preferences.getPreference( "autoconnect" ) );
            }
            catch { }
        }

        /// <summary>
        /// Updates the user's preferences and exits this dialog.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void accept( object sender, EventArgs e )
        {
            bool inputValid = true;
            string validationErrors = "";
            
            string name = displayNameTextBox.Text.Trim();
            string host = hostNameTextBox.Text.Trim();
            string port = portTextBox.Text.Trim();

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

            CsUtils.Utils.Preferences.setPreference( "user", name );
            CsUtils.Utils.Preferences.setPreference( "host", host );
            CsUtils.Utils.Preferences.setPreference( "port", port );
            CsUtils.Utils.Preferences.setPreference( "autoconnect", autoConnectCheckBox.Checked.ToString() );
            
            DialogResult = DialogResult.OK;
            Dispose();
        }

        /// <summary>
        /// Exits this dialog. No changes will be saved to the user's preferences.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void cancel( object sender, EventArgs e )
        {
            DialogResult = DialogResult.Cancel;
            Dispose();
        }
    }
}
