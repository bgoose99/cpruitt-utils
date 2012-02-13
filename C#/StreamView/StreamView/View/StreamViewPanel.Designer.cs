namespace StreamView.View
{
    partial class StreamViewPanel
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

        #region Component Designer generated code

        /// <summary> 
        /// Required method for Designer support - do not modify 
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.navigatorPanel = new StreamView.View.NavigatorPanel();
            this.byteStreamView = new StreamView.View.ByteStreamView();
            this.SuspendLayout();
            // 
            // navigatorPanel
            // 
            this.navigatorPanel.Anchor = ( (System.Windows.Forms.AnchorStyles)( ( ( System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left )
                        | System.Windows.Forms.AnchorStyles.Right ) ) );
            this.navigatorPanel.GotoEnabled = true;
            this.navigatorPanel.GotoText = "0";
            this.navigatorPanel.LabelText = "Showing item 0 of 0";
            this.navigatorPanel.Location = new System.Drawing.Point( 4, 235 );
            this.navigatorPanel.Name = "navigatorPanel";
            this.navigatorPanel.NextEnabled = true;
            this.navigatorPanel.PrevEnabled = true;
            this.navigatorPanel.Progress = 0;
            this.navigatorPanel.Size = new System.Drawing.Size( 499, 70 );
            this.navigatorPanel.TabIndex = 1;
            // 
            // byteStreamView
            // 
            this.byteStreamView.Anchor = ( (System.Windows.Forms.AnchorStyles)( ( ( ( System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom )
                        | System.Windows.Forms.AnchorStyles.Left )
                        | System.Windows.Forms.AnchorStyles.Right ) ) );
            this.byteStreamView.Location = new System.Drawing.Point( 3, 3 );
            this.byteStreamView.Name = "byteStreamView";
            this.byteStreamView.Size = new System.Drawing.Size( 500, 225 );
            this.byteStreamView.TabIndex = 0;
            // 
            // StreamViewPanel
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF( 6F, 13F );
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add( this.navigatorPanel );
            this.Controls.Add( this.byteStreamView );
            this.Name = "StreamViewPanel";
            this.Size = new System.Drawing.Size( 508, 312 );
            this.ResumeLayout( false );

        }

        #endregion

        private ByteStreamView byteStreamView;
        private NavigatorPanel navigatorPanel;
    }
}
