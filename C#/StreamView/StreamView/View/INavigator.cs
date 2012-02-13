using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace StreamView.View
{
    interface INavigator
    {
        #region EventHandlers
        event EventHandler PrevButtonPressed;
        event EventHandler NextButtonPressed;
        event EventHandler GotoButtonPressed;
        #endregion

        #region Properties

        /// <summary>
        /// The text of the label indicating what is being shown.
        /// </summary>
        string LabelText { get; set; }

        /// <summary>
        /// Text in the goto text box.
        /// </summary>
        string GotoText { get; set; }

        /// <summary>
        /// Value of the progress bar.
        /// </summary>
        int Progress { get; set; }

        /// <summary>
        /// Status of the previous button.
        /// </summary>
        bool PrevEnabled { get; set; }

        /// <summary>
        /// Status of the next button.
        /// </summary>
        bool NextEnabled { get; set; }

        /// <summary>
        /// Status of the goto button.
        /// </summary>
        bool GotoEnabled { get; set; }

        #endregion
    }
}
