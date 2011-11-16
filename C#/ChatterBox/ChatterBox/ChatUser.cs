using System.Security.Principal;

namespace ChatterBox
{
    class ChatUser : IUser
    {
        private string name;
        private string displayName;

        public ChatUser( string displayName )
        {
            this.name = WindowsIdentity.GetCurrent().Name; ;
            this.displayName = displayName;
        }

        public string getName()
        {
            return name;
        }

        public string getDisplayName()
        {
            return displayName;
        }

        public void setDisplayName( string displayName )
        {
            this.displayName = displayName;
        }
    }
}
