using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CsvEditor.model;
using System.Data;

namespace CsvEditor.presenter
{
    /// <summary>
    /// Simple concreate CSV presenter.
    /// </summary>
    class CsvPresenter : ICsvPresenter
    {
        ICsvDataModel model;
        ICsvSerializer serializer;

        /// <summary>
        /// Constructor
        /// </summary>
        public CsvPresenter()
        {
            model = new CsvDataModel();
            serializer = new CsvSerializer();
        }

        /// <summary cref="ICsvPresenter.getDataTable">
        /// <see cref="ICsvPresenter.getDataTable"/>
        /// </summary>
        /// <returns></returns>
        public DataTable getDataTable()
        {
            return model.getDataTable();
        }

        /// <summary cref="ICsvPresenter.openFile">
        /// <see cref="ICsvPresenter.openFile"/>
        /// </summary>
        /// <param name="path"></param>
        public void openFile( string path )
        {
            model.clear();
            model = serializer.readItem( path );
        }

        /// <summary cref="ICsvPresenter.saveFile">
        /// <see cref="ICsvPresenter.saveFile"/>
        /// </summary>
        /// <param name="path"></param>
        public void saveFile( string path )
        {
            serializer.writeItem( model, path );
        }

        /// <summary cref="ICsvPresenter.resetData">
        /// <see cref="ICsvPresenter.resetData"/>
        /// </summary>
        public void resetData()
        {
            model.clear();
        }
    }
}
