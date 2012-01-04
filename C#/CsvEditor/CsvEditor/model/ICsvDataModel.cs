using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;

namespace CsvEditor.model
{
    /// <summary>
    /// Simple interface for the CSV data model.
    /// </summary>
    interface ICsvDataModel
    {
        /// <summary>
        /// Returns the DataTable that represents this data model.
        /// </summary>
        /// <returns></returns>
        DataTable getDataTable();

        /// <summary>
        /// Clears all data from this model, including row/column headers.
        /// </summary>
        void clear();
    }
}
