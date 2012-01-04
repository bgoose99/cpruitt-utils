using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CsvEditor.model
{
    /// <summary>
    /// Simple serializer interface for an ICsvDataModel.
    /// </summary>
    interface ICsvSerializer
    {
        /// <summary>
        /// Reads the CSV data contained in the file denoted by the supplied string
        /// and returns a new ICsvDataModel.
        /// </summary>
        /// <param name="file"></param>
        /// <returns></returns>
        ICsvDataModel readItem( string file );

        /// <summary>
        /// Writes the supplied CSV data model out to the file denoted by the supplied
        /// string.
        /// </summary>
        /// <param name="model"></param>
        /// <param name="file"></param>
        void writeItem( ICsvDataModel model, string file );
    }
}
