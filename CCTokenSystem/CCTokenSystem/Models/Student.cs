using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CCTokenSystem.Models
{
   public class Student
    {
        [Key]
        public int Id { get; set; }
        public int StudentID { get; set; }
        public string Firstname { get; set; }
        public string Lastname { get; set; }
        public string Phoneno { get; set; }
        public string Course { get; set; }
        public string Email { get; set; }
        [NotMapped]
        public string Password { get; set; }
    }
}
