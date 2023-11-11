using SQLite;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DemoCRUDOperationWithSQLite.Models
{
    public class User
    {
        [PrimaryKey, AutoIncrement]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Location { get; set; }
        public string Email { get; set; }

        public string Date { get; set; }
        public string Length { get; set; }
        public string Level { get; set; }
        public string Disciption { get; set; }
        public string Parking { get; set; }
    }
}
