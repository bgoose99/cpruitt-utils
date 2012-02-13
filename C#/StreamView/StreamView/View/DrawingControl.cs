using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;
using System.Windows.Forms;

namespace StreamView.View
{
    /// <summary>
    /// This class facilitates suspension of drawing a given control. This
    /// allows us to make several sweeping changes to a component without
    /// drawing each intermediate step.
    /// This class came from the discussion here:
    /// http://stackoverflow.com/questions/487661/how-do-i-suspend-painting-for-a-control-and-its-children
    /// </summary>
    class DrawingControl
    {
        [DllImport( "user32.dll" )]
        public static extern int SendMessage( IntPtr hWnd, Int32 wMsg, bool wParam, Int32 lParam );

        private const int WM_SETREDRAW = 11;

        /// <summary>
        /// Suspends the drawing of the given Control.
        /// </summary>
        /// <param name="parent"></param>
        public static void suspendDrawing( Control parent )
        {
            SendMessage( parent.Handle, WM_SETREDRAW, false, 0 );
        }

        /// <summary>
        /// Resumes the drawing of the given Control.
        /// </summary>
        /// <param name="parent"></param>
        public static void resumeDrawing( Control parent )
        {
            SendMessage( parent.Handle, WM_SETREDRAW, true, 0 );
            parent.Refresh();
        }
    }

}
