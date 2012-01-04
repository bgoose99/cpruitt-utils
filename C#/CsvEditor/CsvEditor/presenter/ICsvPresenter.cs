using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;

namespace CsvEditor.presenter
{
    /// <summary>
    /// Simple presenter interface for a CSV data model.
    /// </summary>
    interface ICsvPresenter
    {
        /// <summary>
        /// Returns the DataTable associated with the underlying model.
        /// </summary>
        /// <returns></returns>
        DataTable getDataTable();

        /// <summary>
        /// Attempts to open/read the file denoted by the supplied path.
        /// </summary>
        /// <param name="path"></param>
        void openFile( string path );

        /// <summary>
        /// Attempts to save the current data model to the file denoted by
        /// the supplied path.
        /// </summary>
        /// <param name="path"></param>
        void saveFile( string path );

        /// <summary>
        /// Resets the underlying data.
        /// </summary>
        void resetData();
    }
}
