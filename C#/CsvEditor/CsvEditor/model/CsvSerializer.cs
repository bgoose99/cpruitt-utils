using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Windows.Forms;

namespace CsvEditor.model
{
    /// <summary>
    /// Simple concrete CSV serializer.
    /// </summary>
    class CsvSerializer : ICsvSerializer
    {
        /// <summary>
        /// Default constructor.
        /// </summary>
        public CsvSerializer()
        {
        }

        /// <summary cref="ICsvSerializer.readItem">
        /// <see cref="ICsvSerializer.readItem"/>
        /// </summary>
        /// <param name="filename"></param>
        /// <returns></returns>
        public ICsvDataModel readItem( string filename )
        {
            if( !File.Exists( filename ) )
            {
                throw new IOException( "Specified file '" + filename + "' does not exist." );
            }

            ICsvDataModel model = new CsvDataModel();

            using( StreamReader reader = new StreamReader( filename ) )
            {
                string s;
                string[] sArray;

                // read first line, which should be a list of column headings
                s = reader.ReadLine();
                if( s == null )
                    throw new IOException( "File '" + filename + "' is empty." );
                sArray = s.Split( ',' );

                foreach( string columnName in sArray )
                {
                    try
                    {
                        model.getDataTable().Columns.Add( columnName );
                    }
                    catch( System.Data.DuplicateNameException )
                    {
                        bool done = false;
                        int counter = 0;
                        while( !done )
                        {
                            string temp = columnName + "_" + counter;
                            if( model.getDataTable().Columns.Contains( temp ) )
                            {
                                counter++;
                                continue;
                            }
                            else
                            {
                                MessageBox.Show( "A duplicate column heading has been found.\n'" +
                                    columnName + "' has been renamed to '" + temp + ".", "Info",
                                    MessageBoxButtons.OK, MessageBoxIcon.Information );
                                model.getDataTable().Columns.Add( temp );
                                done = true;
                            }
                        }
                    }
                }

                while( ( s = reader.ReadLine() ) != null )
                {
                    s = s.Trim();
                    if( s.Length < 1 ) continue;

                    // each additional line should be a record of data
                    sArray = s.Split( ',' );
                    if( sArray.Length != model.getDataTable().Columns.Count )
                        throw new IOException( "Data in '" + filename + "' is not formatted correctly." );
                    model.getDataTable().Rows.Add( sArray );
                }
            }

            return model;
        }

        /// <summary cref="ICsvSerializer.writeItem">
        /// <see cref="ICsvSerializer.writeItem"/>
        /// </summary>
        /// <param name="model"></param>
        /// <param name="filename"></param>
        public void writeItem( ICsvDataModel model, string filename )
        {
            using( StreamWriter writer = new StreamWriter( filename ) )
            {
                // write header
                for( int i = 0; i < model.getDataTable().Columns.Count; i++ )
                {
                    writer.Write( model.getDataTable().Columns[i].ColumnName );
                    if( i != model.getDataTable().Columns.Count - 1 )
                        writer.Write( "," );
                }
                writer.WriteLine();

                // write data
                foreach( System.Data.DataRow row in model.getDataTable().Rows )
                {
                    for( int i = 0; i < row.ItemArray.Length; i++ )
                    {
                        writer.Write( row.ItemArray[i] );
                        if( i != row.ItemArray.Length - 1 )
                            writer.Write( "," );
                    }
                    writer.WriteLine();
                }
                writer.WriteLine();
            }

            return;
        }
    }
}
