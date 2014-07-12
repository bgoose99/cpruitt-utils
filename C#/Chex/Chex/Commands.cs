using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace Chex
{
    static class Commands
    {
        public static readonly RoutedUICommand Exit = 
            new RoutedUICommand( "Exit", "Exit", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.F4, ModifierKeys.Alt ) } );
        public static readonly RoutedUICommand Open =
            new RoutedUICommand( "Open", "Open", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.O, ModifierKeys.Control ) } );
        public static readonly RoutedUICommand Close =
            new RoutedUICommand( "Close", "Close", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.W, ModifierKeys.Control ) } );
        public static readonly RoutedUICommand New =
            new RoutedUICommand( "New", "New", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.N, ModifierKeys.Control ) } );
        public static readonly RoutedUICommand GotoBlock =
            new RoutedUICommand( "GotoBlock", "GotoBlock", typeof( Commands ) );
        public static readonly RoutedUICommand NextBlock =
            new RoutedUICommand( "NextBlock", "NextBlock", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.Right, ModifierKeys.Control ) } );
        public static readonly RoutedUICommand PrevBlock =
            new RoutedUICommand( "PrevBlock", "PrevBlock", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.Left, ModifierKeys.Control ) } );
        public static readonly RoutedUICommand Save =
            new RoutedUICommand( "Save", "Save", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.S, ModifierKeys.Control ) } );
        public static readonly RoutedUICommand SaveAs =
            new RoutedUICommand( "SaveAs", "SaveAs", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.N, ModifierKeys.Control | ModifierKeys.Shift ) } );
        public static readonly RoutedUICommand GetGotoOffsetInput =
            new RoutedUICommand( "GetGotoOffsetInput", "GetGotoOffsetInput", typeof( Commands ) );
        public static readonly RoutedUICommand GotoOffset =
            new RoutedUICommand( "GotoOffset", "GotoOffset", typeof( Commands ) );
        public static readonly RoutedUICommand GetFindInput =
            new RoutedUICommand( "Find", "Find", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.F, ModifierKeys.Control ) } );
        public static readonly RoutedUICommand FindNext =
            new RoutedUICommand( "FindNext", "FindNext", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.G, ModifierKeys.Control ) } );
        public static readonly RoutedUICommand FindPrev =
            new RoutedUICommand( "FindPrev", "FindPrev", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.G, ModifierKeys.Control | ModifierKeys.Shift ) } );
        public static readonly RoutedUICommand AddBytes =
            new RoutedUICommand( "AddBytes", "AddBytes", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.Add, ModifierKeys.Control ) } );
        public static readonly RoutedUICommand DeleteBytes =
            new RoutedUICommand( "DeleteBytes", "DeleteBytes", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.Subtract, ModifierKeys.Control ) } );
        public static readonly RoutedUICommand About =
            new RoutedUICommand( "About", "About", typeof( Commands ) );
        public static readonly RoutedUICommand Ascii =
            new RoutedUICommand( "Ascii", "Ascii", typeof( Commands ) );
        public static readonly RoutedUICommand Cancel =
            new RoutedUICommand( "Cancel", "Cancel", typeof( Commands ), new InputGestureCollection() { new KeyGesture( Key.Escape, ModifierKeys.None ) } );
        public static readonly RoutedUICommand FindByte =
            new RoutedUICommand( "FindByte", "FindByte", typeof( Commands ) );

    }
}
