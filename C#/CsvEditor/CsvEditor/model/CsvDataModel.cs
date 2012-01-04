using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;

namespace CsvEditor.model
{
    /// <summary>
    /// Simple concrete CSV data model.
    /// </summary>
    class CsvDataModel : ICsvDataModel
    {
        private DataTable table;

        /// <summary>
        /// Constructor
        /// </summary>
        public CsvDataModel()
        {
            table = new DataTable();
        }
        
        /// <summary cref="ICsvDataModel.getDataTable">
        /// <see cref="ICsvDataModel.getDataTable"/>
        /// </summary>
        /// <returns></returns>
        public DataTable getDataTable()
        {
            return table;
        }

        /// <summary cref="ICsvDataModel.clear">
        /// <see cref="ICsvDataModel.clear"/>
        /// </summary>
        public void clear()
        {
            table.Clear();
            table.Columns.Clear();
            table.Rows.Clear();
        }
    }
}
