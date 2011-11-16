using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChatterBox
{
    interface IUser
    {
        string getName();
        string getDisplayName();
        void setDisplayName( string displayName );
    }
}
