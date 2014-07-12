using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Threading;

namespace Chex
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        public App()
            : base()
        {
            this.DispatcherUnhandledException += handleUncaughtException;
        }

        public void handleUncaughtException( object sender, DispatcherUnhandledExceptionEventArgs e )
        {
            MessageBox.Show( "An uncaught exception occurred:" + Environment.NewLine + e.Exception.Message
                + Environment.NewLine + Environment.NewLine + "Application will now exit.",
                "Error!", MessageBoxButton.OK, MessageBoxImage.Error );
            e.Handled = true;
            Application.Current.Shutdown();
        }
    }
}
