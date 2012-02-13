namespace StreamView.View
{
    partial class NavigatorPanel
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
            this.prevButton = new System.Windows.Forms.Button();
            this.nextButton = new System.Windows.Forms.Button();
            this.gotoTextBox = new System.Windows.Forms.TextBox();
            this.gotoButton = new System.Windows.Forms.Button();
            this.label = new System.Windows.Forms.Label();
            this.progressBar = new System.Windows.Forms.ProgressBar();
            this.SuspendLayout();
            // 
            // prevButton
            // 
            this.prevButton.Location = new System.Drawing.Point( 7, 20 );
            this.prevButton.Name = "prevButton";
            this.prevButton.Size = new System.Drawing.Size( 75, 23 );
            this.prevButton.TabIndex = 0;
            this.prevButton.Text = "Previous";
            this.prevButton.UseVisualStyleBackColor = true;
            this.prevButton.Click += new System.EventHandler( this.prevButton_Click );
            // 
            // nextButton
            // 
            this.nextButton.Location = new System.Drawing.Point( 89, 20 );
            this.nextButton.Name = "nextButton";
            this.nextButton.Size = new System.Drawing.Size( 75, 23 );
            this.nextButton.TabIndex = 1;
            this.nextButton.Text = "Next";
            this.nextButton.UseVisualStyleBackColor = true;
            this.nextButton.Click += new System.EventHandler( this.nextButton_Click );
            // 
            // gotoTextBox
            // 
            this.gotoTextBox.Anchor = ( (System.Windows.Forms.AnchorStyles)( ( ( System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left )
                        | System.Windows.Forms.AnchorStyles.Right ) ) );
            this.gotoTextBox.Location = new System.Drawing.Point( 171, 20 );
            this.gotoTextBox.Name = "gotoTextBox";
            this.gotoTextBox.Size = new System.Drawing.Size( 105, 20 );
            this.gotoTextBox.TabIndex = 2;
            this.gotoTextBox.Text = "0";
            // 
            // gotoButton
            // 
            this.gotoButton.Anchor = ( (System.Windows.Forms.AnchorStyles)( ( System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right ) ) );
            this.gotoButton.Location = new System.Drawing.Point( 282, 20 );
            this.gotoButton.Name = "gotoButton";
            this.gotoButton.Size = new System.Drawing.Size( 75, 23 );
            this.gotoButton.TabIndex = 3;
            this.gotoButton.Text = "Go";
            this.gotoButton.UseVisualStyleBackColor = true;
            this.gotoButton.Click += new System.EventHandler( this.gotoButton_Click );
            // 
            // label
            // 
            this.label.AutoSize = true;
            this.label.Location = new System.Drawing.Point( 4, 4 );
            this.label.Name = "label";
            this.label.Size = new System.Drawing.Size( 100, 13 );
            this.label.TabIndex = 4;
            this.label.Text = "Showing item 0 of 0";
            // 
            // progressBar
            // 
            this.progressBar.Anchor = ( (System.Windows.Forms.AnchorStyles)( ( ( System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left )
                        | System.Windows.Forms.AnchorStyles.Right ) ) );
            this.progressBar.Location = new System.Drawing.Point( 7, 49 );
            this.progressBar.Name = "progressBar";
            this.progressBar.Size = new System.Drawing.Size( 350, 14 );
            this.progressBar.TabIndex = 5;
            // 
            // NavigatorPanel
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF( 6F, 13F );
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add( this.progressBar );
            this.Controls.Add( this.label );
            this.Controls.Add( this.gotoButton );
            this.Controls.Add( this.gotoTextBox );
            this.Controls.Add( this.nextButton );
            this.Controls.Add( this.prevButton );
            this.Name = "NavigatorPanel";
            this.Size = new System.Drawing.Size( 360, 70 );
            this.ResumeLayout( false );
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button prevButton;
        private System.Windows.Forms.Button nextButton;
        private System.Windows.Forms.TextBox gotoTextBox;
        private System.Windows.Forms.Button gotoButton;
        private System.Windows.Forms.Label label;
        private System.Windows.Forms.ProgressBar progressBar;
    }
}
