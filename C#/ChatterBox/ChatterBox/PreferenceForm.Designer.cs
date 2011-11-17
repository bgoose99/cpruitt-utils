namespace ChatterBox
{
    partial class PreferenceForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose( bool disposing )
        {
            if( disposing && ( components != null ) )
            {
                components.Dispose();
            }
            base.Dispose( disposing );
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager( typeof( PreferenceForm ) );
            this.displayNameLabel = new System.Windows.Forms.Label();
            this.hostNameLabel = new System.Windows.Forms.Label();
            this.portLabel = new System.Windows.Forms.Label();
            this.autoConnectCheckBox = new System.Windows.Forms.CheckBox();
            this.acceptButton = new System.Windows.Forms.Button();
            this.cancelButton = new System.Windows.Forms.Button();
            this.displayNameTextBox = new System.Windows.Forms.TextBox();
            this.hostNameTextBox = new System.Windows.Forms.TextBox();
            this.portTextBox = new System.Windows.Forms.TextBox();
            this.SuspendLayout();
            // 
            // displayNameLabel
            // 
            this.displayNameLabel.AutoSize = true;
            this.displayNameLabel.Location = new System.Drawing.Point( 12, 9 );
            this.displayNameLabel.Name = "displayNameLabel";
            this.displayNameLabel.Size = new System.Drawing.Size( 70, 13 );
            this.displayNameLabel.TabIndex = 0;
            this.displayNameLabel.Text = "Display name";
            // 
            // hostNameLabel
            // 
            this.hostNameLabel.AutoSize = true;
            this.hostNameLabel.Location = new System.Drawing.Point( 12, 31 );
            this.hostNameLabel.Name = "hostNameLabel";
            this.hostNameLabel.Size = new System.Drawing.Size( 82, 13 );
            this.hostNameLabel.TabIndex = 1;
            this.hostNameLabel.Text = "Host IP address";
            // 
            // portLabel
            // 
            this.portLabel.AutoSize = true;
            this.portLabel.Location = new System.Drawing.Point( 12, 53 );
            this.portLabel.Name = "portLabel";
            this.portLabel.Size = new System.Drawing.Size( 74, 13 );
            this.portLabel.TabIndex = 2;
            this.portLabel.Text = "Port (1-65535)";
            // 
            // autoConnectCheckBox
            // 
            this.autoConnectCheckBox.AutoSize = true;
            this.autoConnectCheckBox.Location = new System.Drawing.Point( 15, 69 );
            this.autoConnectCheckBox.Name = "autoConnectCheckBox";
            this.autoConnectCheckBox.Size = new System.Drawing.Size( 90, 17 );
            this.autoConnectCheckBox.TabIndex = 3;
            this.autoConnectCheckBox.Text = "Auto-connect";
            this.autoConnectCheckBox.UseVisualStyleBackColor = true;
            // 
            // acceptButton
            // 
            this.acceptButton.Anchor = System.Windows.Forms.AnchorStyles.Bottom;
            this.acceptButton.AutoSize = true;
            this.acceptButton.Image = ( (System.Drawing.Image)( resources.GetObject( "acceptButton.Image" ) ) );
            this.acceptButton.Location = new System.Drawing.Point( 87, 92 );
            this.acceptButton.Name = "acceptButton";
            this.acceptButton.Size = new System.Drawing.Size( 60, 40 );
            this.acceptButton.TabIndex = 4;
            this.acceptButton.UseVisualStyleBackColor = true;
            this.acceptButton.Click += new System.EventHandler( this.accept );
            // 
            // cancelButton
            // 
            this.cancelButton.Anchor = System.Windows.Forms.AnchorStyles.Bottom;
            this.cancelButton.AutoSize = true;
            this.cancelButton.Image = ( (System.Drawing.Image)( resources.GetObject( "cancelButton.Image" ) ) );
            this.cancelButton.Location = new System.Drawing.Point( 153, 92 );
            this.cancelButton.Name = "cancelButton";
            this.cancelButton.Size = new System.Drawing.Size( 60, 40 );
            this.cancelButton.TabIndex = 5;
            this.cancelButton.UseVisualStyleBackColor = true;
            this.cancelButton.Click += new System.EventHandler( this.cancel );
            // 
            // displayNameTextBox
            // 
            this.displayNameTextBox.Anchor = ( (System.Windows.Forms.AnchorStyles)( ( ( System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left )
                        | System.Windows.Forms.AnchorStyles.Right ) ) );
            this.displayNameTextBox.Location = new System.Drawing.Point( 127, 6 );
            this.displayNameTextBox.Name = "displayNameTextBox";
            this.displayNameTextBox.Size = new System.Drawing.Size( 160, 20 );
            this.displayNameTextBox.TabIndex = 6;
            // 
            // hostNameTextBox
            // 
            this.hostNameTextBox.Anchor = ( (System.Windows.Forms.AnchorStyles)( ( ( System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left )
                        | System.Windows.Forms.AnchorStyles.Right ) ) );
            this.hostNameTextBox.Location = new System.Drawing.Point( 127, 28 );
            this.hostNameTextBox.Name = "hostNameTextBox";
            this.hostNameTextBox.Size = new System.Drawing.Size( 160, 20 );
            this.hostNameTextBox.TabIndex = 7;
            // 
            // portTextBox
            // 
            this.portTextBox.Anchor = ( (System.Windows.Forms.AnchorStyles)( ( ( System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left )
                        | System.Windows.Forms.AnchorStyles.Right ) ) );
            this.portTextBox.Location = new System.Drawing.Point( 127, 50 );
            this.portTextBox.Name = "portTextBox";
            this.portTextBox.Size = new System.Drawing.Size( 160, 20 );
            this.portTextBox.TabIndex = 8;
            // 
            // PreferenceForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF( 6F, 13F );
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size( 299, 144 );
            this.Controls.Add( this.portTextBox );
            this.Controls.Add( this.hostNameTextBox );
            this.Controls.Add( this.displayNameTextBox );
            this.Controls.Add( this.cancelButton );
            this.Controls.Add( this.acceptButton );
            this.Controls.Add( this.autoConnectCheckBox );
            this.Controls.Add( this.portLabel );
            this.Controls.Add( this.hostNameLabel );
            this.Controls.Add( this.displayNameLabel );
            this.Name = "PreferenceForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "ChatterBox Preferences";
            this.ResumeLayout( false );
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label displayNameLabel;
        private System.Windows.Forms.Label hostNameLabel;
        private System.Windows.Forms.Label portLabel;
        private System.Windows.Forms.CheckBox autoConnectCheckBox;
        private System.Windows.Forms.Button acceptButton;
        private System.Windows.Forms.Button cancelButton;
        private System.Windows.Forms.TextBox displayNameTextBox;
        private System.Windows.Forms.TextBox hostNameTextBox;
        private System.Windows.Forms.TextBox portTextBox;
    }
}